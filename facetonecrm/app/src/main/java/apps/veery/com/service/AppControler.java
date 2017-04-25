package apps.veery.com.service;

import android.app.Application;
import android.util.Log;

//import com.squareup.leakcanary.LeakCanary;

import apps.veery.com.model.Auth;
import apps.veery.com.model.jwtres.Decode;

/**
 * Created by Lakshan on 9/12/2016.
 */
public class AppControler extends Application {
   public static Auth user;
    private static Decode jwtres;
    private static boolean ticket=true; //true by lucky earlier
    private static boolean call=false;

    public static boolean isTicket() {
        return ticket;
    }

    public static void setTicket(boolean ticket) {
        AppControler.ticket = ticket;
    }

    public static boolean isCall() {
        return call;
    }

    public static void setCall(boolean call) {
        AppControler.call = call;
    }

    public static Auth getUser() {
        return user;
    }

    public static void setUser(Auth user) {
        AppControler.user = user;
    }

    public static Decode getJwtres() {
        return jwtres;
    }

    public static void setJwtres(Decode jwtres) {
        AppControler.jwtres = jwtres;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
    }

}
