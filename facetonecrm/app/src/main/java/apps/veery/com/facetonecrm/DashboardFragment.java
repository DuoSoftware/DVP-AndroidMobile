package apps.veery.com.facetonecrm;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import apps.veery.com.model.Auth;
import apps.veery.com.model.DashTimes;
import apps.veery.com.model.DashboardTicketResponse;
import apps.veery.com.model.GroupDetails;
import apps.veery.com.model.jwtres.Decode;
import apps.veery.com.restInterfaces.UserApiInterface;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.DashboardService;
import apps.veery.com.service.DashboardApiInterface;
import apps.veery.com.service.ResourceServiceGenarator;
import apps.veery.com.service.UserService;
import apps.veery.com.utilities.Components;
import apps.veery.com.utilities.DateMatcher;
import apps.veery.com.utilities.Validations;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private static final String TICKET_FAIL_MESSAGE = "Failed to get response";
    private Decode jwtObject;
    private Auth authUser;

    TextView tv_my_open, tv_my_close, tv_group_open, tv_group_close, tv_time_staff, tv_time_break, tv_time_oncall, tv_time_idle;
    TextView lable_my_open, lable_my_close, lable_group_open, lable_group_close, lable_time_staff, lable_time_break, lable_time_oncall, lable_time_idle;
    Typeface type_regular, type_bold;

    private DashboardApiInterface apiService;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        if(null != sharedpreferences.getString(MainActivity.USERJWT,null)) {
            jwtObject = new Gson().fromJson(sharedpreferences.getString(MainActivity.USERJWT, null), Decode.class);
        }
        if(null != sharedpreferences.getString(MainActivity.USER,null)) {
            authUser = new Gson().fromJson(sharedpreferences.getString(MainActivity.USER, null), Auth.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setHasOptionsMenu(true);
        fontApply(v);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dashboard");

        apiService = DashboardService.getClient().create(DashboardApiInterface.class);
        getMyOpenTicket();
        getMyCloseTicket();
        getGroupDetails();
        getDashTimes();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_home_refresh) {
            getDashTimes();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fontApply(View v) {
        type_regular = Typeface.createFromAsset(getResources().getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        type_bold = Typeface.createFromAsset(getResources().getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        tv_my_open = (TextView) v.findViewById(R.id.tv_dash_my_new_tickets);
        tv_my_open.setTypeface(type_regular);
        tv_my_close = (TextView) v.findViewById(R.id.tv_dash_my_close_tickets);
        tv_my_close.setTypeface(type_regular);
        tv_group_open = (TextView) v.findViewById(R.id.tv_dash_group_new_tickets);
        tv_group_open.setTypeface(type_regular);
        tv_group_close = (TextView) v.findViewById(R.id.tv_dash_group_close_tickets);
        tv_group_close.setTypeface(type_regular);

        tv_time_staff = (TextView) v.findViewById(R.id.tv_dash_time_staff);
        tv_time_staff.setTypeface(type_bold);
        tv_time_break = (TextView) v.findViewById(R.id.tv_dash_time_break);
        tv_time_break.setTypeface(type_bold);
        tv_time_oncall = (TextView) v.findViewById(R.id.tv_dash_time_oncall);
        tv_time_oncall.setTypeface(type_bold);
        tv_time_idle = (TextView) v.findViewById(R.id.tv_dash_time_idle);
        tv_time_idle.setTypeface(type_bold);


        lable_my_open = (TextView) v.findViewById(R.id.lable_dash_my_new_tickets);
        lable_my_open.setTypeface(type_bold);
        lable_my_close = (TextView) v.findViewById(R.id.lable_dash_my_close_tickets);
        lable_my_close.setTypeface(type_bold);
        lable_group_open = (TextView) v.findViewById(R.id.lable_dash_group_new_tickets);
        lable_group_open.setTypeface(type_bold);
        lable_group_close = (TextView) v.findViewById(R.id.lable_dash_group_close_tickets);
        lable_group_close.setTypeface(type_bold);
        lable_time_staff = (TextView) v.findViewById(R.id.lable_dash_time_staff);
        lable_time_staff.setTypeface(type_regular);
        lable_time_break = (TextView) v.findViewById(R.id.lable_dash_time_break);
        lable_time_break.setTypeface(type_regular);
        lable_time_oncall = (TextView) v.findViewById(R.id.lable_dash_time_oncall);
        lable_time_oncall.setTypeface(type_regular);
        lable_time_idle = (TextView) v.findViewById(R.id.lable_dash_time_idle);
        lable_time_idle.setTypeface(type_regular);
    }

    private void getGroupDetails() {
        UserApiInterface apiService = UserService.getClient().create(UserApiInterface.class);
        Call<GroupDetails> call = apiService.getGroupDetails("Bearer " + authUser.getAccessToken(), jwtObject.getContext().getGroup());
        call.enqueue(new Callback<GroupDetails>() {
            @Override
            public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSuccess()) {
                        getGroupNewTickets("ugroup_" + response.body().getResult().getName());
                        getGroupCloseTickets("ugroup_" + response.body().getResult().getName());
                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<GroupDetails> call, Throwable t) {

            }
        });
    }

    private void getDashTimes() {
        final SweetAlertDialog dd = new Components().showLoadingSuccessMessage(getActivity());
        dd.show();
        ResourceServiceGenarator.ResourceServiceFactory.getInstance().getDashTimes("Bearer " + authUser.getAccessToken(), jwtObject.getContext().getResourceid()).enqueue(new Callback<DashTimes>() {
            @Override
            public void onResponse(Call<DashTimes> call, Response<DashTimes> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSuccess()) {
                        Validations.setTextToTextView(tv_time_staff, DateMatcher.getTimeDuration(response.body().getResult().getStaffedTime().toString()));
                        Validations.setTextToTextView(tv_time_break, DateMatcher.getTimeDuration(response.body().getResult().getBreakTime().toString()));
                        Validations.setTextToTextView(tv_time_oncall, DateMatcher.getTimeDuration(response.body().getResult().getOnCallTime().toString()));
                        Validations.setTextToTextView(tv_time_idle, DateMatcher.getTimeDuration(response.body().getResult().getIdleTime().toString()));
                        dd.setTitleText("Success!")
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        Components.dismissMessage(dd);

                    } else {
                        dd.setTitleText("opps!").setContentText(response.body().getCustomMessage())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                } else {
                    try {
                        dd.setTitleText("opps!").setContentText(response.errorBody().string())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);


                    } catch (IOException e) {
                        dd.setTitleText("opps!").setContentText(e.getMessage())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<DashTimes> call, Throwable t) {
                dd.setTitleText("opps!").setContentText(t.getMessage())
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
        });
    }

    private void getMyOpenTicket() {
        apiService.getOpenTicket("Bearer " + authUser.getAccessToken(), "NEWTICKET", "user_" + jwtObject.getIss()).enqueue(new Callback<DashboardTicketResponse>() {
            @Override
            public void onResponse(Call<DashboardTicketResponse> call, Response<DashboardTicketResponse> response) {
                if (response.isSuccessful()) {
                    new Components().animateTextView(0, response.body().getResult(), tv_my_open);
                } else {
                    try {
                        tv_my_open.setText(response.errorBody().string());
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<DashboardTicketResponse> call, Throwable t) {
                tv_my_open.setText(TICKET_FAIL_MESSAGE);
            }
        });
    }

    private void getMyCloseTicket() {
        apiService.getOpenTicket("Bearer " + authUser.getAccessToken(), "CLOSEDTICKET", "user_" + jwtObject.getIss()).enqueue(new Callback<DashboardTicketResponse>() {
            @Override
            public void onResponse(Call<DashboardTicketResponse> call, Response<DashboardTicketResponse> response) {
                if (response.isSuccessful()) {
                    new Components().animateTextView(0, response.body().getResult(), tv_my_close);
                } else {
                    try {
                        tv_my_close.setText(response.errorBody().string());
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<DashboardTicketResponse> call, Throwable t) {
                tv_my_close.setText(TICKET_FAIL_MESSAGE);
            }
        });
    }

    private void getGroupNewTickets(String userGroup) {
        apiService.getOpenTicket("Bearer " + authUser.getAccessToken(), "NEWTICKET", userGroup).enqueue(new Callback<DashboardTicketResponse>() {
            @Override
            public void onResponse(Call<DashboardTicketResponse> call, Response<DashboardTicketResponse> response) {
                if (response.isSuccessful()) {

                    new Components().animateTextView(0, response.body().getResult(), tv_group_open);
                } else {
                    try {
                        tv_group_open.setText(response.errorBody().string());
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<DashboardTicketResponse> call, Throwable t) {
                tv_group_open.setText(TICKET_FAIL_MESSAGE);
            }
        });
    }

    private void getGroupCloseTickets(String userGroup) {
        apiService.getOpenTicket("Bearer " + authUser.getAccessToken(), "CLOSEDTICKET", userGroup).enqueue(new Callback<DashboardTicketResponse>() {
            @Override
            public void onResponse(Call<DashboardTicketResponse> call, Response<DashboardTicketResponse> response) {
                if (response.isSuccessful()) {

                    new Components().animateTextView(0, response.body().getResult(), tv_group_close);
                } else {
                    try {
                        tv_group_close.setText(response.errorBody().string());
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<DashboardTicketResponse> call, Throwable t) {
                tv_group_close.setText(TICKET_FAIL_MESSAGE);
            }
        });
    }

}
