package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sijith on 9/23/2016.
 */
public class CommentResponse {
    @SerializedName("CustomMessage")
    private String customMessage;
    @SerializedName("IsSuccess")
    private boolean isSuccess;

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
