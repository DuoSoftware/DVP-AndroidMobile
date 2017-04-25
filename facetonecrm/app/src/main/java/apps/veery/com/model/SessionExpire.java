package apps.veery.com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionExpire {

    @SerializedName("message")
    @Expose
    private String message;

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}