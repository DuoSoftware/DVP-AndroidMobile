package apps.veery.com.model.Engagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionListResult {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("engagement_id")
    @Expose
    private String engagementId;
    @SerializedName("channel_from")
    @Expose
    private String channelFrom;
    @SerializedName("channel_to")
    @Expose
    private String channelTo;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("company")
    @Expose
    private Integer company;
    @SerializedName("has_profile")
    @Expose
    private Boolean hasProfile;
    @SerializedName("tenant")
    @Expose
    private Integer tenant;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("notes")
    @Expose
    private List<Note> notes = new ArrayList<Note>();
    @SerializedName("channel")
    @Expose
    private String channel;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The _id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The engagementId
     */
    public String getEngagementId() {
        return engagementId;
    }

    /**
     *
     * @param engagementId
     * The engagement_id
     */
    public void setEngagementId(String engagementId) {
        this.engagementId = engagementId;
    }

    /**
     *
     * @return
     * The channelFrom
     */
    public String getChannelFrom() {
        return channelFrom;
    }

    /**
     *
     * @param channelFrom
     * The channel_from
     */
    public void setChannelFrom(String channelFrom) {
        this.channelFrom = channelFrom;
    }

    /**
     *
     * @return
     * The channelTo
     */
    public String getChannelTo() {
        return channelTo;
    }

    /**
     *
     * @param channelTo
     * The channel_to
     */
    public void setChannelTo(String channelTo) {
        this.channelTo = channelTo;
    }

    /**
     *
     * @return
     * The direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     *
     * @param direction
     * The direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     *
     * @return
     * The company
     */
    public Integer getCompany() {
        return company;
    }

    /**
     *
     * @param company
     * The company
     */
    public void setCompany(Integer company) {
        this.company = company;
    }

    /**
     *
     * @return
     * The hasProfile
     */
    public Boolean getHasProfile() {
        return hasProfile;
    }

    /**
     *
     * @param hasProfile
     * The has_profile
     */
    public void setHasProfile(Boolean hasProfile) {
        this.hasProfile = hasProfile;
    }

    /**
     *
     * @return
     * The tenant
     */
    public Integer getTenant() {
        return tenant;
    }

    /**
     *
     * @param tenant
     * The tenant
     */
    public void setTenant(Integer tenant) {
        this.tenant = tenant;
    }

    /**
     *
     * @return
     * The v
     */
    public Integer getV() {
        return v;
    }

    /**
     *
     * @param v
     * The __v
     */
    public void setV(Integer v) {
        this.v = v;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The notes
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     *
     * @param notes
     * The notes
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    /**
     *
     * @return
     * The channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     *
     * @param channel
     * The channel
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

}
