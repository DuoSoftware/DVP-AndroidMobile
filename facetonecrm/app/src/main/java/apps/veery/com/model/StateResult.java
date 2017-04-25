package apps.veery.com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateResult {

    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("StateChangeTime")
    @Expose
    private String stateChangeTime;

    /**
     *
     * @return
     * The state
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     * The State
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     * The reason
     */
    public String getReason() {
        return reason;
    }

    /**
     *
     * @param reason
     * The Reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     *
     * @return
     * The stateChangeTime
     */
    public String getStateChangeTime() {
        return stateChangeTime;
    }

    /**
     *
     * @param stateChangeTime
     * The StateChangeTime
     */
    public void setStateChangeTime(String stateChangeTime) {
        this.stateChangeTime = stateChangeTime;
    }

}
