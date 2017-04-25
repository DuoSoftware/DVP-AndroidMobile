package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

import apps.veery.com.model.jwtres.TicketResult;

/**
 * Created by Sijith on 9/21/2016.
 */
public class OneTicketResponse {
    @SerializedName("CustomMessage")
    private String customMessage;
    @SerializedName("IsSuccess")
    private boolean isSuccess;
    @SerializedName("Result")
    private TicketResult ticketResult;

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

    public TicketResult getTicketResult() {
        return ticketResult;
    }

    public void setTicketResult(TicketResult ticketResult) {
        this.ticketResult = ticketResult;
    }
}
