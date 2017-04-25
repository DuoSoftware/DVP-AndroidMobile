package apps.veery.com.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import apps.veery.com.model.SessionExpire;
import apps.veery.com.model.count.Exception;

/**
 * Created by Lakshan on 9/30/2016.
 */
public class Validations {
    public static final String MyPREFERENCES = "FACETONEDATA";
    public static boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void setTextToTextView(TextView tv,String arg){

        if(null!=arg){
            tv.setText(arg);
        }else if(null==arg){
            tv.setText("");

        }

    }

    public static boolean checkExpiretion(String response){
        Gson gson = new Gson();
        SharedPreferences sharedpreferences;
        SharedPreferences.Editor editor;
        try {
            SessionExpire obj_user = gson.fromJson(response, SessionExpire.class);
            if (obj_user.getMessage().equals("missing_secret")){
                return true;
            }else{
                return false;
            }
        }catch (java.lang.Exception e){
            return false;
        }



    }

}
