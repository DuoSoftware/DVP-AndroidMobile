package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sijith on 9/23/2016.
 */
public class NewComment {


    @SerializedName("body")
    private String body;
    @SerializedName("body_type")
    private String bodyType;
    @SerializedName("type")
    private String type;
    @SerializedName("public")
    private String publicType;
    @SerializedName("channel")
    private String channel;
    @SerializedName("channel_from")
    private String channelFrom;
    @SerializedName("channel_to")
    private String channelTo;
    @SerializedName("engagement_session")
    private String engagementSession;
    @SerializedName("reply_session")
    private String replySession;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublicType() {
        return publicType;
    }

    public void setPublicType(String publicType) {
        this.publicType = publicType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public String getEngagementSession() {
        return engagementSession;
    }

    public void setEngagementSession(String engagementSession) {
        this.engagementSession = engagementSession;
    }

    public String getReplySession() {
        return replySession;
    }

    public void setReplySession(String replySession) {
        this.replySession = replySession;
    }

    //    {
//        "body": "test comment 111930",
//            "body_type": "text",
//            "type": "comment",
//            "public": "public",
//            "channel": "twitter",
//            "channel_from": "duoxdemouser",
//            "channel_to": "sukithachanaka",
//            "engagement_session": "",
//            "reply_session": "777840154689933312"
//    }

}
