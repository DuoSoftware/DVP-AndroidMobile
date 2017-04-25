package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sijith on 10/10/2016.
 */
public class SubTicket {
    @SerializedName("_id")
    private String ticketId;
    @SerializedName("subject")
    private String subject;
    @SerializedName("reference")
    private String reference;
    @SerializedName("description")
    private String description;
    @SerializedName("status")
    private String status;
    @SerializedName("priority")
    private String priority;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("type")
    private String type;
    @SerializedName("requester")
    private String requester;     // diff from TICKET.Class ... here requester is a string not an object
    @SerializedName("submitter")
    private String submitter;
    @SerializedName("channel")
    private String channel;
    @SerializedName("tags")
    private List<String> tags;
    @SerializedName("comments")     // to get no of comments.
    private List<String> comments;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
