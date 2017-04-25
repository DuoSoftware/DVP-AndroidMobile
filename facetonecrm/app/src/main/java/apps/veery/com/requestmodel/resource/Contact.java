
package apps.veery.com.requestmodel.resource;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Contact {

    @SerializedName("ContactName")
    @Expose
    private String contactName;
    @SerializedName("Domain")
    @Expose
    private String domain;
    @SerializedName("Extention")
    @Expose
    private String extention;
    @SerializedName("ContactType")
    @Expose
    private String contactType;
    @SerializedName("Profile")
    private String profile;

    /**
     * 
     * @return
     *     The contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 
     * @param contactName
     *     The ContactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 
     * @return
     *     The domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * 
     * @param domain
     *     The Domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * 
     * @return
     *     The extention
     */
    public String getExtention() {
        return extention;
    }

    /**
     * 
     * @param extention
     *     The Extention
     */
    public void setExtention(String extention) {
        this.extention = extention;
    }

    /**
     * 
     * @return
     *     The contactType
     */
    public String getContactType() {
        return contactType;
    }

    /**
     * 
     * @param contactType
     *     The ContactType
     */
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
