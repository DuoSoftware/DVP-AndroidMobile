package apps.veery.com.utilities;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * Created by Lakshan on 9/14/2016.
 */
public class JWTUtils {

    public static String decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
//            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
//            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
            return getJson(split[1]);
        } catch (UnsupportedEncodingException e) {
            //Error
            return "error";
        }

    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.NO_WRAP);
        return new String(decodedBytes, "UTF-8");
    }
}
