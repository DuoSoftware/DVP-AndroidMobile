
package apps.veery.com.model.ticketviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("company")
    @Expose
    private Integer company;
    @SerializedName("tenant")
    @Expose
    private Integer tenant;
    @SerializedName("public")
    @Expose
    private Boolean _public;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("conditions")
    @Expose
    private Conditions conditions;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

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
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 
     * @param owner
     *     The owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
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
     *     The _public
     */
    public Boolean getPublic() {
        return _public;
    }

    /**
     * 
     * @param _public
     *     The public
     */
    public void setPublic(Boolean _public) {
        this._public = _public;
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
     *     The active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * 
     * @param active
     *     The active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * 
     * @return
     *     The conditions
     */
    public Conditions getConditions() {
        return conditions;
    }

    /**
     * 
     * @param conditions
     *     The conditions
     */
    public void setConditions(Conditions conditions) {
        this.conditions = conditions;
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
