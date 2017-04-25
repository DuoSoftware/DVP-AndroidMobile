package apps.veery.com.facetonecrm;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.veery.com.model.Auth;
import apps.veery.com.model.CurrentUserResponse;
import apps.veery.com.model.NewTicketResponse;
import apps.veery.com.model.Ticket;
import apps.veery.com.model.TicketTag;
import apps.veery.com.model.TicketTagResponse;
import apps.veery.com.model.User;
import apps.veery.com.model.UsersResponse;
import apps.veery.com.restInterfaces.TicketApiInterface;
import apps.veery.com.requestmodel.resource.NewTicket;
import apps.veery.com.restInterfaces.UserApiInterface;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.TicketService;
import apps.veery.com.service.TicketViewServiceGenarator;
import apps.veery.com.service.UserService;
import apps.veery.com.utilities.Components;
import apps.veery.com.utilities.FontChanger;
import apps.veery.com.utilities.Validations;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTicketActivity extends AppCompatActivity {

    TextView labelTicketSubject, labelTicketPriority, labelTicketTag, labelTicketDes, labelTicketUser, newTicketAssignMe, labelTicketType;
    EditText newTicketSubject, newTicketDescription;
    LinearLayout tagContainer;
    private ProgressDialog progressDialog;
    Button addTagButton;
    Spinner spinnerNewTicket, newTicketAssignee;
    RadioGroup priorityRadioGroup;
    RadioButton radioButton;
    public static String update = "empty";
    Ticket res;
    int requestCode;

    User user;
    SweetAlertDialog sweetAlertDialog;
    ArrayAdapter<User> adapterassignee;
    List<TicketTag> tagCategories;
    Map<String, TicketTag> tags;
    List<String> tagsForTicket;
    TicketApiInterface apiService;

    private Auth authUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        setContentView(R.layout.activity_new_ticket);

        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        if(null != sharedpreferences.getString(MainActivity.USER,null)) {
            authUser = new Gson().fromJson(sharedpreferences.getString(MainActivity.USER, null), Auth.class);
        }

        progressDialog = new ProgressDialog(NewTicketActivity.this);
        sweetAlertDialog = Components.showProgressMessage(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new_ticket);
        requestCode = getIntent().getIntExtra("requestCode", 0); // this action valid for create new ticket,create sub ticket or update ticket
        if (requestCode == TicketFragment.NEW_TICKET_REQ_CODE) {
            toolbar.setTitle("Create New Ticket");
        } else if (requestCode == TicketDetailActivity.CREATE_SUB_TICKET) {
            toolbar.setTitle("Create New Sub Ticket");
        }
        setSupportActionBar(toolbar);

        apiService = TicketService.getClient().create(TicketApiInterface.class);

        labelTicketSubject = (TextView) findViewById(R.id.labelTicketSubject);
        labelTicketPriority = (TextView) findViewById(R.id.labelTicketPriority);
        labelTicketTag = (TextView) findViewById(R.id.labelTicketTag);
        labelTicketDes = (TextView) findViewById(R.id.labelTicketDes);
        labelTicketUser = (TextView) findViewById(R.id.labelTicketUser);
        labelTicketType = (TextView) findViewById(R.id.labelTicketType);
        newTicketAssignMe = (TextView) findViewById(R.id.newTicketAssignMe);
        spinnerNewTicket = (Spinner) findViewById(R.id.spinnerNewTicket);
        priorityRadioGroup = (RadioGroup) findViewById(R.id.priorityRadioGroup);

        newTicketSubject = (EditText) findViewById(R.id.newTicketSubject);
        newTicketDescription = (EditText) findViewById(R.id.newTicketDescription);
        newTicketAssignee = (Spinner) findViewById(R.id.newTicketAssignee);
        newTicketAssignee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user = (User) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tagContainer = (LinearLayout) findViewById(R.id.tagContainer);
        addTagButton = (Button) findViewById(R.id.addTagButton);

        changeFont();

        if (null == tagCategories) {
            tagCategories = new ArrayList<>();
            getTagCategories();
        }

        if (null == tags) {
            tags = new HashMap<>();
            getTags();
        }


        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StringBuffer tagToPass = new StringBuffer();
                final Dialog dialog;
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewTicketActivity.this);
                final ArrayAdapter<TicketTag> arrayAdapter = new ArrayAdapter<TicketTag>(NewTicketActivity.this, android.R.layout.simple_list_item_1);
                if (tagCategories.size() == 0) {
                    Toast.makeText(NewTicketActivity.this, "Cannot receive ticket tags", Toast.LENGTH_SHORT).show();
                    //if necessary can implement retry mechanism here......
                } else {
                    arrayAdapter.addAll(tagCategories);
                }

                ListView listView = new ListView(NewTicketActivity.this);
                listView.setAdapter(arrayAdapter);

                alertDialog.setTitle("Select Tag");
                alertDialog.setNegativeButton(
                        "cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.setView(listView);
                dialog = alertDialog.show();


                listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                        tagToPass.append(arrayAdapter.getItem(position).getName());
                        if (arrayAdapter.getItem(position).getTags().size() == 0) {
                            dialog.dismiss();
                            setTagToUI(tagToPass.toString());
                        } else {
                            tagToPass.append(".");
                            List<String> tagIds = arrayAdapter.getItem(position).getTags();
                            arrayAdapter.clear();
                            for (String tagId : tagIds) {
                                if (null != tags.get(tagId)) {
                                    arrayAdapter.add(tags.get(tagId));
                                }
                            }
                        }
                    }
                });

            }

        });

        if (!update.equals("empty")) {
//            toolbar.getMenu().clear();
            toolbar.setTitle("Update ticket");
            String json = getIntent().getStringExtra("ticket");
            Gson gson = new Gson();
            res = gson.fromJson(json, Ticket.class);
            setUsersToAssigneeList(res);
            spinnerNewTicket.setSelection(((ArrayAdapter<String>) spinnerNewTicket.getAdapter()).getPosition(res.getType()));
            spinnerNewTicket.setEnabled(false);
            newTicketSubject.setText(res.getSubject());
            newTicketSubject.setEnabled(true);
            newTicketDescription.setText(res.getDescription());
            newTicketDescription.setEnabled(true);
//            priorityRadioGroup.sets(getRadioButtonID(res.getPriority()));
            radioButton = (RadioButton) findViewById(getRadioButtonID(res.getPriority()));
            radioButton.setChecked(true);
            priorityRadioGroup.setEnabled(false);
        } else {
            setUsersToAssigneeList(null);
        }

        //set logged user as assignee
        newTicketAssignMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApiInterface userService = UserService.getClient().create(UserApiInterface.class);
                Call<CurrentUserResponse> call = userService.getCurrentUser("Bearer " + authUser.getAccessToken());
                call.enqueue(new Callback<CurrentUserResponse>() {
                    @Override
                    public void onResponse(Call<CurrentUserResponse> call, Response<CurrentUserResponse> response) {
                        if (response.isSuccessful()) {
                            User currentUser = response.body().getUser();

                            if (null != currentUser) {
                                if (null != newTicketAssignee.getAdapter()) {
                                    getIndex(adapterassignee, currentUser.getName());
                                    Toast.makeText(NewTicketActivity.this, "Ticket assigned to me", Toast.LENGTH_SHORT).show();
                                } else {
                                    // if adapter is null do nothing
                                    Toast.makeText(NewTicketActivity.this, "Setting assignees failed.Ticket Not assigned to me.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(NewTicketActivity.this, "Current user not retrieved.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NewTicketActivity.this, "Unable to get current user. ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentUserResponse> call, Throwable t) {
                        Toast.makeText(NewTicketActivity.this, "Error occurred. Check your connection. " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getTagCategories() {
        Call<TicketTagResponse> call = apiService.getTicketCategories("Bearer " + authUser.getAccessToken());
        call.enqueue(new Callback<TicketTagResponse>() {

            @Override
            public void onResponse(Call<TicketTagResponse> call, Response<TicketTagResponse> response) {
                Components.hideProgressDialog(progressDialog);
                if (response.isSuccessful()) {
                    List<TicketTag> ticketCategories = response.body().getResults();

                    if (null != ticketCategories) {
                        for (TicketTag category : ticketCategories) {
                            tagCategories.add(category);
                        }
                    } else {
                        Toast.makeText(NewTicketActivity.this, "Ticket categories not retrieved.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewTicketActivity.this, "Unable to get a category response. ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TicketTagResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
                Toast.makeText(NewTicketActivity.this, "Error occurred. Check your connection. " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTags() {
        Call<TicketTagResponse> call = apiService.getAllTicketTags("Bearer " + authUser.getAccessToken());
        call.enqueue(new Callback<TicketTagResponse>() {

            @Override
            public void onResponse(Call<TicketTagResponse> call, Response<TicketTagResponse> response) {
                Components.hideProgressDialog(progressDialog);
                if (response.isSuccessful()) {
                    List<TicketTag> ticketCategories = response.body().getResults();

                    if (null != ticketCategories) {
                        for (TicketTag category : ticketCategories) {
                            tags.put(category.getTicketId(), category);
                        }
                    } else {
                        Toast.makeText(NewTicketActivity.this, "Ticket tags not retrieved.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewTicketActivity.this, "Unable to get a tag response. ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TicketTagResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
                Toast.makeText(NewTicketActivity.this, "Error occurred. Check your connection. " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTagToUI(final String tag) {
        if (null == tagsForTicket)
            tagsForTicket = new ArrayList<String>();
        final TextView createdTag = new TextView(NewTicketActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 2, 4, 2);
        createdTag.setLayoutParams(lp);
        createdTag.setPadding(4, 0, 4, 0);
        createdTag.setTextColor(Color.WHITE);
        createdTag.setBackgroundColor(Color.GRAY);
        createdTag.setText(tag);
        createdTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagContainer.removeView(v);
                tagsForTicket.remove(tag);
            }
        });
        tagsForTicket.add(tag);
        tagContainer.addView(createdTag);
    }

    private int getIndex(ArrayAdapter<User> adapterassignee, String myString) {

        int index = 0;

        for (int i = 0; i < adapterassignee.getCount(); i++) {
            if (myString.equals(adapterassignee.getItem(i).getName())) {
                newTicketAssignee.setSelection(i);
                break;
            }
        }
        return index;
    }

    private void setUsersToAssigneeList(final Ticket ticket) {
        UserApiInterface apiService = UserService.getClient().create(UserApiInterface.class);
        Call<UsersResponse> call = apiService.getAllUsers("Bearer " + authUser.getAccessToken()); // change the number of tickets to be fetched
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Components.hideProgressDialog(progressDialog);
                        adapterassignee = new ArrayAdapter<User>(NewTicketActivity.this, android.R.layout.simple_list_item_1,
                                response.body().getResults());
                        newTicketAssignee.setAdapter(adapterassignee);
                        if (null != ticket && null != ticket.getAssignee()) {
                            getIndex(adapterassignee, ticket.getAssignee().getName());
                        }

                    } else {
                        Components.hideProgressDialog(progressDialog);
                        Toast.makeText(NewTicketActivity.this, "Retrieving users failed. No response body", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Components.hideProgressDialog(progressDialog);
                    Toast.makeText(NewTicketActivity.this, "Retrieving users failed. No response" + response.body().isSuccess(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                Components.hideProgressDialog(progressDialog);
                Toast.makeText(NewTicketActivity.this, "Retrieving users failed", Toast.LENGTH_LONG).show();
                NewTicketActivity.this.finish();// finishing this activity
            }
        });
    }

    private int getRadioButtonID(String stat) {
        if (stat.equals("urgent")) {
            return R.id.rbPriorityUrgent;

        } else if (stat.equals("high")) {
            return R.id.rbPriorityHigh;
        } else if (stat.equals("normal")) {
            return R.id.rbPriorityNormal;
        } else if (stat.equals("low")) {
            return R.id.rbPriorityLow;
        } else {
            return 0;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        update = "empty";
        TicketFragment.filterid = "";
        this.finish();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!update.equals("empty")) {
            getMenuInflater().inflate(R.menu.menu_ticket_update, menu);
//            MenuItem sendTicket = menu.findItem(R.id.menu_ticket_create);
        } else {
            getMenuInflater().inflate(R.menu.menu_ticket_create, menu);
//            MenuItem sendTicket = menu.findItem(R.id.menu_ticket_create);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (!Validations.isInternetOn(getApplicationContext())) {
            Components.showErrorMessage(this, "Opps..", "Check Your Internet Connection");

        } else {
            if (id == R.id.menu_ticket_create) {
                createNewTicket();
                return true;
            } else if (id == R.id.menu_ticket_detail_update) {
                updateTicket();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateTicket() {
        String subject, description;

        if (newTicketSubject.getText().length() != 0) {
            subject = newTicketSubject.getText().toString();
        } else {
            Toast.makeText(this, "Please enter a subject", Toast.LENGTH_LONG).show();
            return;
        }


        if (newTicketDescription.getText().length() != 0) {
            description = newTicketDescription.getText().toString();
        } else {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_LONG).show();
            return;
        }
//        newTicket.setTicketId(res.getTicketId());
//        newTicket.setType(ticketType);
//        newTicket.setSubject(subject);
//        newTicket.setDescription(description);
//        newTicket.setPriority(priority);
//        newTicket.setStatus("new");
//        newTicket.setChannel("new");
        res.setSubject(subject);
        res.setDescription(description);

        sendTicketUpdate(res);

    }

    private void sendUpdateAssignee(final Ticket ticket) {

        if (null == res && null == user) {

        } else {
            if (null == res.getAssignee() && null != user) {
                changeAssignee(ticket);
            } else if (null != res.getAssignee() && null != user) {

                if (res.getAssignee().getUserId().equals(user.getUserId())) { // assignee not change. but need to dismiss dialog and go back
                    sweetAlertDialog.dismiss();
                    Intent intent = getIntent();
                    intent.putExtra("passingTicket", new Gson().toJson(ticket));
//                    intent.putExtra("ticketPosition", ticketPosition);
                    setResult(Activity.RESULT_OK, intent);
                    Toast.makeText(NewTicketActivity.this, "Success update ticket", Toast.LENGTH_LONG).show();
                    update = "empty";
                    NewTicketActivity.this.finish();
                } else {
                    changeAssignee(ticket);
                }
            }
        }
    }

    private void changeAssignee(final Ticket ticket) {
        ticket.setAssignee(user);
        TicketViewServiceGenarator.TicketViewServiceFactory.getInstance().getAssignToUser("Bearer " + authUser.getAccessToken(), res.getTicketId(), user.getUserId()).enqueue(new Callback<NewTicketResponse>() {
            @Override
            public void onResponse(Call<NewTicketResponse> call, Response<NewTicketResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        sweetAlertDialog.dismiss();
                        Intent intent = getIntent();
                        intent.putExtra("passingTicket", new Gson().toJson(ticket));
//                    intent.putExtra("ticketPosition", ticketPosition);
                        setResult(Activity.RESULT_OK, intent);
                        Toast.makeText(NewTicketActivity.this, "Success change assignee", Toast.LENGTH_LONG).show();
                        update = "empty";
                        NewTicketActivity.this.finish();
//                        Intent intent = new Intent(getApplicationContext(), TicketDetailActivity.class);
//                        String newTicketString = new Gson().toJson(ticket);
//                        intent.putExtra("tv_tag", newTicketString);
//                        startActivity(intent);
                    } else {
                        Components.changeAlert(sweetAlertDialog, "opps!", response.body().getCustomMessage(), SweetAlertDialog.ERROR_TYPE, "OK");
                    }
                } else {
                    try {
                        Components.changeAlert(sweetAlertDialog, "opps!", response.errorBody().string(), SweetAlertDialog.ERROR_TYPE, "OK");
                    } catch (IOException e) {
                        Components.changeAlert(sweetAlertDialog, "opps!", e.getMessage(), SweetAlertDialog.ERROR_TYPE, "OK");
                    }
                }
            }

            @Override
            public void onFailure(Call<NewTicketResponse> call, Throwable t) {
                Components.changeAlert(sweetAlertDialog, "opps!", t.getMessage(), SweetAlertDialog.ERROR_TYPE, "OK");
            }
        });
    }

    private void sendTicketUpdate(final Ticket ticket) {
        sweetAlertDialog.show();
        TicketApiInterface apiService = TicketService.getClient().create(TicketApiInterface.class);
        Call<NewTicketResponse> call = apiService.getUpdateTicket("Bearer " + authUser.getAccessToken(), ticket, ticket.getTicketId());
        call.enqueue(new Callback<NewTicketResponse>() {
            @Override
            public void onResponse(Call<NewTicketResponse> call, Response<NewTicketResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
//                        sweetAlertDialog.setTitleText("Success Update ticket")
//                                .setConfirmText("OK")
//                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                        Components.changeAlert(sweetAlertDialog, "Success Update ticket", null, SweetAlertDialog.SUCCESS_TYPE, "OK");
//                        Toast.makeText(NewTicketActivity.this, response.body().getCustomMessage(), Toast.LENGTH_SHORT).show();
//                        changeFragment(new TicketFragment());
                        sendUpdateAssignee(ticket);
                    } else {
//                        dd.setTitleText("opps!").setContentText(response.body().getCustomMessage())
//                                .setConfirmText("OK")
//                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        Components.changeAlert(sweetAlertDialog, "opps!", response.body().getCustomMessage(), SweetAlertDialog.ERROR_TYPE, "OK");
                    }
                } else {
                    try {
                        Components.changeAlert(sweetAlertDialog, "opps!", response.errorBody().string(), SweetAlertDialog.ERROR_TYPE, "OK");
                    } catch (IOException e) {
                        Components.changeAlert(sweetAlertDialog, "opps!", e.getMessage(), SweetAlertDialog.ERROR_TYPE, "OK");
                    }
                }
            }

            @Override
            public void onFailure(Call<NewTicketResponse> call, Throwable t) {
                Components.changeAlert(sweetAlertDialog, "opps!", t.getMessage(), SweetAlertDialog.ERROR_TYPE, "OK");
            }
        });
    }

    private void createNewTicket() {
        NewTicket newTicket = new NewTicket();
        String ticketType = spinnerNewTicket.getSelectedItem().toString();
        String subject, priority, description;
        RadioButton rb = (RadioButton) findViewById(priorityRadioGroup.getCheckedRadioButtonId());
        priority = rb.getText().toString();

        if (newTicketSubject.getText().length() != 0) {
            subject = newTicketSubject.getText().toString();
        } else {
            Toast.makeText(this, "Please enter a subject", Toast.LENGTH_LONG).show();
            return;
        }


        if (newTicketDescription.getText().length() != 0) {
            description = newTicketDescription.getText().toString();
        } else {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_LONG).show();
            return;
        }

        User assignee = (User) newTicketAssignee.getSelectedItem();
        if (null != assignee) {
            newTicket.setAssignee(assignee.getUserId());
        }
        newTicket.setType(ticketType);
        newTicket.setSubject(subject);
        newTicket.setDescription(description);
        newTicket.setPriority(priority);
        if (null != tagsForTicket)
            newTicket.setTags(tagsForTicket);
        newTicket.setStatus("new");
        newTicket.setChannel("new");

        sendNewTicket(newTicket);
    }

    private void sendNewTicket(final NewTicket newTicket) {
        showProgress();
        TicketApiInterface apiService = TicketService.getClient().create(TicketApiInterface.class);
        Call<NewTicketResponse> call = null;
        if (requestCode == TicketFragment.NEW_TICKET_REQ_CODE) {
            call = apiService.createNewTicket("Bearer " + authUser.getAccessToken(), newTicket);
        } else if (requestCode == TicketDetailActivity.CREATE_SUB_TICKET) {
            call = apiService.createNewSubTicket("Bearer " + authUser.getAccessToken(),
                    getIntent().getStringExtra("parentTicketId"), newTicket);
        }

        call.enqueue(new Callback<NewTicketResponse>() {
            @Override
            public void onResponse(Call<NewTicketResponse> call, Response<NewTicketResponse> response) {
                Components.hideProgressDialog(progressDialog);
                if (response.body().isSuccess()) {
                    if(requestCode == TicketDetailActivity.CREATE_SUB_TICKET){
                        Intent intent = getIntent();
                        intent.putExtra("isSubTicketCreated",true);
                        setResult(Activity.RESULT_OK, intent);
                        NewTicketActivity.this.finish();
                    }else {
                        setResult(Activity.RESULT_OK, getIntent());
                        Toast.makeText(NewTicketActivity.this, response.body().getCustomMessage(), Toast.LENGTH_LONG).show();
                        NewTicketActivity.this.finish();// finishing this activity
                    }
                } else {
                    Toast.makeText(NewTicketActivity.this, "Ticket creation failed. " + response.body().getCustomMessage(), Toast.LENGTH_LONG).show();
                    NewTicketActivity.this.finish();// finishing this activity
                }
            }

            @Override
            public void onFailure(Call<NewTicketResponse> call, Throwable t) {
                // Log error here since request failed
                Components.hideProgressDialog(progressDialog);
                Toast.makeText(NewTicketActivity.this, "Ticket creation failed", Toast.LENGTH_LONG).show();
                NewTicketActivity.this.finish();// finishing this activity
            }
        });
    }

    private void changeFont() {
        List<View> sendViews = new ArrayList<>();
        sendViews.add(labelTicketSubject);
        sendViews.add(labelTicketPriority);
        sendViews.add(labelTicketTag);
        sendViews.add(labelTicketDes);
        sendViews.add(labelTicketUser);
        sendViews.add(labelTicketType);
        sendViews.add(newTicketAssignMe);
        sendViews.add(newTicketSubject);
        sendViews.add(newTicketDescription);
        FontChanger.changeFont(sendViews, this, FontChanger.FONT_REGULAR);
    }

    private void showProgress(){
        if(null == progressDialog){
            progressDialog = new ProgressDialog(NewTicketActivity.this);
            Components.showProgressDialog(progressDialog);
        }
        else{
            Components.showProgressDialog(progressDialog);
        }
    }
}
