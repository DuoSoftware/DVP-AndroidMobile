
package apps.veery.com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("AcwTime")
    @Expose
    private Integer acwTime;
    @SerializedName("BreakTime")
    @Expose
    private Integer breakTime;
    @SerializedName("OnCallTime")
    @Expose
    private Integer onCallTime;
    @SerializedName("StaffedTime")
    @Expose
    private Integer staffedTime;
    @SerializedName("IdleTime")
    @Expose
    private Integer idleTime;
    @SerializedName("HoldTime")
    @Expose
    private Integer holdTime;
    @SerializedName("IncomingCallCount")
    @Expose
    private Integer incomingCallCount;
    @SerializedName("TransferCallCount")
    @Expose
    private Integer transferCallCount;
    @SerializedName("MissCallCount")
    @Expose
    private Integer missCallCount;

    /**
     * 
     * @return
     *     The resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * 
     * @param resourceId
     *     The ResourceId
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * 
     * @return
     *     The acwTime
     */
    public Integer getAcwTime() {
        return acwTime;
    }

    /**
     * 
     * @param acwTime
     *     The AcwTime
     */
    public void setAcwTime(Integer acwTime) {
        this.acwTime = acwTime;
    }

    /**
     * 
     * @return
     *     The breaktime
     */
    public Integer getBreakTime() {
        return breakTime;
    }

    /**
     * 
     * @param breakTime
     *     The BreakTime
     */
    public void setBreakTime(Integer breakTime) {
        this.breakTime = breakTime;
    }

    /**
     * 
     * @return
     *     The onCallTime
     */
    public Integer getOnCallTime() {
        return onCallTime;
    }

    /**
     * 
     * @param onCallTime
     *     The OnCallTime
     */
    public void setOnCallTime(Integer onCallTime) {
        this.onCallTime = onCallTime;
    }

    /**
     * 
     * @return
     *     The staffedTime
     */
    public Integer getStaffedTime() {
        return staffedTime;
    }

    /**
     * 
     * @param staffedTime
     *     The StaffedTime
     */
    public void setStaffedTime(Integer staffedTime) {
        this.staffedTime = staffedTime;
    }

    /**
     * 
     * @return
     *     The idleTime
     */
    public Integer getIdleTime() {
        return idleTime;
    }

    /**
     * 
     * @param idleTime
     *     The IdleTime
     */
    public void setIdleTime(Integer idleTime) {
        this.idleTime = idleTime;
    }

    /**
     * 
     * @return
     *     The holdTime
     */
    public Integer getHoldTime() {
        return holdTime;
    }

    /**
     * 
     * @param holdTime
     *     The HoldTime
     */
    public void setHoldTime(Integer holdTime) {
        this.holdTime = holdTime;
    }

    /**
     * 
     * @return
     *     The incomingCallCount
     */
    public Integer getIncomingCallCount() {
        return incomingCallCount;
    }

    /**
     * 
     * @param incomingCallCount
     *     The IncomingCallCount
     */
    public void setIncomingCallCount(Integer incomingCallCount) {
        this.incomingCallCount = incomingCallCount;
    }

    /**
     * 
     * @return
     *     The transferCallCount
     */
    public Integer getTransferCallCount() {
        return transferCallCount;
    }

    /**
     * 
     * @param transferCallCount
     *     The TransferCallCount
     */
    public void setTransferCallCount(Integer transferCallCount) {
        this.transferCallCount = transferCallCount;
    }

    /**
     * 
     * @return
     *     The missCallCount
     */
    public Integer getMissCallCount() {
        return missCallCount;
    }

    /**
     * 
     * @param missCallCount
     *     The MissCallCount
     */
    public void setMissCallCount(Integer missCallCount) {
        this.missCallCount = missCallCount;
    }

}
