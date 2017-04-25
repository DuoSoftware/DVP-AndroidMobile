package apps.veery.com.model.ticketviews;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sijith on 10/7/2016.
 */
public class TicketStatusResponse {

    @SerializedName("CustomMessage")
    private String customMessage;
    @SerializedName("IsSuccess")
    private boolean isSuccess;
    @SerializedName("Result")
    private List<String> ticketResult;

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

    public List<String> getTicketResult() {
        return ticketResult;
    }

    public void setTicketResult(List<String> ticketResult) {
        this.ticketResult = ticketResult;
    }
}
