
package apps.veery.com.model.jwtres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context {

    @SerializedName("resourceid")
    @Expose
    private String resourceid;
    @SerializedName("veeryaccount")
    @Expose
    private Veeryaccount veeryaccount;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("group")
    @Expose
    private String group;

    /**
     * 
     * @return
     *     The resourceid
     */
    public String getResourceid() {
        return resourceid;
    }

    /**
     * 
     * @param resourceid
     *     The resourceid
     */
    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    /**
     * 
     * @return
     *     The veeryaccount
     */
    public Veeryaccount getVeeryaccount() {
        return veeryaccount;
    }

    /**
     * 
     * @param veeryaccount
     *     The veeryaccount
     */
    public void setVeeryaccount(Veeryaccount veeryaccount) {
        this.veeryaccount = veeryaccount;
    }

    /**
     * 
     * @return
     *     The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 
     * @param avatar
     *     The avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
