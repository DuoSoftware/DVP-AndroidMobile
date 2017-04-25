package apps.veery.com.facetonecrm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import apps.veery.com.model.Address;
import apps.veery.com.model.EngagementUser;
import apps.veery.com.model.EngagementUserResponse;
import apps.veery.com.restInterfaces.UserApiInterface;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EngagementEngageFragment extends Fragment {

    private ImageView engagementUserImage;
    private TextView engagementCompany;
    private TextView engagementDirection;
    private TextView engagementFrom;
    private TextView engagementSkill;
    private TextView engagementName;
    private TextView engagementEmail;
    private TextView engagementAddress;
    private TextView engagementContact;
    private TextView engagementAccount;

    private String retrievedNo = "";

    public EngagementEngageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getUserProfile(); // fill the user values
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Engagement");
        View view = inflater.inflate(R.layout.fragment_engagement_engage, container, false);

        engagementUserImage = (ImageView) view.findViewById(R.id.engagementUserImage);
        engagementCompany = (TextView) view.findViewById(R.id.engagementCompany);
        engagementDirection = (TextView) view.findViewById(R.id.engagementDirection);
        engagementFrom = (TextView) view.findViewById(R.id.engagementFrom);
        engagementSkill = (TextView) view.findViewById(R.id.engagementSkill);
        engagementName = (TextView) view.findViewById(R.id.engagementName);
        engagementEmail = (TextView) view.findViewById(R.id.engagementEmail);
        engagementAddress = (TextView) view.findViewById(R.id.engagementAddress);
        engagementContact = (TextView) view.findViewById(R.id.engagementContact);
        engagementAccount = (TextView) view.findViewById(R.id.engagementAccount);


        if (getArguments() != null) {
            retrievedNo = getArguments().getString("phoneNo");
            setUserData(getArguments().getString("jsonUser"));

            engagementCompany.setText(getArguments().getString("company"));
            engagementDirection.setText(getArguments().getString("direction"));
            engagementSkill.setText(getArguments().getString("skill"));
            engagementFrom.setText(retrievedNo);
        }

        return view;

    }

    private void setUserData(String jsonUser) {
        Gson gson = new Gson();
        EngagementUser user = gson.fromJson(jsonUser, EngagementUser.class);

        validateUser(user);
        if (null != user.getAvatar() && !user.getAvatar().isEmpty()) {
            Picasso.with(getActivity()).load(user.getAvatar()).into(engagementUserImage);
        }
        engagementName.setText(user.getFirstName() + " " + user.getLastName());
        engagementEmail.setText(user.getEmail());
        engagementContact.setText(user.getPhone());

        Address address = user.getAddress();
        if (null != address) {
            engagementAddress.setText(address.getNumber() + "," + address.getStreet() + "," + address.getCity());
        }

    }

//    private void getUserProfile() {
//        if (!retrievedNo.isEmpty()) {
//            UserApiInterface apiService = UserService.getClient().create(UserApiInterface.class);
//            Call<EngagementUserResponse> call = apiService.getFilterUser("Bearer " + AppControler.getUser().getAccessToken(), retrievedNo);
//            call.enqueue(new Callback<EngagementUserResponse>() {
//                @Override
//                public void onResponse(Call<EngagementUserResponse> call, Response<EngagementUserResponse> response) {
//                    if (response.isSuccessful()) {
//                        if (response.body().isSuccess()) {
//                            if (response.body().getResults().size() == 0) {
//                                Toast.makeText(getActivity(), "No User detail for contact number", Toast.LENGTH_SHORT).show();
//                            } else {
//                                EngagementUser user = response.body().getResults().get(0);
//                                validateUser(user);
//                                if (null != user.getAvatar() && !user.getAvatar().isEmpty()) {
//                                    Picasso.with(getActivity()).load(user.getAvatar()).into(engagementUserImage);
//                                }
//                                engagementName.setText(user.getFirstName() + " " + user.getLastName());
//                                engagementEmail.setText(user.getEmail());
//                                engagementContact.setText(user.getPhone());
//
//                                Address address = user.getAddress();
//                                if (null != address) {
//                                    engagementAddress.setText(address.getNumber() + "," + address.getStreet() + "," + address.getCity());
//                                }
//                            }
//                        }
//                    } else {
//                        Toast.makeText(getActivity(), "Getting user details failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<EngagementUserResponse> call, Throwable t) {
//                    Log.d("Test", "error getting user details" + t.toString());
//                }
//            });
//        } else {
//            Toast.makeText(getActivity(), "Contact number not retrieved", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void validateUser(EngagementUser user) {
        if (null == user.getFirstName())
            user.setFirstName(" ");
        if (null == user.getLastName())
            user.setLastName(" ");
        if (null == user.getEmail())
            user.setEmail(" ");
        if (null == user.getPhone())
            user.setPhone(" ");
    }

}
