package apps.veery.com.facetonecrm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import apps.veery.com.adapter.NotificationAdapter;
import apps.veery.com.model.Auth;
import apps.veery.com.model.Break;
import apps.veery.com.model.NotificationMessage;
import apps.veery.com.model.Revoke;
import apps.veery.com.model.State;
import apps.veery.com.model.StateResource;
import apps.veery.com.model.jwtres.Decode;
import apps.veery.com.requestmodel.resource.Contact;
import apps.veery.com.requestmodel.resource.HandlingType;
import apps.veery.com.requestmodel.resource.Resourcereq;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.ArdsService;
import apps.veery.com.service.ArdsServiceInterface;
import apps.veery.com.service.RegistrationIntentService;
import apps.veery.com.service.ServiceGenarator;
import apps.veery.com.utilities.Components;
import apps.veery.com.utilities.Validations;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NotificationAdapter.OnNotificationMessageChangedListener {

    private static final String NEWTICKETUPDATE = "empty";
    private static final String BEARERTEXT = "Bearer ";
    private static final String AVAILABLE = "Available";
    private static final String NOTAVAILABLE = "NotAvailable";
    private static final String OFFICIALBREAK = "OfficialBreak";
    private static final String MEALBREAK = "MealBreak";

    private TextView tv_nav_title, tv_nav_username, tv_nav_status;
    private Typeface type_regular, type_bold;
    private ImageView iv_nav_status, nav_back;
    private CircleImageView iv;
    private Menu menuNav;
    private ImageView iv_call, iv_ticket, iv_available, iv_meal, iv_official;
    private DrawerLayout drawer;
    private String selected_break;
    SharedPreferences.Editor editor;
    BottomBar bottomBar;
//    SweetAlertDialog swdGloble;
    int notificationCount;
    ArrayList<NotificationMessage> notificationMessages;
    private BroadcastReceiver receiver;
    Decode jwtObject;
    Auth authUser;

    private ArdsServiceInterface ardsServiceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ardsServiceInterface = ArdsService.getClient().create(ArdsServiceInterface.class);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                notificationMessages = new ArrayList<NotificationMessage>();
                notificationCount++;
                bottomBar.getTabWithId(R.id.tab_notification).setBadgeCount(notificationCount);
                // don't change automatically the fragment. let user to click on the fragment
                // save from,message,time data
                NotificationMessage message = new NotificationMessage();
                message.setFromUser(intent.getStringExtra("From"));
                message.setNotificationMessage(intent.getStringExtra("Message"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                message.setReceivedDate(simpleDateFormat.format(Calendar.getInstance().getTime()));
                notificationMessages.add(message);

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.contentContainer);
                if (currentFragment instanceof NotificationFragment) {
                    ((NotificationFragment) currentFragment).notificationAdapter.notifyDataSetChanged();
                    Toast.makeText(HomeActivity.this,"New message received",Toast.LENGTH_SHORT).show();
                }
            }
        };

        type_regular = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        type_bold = Typeface.createFromAsset(getResources().getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        editor = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE).edit();

//        swdGloble = Components.showProgressMessage(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menuNav = navigationView.getMenu();

        View headerView = navigationView.getHeaderView(0);

        if (((ViewGroup.MarginLayoutParams) drawer.getLayoutParams()).leftMargin == (int) getResources().getDimension(R.dimen.drawer_size)) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, navigationView);
            drawer.setScrimColor(Color.TRANSPARENT);
        }

        tv_nav_title = (TextView) headerView.findViewById(R.id.tv_nav_title);
        tv_nav_title.setTypeface(type_regular);
        tv_nav_username = (TextView) headerView.findViewById(R.id.tv_nav_username);
        tv_nav_username.setTypeface(type_bold);
        tv_nav_status = (TextView) headerView.findViewById(R.id.tv_nav_status);
        tv_nav_status.setTypeface(type_regular);
        iv_nav_status = (ImageView) headerView.findViewById(R.id.iv_nav_status);
        iv = (CircleImageView) headerView.findViewById(R.id.nav_iv_avatar);
        nav_back = (ImageView) headerView.findViewById(R.id.iv_nav_back);
        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        // prevent app crash.
//        if(null == AppControler.getJwtres()){
//            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
//            if(null != sharedpreferences.getString(MainActivity.USERJWT,null)){
//                Decode res = new Gson().fromJson(sharedpreferences.getString(MainActivity.USERJWT,null), Decode.class);
//                AppControler.setJwtres(res);
//            }
//        }
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        if(null != sharedpreferences.getString(MainActivity.USERJWT,null)) {
            jwtObject = new Gson().fromJson(sharedpreferences.getString(MainActivity.USERJWT, null), Decode.class);
        }
        if(null != sharedpreferences.getString(MainActivity.USER,null)) {
            authUser = new Gson().fromJson(sharedpreferences.getString(MainActivity.USER, null), Auth.class);
        }

//        Log.d("Test","token = "+sharedpreferences.getString(MainActivity.USER, null));
//        **** to see the JWT value

        tv_nav_username.setText(jwtObject.getIss());

        if (null != jwtObject.getContext().getAvatar()) {
            Picasso.with(getApplicationContext()).load(jwtObject.getContext().getAvatar()).
                    resize(80, 80).into(iv);
        } else {
            iv.setImageResource(R.drawable.avatar);
        }


//        bottomBar.setBadgeBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        bottomBar.setBackgroundColor(getResources().getColor(android.R.color.black));

        if(notificationCount != 0) {
            bottomBar.getTabWithId(R.id.tab_notification).setBadgeCount(notificationCount);
        }

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    NewTicketActivity.update = NEWTICKETUPDATE;
                    TicketFragment.filterid = "";
                    changeFragment(new DashboardFragment());
                } else if (tabId == R.id.tab_inbox) {
                    toolbar.getMenu().clear();
                    changeFragment(new TicketFragment());
                } else if (tabId == R.id.tab_engagement) {
                    NewTicketActivity.update = NEWTICKETUPDATE;
                    TicketFragment.filterid = "";
                    changeFragment(new EngagementFragment());
                } else if (tabId == R.id.tab_notification) {
                    NewTicketActivity.update = NEWTICKETUPDATE;
                    TicketFragment.filterid = "";
                    NotificationFragment notificationFragment = new NotificationFragment();
                    if(null!= notificationMessages && notificationMessages.size()!=0) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("notificationMessages", notificationMessages);
                        notificationFragment.setArguments(bundle);
                    }
                    changeFragment(notificationFragment);
                }
            }
        });

        iv_call = (ImageView) menuNav.findItem(R.id.nav_call).getActionView();
        iv_ticket = (ImageView) menuNav.findItem(R.id.nav_ticket).getActionView();
        iv_available = (ImageView) menuNav.findItem(R.id.nav_available).getActionView();
        iv_meal = (ImageView) menuNav.findItem(R.id.nav_meal).getActionView();
        iv_official = (ImageView) menuNav.findItem(R.id.nav_official).getActionView();

        iv_call.setVisibility(View.GONE);
        iv_ticket.setVisibility(View.GONE);
        iv_available.setVisibility(View.GONE);
        iv_meal.setVisibility(View.GONE);
        iv_official.setVisibility(View.GONE);

        getCurrentState();
        getCurrentStateResource();

        changeToEngagementWhenPushNotification(getIntent());

        startService(new Intent(this, RegistrationIntentService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(receiver,new IntentFilter("notification_receiver"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(receiver);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        changeToEngagementWhenPushNotification(intent);
    }

    public void changeToEngagementWhenPushNotification(Intent passedIntent){
        if(null != passedIntent.getExtras()) {
//            Log.d("Test", "***********" + passedIntent.getStringExtra("Message")); //agent_found|e35ebf52-3f3c-49c4-b71c-aea68f9d1794|61|18705056580|Extension 18705056580|94112375000|Implementation|inbound
            String values[] = passedIntent.getStringExtra("Message").split("\\|"); //agent_rejected|10bc0498-036c-47f0-a524-b587ce3d13ca
            String company = passedIntent.getStringExtra("Company");
            if ("agent_found".equals(values[0])) { // looking only for agent_found. For now no need of other message types.
                EngagementFragment engagementFragment = new EngagementFragment();
                Bundle bundle = new Bundle();
                bundle.putString("phoneNo", values[3]);
                bundle.putString("skill", values[6]);
                bundle.putString("direction", values[7]);
                bundle.putString("company", company);
                engagementFragment.setArguments(bundle);
                changeFragment(engagementFragment);
                bottomBar.setDefaultTabPosition(2);
            }
//            else if(null != passedIntent.getStringExtra("From")){ // From is coming only if it is a message
////                Log.d("Test", "*********** Home Act message" + passedIntent.getStringExtra("Message"));
//                // change the bottom bar count (count should hold in a variable)
//                notificationCount++;
//                bottomBar.getTabWithId(R.id.tab_notification).setBadgeCount(notificationCount);
//                // don't change automatically the fragment. let user to click on the fragment
//                // save from,message,time data
//                NotificationMessage message = new NotificationMessage();
//                message.setFromUser(passedIntent.getStringExtra("From"));
//                message.setNotificationMessage(passedIntent.getStringExtra("Message"));
//                message.setReceivedDate(Calendar.getInstance().getTime().toString());
//                notificationMessages.add(message);
//                Log.d("Test"," a new notification added");
//                // when user click on notification retrieve data from file and display
//                // keep and discard
//                // when home activity kills remove all the message data
//            }
            else {
                Toast.makeText(HomeActivity.this, "Invalid push notification", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getCurrentState() {
        final SweetAlertDialog alertDialog = Components.showProgressMessage(this);
        alertDialog.show();
        Call<State> call = ardsServiceInterface.getCurrentState(BEARERTEXT + authUser.getAccessToken(), jwtObject.getContext().getResourceid());
        call.enqueue(new Callback<State>() {
            @Override
            public void onResponse(Call<State> call, Response<State> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSuccess()) {
                        Components.dismissMessage(alertDialog);
                        if (response.body().getResult().getState().equals(AVAILABLE)) {
                            iv_available.setVisibility(View.VISIBLE);
                            iv_meal.setVisibility(View.GONE);
                            iv_official.setVisibility(View.GONE);
                            tv_nav_status.setText("online");
                            tv_nav_status.setTextColor(getResources().getColor(R.color.customGreen));
                            iv_nav_status.setImageResource(R.drawable.online);
                        } else if (response.body().getResult().getState().equals(NOTAVAILABLE)) {
                            if (response.body().getResult().getReason().equals(OFFICIALBREAK)) {
                                iv_available.setVisibility(View.GONE);
                                iv_meal.setVisibility(View.GONE);
                                iv_official.setVisibility(View.VISIBLE);
                                tv_nav_status.setText(OFFICIALBREAK);
                                tv_nav_status.setTextColor(getResources().getColor(R.color.offline));
                                iv_nav_status.setImageResource(R.drawable.offline);
                            } else if (response.body().getResult().getReason().equals(MEALBREAK)) {
                                iv_available.setVisibility(View.GONE);
                                iv_meal.setVisibility(View.VISIBLE);
                                iv_official.setVisibility(View.GONE);
                                tv_nav_status.setText(MEALBREAK);
                                tv_nav_status.setTextColor(getResources().getColor(R.color.offline));
                                iv_nav_status.setImageResource(R.drawable.offline);
                            } else {
                                iv_available.setVisibility(View.VISIBLE);
                                iv_meal.setVisibility(View.GONE);
                                iv_official.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        Components.changeAlert(alertDialog,getString(R.string.opps),response.body().getCustomMessage(),
                                SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                    }
                } else {

                    try {
                        if (Validations.checkExpiretion(response.errorBody().string())) {
                            Components.changeAlert(alertDialog,getString(R.string.opps),"Your Session Expired Please login",
                                    SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                            alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    goToExpireLogin();
                                }
                            });

                        } else {
                            Components.changeAlert(alertDialog,getString(R.string.opps),response.errorBody().string(),
                                    SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                        }


                    } catch (Exception e) {
                        Components.changeAlert(alertDialog,getString(R.string.opps),e.getMessage(),
                                SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                    }
                }
            }

            @Override
            public void onFailure(Call<State> call, Throwable t) {
                if (null != t.getMessage()) {
                    Components.changeAlert(alertDialog,getString(R.string.opps),t.getMessage(),
                            SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                }
            }
        });
    }

    private void getCurrentStateResource() {
        final SweetAlertDialog alertDialog = Components.showProgressMessage(this);
        alertDialog.show();
        Call<StateResource> call = ardsServiceInterface.getCurrentStateResource(BEARERTEXT + authUser.getAccessToken(), jwtObject.getContext().getResourceid());
        call.enqueue(new Callback<StateResource>() {
            @Override
            public void onResponse(Call<StateResource> call, Response<StateResource> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSuccess()) {
                        Log.d("Test",new Gson().toJson(response.body()));
                        Components.dismissMessage(alertDialog);

                        if (null != response.body().getResult().getObj()) {
                            int size = response.body().getResult().getObj().getLoginTasks().size();
                            if (size != 0) {
                                Components.changeAlert(alertDialog,getString(R.string.success),null,
                                    SweetAlertDialog.SUCCESS_TYPE,getString(R.string.ok));
                                Components.dismissMessage(alertDialog);
                                for (int i = 0; i < size; i++) {
                                    if (response.body().getResult().getObj().getLoginTasks().get(i).equals("CALL")) {
                                        iv_call.setVisibility(View.VISIBLE);
                                        AppControler.setCall(true);
                                    }
                                    if (response.body().getResult().getObj().getLoginTasks().get(i).equals("TICKET")) {
                                        iv_ticket.setVisibility(View.VISIBLE);
                                        AppControler.setTicket(true);
                                    }
                                }
                            } else {
                                Components.changeAlert(alertDialog,getString(R.string.opps),"Something went wrong",
                                        SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                            }
                        }
//                        else {
//                            Components.changeAlert(alertDialog,getString(R.string.opps),"Please login again",
//                                    SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
//                            alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    goToExpireLogin();
//                                }
//                            });
//                        }

                    } else {
                        Components.changeAlert(alertDialog,getString(R.string.opps),response.body().getCustomMessage(),
                            SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                    }
                } else {
                    try {

                        if (Validations.checkExpiretion(response.errorBody().string())) {

                            Components.changeAlert(alertDialog,getString(R.string.opps),"Your Session Expired Please login",
                                    SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                            alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    goToExpireLogin();
                                }
                            });
                        } else {
                            Components.changeAlert(alertDialog,getString(R.string.opps),response.errorBody().string(),
                                    SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                        }

                    } catch (Exception e) {
                        Components.changeAlert(alertDialog,getString(R.string.opps),e.getMessage(),
                                SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                    }
                }
            }

            @Override
            public void onFailure(Call<StateResource> call, Throwable t) {
                if (null != t.getMessage())
                    Components.changeAlert(alertDialog,getString(R.string.opps),t.getMessage(),
                            SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
            }
        });
    }

    private void goToExpireLogin() {
        editor.putString("User", null);
        editor.putString("UserJWT", null);
        editor.commit();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        bottomBar.setVisibility(View.VISIBLE);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.contentContainer);
            TicketFragment ticket = new TicketFragment();
//            TicketFilterFragment ticketFilter = new TicketFilterFragment();
//            DashboardFragment dashBoard = new DashboardFragment();
            if (currentFragment instanceof TicketFilterFragment) {
                changeFragment(ticket);
            }
            else if (currentFragment instanceof EngagementFragment){
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
            else{
                // stuff for dashboard and notification tabs
            }



//            if (currentFragment instanceof TicketFilterFragment) {
//                changeFragment(ticket);
//            }
//            else if (currentFragment instanceof TicketFragment) {
//                String title = (String) getSupportActionBar().getTitle();
//                if (title.equals("Inbox")) {
//                    changeFragment(ticketFilter);
//                }
//            } else {
//                String title = (String) getSupportActionBar().getTitle();
//                if (title.equals("Engagement Ticket")) {
//                    changeFragment(new EngagementFragment());
//                } else if (title.equals("Engagement")) {
//                    changeFragment(dashBoard);
//                }
//            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (!Validations.isInternetOn(getApplicationContext())) {
            Components.showErrorMessage(this, getString(R.string.opps), getString(R.string.internet_failed));

        } else {
            if (id == R.id.nav_call) {
                if (AppControler.isCall()) {
                    AppControler.setCall(false);
                    getResourceRemove("CALL");
                } else {
                    getResourceObject("CALL");
                    AppControler.setCall(true);
                }
            } else if (id == R.id.nav_ticket) {
                if (AppControler.isTicket()) {
                    AppControler.setTicket(false);
                    getResourceRemove("TICKET");
                } else {
                    getResourceObject("TICKET");
                    AppControler.setTicket(true);
                }

            } else if (id == R.id.nav_break) {
//            getBreak("Break");
            } else if (id == R.id.nav_available) {
                getEndBreak();
                selected_break = "available";
            } else if (id == R.id.nav_official) {
                getBreak(OFFICIALBREAK);
                selected_break = OFFICIALBREAK;
            } else if (id == R.id.nav_meal) {
                getBreak(MEALBREAK);
                selected_break = MEALBREAK;
            } else if (id == R.id.nav_logout) {
                getRevoke();

            }
        }


//        if(id==R.id.nav_register||id==R.id.nav_break){
//
//        }else{
//            drawer.closeDrawer(GravityCompat.START);
//        }
        return true;
    }

    private void getResourceRemove(final String type) {
        final SweetAlertDialog alertDialog = new Components().showLoadingSuccessMessage(this);
        alertDialog.show();
        Call<Break> call = ardsServiceInterface.getRemoveResourceSharing(BEARERTEXT + authUser.getAccessToken(), jwtObject.getContext().getResourceid(), type);
        call.enqueue(new Callback<Break>() {
            @Override
            public void onResponse(Call<Break> call, Response<Break> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSuccess()) {
                        Components.changeAlert(alertDialog,getString(R.string.success),null,SweetAlertDialog.SUCCESS_TYPE,
                                getString(R.string.ok));
                        Components.dismissMessage(alertDialog);
                        if (type.equals("CALL")) {
                            AppControler.setCall(false);
                            iv_call.setVisibility(View.GONE);
                            editor.putString("RegisterCall", NEWTICKETUPDATE);
                            editor.commit();
                        } else if (type.equals("TICKET")) {
                            AppControler.setTicket(false);
                            iv_ticket.setVisibility(View.GONE);
                            editor.putString("RegisterTicket", NEWTICKETUPDATE);
                            editor.commit();
                        }
                    } else {
                        if (type.equals("CALL")) {
                            AppControler.setCall(true);
                        } else if (type.equals("TICKET")) {
                            AppControler.setTicket(true);
                        }
                        Components.changeAlert(alertDialog,getString(R.string.opps),response.body().getCustomMessage()
                                ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                    }
                } else {
                    if (type.equals("CALL")) {
                        AppControler.setCall(true);
                    } else if (type.equals("TICKET")) {
                        AppControler.setTicket(true);
                    }
                        Components.changeAlert(alertDialog,getString(R.string.opps),getString(R.string.request_failed)
                                ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));

                }
            }
            @Override
            public void onFailure(Call<Break> call, Throwable t) {
                if (type.equals("CALL")) {
                    AppControler.setCall(true);
                } else if (type.equals("TICKET")) {
                    AppControler.setTicket(true);
                }
                Components.changeAlert(alertDialog,getString(R.string.opps),t.getMessage()
                        ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
            }
        });
    }

    private void getResourceObject(String type) {
        String[] split = jwtObject.getContext().getVeeryaccount().getContact().split("@");
        Resourcereq req = new Resourcereq();
        req.setResourceId(jwtObject.getContext().getResourceid());
        List<HandlingType> lis = new ArrayList<>();
        HandlingType ht = new HandlingType();
        ht.setType(type);
        Contact cont = new Contact();
        cont.setContactName(split[0]);
        cont.setDomain(split[1]);
        cont.setExtention(jwtObject.getContext().getVeeryaccount().getDisplay());
        cont.setContactType("PRIVATE");
        cont.setProfile(jwtObject.getIss());
        ht.setContact(cont);
        lis.add(ht);
        req.setHandlingTypes(lis);

        getAddResource(req);


    }

    private void getAddResource(final Resourcereq req) {
        final SweetAlertDialog alertDialog = new Components().showLoadingSuccessMessage(this);
        alertDialog.show();
        Call<Break> call = ardsServiceInterface.getAddResource(BEARERTEXT + authUser.getAccessToken(),req);
        call.enqueue(new Callback<Break>() {
            @Override
            public void onResponse(Call<Break> call, Response<Break> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSuccess()) {
                        Components.changeAlert(alertDialog,getString(R.string.success),null,SweetAlertDialog.SUCCESS_TYPE,
                                getString(R.string.ok));
                        Components.dismissMessage(alertDialog);
                        if (req.getHandlingTypes().get(0).getType().equals("CALL")) {
                            iv_call.setVisibility(View.VISIBLE);

                        }
                        if (req.getHandlingTypes().get(0).getType().equals("TICKET")) {
                            iv_ticket.setVisibility(View.VISIBLE);

                        }
                    } else {
                        if (req.getHandlingTypes().get(0).getType().equals("CALL")) {
                            AppControler.setCall(false);
                            editor.putString("RegisterCall", NEWTICKETUPDATE);
                            editor.commit();
                        } else if (req.getHandlingTypes().get(0).getType().equals("TICKET")) {
                            AppControler.setTicket(false);
                            editor.putString("RegisterTicket", NEWTICKETUPDATE);
                            editor.commit();
                        }
                        Components.changeAlert(alertDialog,getString(R.string.opps),response.body().getCustomMessage()
                                ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                    }

                } else {
                    Components.changeAlert(alertDialog,getString(R.string.opps),getString(R.string.request_failed)
                            ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                }
            }

            @Override
            public void onFailure(Call<Break> call, Throwable t) {
                Components.changeAlert(alertDialog,getString(R.string.opps),t.getMessage()
                        ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
            }
        });
    }

    private void getRevoke() {
        final SweetAlertDialog alertDialog = new Components().showLoadingSuccessMessage(this);
        alertDialog.show();
        ServiceGenarator.CurrentFactory.getInstance().getRevoke(BEARERTEXT + authUser.getAccessToken(), jwtObject.getJti()).enqueue(new Callback<Revoke>() {
            @Override
            public void onResponse(Call<Revoke> call, Response<Revoke> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSuccess()) {
                        Components.changeAlert(alertDialog,getString(R.string.success),null,SweetAlertDialog.SUCCESS_TYPE,
                                getString(R.string.ok));
                        Components.dismissMessage(alertDialog);
                        editor.putString("User", null);
                        editor.putString("UserJWT", null);
                        editor.commit();
                    } else {
                        Components.changeAlert(alertDialog,getString(R.string.opps),response.body().getCustomMessage()
                                ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                    }
                } else {
                    Components.changeAlert(alertDialog,getString(R.string.opps),getString(R.string.request_failed)
                            ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                }
            }
            @Override
            public void onFailure(Call<Revoke> call, Throwable t) {
                Components.changeAlert(alertDialog,getString(R.string.opps),t.getMessage()
                        ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
            }
        });
    }


    /**
     * This method is use to send end break request
     *
     * @param breaks Must send official/meal
     */

    private void getBreak(String breaks) {
        final SweetAlertDialog alertDialog = new Components().showLoadingSuccessMessage(this);
        alertDialog.show();
        Call<Break> call = ardsServiceInterface.getBreakRequest(BEARERTEXT + authUser.getAccessToken(), jwtObject.getContext().getResourceid(),breaks);
        call.enqueue(new Callback<Break>() {
            @Override
            public void onResponse(Call<Break> call, Response<Break> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSuccess()) {
                        Components.changeAlert(alertDialog,getString(R.string.success),null,SweetAlertDialog.SUCCESS_TYPE,
                                getString(R.string.ok));
                        Components.dismissMessage(alertDialog);

                        tv_nav_status.setText("online");
                        tv_nav_status.setTextColor(getResources().getColor(R.color.offline));
                        iv_nav_status.setImageResource(R.drawable.offline);
                        if (selected_break.equals(OFFICIALBREAK)) {
                            iv_available.setVisibility(View.GONE);
                            iv_meal.setVisibility(View.GONE);
                            iv_official.setVisibility(View.VISIBLE);
                            tv_nav_status.setText(OFFICIALBREAK);
                            editor.putString("Break", OFFICIALBREAK);
                            editor.commit();

                        } else if (selected_break.equals(MEALBREAK)) {
                            iv_available.setVisibility(View.GONE);
                            iv_meal.setVisibility(View.VISIBLE);
                            tv_nav_status.setText(OFFICIALBREAK);
                            iv_official.setVisibility(View.GONE);
                            editor.putString("Break", MEALBREAK);
                            editor.commit();
                        }
                    } else {
                        Components.changeAlert(alertDialog,getString(R.string.opps),response.body().getCustomMessage()
                            ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                    }

                } else {
                    Components.changeAlert(alertDialog,getString(R.string.opps),getString(R.string.request_failed)
                            ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                }
            }

            @Override
            public void onFailure(Call<Break> call, Throwable t) {
                Components.changeAlert(alertDialog,getString(R.string.opps),t.getMessage()
                        ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
            }
        });

    }

    /**
     * This method use to end the break and make user available
     */
    private void getEndBreak() {
        final SweetAlertDialog alertDialog = new Components().showLoadingSuccessMessage(this);
        alertDialog.show();
        Call<Break> call = ardsServiceInterface.getEndBreakRequest(BEARERTEXT + authUser.getAccessToken(), jwtObject.getContext().getResourceid());
        call.enqueue(new Callback<Break>() {
            @Override
            public void onResponse(Call<Break> call, Response<Break> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSuccess()) {
                        Components.changeAlert(alertDialog,getString(R.string.success),null,SweetAlertDialog.SUCCESS_TYPE,
                                getString(R.string.ok));
                        Components.dismissMessage(alertDialog);

                        tv_nav_status.setText("online");
                        tv_nav_status.setTextColor(getResources().getColor(R.color.customGreen));
                        iv_nav_status.setImageResource(R.drawable.online);
                        iv_available.setVisibility(View.VISIBLE);
                        iv_meal.setVisibility(View.GONE);
                        iv_official.setVisibility(View.GONE);
                        editor.putString("Break", AVAILABLE);
                        editor.commit();

                    } else {
                        Components.changeAlert(alertDialog,getString(R.string.opps),response.body().getCustomMessage()
                                ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                    }
                } else {
                    Components.changeAlert(alertDialog,getString(R.string.opps),getString(R.string.request_failed)
                            ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
                }
            }

            @Override
            public void onFailure(Call<Break> call, Throwable t) {
                Components.changeAlert(alertDialog,getString(R.string.opps),t.getMessage()
                        ,SweetAlertDialog.ERROR_TYPE,getString(R.string.ok));
            }
        });
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction;

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.contentContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onMessageCountChanged(int messageCount) {
        bottomBar.getTabWithId(R.id.tab_notification).setBadgeCount(messageCount);
        notificationCount = messageCount;
    }
}
