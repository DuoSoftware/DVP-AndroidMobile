package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sijith on 12/23/2016.
 */

public class NewExternalUserResponse {
    @SerializedName("CustomMessage")
    private String customMessage;
    @SerializedName("IsSuccess")
    private boolean isSuccess;
    @SerializedName("Result")
    private EngagementUser engagementUser;

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

    public EngagementUser getEngagementUser() {
        return engagementUser;
    }

    public void setEngagementUser(EngagementUser engagementUser) {
        this.engagementUser = engagementUser;
    }
}
