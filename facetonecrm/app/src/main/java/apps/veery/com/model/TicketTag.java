package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sijith on 9/19/2016.
 */
public class TicketTag {
    @SerializedName("_id")
    private String ticketId;
    @SerializedName("name")
    private String name;
    @SerializedName("tags")
    private List<String> tags;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return getName();
    }


}
