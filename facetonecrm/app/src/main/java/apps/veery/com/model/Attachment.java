package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sajith on 3/20/17.
 */

public class Attachment {
    @SerializedName("file")
    private String attachmentName;
    @SerializedName("size")
    private String attachmentSize;
    @SerializedName("type")
    private String attachmentType;
    @SerializedName("url")
    private String attachmentUrl;

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentSize() {
        return attachmentSize;
    }

    public void setAttachmentSize(String attachmentSize) {
        this.attachmentSize = attachmentSize;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
}
