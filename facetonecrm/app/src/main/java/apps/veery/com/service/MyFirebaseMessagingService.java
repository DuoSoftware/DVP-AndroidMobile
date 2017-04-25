package apps.veery.com.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import apps.veery.com.facetonecrm.HomeActivity;
import apps.veery.com.facetonecrm.MainActivity;
import apps.veery.com.facetonecrm.R;

/**
 * Created by Sijith on 10/20/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Test";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload here: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification data: " + remoteMessage.getNotification());
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendNotification(remoteMessage.getData());
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(Map messageBody) {

        if(messageBody.get("eventName").equals("message")){ // if this is a message type
            Intent intent = new Intent("notification_receiver");
            intent.putExtra("Message", (String) messageBody.get("Message")); // validating Message data in HomeActivity.(same as MainActvity)
            intent.putExtra("Company", (String) messageBody.get("Company")); //company is int as 103
            intent.putExtra("From", (String) messageBody.get("From"));
            this.sendBroadcast(intent);
        }
        else { // if this is a agent calling
            if (messageBody.get("eventName").equals("agent_found")) {
                Intent intent = new Intent(this, HomeActivity.class);
//            Log.d("Test","message in firebase service = "+messageBody.get("Message"));
                intent.putExtra("Message", (String) messageBody.get("Message")); // validating Message data in HomeActivity.(same as MainActvity)
                intent.putExtra("Company", (String) messageBody.get("Company")); //company is int as 103

                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Veery Message")
                        .setContentText("push message received")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }
            else{
                // NOT a agent_found.do not display notification
            }
        }
    }
}
