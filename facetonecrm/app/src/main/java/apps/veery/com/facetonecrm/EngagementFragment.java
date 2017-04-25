package apps.veery.com.facetonecrm;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apps.veery.com.adapter.ViewPagerAdapter;
import apps.veery.com.model.Engagement.Engagements;
import apps.veery.com.model.EngagementUserResponse;
import apps.veery.com.restInterfaces.UserApiInterface;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.EngagementServiceGenarator;
import apps.veery.com.service.UserService;
import apps.veery.com.utilities.Components;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EngagementFragment extends Fragment {
    Context context;
    View v;
    ViewPager viewPager;
    TabLayout tabLayout;
    SweetAlertDialog dd;
    public static String PHONE_NUMBER_FROM_NOTIFICATION = "";
    public static String MULTI_USER = "multi";
    public static String ONE_USER = "one";
    public static String NEW_USER = "new";
    public String skill, direction, company, jsonUser;
    public static String USER_ID = "";
    static String ENGAGEMENT_ID;
    static List<String> globle_sessions;

    public EngagementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            PHONE_NUMBER_FROM_NOTIFICATION = getArguments().getString("phoneNo");
            skill = getArguments().getString("skill");
            direction = getArguments().getString("direction");
            company = getArguments().getString("company");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_engagement, container, false);
        dd = new Components().showLoadingSuccessMessage(getActivity());

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Engagement User");

        tabLayout = (TabLayout) v.findViewById(R.id.tabeng_tabs);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(3);
        globle_sessions = new ArrayList<>();

        if (getArguments() != null && getArguments().containsKey("userType")) {
            if (getArguments().getString("userType").equals(MULTI_USER)) { // one of a user selected
                jsonUser = getArguments().getString("jsonUser");
                USER_ID = getArguments().getString("userID");
                getEngagement(USER_ID);
            } else { // trying to create a new user

            }
        } else {   // first engagement call
            getProfileById();
        }
        return v;
    }

    private void getProfileById() {
        dd.show();
        UserApiInterface apiService = UserService.getClient().create(UserApiInterface.class);
        Call<EngagementUserResponse> call = apiService.getUsersByNumber("Bearer " + AppControler.getUser().getAccessToken(), PHONE_NUMBER_FROM_NOTIFICATION);
        call.enqueue(new Callback<EngagementUserResponse>() {
            @Override
            public void onResponse(Call<EngagementUserResponse> call, Response<EngagementUserResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Gson gson = new Gson();
                        if (response.body().getResults().size() != 0) {
//                            Log.d("Test","***** "+gson.toJson(response.body().getResults()));
                            if (response.body().getResults().size() > 1) {
                                MultiUserViewFragment multiUserViewFragment = new MultiUserViewFragment();
                                if (null != getArguments()) {
                                    Bundle bundle = getArguments();
                                    bundle.putString("users", gson.toJson(response.body().getResults()));
                                    multiUserViewFragment.setArguments(bundle);
                                    changeFragment(multiUserViewFragment);
                                }
                            } else { //only one user
                                jsonUser = gson.toJson(response.body().getResults().get(0));
                                USER_ID = response.body().getResults().get(0).getUserId();
                                getEngagement(USER_ID);
                            }
                            dd.setTitleText("Success!")
                                    .setConfirmText("OK")
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            Components.dismissMessage(dd);
                        } else { // create a new user here
                            Toast.makeText(getActivity(), "Cannot find user for " + PHONE_NUMBER_FROM_NOTIFICATION, Toast.LENGTH_LONG).show();
                            Components.dismissMessage(dd);
                        }
                    } else {
                        dd.setTitleText("opps!").setContentText(response.body().getCustomMessage())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                    }
                } else {
                    Toast.makeText(getActivity(), "No Data For engagement", Toast.LENGTH_SHORT).show();
                    Components.dismissMessage(dd);
                }
            }

            @Override
            public void onFailure(Call<EngagementUserResponse> call, Throwable t) {
                dd.setTitleText("opps!").setContentText(t.getMessage())
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
        });
    }


    public void getEngagement(String id) {
        EngagementServiceGenarator.EngagementServiceFactory.getInstance().getEngagementByProfile("Bearer " + AppControler.getUser().getAccessToken(), id).enqueue(new Callback<Engagements>() {
            @Override
            public void onResponse(Call<Engagements> call, Response<Engagements> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSuccess()) {
                        globle_sessions = response.body().getResult().getEngagements();
                        Collections.reverse(globle_sessions);
                        ENGAGEMENT_ID = response.body().getResult().getId();
                        dd.setTitleText("Success!")
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        dd.dismiss();
//                        getSessions(response.body().getResult().getId(), out);
                        setupViewPager();  // initialize the child view here
                        tabLayout.setupWithViewPager(viewPager);

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
            public void onFailure(Call<Engagements> call, Throwable t) {
                dd.setTitleText("opps!").setContentText(t.getMessage())
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
        });
    }

    public void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        EngagementEngageFragment engagementEngageFragment = new EngagementEngageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("phoneNo", PHONE_NUMBER_FROM_NOTIFICATION);
        bundle.putString("skill", skill);
        bundle.putString("direction", direction);
        bundle.putString("company", company);
        bundle.putString("jsonUser", jsonUser);
        engagementEngageFragment.setArguments(bundle);
        adapter.addFrag(engagementEngageFragment, "ENGAGEMENT");
        adapter.addFrag(new EngagementTimelineFragment(), "TIME LINE");
        adapter.addFrag(new EngagementTicketFragment(), "TICKET");
        viewPager.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    /**
     * Change the fragment in frame layout
     *
     * @param fragment fragment want to change
     */
    private void changeFragment(Fragment fragment) {
        if (null != context) {
            //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();//getFragmentManager returns null
            FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();//getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentTransaction.replace(R.id.contentContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            Log.d("Test", "getActivity is null in engagement fragment");
        }
    }

}
