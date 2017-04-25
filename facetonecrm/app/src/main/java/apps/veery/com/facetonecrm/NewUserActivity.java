package apps.veery.com.facetonecrm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import apps.veery.com.components.CustomDatePicker;
import apps.veery.com.model.Address;
import apps.veery.com.model.CommentResponse;
import apps.veery.com.model.EngagementUser;
import apps.veery.com.model.NewExternalUser;
import apps.veery.com.model.NewExternalUserResponse;
import apps.veery.com.restInterfaces.UserApiInterface;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.UserService;
import apps.veery.com.utilities.Components;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewUserActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button setDateButton, saveButton, cancelButton;
    private TextView selectedBirthDate;
    private EditText newUserFirstName, newUserLastName, newUserNickName, newUserPhone, newUserEmail, newUserPOBox, newUserStreet, newUserCity;
    private EditText newUserProvince, newUserCountry, newUserZip, newUserSSN, newUserTags;

    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        sweetAlertDialog = new Components().showLoadingSuccessMessage(this);

        setDateButton = (Button) findViewById(R.id.newUserBirthDate);
        saveButton = (Button) findViewById(R.id.newUserSave);
        cancelButton = (Button) findViewById(R.id.newUserCancel);

        selectedBirthDate = (TextView) findViewById(R.id.newUserBirthText);

        newUserFirstName = (EditText) findViewById(R.id.newUserFirstName);
        newUserLastName = (EditText) findViewById(R.id.newUserLastName);
        newUserNickName = (EditText) findViewById(R.id.newUserNickName);
        newUserPhone = (EditText) findViewById(R.id.newUserPhone);
        newUserEmail = (EditText) findViewById(R.id.newUserEmail);
        newUserPOBox = (EditText) findViewById(R.id.newUserPOBox);
        newUserStreet = (EditText) findViewById(R.id.newUserStreet);
        newUserCity = (EditText) findViewById(R.id.newUserCity);
        newUserProvince = (EditText) findViewById(R.id.newUserProvince);
        newUserCountry = (EditText) findViewById(R.id.newUserCountry);
        newUserZip = (EditText) findViewById(R.id.newUserZip);
        newUserSSN = (EditText) findViewById(R.id.newUserSSN);
        newUserTags = (EditText) findViewById(R.id.newUserTags);

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new CustomDatePicker();
//                newFragment.setTargetFragment(DetailsFragment.this, 0); // setting the callback fragment
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUserInput()) {
                    // network call. register user . if success finish the activity.ELSE remain in the view.
                    NewExternalUser externalUser = new NewExternalUser();
                    externalUser.setName(newUserFirstName.getText().toString().concat(" ").concat(newUserLastName.getText().toString()));
                    externalUser.setFirstName(newUserFirstName.getText().toString());
                    externalUser.setLastName(newUserLastName.getText().toString());
                    externalUser.setPhone(newUserPhone.getText().toString());
                    externalUser.setEmail(newUserEmail.getText().toString());
                    externalUser.setSsn(newUserSSN.getText().toString());

                    if (!selectedBirthDate.getText().toString().isEmpty())
                        externalUser.setBirthday(selectedBirthDate.getText().toString());
                    if (!newUserPOBox.getText().toString().isEmpty()) {
                        Address address = new Address();
                        address.setNumber(newUserPOBox.getText().toString());
                        address.setStreet(!newUserStreet.getText().toString().isEmpty() ? newUserStreet.getText().toString() : " ");
                        address.setCity(!newUserCity.getText().toString().isEmpty() ? newUserCity.getText().toString() : " ");
                        address.setProvince(!newUserProvince.getText().toString().isEmpty() ? newUserProvince.getText().toString() : " ");
                        address.setCountry(!newUserCountry.getText().toString().isEmpty() ? newUserCountry.getText().toString() : " ");
                        address.setZipCode(!newUserZip.getText().toString().isEmpty() ? newUserZip.getText().toString() : " ");
                        externalUser.setAddress(address);
                    }

                    sendNewExternalUser(externalUser);
                } else {
                    Toast.makeText(NewUserActivity.this, "One or more required fields are missing..", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validateUserInput() {
        boolean allInputsAreValid;
        if (newUserFirstName.getText().toString().isEmpty() ||
                newUserLastName.getText().toString().isEmpty() ||
                newUserPhone.getText().toString().isEmpty() ||
                newUserEmail.getText().toString().isEmpty() ||
                newUserSSN.getText().toString().isEmpty()) {
            allInputsAreValid = false;
        } else {
            allInputsAreValid = true;
        }

        return allInputsAreValid;
    }

    private void sendNewExternalUser(final NewExternalUser newExternalUser) {

        sweetAlertDialog.show();
        UserApiInterface apiService = UserService.getClient().create(UserApiInterface.class);
        Call<NewExternalUserResponse> call = apiService.createExternalUser("Bearer " + AppControler.getUser().getAccessToken(), newExternalUser);

        call.enqueue(new Callback<NewExternalUserResponse>() {
            @Override
            public void onResponse(Call<NewExternalUserResponse> call, Response<NewExternalUserResponse> response) {
                if (response.body().isSuccess()) {
                    Intent intent = new Intent();
                    EngagementUser result = response.body().getEngagementUser();
                    intent.putExtra("_id",result.getUserId());
                    intent.putExtra("name",result.getName());
                    intent.putExtra("avatar",result.getAvatar());
                    intent.putExtra("firstname",result.getFirstName());
                    intent.putExtra("lastname",result.getLastName());
                    intent.putExtra("phone",result.getPhone());
                    intent.putExtra("email",result.getEmail());
                    intent.putExtra("zipcode",result.getAddress().getZipCode());
                    intent.putExtra("number",result.getAddress().getNumber());
                    intent.putExtra("street",result.getAddress().getStreet());
                    intent.putExtra("city",result.getAddress().getCity());
                    intent.putExtra("province",result.getAddress().getProvince());
                    intent.putExtra("country",result.getAddress().getCountry());

                    setResult(Activity.RESULT_OK, intent);
                }
                sweetAlertDialog.hide();
                Toast.makeText(NewUserActivity.this, response.body().getCustomMessage(), Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<NewExternalUserResponse> call, Throwable t) {
                // Log error here since request failed
                sweetAlertDialog.hide();
                Toast.makeText(NewUserActivity.this, "message creation failed", Toast.LENGTH_LONG).show();
                finish();
            }

        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        long epoch = 0;

        // month-day-year
        StringBuffer dateBuilder = new StringBuffer();

        if (monthOfYear < 10) {
            dateBuilder.append("0").append(monthOfYear + 1);
        } else {
            dateBuilder.append(monthOfYear + 1);
        }
        dateBuilder.append("-");
        if (dayOfMonth < 10) {
            dateBuilder.append("0").append(dayOfMonth);
        } else {
            dateBuilder.append(dayOfMonth);
        }
        dateBuilder.append("-").append(year);

        String formattedDate = dateBuilder.toString();
        selectedBirthDate.setText(formattedDate);
//        Log.d("Test",formattedDate);

//        try {
//            epoch = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(savedBirthDate).getTime();
//            Calendar calendar = Calendar.getInstance();
//            if(calendar.getTimeInMillis()<epoch){
//                dateToActivate.setText(formattedDate);
//            }
//            else{
////                openAlert("Please select a future date");
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }
}
