package apps.veery.com.requestmodel.resource;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sijith on 9/19/2016.
 */
public class NewTicket {

    @SerializedName("_id")
    private String ticketId;
    @SerializedName("type")
    private String type;
    @SerializedName("subject")
    private String subject;
    @SerializedName("description")
    private String description;
    @SerializedName("priority")
    private String priority;
    @SerializedName("status")
    private String status;
    @SerializedName("requester")
    private Long requester;
    @SerializedName("channel")
    private String channel;
    @SerializedName("assignee")
    private String assignee;
    @SerializedName("tags")
    private List<String> tags;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getRequester() {
        return requester;
    }

    public void setRequester(Long requester) {
        this.requester = requester;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
