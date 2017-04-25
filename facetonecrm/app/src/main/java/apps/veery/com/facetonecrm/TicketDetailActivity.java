package apps.veery.com.facetonecrm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import apps.veery.com.adapter.AttachmentAdapter;
import apps.veery.com.adapter.CommentAdapter;
import apps.veery.com.adapter.SubTicketAdapter;
import apps.veery.com.model.Attachment;
import apps.veery.com.model.Auth;
import apps.veery.com.model.Comment;
import apps.veery.com.model.CommentResponse;
import apps.veery.com.model.EngagementSession;
import apps.veery.com.model.ImageUploadResponse;
import apps.veery.com.model.NewComment;
import apps.veery.com.model.OneTicketResponse;
import apps.veery.com.model.SubTicket;
import apps.veery.com.model.Ticket;
import apps.veery.com.model.TicketStatus;
import apps.veery.com.model.User;
import apps.veery.com.model.UserGroup;
import apps.veery.com.model.UserGroupResponse;
import apps.veery.com.model.UsersResponse;
import apps.veery.com.model.jwtres.Decode;
import apps.veery.com.model.jwtres.TicketResult;
import apps.veery.com.model.ticketviews.TicketStatusResponse;
import apps.veery.com.restInterfaces.AttachmentApiInterface;
import apps.veery.com.restInterfaces.TicketApiInterface;
import apps.veery.com.restInterfaces.UserApiInterface;
import apps.veery.com.service.AttachmentUploadService;
import apps.veery.com.service.TicketService;
import apps.veery.com.service.UserService;
import apps.veery.com.utilities.Components;
import apps.veery.com.utilities.DateMatcher;
import apps.veery.com.utilities.FontChanger;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketDetailActivity extends AppCompatActivity {

    final static int CREATE_SUB_TICKET = 111;
    private static final int CHANGE_TICKET_DETAIL = 444;
    private static final int IMAGE_REQUEST_CODE = 99;
    private static final int FILE_SELECT_CODE = 1010;
    public static final String DOWNLOAD_URL = "downloadUrl";
    public static final String FILE_NAME = "fileName";

    Bitmap bitmap = null;
    Context context;
    TextView tv_subject, tv_status, tv_priority, tv_description, tv_reference, tv_created;
    CircleImageView requsterImage, submitterImage, assigneeImage;
    TextView requesterText, submitterText, assigneeText, requsterName, submitterName, assigneeName;
    TextView subTicketText, no_of_sub_tickets, show_sub_tickets, commentsText;
    LinearLayout userBar, ticket_detail_tag_container, scanned_image_container;
    ListView commentsContainer, sub_ticket_container;
    String newCommentText;
    FloatingActionButton fab_comment, fab_scan_attachment, fab_file_attachment;
    TicketResult ticketResult;
    String tv_id;
    TicketApiInterface apiService;
    Ticket newTicket;
    String ticketPosition;
    String ticketJsonObject;
    private boolean isTicketChanged = false;
    private boolean isSubTicketsVisible = false;
    private boolean isSubTicketCreated;
    private ProgressDialog progressDialog;
    private CommentAdapter commentAdapter;
    private SubTicketAdapter subTicketAdapter;
    private AttachmentAdapter attachmentAdapter;

    private Auth authUser;
    private Decode jwtObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        setContentView(R.layout.activity_ticket_detail);

        // getting data from prev
        Intent intent = getIntent();
        apiService = TicketService.getClient().create(TicketApiInterface.class);

        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        if(null != sharedpreferences.getString(MainActivity.USERJWT,null)) {
            jwtObject = new Gson().fromJson(sharedpreferences.getString(MainActivity.USERJWT, null), Decode.class);
        }
        if(null != sharedpreferences.getString(MainActivity.USER,null)) {
            authUser = new Gson().fromJson(sharedpreferences.getString(MainActivity.USER, null), Auth.class);
        }

        // if this activity destroyed due to scan library and saved it's ticket json data
        if (null != savedInstanceState && savedInstanceState.containsKey("ticketJsonObject")) {
            ticketJsonObject = savedInstanceState.getString("ticketJsonObject");
        } else {
            ticketJsonObject = intent.getStringExtra("tv_tag");
        }

        ticketPosition = intent.getStringExtra("ticketPosition");
        newTicket = new Gson().fromJson(ticketJsonObject, Ticket.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ticketdeatials);
        if (null != newTicket) {
            toolbar.setTitle(newTicket.getReference());
            toolbar.setSubtitle(newTicket.getChannel());
        } else {
            toolbar.setTitle("Ticket Detail");
        }
        setSupportActionBar(toolbar);

        context = this;
        progressDialog = new ProgressDialog(TicketDetailActivity.this);

        String subject = newTicket.getSubject();
        String status = newTicket.getStatus();
        String priority = newTicket.getPriority();
        String description = newTicket.getDescription();
        String reference = newTicket.getSubmitter().getName();
        String created = DateMatcher.getTicketDuration(newTicket.getCreated_at());
        tv_id = newTicket.getTicketId();

        User requester = newTicket.getRequester();
        User submitter = newTicket.getSubmitter();
        User assignee = newTicket.getAssignee();

        tv_subject = (TextView) findViewById(R.id.ticket_detail_subject);
        tv_status = (TextView) findViewById(R.id.ticket_detail_status);
        tv_priority = (TextView) findViewById(R.id.ticket_detail_priority);
        tv_description = (TextView) findViewById(R.id.ticket_detail_description);
        tv_reference = (TextView) findViewById(R.id.ticket_detail_created_by);
        tv_created = (TextView) findViewById(R.id.ticket_detail_create_time);

        //for users
        requsterImage = (CircleImageView) findViewById(R.id.requester_avatar);
        submitterImage = (CircleImageView) findViewById(R.id.submitter_avatar);
        assigneeImage = (CircleImageView) findViewById(R.id.assignee_avatar);
        requesterText = (TextView) findViewById(R.id.requesterText);
        submitterText = (TextView) findViewById(R.id.submitterText);
        assigneeText = (TextView) findViewById(R.id.assigneeText);
        requsterName = (TextView) findViewById(R.id.requesterName);
        submitterName = (TextView) findViewById(R.id.submitterName);
        assigneeName = (TextView) findViewById(R.id.assigneeName);

        //show / hide subtickets
        subTicketText = (TextView) findViewById(R.id.subTicketText);
        no_of_sub_tickets = (TextView) findViewById(R.id.no_of_sub_tickets);
        show_sub_tickets = (TextView) findViewById(R.id.show_sub_tickets);
        commentsText = (TextView) findViewById(R.id.commentsText);

        userBar = (LinearLayout) findViewById(R.id.userBar);
        ticket_detail_tag_container = (LinearLayout) findViewById(R.id.ticket_detail_tag_container);

        scanned_image_container = (LinearLayout) findViewById(R.id.scanned_image_container);
        commentsContainer = (ListView) findViewById(R.id.ticket_detail_comments_container);
        sub_ticket_container = (ListView) findViewById(R.id.sub_ticket_container);

        fab_comment = (FloatingActionButton) findViewById(R.id.fab_comment);
        fab_scan_attachment = (FloatingActionButton) findViewById(R.id.fab_scan_attachment);
        fab_file_attachment = (FloatingActionButton) findViewById(R.id.fab_file_attachment);

        show_sub_tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSubTicketsVisible) {
                    show_sub_tickets.setText("Show");
                    isSubTicketsVisible = false;
                    sub_ticket_container.setVisibility(View.GONE);
                } else {
                    show_sub_tickets.setText("Hide");
                    isSubTicketsVisible = true;
                    sub_ticket_container.setVisibility(View.VISIBLE);
                }
            }
        });

        fab_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(TicketDetailActivity.this);

                dialog.setContentView(R.layout.new_comment_dialog);
//                dialog.setTitle("Enter your comment below");
                final RadioGroup commentRadioGroup = (RadioGroup) dialog.findViewById(R.id.commentRadioGroup);

                final EditText text = (EditText) dialog.findViewById(R.id.editText_comment);

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok_comment);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgress();

                        if (!text.getText().toString().isEmpty()) {
                            newCommentText = text.getText().toString();
                            RadioButton rb = (RadioButton) dialog.findViewById(commentRadioGroup.getCheckedRadioButtonId());
                            String commentType = rb.getText().toString();
                            sendNewComment(newCommentText, commentType);
                            dialog.dismiss();
                        } else {
                            Components.hideProgressDialog(progressDialog);
                            Toast.makeText(TicketDetailActivity.this, "No comments added.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        fab_scan_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });

        fab_file_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        // setting UI data
        tv_subject.setText(subject);
        tv_status.setText(status);
        tv_priority.setText(priority);
        if (priority.equals("urgent")) {
            tv_priority.setBackgroundResource(R.drawable.corner_radius_red);
        } else if (priority.equals("high")) {
            tv_priority.setBackgroundResource(R.drawable.corner_radius_yellow);
        } else if (priority.equals("normal")) {
            tv_priority.setBackgroundResource(R.drawable.corner_radius_grey);
        } else {
            tv_priority.setBackgroundResource(R.drawable.corner_radius);
        }
        tv_description.setText(description);
        tv_description.setMovementMethod(new ScrollingMovementMethod()); // for scrolling textview. not smooth as scrollview though.
        tv_reference.setText(reference);
        tv_created.setText(created);

        if (null != requester) {
            if (null != requester.getAvatar() && !requester.getAvatar().isEmpty()) {
                Picasso.with(this).load(requester.getAvatar()).resize(50, 50).into(requsterImage);
            }
            if (!requester.getName().isEmpty()) {
                requsterName.setText(requester.getName());
            }
        }
        if (null != submitter) {
            if (null != submitter.getAvatar() && !submitter.getAvatar().isEmpty()) {
                Picasso.with(this).load(submitter.getAvatar()).resize(50, 50).into(submitterImage);
            }
            if (!submitter.getName().isEmpty()) {
                submitterName.setText(submitter.getName());
            }
        }
        if (null != assignee) {
            if (null != assignee.getAvatar() && !assignee.getAvatar().isEmpty()) {
                Picasso.with(this).load(assignee.getAvatar()).resize(50, 50).into(assigneeImage);
            }
            if (!assignee.getName().isEmpty()) {
                assigneeName.setText(assignee.getName());
            }
        }

        if (null != newTicket.getTags()) {
            ArrayList<String> tags = (ArrayList<String>) newTicket.getTags();
            for (String tag : tags) {
                final TextView tagName = new TextView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(2, 2, 2, 2);
                tagName.setLayoutParams(lp);
                tagName.setText(tag);
                tagName.setPadding(6, 0, 6, 0);
                tagName.setTextColor(Color.WHITE);
                tagName.setBackgroundColor(Color.GRAY);
                ticket_detail_tag_container.addView(tagName);
            }
        }
        Log.d("Test", "on create ticket detail");
        getExtraTicketDetails(tv_id);
        changeFont();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("ticketJsonObject", ticketJsonObject);
        Log.d("Test", "onsave instance state in ticket detail activity");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        // pass the change ticket if only changed. else don't.
        if (isTicketChanged) {
            Intent intent = getIntent();
            intent.putExtra("ticketOperationType", "updateTicket"); // ticket will update for pick,forward,change status and edit ticket details
            intent.putExtra("passingTicket", new Gson().toJson(newTicket));
            intent.putExtra("ticketPosition", ticketPosition);
            setResult(Activity.RESULT_OK, intent);
            this.finish();
        } else if (isSubTicketCreated) {
            Intent intent = getIntent();
            intent.putExtra("ticketOperationType", "createSubTicket"); // created a new sub ticket
            setResult(Activity.RESULT_OK, intent);
            this.finish();
        } else {                                                          // no change detected
            Intent intent = getIntent();
            setResult(Activity.RESULT_CANCELED, intent);
            this.finish();
        }
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Components.setpDialog(TicketDetailActivity.this); // else app crash when create sub ticket -- change status
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ticket_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_ticket_detail_edit) {
            goToUpdate();
        }
        if (id == R.id.menu_ticket_detail_pick) {
            pickTicket();
        }
        if (id == R.id.menu_ticket_detail_forward) {
            forwardTicket();
        }
        if (id == R.id.menu_ticket_detail_change_status) {
            getNextStatus(newTicket.getType(), newTicket.getStatus());
        }
        if (id == R.id.menu_ticket_detail_sub) {
            Intent intent = new Intent(this, NewTicketActivity.class);
            intent.putExtra("requestCode", CREATE_SUB_TICKET);
            intent.putExtra("parentTicketId", tv_id);
            startActivityForResult(intent, CREATE_SUB_TICKET);
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    private void getNextStatus(String ticketType, String currentStatus) {
        showProgress();
        Call<TicketStatusResponse> call = apiService.getNextTicketStatus("Bearer " + authUser.getAccessToken(), ticketType, currentStatus);
        call.enqueue(new Callback<TicketStatusResponse>() {
            @Override
            public void onResponse(Call<TicketStatusResponse> call, Response<TicketStatusResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (null != response.body().getTicketResult()) {
                            showTicketStatusDialog(response.body().getTicketResult());
                        }

                    } else {
                        Toast.makeText(TicketDetailActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Components.hideProgressDialog(progressDialog);
                } else {
                    Toast.makeText(TicketDetailActivity.this, "Getting next status for ticket failed. Check internet connection.", Toast.LENGTH_SHORT).show();
                    Components.hideProgressDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<TicketStatusResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
            }
        });
    }

    private void showTicketStatusDialog(final List<String> ticketResult) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(TicketDetailActivity.this);
        builderSingle.setTitle("Select a status");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                TicketDetailActivity.this,
                android.R.layout.simple_list_item_single_choice);
        arrayAdapter.addAll(ticketResult);

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        TicketStatus ticketStatus = new TicketStatus();
                        ticketStatus.setStatus(arrayAdapter.getItem(position));
                        changeTicketStatus(ticketStatus);
                        dialog.dismiss();
                    }
                });
        builderSingle.show();
    }

    private void changeTicketStatus(final TicketStatus newStatus) {
        showProgress();
        Call<CommentResponse> call = apiService.changeTicketStatus("Bearer " + authUser.getAccessToken(), tv_id, newStatus);
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(TicketDetailActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
                        newTicket.setStatus(newStatus.getStatus()); // now the passed ticket has changed with new assignee
                        isTicketChanged = true;
                        // showing the new status in view
                        tv_status.setText(newTicket.getStatus());
                    } else {
                        Toast.makeText(TicketDetailActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Components.hideProgressDialog(progressDialog);
                } else {
                    Toast.makeText(TicketDetailActivity.this, "Change ticket status failed.", Toast.LENGTH_SHORT).show();
                    Components.hideProgressDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
                Toast.makeText(TicketDetailActivity.this, "Change ticket status failed. Check internet connection.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void forwardTicket() {
        final Dialog dialogForward = new Dialog(TicketDetailActivity.this);
        dialogForward.setContentView(R.layout.forward_ticket_dialog);
        dialogForward.setTitle("Select a user or user group");
        final ListView listView = (ListView) dialogForward.findViewById(R.id.userForwardContainer);

        final List<User> users = getAllUsers(listView); // first view wait for data for show
        final List<UserGroup> userGroups = getAllUserGroups();

        final ArrayAdapter<User> userAdapter = new ArrayAdapter<User>(TicketDetailActivity.this, android.R.layout.simple_list_item_single_choice,
                users);
        final ArrayAdapter<UserGroup> userGroupAdapter = new ArrayAdapter<UserGroup>(TicketDetailActivity.this, android.R.layout.simple_list_item_single_choice,
                userGroups);

        final RadioGroup forwardRadioGroup = (RadioGroup) dialogForward.findViewById(R.id.forwardRadioGroup);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        forwardRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbForwardUser) { // select a user
                    listView.setAdapter(userAdapter);
                } else {// select a user group
                    listView.setAdapter(userGroupAdapter);
                }
            }
        });

        Button dialogOkButton = (Button) dialogForward.findViewById(R.id.btn_ok_forward);
        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                if (null != listView.getAdapter().getItem(listView.getCheckedItemPosition())) {
                    if (listView.getAdapter().getItem(listView.getCheckedItemPosition()) instanceof User) {
                        User assignTo = (User) listView.getAdapter().getItem(listView.getCheckedItemPosition());
                        forwardTicketToUser(assignTo, dialogForward);
                    } else {
                        UserGroup assignGroup = (UserGroup) listView.getAdapter().getItem(listView.getCheckedItemPosition());
                        forwardTicketToUserGroup(assignGroup, dialogForward);
                    }
                    Components.hideProgressDialog(progressDialog);
                } else {
                    Components.hideProgressDialog(progressDialog);
                }
            }
        });

        Button dialogCancelButton = (Button) dialogForward.findViewById(R.id.btn_cancel_forward);
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Components.hideProgressDialog(progressDialog);
                Toast.makeText(TicketDetailActivity.this, "Assignee not set.", Toast.LENGTH_SHORT).show();
                dialogForward.dismiss();
            }
        });
        dialogForward.show();
    }

    private void forwardTicketToUser(final User user, final Dialog dialogForward) {
//        Components.setpDialog(dialogForward.getContext());
        showProgress();
        Call<CommentResponse> call = apiService.assignTicketToUser("Bearer " + authUser.getAccessToken(), tv_id, user.getUserId());
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(TicketDetailActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
                        Picasso.with(TicketDetailActivity.this)
                                .load(user.getAvatar())
                                .resize(50, 50).into(assigneeImage);
                        assigneeName.setText(user.getUserName());

                        //creating a new assignee to pass back to the ticket list.
                        User assignee = new User();
                        assignee.setAvatar(user.getAvatar());
                        assignee.setName(user.getUserName());
                        newTicket.setAssignee(assignee); // now the passed ticket has changed with new assignee
                        isTicketChanged = true;
                    } else {
                        Toast.makeText(TicketDetailActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Components.hideProgressDialog(progressDialog);
                    dialogForward.dismiss();
                } else {
                    Toast.makeText(TicketDetailActivity.this, "forward ticket failed. Check internet connection.", Toast.LENGTH_SHORT).show();
                    Components.hideProgressDialog(progressDialog);
                    dialogForward.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
                dialogForward.dismiss();
            }
        });
    }

    private void forwardTicketToUserGroup(final UserGroup group, final Dialog dialogForward) {
        showProgress();
        Call<CommentResponse> call = apiService.assignTicketToGroup("Bearer " + authUser.getAccessToken(), tv_id, group.getUserId());
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(TicketDetailActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
                        assigneeImage.setImageResource(R.drawable.avatar);
                        assigneeName.setText(group.getName());

                        //creating a new assignee to pass back to the ticket list.
                        User assignee = new User();
                        assignee.setAvatar("");
                        assignee.setName(group.getName());
                        newTicket.setAssignee(assignee); // now the passed ticket has changed with new assignee
                        isTicketChanged = true;
                    } else {
                        Toast.makeText(TicketDetailActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Components.hideProgressDialog(progressDialog);
                    dialogForward.dismiss();
                } else {
                    Toast.makeText(TicketDetailActivity.this, "forward ticket failed. Check internet connection.", Toast.LENGTH_SHORT).show();
                    Components.hideProgressDialog(progressDialog);
                    dialogForward.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
                dialogForward.dismiss();
            }
        });
    }

    private List<UserGroup> getAllUserGroups() {
        final List<UserGroup> userGroupList = new ArrayList<UserGroup>();
        UserApiInterface apiService = UserService.getClient().create(UserApiInterface.class);
        Call<UserGroupResponse> call = apiService.getAllUserGroups("Bearer " + authUser.getAccessToken()); // change the number of tickets to be fetched
        call.enqueue(new Callback<UserGroupResponse>() {
            @Override
            public void onResponse(Call<UserGroupResponse> call, Response<UserGroupResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Components.hideProgressDialog(progressDialog);
                        userGroupList.addAll(response.body().getResults());
                    } else {
                        Components.hideProgressDialog(progressDialog);
                        Toast.makeText(TicketDetailActivity.this, "Retrieving user groups failed. No response body", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Components.hideProgressDialog(progressDialog);
                    Toast.makeText(TicketDetailActivity.this, "Retrieving user groups failed. No response" + response.body().isSuccess(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UserGroupResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
                Toast.makeText(TicketDetailActivity.this, "Retrieving user groups failed", Toast.LENGTH_LONG).show();
            }
        });
        return userGroupList;
    }

    private List<User> getAllUsers(final ListView listView) {
        showProgress();
        final List<User> userList = new ArrayList<User>();
        UserApiInterface apiService = UserService.getClient().create(UserApiInterface.class);
        Call<UsersResponse> call = apiService.getAllUsers("Bearer " + authUser.getAccessToken()); // change the number of tickets to be fetched
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Components.hideProgressDialog(progressDialog);
                        userList.addAll(response.body().getResults());
                        ArrayAdapter<User> adapter = new ArrayAdapter<User>(TicketDetailActivity.this, android.R.layout.simple_list_item_single_choice,
                                userList);
                        listView.setAdapter(adapter);
                        listView.invalidate();
                    } else {
                        Components.hideProgressDialog(progressDialog);
                        Toast.makeText(TicketDetailActivity.this, "Retrieving users failed. No response body", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Components.hideProgressDialog(progressDialog);
                    Toast.makeText(TicketDetailActivity.this, "Retrieving users failed. No response" + response.body().isSuccess(), Toast.LENGTH_LONG).show();
                }
                Components.hideProgressDialog(progressDialog);
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
                Toast.makeText(TicketDetailActivity.this, "Retrieving users failed", Toast.LENGTH_LONG).show();
            }
        });
        return userList;
    }

    private void pickTicket() {
        showProgress();
        Call<CommentResponse> call = apiService.pickTicket("Bearer " + authUser.getAccessToken(), tv_id);
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(TicketDetailActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
                        Picasso.with(TicketDetailActivity.this)
                                .load(jwtObject.getContext().getAvatar())
                                .resize(50, 50).into(assigneeImage);
                        assigneeName.setText(jwtObject.getIss());

                        //creating a new assignee to pass back to the ticket list.
                        User assignee = new User();
                        assignee.setAvatar(jwtObject.getContext().getAvatar());
                        assignee.setName(jwtObject.getIss());
                        newTicket.setAssignee(assignee); // now the passed ticket has changed with new assignee
                        isTicketChanged = true;

                    } else {
                        Toast.makeText(TicketDetailActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Components.hideProgressDialog(progressDialog);
                } else {
                    Toast.makeText(TicketDetailActivity.this, "pick ticket failed. Check internet connection.", Toast.LENGTH_SHORT).show();
                    Components.hideProgressDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
            }
        });
    }

    private void goToUpdate() {
        Gson gson = new Gson();
        NewTicketActivity.update = newTicket.getTicketId();
        Intent intent = new Intent(this, NewTicketActivity.class);
        String jsonTicket = gson.toJson(newTicket);
        intent.putExtra("ticket", jsonTicket);
        startActivityForResult(intent, CHANGE_TICKET_DETAIL);
    }

    // getting comments,engagement sessions(use for sending comments), sub tickets, attachment images
    private void getExtraTicketDetails(String ticketId) {

        Call<OneTicketResponse> call = apiService.getTicketDetail("Bearer " + authUser.getAccessToken(), ticketId);
        call.enqueue(new Callback<OneTicketResponse>() {
            @Override
            public void onResponse(Call<OneTicketResponse> call, Response<OneTicketResponse> response) {
                ticketResult = response.body().getTicketResult();
                if (null == ticketResult) {
                } else {
                    if (ticketResult.getComments().isEmpty()) {
                    } else {
                        List<Comment> comments = ticketResult.getComments();
                        commentAdapter = new CommentAdapter(TicketDetailActivity.this, R.layout.comment_item, comments);
                        commentsContainer.setAdapter(commentAdapter);
                    }
                    // getting sub tickets
                    if (ticketResult.getSubTickets().isEmpty()) {
                    } else {
                        show_sub_tickets.setVisibility(View.VISIBLE);
                        isSubTicketsVisible = false; // this is the first assignment for this boolean
                        List<SubTicket> subTickets = ticketResult.getSubTickets();
                        no_of_sub_tickets.setText(String.valueOf(subTickets.size()));
                        subTicketAdapter = new SubTicketAdapter(TicketDetailActivity.this, R.layout.sub_ticket_item, subTickets);
                        sub_ticket_container.setAdapter(subTicketAdapter);
                    }
                    // getting attachments
                    if (ticketResult.getAttachments().isEmpty()) {
                    } else {
//                        List<Attachment> attachments = ticketResult.getAttachments();
//                        List<String> urls = new ArrayList<String>();
//                        for (Attachment attach: attachments) {
//                            String url = "http://fileservice.app.veery.cloud/DVP/API/1.0.0.0/InternalFileService/File/Thumbnail/"+
//                                    newTicket.getTenant()+"/"+newTicket.getCompany()+"/"+attach.getAttachmentUrl()+"/SampleAttachment";
//                            urls.add(url);
//                        }
//                        attachmentAdapter = new AttachmentAdapter(TicketDetailActivity.this, R.layout.attachment_item, urls);
//                        scanned_image_container.setAdapter(attachmentAdapter);
//                        scanned_image_container.setVisibility(View.VISIBLE);


                        List<Attachment> attachments = ticketResult.getAttachments();
                        for (final Attachment attach: attachments) {
                            ImageView retrievedImage = new ImageView(context);
                            StringBuilder url = new StringBuilder();
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(140, 140);
                            lp.setMargins(2, 2, 2, 2);
                            retrievedImage.setLayoutParams(lp);
                            retrievedImage.setPadding(2, 0, 2, 0);
                            retrievedImage.setImageResource(R.drawable.avatar);

                            url.append("http://fileservice.app.veery.cloud/DVP/API/1.0.0.0/InternalFileService/File/Thumbnail/");
                            url.append(newTicket.getTenant());
                            url.append("/");
                            url.append(newTicket.getCompany());
                            url.append("/");
                            url.append(attach.getAttachmentUrl());
                            url.append("/SampleAttachment");
//
//                            String url = "http://fileservice.app.veery.cloud/DVP/API/1.0.0.0/InternalFileService/File/Thumbnail/"+
//                                    newTicket.getTenant()+"/"+newTicket.getCompany()+"/"+attach.getAttachmentUrl()+"/SampleAttachment";

                            Picasso.with(context).load(url.toString()).resize(50, 50).placeholder(R.drawable.no_preview)
                                    .error(R.drawable.error_loading).into(retrievedImage);
                            retrievedImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    StringBuilder downloadUrl = new StringBuilder();
                                    downloadUrl.append("http://fileservice.app.veery.cloud/DVP/API/1.0.0.0/InternalFileService/File/Download/");
                                    downloadUrl.append(newTicket.getTenant());
                                    downloadUrl.append("/");
                                    downloadUrl.append(newTicket.getCompany());
                                    downloadUrl.append("/");
                                    downloadUrl.append(attach.getAttachmentUrl());
                                    downloadUrl.append("/SampleAttachment");

                                    Intent intent = new Intent(TicketDetailActivity.this,DownloadAttachmentActivity.class);
                                    intent.putExtra(DOWNLOAD_URL,downloadUrl.toString());
                                    intent.putExtra(FILE_NAME,attach.getAttachmentName());
                                    startActivity(intent);
                                }
                            });

                            scanned_image_container.addView(retrievedImage);
                        }
                    }
                }
                Components.hideProgressDialog(progressDialog);
            }

            @Override
            public void onFailure(Call<OneTicketResponse> call, Throwable t) {
                // Log error here since request failed
                Components.hideProgressDialog(progressDialog);
                Log.e("Test", t.toString());
            }
        });
    }

    private void sendNewComment(String comment, String commentType) {
        EngagementSession engagementSession = ticketResult.getEngagementSession();
        String engagementId = "";
        String channelFrom = "";
        String channelTo = "";
        String channel = "";
        String commentVisibility = commentType.toLowerCase();
        if (null != engagementSession) {
            engagementId = engagementSession.getEngagementId();
            channelFrom = engagementSession.getChannelFrom();
            if (commentVisibility.equals("public")) {
                channelTo = engagementSession.getChannelTo();
                channel = engagementSession.getChannel();
            }
        }

        NewComment newComment = new NewComment();
        newComment.setBody(comment);
        newComment.setBodyType("text");
        newComment.setType("comment");
        newComment.setPublicType(commentVisibility);// here it should be commentType parameter
        newComment.setChannel(channel);
        newComment.setChannelFrom(channelTo); // channel changed on purpose
        newComment.setChannelTo(channelFrom);
        newComment.setEngagementSession("");
        newComment.setReplySession(engagementId);

        Call<CommentResponse> call = apiService.sendComment("Bearer " + authUser.getAccessToken(), tv_id, newComment);
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.body().isSuccess()) {
                    Toast.makeText(TicketDetailActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
                    getExtraTicketDetails(tv_id);
                } else {
                    Toast.makeText(TicketDetailActivity.this, "Failed to add the comment. " + response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();

                }
                Components.hideProgressDialog(progressDialog);
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
                Toast.makeText(TicketDetailActivity.this, "Failed to add the comment. " + t.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Test", t.toString());
            }
        });
    }

    // when editing the ticket details and retrieve edited data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == CHANGE_TICKET_DETAIL) { // update a ticket
            isTicketChanged = true;
            String passingTicket = data.getStringExtra("passingTicket");
            Ticket changedTicket = new Gson().fromJson(passingTicket, Ticket.class);
            newTicket = changedTicket;
            tv_subject.setText(newTicket.getSubject());
            tv_description.setText(newTicket.getDescription());

            User assignee = newTicket.getAssignee();
            if (null != assignee) {
                if (null != assignee.getAvatar() && !assignee.getAvatar().isEmpty()) {
                    Picasso.with(this).load(assignee.getAvatar()).resize(50, 50).into(assigneeImage);
                }
                if (!assignee.getName().isEmpty()) {
                    assigneeName.setText(assignee.getName());
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == CREATE_SUB_TICKET) {
            isSubTicketCreated = data.getBooleanExtra("isSubTicketCreated", false);
        } else if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            try {
                final ImageView scannedImage = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(140, 140);
                lp.setMargins(2, 2, 2, 2);
                scannedImage.setLayoutParams(lp);
                scannedImage.setPadding(2, 0, 2, 0);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                final String fileName;
                final String fileSize;

                Cursor returnCursor =
                        getContentResolver().query(uri, null, null, null, null);
                try {
                    if (returnCursor != null && returnCursor.moveToFirst()) {
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        fileName = returnCursor.getString(nameIndex);  // returns as 22423434.jpg
                        fileSize = Long.toString(returnCursor.getLong(sizeIndex)); // always returns 0


                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        stream.close(); // to avoid memory leaks

                        //uploading the image
                        AttachmentApiInterface attachmentUploadService = AttachmentUploadService.getClient()
                                .create(AttachmentApiInterface.class);

                        RequestBody fileCategory = RequestBody.create(
                                MediaType.parse("multipart/form-data"), "TICKET_ATTACHMENTS");

                        RequestBody imageFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("", "", imageFile);

                        Call<ImageUploadResponse> call = attachmentUploadService.sendScannedImage(
                                "Bearer " + authUser.getAccessToken(), fileCategory, body);

                        showProgress();
                        call.enqueue(new Callback<ImageUploadResponse>() {
                            @Override
                            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                                if (response.body().isSuccess()) {
                                    Attachment attachment = new Attachment();
                                    attachment.setAttachmentName(fileName);
                                    attachment.setAttachmentSize("10"); //fileSize
                                    attachment.setAttachmentType("image/jpeg");
                                    attachment.setAttachmentUrl(response.body().getResult());

                                    scannedImage.setImageBitmap(bitmap);
                                    scanned_image_container.addView(scannedImage);

                                    attachImageToTicket(newTicket.getTicketId(), attachment);
                                } else {
                                    Toast.makeText(TicketDetailActivity.this, "Attachment failed", Toast.LENGTH_SHORT).show();
                                    Components.hideProgressDialog(progressDialog);
                                }
                            }

                            @Override
                            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                                Toast.makeText(TicketDetailActivity.this, "Attachment failed. No response"+t, Toast.LENGTH_SHORT).show();
                                Components.hideProgressDialog(progressDialog);
                            }
                        });

                    }
                } catch (Exception e) {
                    Toast.makeText(TicketDetailActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    Components.hideProgressDialog(progressDialog);
                } finally {
                    returnCursor.close();
                }

                this.getContentResolver().delete(uri, null, null);  // deleting the scanned image

            } catch (IOException e) {
                Toast.makeText(TicketDetailActivity.this, "IO Error occurred", Toast.LENGTH_SHORT).show();
                Components.hideProgressDialog(progressDialog);
            } catch (OutOfMemoryError e) {
                Toast.makeText(TicketDetailActivity.this, "Out of memory Error occurred", Toast.LENGTH_SHORT).show();
                Components.hideProgressDialog(progressDialog);
            } catch (Exception e) {
                Toast.makeText(TicketDetailActivity.this, "Error occurred"+ e, Toast.LENGTH_SHORT).show();
                Components.hideProgressDialog(progressDialog);
            } finally {
//                bitmap.recycle();
            }
        }
        else if(requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            Log.d("Test", "File Uri: " + uri.toString());
            // Get the path
            try {
                String path = getPath(this, uri);
                Log.d("Test", "File Path: " + path);
                sendAttachment(path,uri);
            } catch (URISyntaxException e) {
                Log.d("Test","URI exception");
            }
        }
    }

    private void sendAttachment(String path,Uri uri){
        try {

            AttachmentApiInterface attachmentUploadService = AttachmentUploadService.getClient()
                    .create(AttachmentApiInterface.class);

            RequestBody fileCategory = RequestBody.create(MediaType.parse("multipart/form-data"), "TICKET_ATTACHMENTS");

            final File file = new File(path);
            final String fileType = getContentResolver().getType(uri);
            Log.d("Test","***** "+fileType);

            RequestBody attachmentFile = RequestBody.create(MediaType.parse(fileType), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("", "", attachmentFile);

            Call<ImageUploadResponse> call = attachmentUploadService.sendScannedImage(
                    "Bearer " + authUser.getAccessToken(), fileCategory, body);

            showProgress();
            call.enqueue(new Callback<ImageUploadResponse>() {
                @Override
                public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                    if (null != response && null != response.body()) {
                        if (response.body().isSuccess()) {
                            Attachment attachment = new Attachment();
                            attachment.setAttachmentName(file.getName());
                            attachment.setAttachmentSize("10"); //fileSize
                            attachment.setAttachmentType(fileType);
                            attachment.setAttachmentUrl(response.body().getResult());

                            final ImageView attachmentPreview = new ImageView(context);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(140, 140);
                            lp.setMargins(2, 2, 2, 2);
                            attachmentPreview.setLayoutParams(lp);
                            attachmentPreview.setPadding(2, 0, 2, 0);

                            attachmentPreview.setImageResource(R.drawable.no_preview);
                            scanned_image_container.addView(attachmentPreview);

                            attachImageToTicket(newTicket.getTicketId(), attachment);
                        } else {
                            Toast.makeText(TicketDetailActivity.this, "Attachment failed", Toast.LENGTH_SHORT).show();
                            Components.hideProgressDialog(progressDialog);
                        }
                    }
                    else{
                        Gson gson = new Gson();
                        Log.d("Test",gson.toJson(response));
                        Toast.makeText(TicketDetailActivity.this, "Attaching file to server failed", Toast.LENGTH_SHORT).show();
                        Components.hideProgressDialog(progressDialog);
                    }
                }

                @Override
                public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                    Toast.makeText(TicketDetailActivity.this, "Attachment failed. No response" + t, Toast.LENGTH_SHORT).show();
                    Components.hideProgressDialog(progressDialog);
                }
            });
        }catch (Exception e){
            Log.d("Test", "exception on attachment"+ e);
        }
    }

    private void startScan() {
        int preference = ScanConstants.OPEN_CAMERA;
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    private void attachImageToTicket(final String ticketId, final Attachment attachment) {
        Call<CommentResponse> call = apiService.addAttachmentToTicket("Bearer " + authUser.getAccessToken(), ticketId, attachment);
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TicketDetailActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
                    Components.hideProgressDialog(progressDialog);
                } else {
                    Components.hideProgressDialog(progressDialog);
                    retryAttachProcess(ticketId,attachment,"Add attachment to ticket failed.");
                }
            }
            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
                retryAttachProcess(ticketId,attachment,"Add attachment to ticket failed. Check internet connection.");
            }
        });
    }

    private void showProgress() {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(TicketDetailActivity.this);
            Components.showProgressDialog(progressDialog);
        } else {
            Components.showProgressDialog(progressDialog);
        }
    }

    private void retryAttachProcess(final String ticketId, final Attachment attachment, final String message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(R.string.retry_tittle);
        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                attachImageToTicket(ticketId,attachment);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(TicketDetailActivity.this,"Please check your connection.",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", "*/*");
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (getPackageManager().resolveActivity(sIntent, 0) != null){
            // it is device with samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
        }
        else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }


        try {
            startActivityForResult(Intent.createChooser(chooserIntent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            } finally {
                cursor.close();
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    private void changeFont() {
        List<View> sendViews = new ArrayList<>();
        sendViews.add(tv_subject);
        sendViews.add(tv_status);
        sendViews.add(tv_priority);
        sendViews.add(tv_description);
        sendViews.add(tv_reference);
        sendViews.add(tv_created);
        sendViews.add(requsterName);
        sendViews.add(submitterName);
        sendViews.add(assigneeName);
        sendViews.add(requesterText);
        sendViews.add(submitterText);
        sendViews.add(assigneeText);
        sendViews.add(subTicketText);
        sendViews.add(commentsText);
        FontChanger.changeFont(sendViews, context, FontChanger.FONT_REGULAR);
    }
}
