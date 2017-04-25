package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sijith on 9/23/2016.
 */
public class EngagementSession {
    @SerializedName("_id")
    private String engagementId;
    @SerializedName("channel_from")
    private String channelFrom;
    @SerializedName("channel_to")
    private String channelTo;
    @SerializedName("channel")
    private String channel;
//    @SerializedName("notes") // notes was string previous.now it's array. no use for now. so comment this out.
//    private String notes;
    @SerializedName("created_at")
    String createdDate;

    public String getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(String engagementId) {
        this.engagementId = engagementId;
    }

    public String getChannelFrom() {
        return channelFrom;
    }

    public void setChannelFrom(String channelFrom) {
        this.channelFrom = channelFrom;
    }

    public String getChannelTo() {
        return channelTo;
    }

    public void setChannelTo(String channelTo) {
        this.channelTo = channelTo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
