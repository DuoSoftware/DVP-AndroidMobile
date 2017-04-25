package apps.veery.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Sijith on 11/23/2016.
 */
public class NotificationMessage implements Parcelable{
    private String fromUser;
    private String notificationMessage;
    private String receivedDate;

    public NotificationMessage(){
    }

    protected NotificationMessage(Parcel in) {
        fromUser = in.readString();
        notificationMessage = in.readString();
        receivedDate = in.readString();
    }

    public static final Creator<NotificationMessage> CREATOR = new Creator<NotificationMessage>() {
        @Override
        public NotificationMessage createFromParcel(Parcel in) {
            return new NotificationMessage(in);
        }

        @Override
        public NotificationMessage[] newArray(int size) {
            return new NotificationMessage[size];
        }
    };

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fromUser);
        dest.writeString(notificationMessage);
        dest.writeString(receivedDate);
    }
}
