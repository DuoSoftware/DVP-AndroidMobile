package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sijith on 9/19/2016.
 */
public class TicketTagResponse {
    @SerializedName("CustomMessage")
    private String customMessage;
    @SerializedName("IsSuccess")
    private boolean isSuccess;
    @SerializedName("Result")
    private List<TicketTag> results;

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

    public List<TicketTag> getResults() {
        return results;
    }

    public void setResults(List<TicketTag> results) {
        this.results = results;
    }
}
