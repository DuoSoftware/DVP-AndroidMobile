package apps.veery.com.facetonecrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import apps.veery.com.model.Auth;
import apps.veery.com.model.ErrorInfo;
import apps.veery.com.model.jwtres.Decode;
import apps.veery.com.requestmodel.rUser;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.ServiceGenarator;
import apps.veery.com.utilities.Components;
import apps.veery.com.utilities.JWTUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    rUser user;
    Button btn_log;
    EditText tv_un, tv_pw;
    TextView tv_powered, tv_www, tv_la_un, tv_la_pw;
    Typeface type_regular, type_bold;
    public static final String MyPREFERENCES = "FACETONEDATA";
    public static final String USERJWT = "UserJWT";
    public static final String USER = "User";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    SweetAlertDialog dd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        type_regular = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        type_bold = Typeface.createFromAsset(getResources().getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        tv_un = (EditText) findViewById(R.id.log_tv_un);
        tv_pw = (EditText) findViewById(R.id.log_tv_pw);
        tv_powered = (TextView) findViewById(R.id.tv_powered);
        tv_www = (TextView) findViewById(R.id.tv_www);
        tv_la_un = (TextView) findViewById(R.id.tv_un);
        tv_la_pw = (TextView) findViewById(R.id.tv_pw);
        btn_log = (Button) findViewById(R.id.log_btn_log);

        tv_powered.setTypeface(type_bold);
        tv_www.setTypeface(type_regular);
        btn_log.setTypeface(type_regular);
        tv_un.setTypeface(type_regular);
        tv_pw.setTypeface(type_regular);
        tv_la_un.setTypeface(type_regular);
        tv_la_pw.setTypeface(type_regular);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        if (null != sharedpreferences.getString(USER, null)) {
            Gson gson = new Gson();
            AppControler.setUser(gson.fromJson(sharedpreferences.getString(USER, null), Auth.class));
            String user_JWT;
            try {
                user_JWT = JWTUtils.decoded(AppControler.getUser().getAccessToken());
                AppControler.setJwtres(gson.fromJson(user_JWT, Decode.class));
            } catch (Exception e) {
               Toast.makeText(this,getString(R.string.failed_to_get_saved_user),Toast.LENGTH_SHORT).show();
            }

            goToHomeActivity();
        }

        user = new rUser();
        user.setGrantType("password");
//        user.setScope("resourceid write_ardsresource write_notification read_myUserProfile profile_veeryaccount profile_avatar");
        user.setScope("all_all resourceid write_ardsresource write_notification read_myUserProfile profile_veeryaccount profile_avatar write_ticket write_ticketview profile_group");
        dd = new Components().showLoadingSuccessMessage(this);
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.show();
                if (!isInternetOn(getApplicationContext())) {

                    dd.setTitleText("opps!").setContentText("Please check you Internet Connection")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                } else {
                    validation();
                }

            }
        });

//        Log.d("Test", "on create in  main activity");

// entering point of push notification when app is dead or background
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
//            for (String key:bundle.keySet()) {
//                Log.d("Test", "starting a dead or background app.  "+key);
//            }
            if(null != bundle.get("eventName")) {
                if (bundle.get("eventName").toString().equals("agent_found")) { // No use of this. system display pop up anyhow.

                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("Message", bundle.get("Message").toString());// validating Message data in HomeActivity.(same as MainActvity)
                    intent.putExtra("Company", bundle.get("Company").toString()); //company is int as 103
                    if (null != bundle.get("From")) { // if this is a message type
                        intent.putExtra("From", bundle.get("From").toString());
                    }
                    startActivity(intent);
                } else {
                    // NOT a agent_found.do not display notification
                }
            }
            else{
                // app crash redirect to if statement. so view HomeActivity again.
                Toast.makeText(this,"Clear recent apps and restart.Caused by saved crash bundle.",Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void validation() {
        if (TextUtils.isEmpty(tv_un.getText())) {
            dd.setTitleText("opps!").setContentText("Username can't empty")
                    .setConfirmText("OK")
                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
        } else if (TextUtils.isEmpty(tv_pw.getText())) {
            dd.setTitleText("opps!").setContentText("Password can't empty")
                    .setConfirmText("OK")
                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
        } else {
            user.setUsername(tv_un.getText().toString());
            user.setPassword(tv_pw.getText().toString());
            networkCall(user);
        }
    }

    private void networkCall(rUser user) {
        final Gson gson = new Gson();
        ServiceGenarator.CurrentFactory.getInstance().getAuth(user).enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (response.isSuccessful()) {
                    dd.setTitleText("Success!")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    dd.dismiss();
                    if (response.body().getAccessToken() != null) {
                        AppControler.setUser(response.body());

                        String jsonUser = gson.toJson(response.body());
                        editor.putString("User", jsonUser);
                        editor.commit();

//                        Key key = MacProvider.generateKey();

//                        String aa= String.valueOf(Jwts.parser().setSigningKey("").parseClaimsJws(AppControler.getUser().getAccessToken()).getBody());
                        String aa;
                        try {
                            aa = JWTUtils.decoded(AppControler.getUser().getAccessToken());

                            Decode res = gson.fromJson(aa, Decode.class);
                            AppControler.setJwtres(res);
//                            String jsonJWT = gson.toJson(aa);
                            editor.putString(USERJWT, aa);
                            editor.commit();
                            goToHomeActivity();
                        } catch (Exception e) {
                            new Components().openAlert(MainActivity.this, "Error", e.toString());
                        }


                    } else {
                        dd.setTitleText("opps!").setContentText("Security Token Error")
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                } else {

                    try {
                        Gson gson = new Gson();
                        String json = response.errorBody().string();
                        Log.d("ERROR JSON", json);
                        ErrorInfo error = gson.fromJson(json, ErrorInfo.class);

                        dd.setTitleText("opps!").setContentText(error.getErrorDescription())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    } catch (IOException e) {
                        new Components().openAlert(MainActivity.this, "Error", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                if (null != t.getMessage())
                    dd.setTitleText("opps!").setContentText(t.getMessage())
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);

            }
        });

    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void goToHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        this.finish();
    }
}
