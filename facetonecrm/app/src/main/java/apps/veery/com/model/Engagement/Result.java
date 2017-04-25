
package apps.veery.com.model.Engagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("company")
    @Expose
    private Integer company;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("tenant")
    @Expose
    private Integer tenant;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("engagements")
    @Expose
    private List<String> engagements = new ArrayList<String>();
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The _id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The company
     */
    public Integer getCompany() {
        return company;
    }

    /**
     * 
     * @param company
     *     The company
     */
    public void setCompany(Integer company) {
        this.company = company;
    }

    /**
     * 
     * @return
     *     The profile
     */
    public String getProfile() {
        return profile;
    }

    /**
     * 
     * @param profile
     *     The profile
     */
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * 
     * @return
     *     The tenant
     */
    public Integer getTenant() {
        return tenant;
    }

    /**
     * 
     * @param tenant
     *     The tenant
     */
    public void setTenant(Integer tenant) {
        this.tenant = tenant;
    }

    /**
     * 
     * @return
     *     The v
     */
    public Integer getV() {
        return v;
    }

    /**
     * 
     * @param v
     *     The __v
     */
    public void setV(Integer v) {
        this.v = v;
    }

    /**
     * 
     * @return
     *     The engagements
     */
    public List<String> getEngagements() {
        return engagements;
    }

    /**
     * 
     * @param engagements
     *     The engagements
     */
    public void setEngagements(List<String> engagements) {
        this.engagements = engagements;
    }

    /**
     * 
     * @return
     *     The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     * @param updatedAt
     *     The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
