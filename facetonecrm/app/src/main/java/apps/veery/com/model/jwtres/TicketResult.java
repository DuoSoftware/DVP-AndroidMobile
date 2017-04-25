package apps.veery.com.model.jwtres;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import apps.veery.com.model.Attachment;
import apps.veery.com.model.Comment;
import apps.veery.com.model.EngagementSession;
import apps.veery.com.model.SubTicket;
import apps.veery.com.model.Ticket;

/**
 * Created by Sijith on 9/21/2016.
 */
public class TicketResult {

    @SerializedName("comments")
    private List<Comment> comments;
    @SerializedName("engagement_session")
    private EngagementSession engagementSession;
    @SerializedName("sub_tickets")  // to get no of sub tickets.
    private List<SubTicket> subTickets;
    @SerializedName("attachments")
    private List<Attachment> attachments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public EngagementSession getEngagementSession() {
        return engagementSession;
    }

    public void setEngagementSession(EngagementSession engagementSession) {
        this.engagementSession = engagementSession;
    }

    public List<SubTicket> getSubTickets() {
        return subTickets;
    }

    public void setSubTickets(List<SubTicket> subTickets) {
        this.subTickets = subTickets;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
