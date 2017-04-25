package apps.veery.com.facetonecrm;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import apps.veery.com.adapter.MultiUserAdapter;
import apps.veery.com.model.Address;
import apps.veery.com.model.EngagementUser;
import apps.veery.com.model.NewExternalUser;
import apps.veery.com.utilities.FontChanger;

/**
 * Created by Sijith on 11/15/2016.
 */
public class MultiUserViewFragment extends Fragment implements RecyclerView.OnItemTouchListener{

    private static final int NEW_USER_REQ_CODE = 5001;
    RecyclerView recyclerView;
    GridLayoutManager linearLayoutManager;
    public String phone_no = "";
    public String skill, direction, company = "";
    TextView engagementMultiCompany, engagementMultiDirection, engagementMultiFrom, engagementMultiSkill;
    TextView engagementMultiCompanyText, engagementMultiDirectionText, engagementMultiFromText, engagementMultiSkillText;
    List<EngagementUser> users;
    MultiUserAdapter multiUserAdapter;
    GestureDetectorCompat gestureDetector;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phone_no = getArguments().getString("phoneNo");
            skill = getArguments().getString("skill");
            direction = getArguments().getString("direction");
            company = getArguments().getString("company");
            Gson gson = new Gson();
            users = gson.fromJson(getArguments().getString("users"), new TypeToken<ArrayList<EngagementUser>>() {
            }.getType());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_multi_user_view, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Engagement User");

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_add_user);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewUserActivity.class);
                startActivityForResult(intent, NEW_USER_REQ_CODE);
            }
        });


        gestureDetector =
                new GestureDetectorCompat(getActivity(), new RecyclerViewDemoOnGestureListener());

        engagementMultiCompany = (TextView) v.findViewById(R.id.engagementMultiCompany);
        engagementMultiDirection = (TextView) v.findViewById(R.id.engagementMultiDirection);
        engagementMultiFrom = (TextView) v.findViewById(R.id.engagementMultiFrom);
        engagementMultiSkill = (TextView) v.findViewById(R.id.engagementMultiSkill);

        engagementMultiCompanyText = (TextView) v.findViewById(R.id.engagementMultiCompanyText);
        engagementMultiDirectionText = (TextView) v.findViewById(R.id.engagementMultiDirectionText);
        engagementMultiFromText = (TextView) v.findViewById(R.id.engagementMultiFromText);
        engagementMultiSkillText = (TextView) v.findViewById(R.id.engagementMultiSkillText);

        engagementMultiCompany.setText(company);
        engagementMultiDirection.setText(direction);
        engagementMultiFrom.setText(phone_no);
        engagementMultiSkill.setText(skill);
        recyclerView = (RecyclerView) v.findViewById(R.id.multi_user_recycler_view);
        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(this);

        changeFont();
        fillMultiUserData();

        return v;
    }

    private void fillMultiUserData() {
        multiUserAdapter = new MultiUserAdapter(users, R.layout.multi_user_item, getActivity());
        recyclerView.setAdapter(multiUserAdapter);
    }

    private void changeFont() {
        List<View> sendViews = new ArrayList<>();
        sendViews.add(engagementMultiCompany);
        sendViews.add(engagementMultiDirection);
        sendViews.add(engagementMultiFrom);
        sendViews.add(engagementMultiSkill);
        sendViews.add(engagementMultiCompanyText);
        sendViews.add(engagementMultiDirectionText);
        sendViews.add(engagementMultiFromText);
        sendViews.add(engagementMultiSkillText);
        FontChanger.changeFont(sendViews, getActivity(), FontChanger.FONT_REGULAR);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class RecyclerViewDemoOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            TextView userID = (TextView) view.findViewById(R.id.multi_item_user_id);
            TextView jsonUser = (TextView) view.findViewById(R.id.multi_item_user_json);

            EngagementFragment engagementFragment = new EngagementFragment();
            Bundle bundle = new Bundle();
            bundle.putString("userID", userID.getText().toString());
            bundle.putString("jsonUser", jsonUser.getText().toString());
            bundle.putString("phoneNo", phone_no);
            bundle.putString("skill", skill);
            bundle.putString("direction", direction);
            bundle.putString("company", company);
            bundle.putString("userType", EngagementFragment.MULTI_USER);
            engagementFragment.setArguments(bundle);
            changeFragment(engagementFragment);
            return super.onSingleTapConfirmed(e);
        }
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.contentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem mainMenuItemToRemove = menu.findItem(R.id.menu_home_refresh);
        mainMenuItemToRemove .setVisible(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == NEW_USER_REQ_CODE) {
            EngagementUser newlyCreatedUser = new EngagementUser();
            newlyCreatedUser.setUserId(data.getStringExtra("_id"));
            newlyCreatedUser.setName(data.getStringExtra("name"));
            newlyCreatedUser.setAvatar(data.getStringExtra("avatar"));
            newlyCreatedUser.setFirstName(data.getStringExtra("firstname"));
            newlyCreatedUser.setLastName(data.getStringExtra("lastname"));
            newlyCreatedUser.setPhone(data.getStringExtra("phone"));
            newlyCreatedUser.setEmail(data.getStringExtra("email"));
            Address address = new Address();
            address.setZipCode("zipcode");
            address.setNumber("number");
            address.setStreet("street");
            address.setCity("city");
            address.setProvince("province");
            address.setCountry("country");
            newlyCreatedUser.setAddress(address);
            multiUserAdapter.addNewUser(newlyCreatedUser);
        }
    }
}
