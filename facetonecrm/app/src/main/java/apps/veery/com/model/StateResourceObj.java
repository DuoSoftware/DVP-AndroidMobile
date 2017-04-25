package apps.veery.com.model;


import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateResourceObj {

    @SerializedName("Company")
    @Expose
    private Integer company;
    @SerializedName("Tenant")
    @Expose
    private Integer tenant;
    @SerializedName("Class")
    @Expose
    private String _class;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("ResourceName")
    @Expose
    private String resourceName;
    @SerializedName("LoginTasks")
    @Expose
    private List<String> loginTasks = new ArrayList<String>();
    @SerializedName("OtherInfo")
    @Expose
    private Object otherInfo;

    /**
     *
     * @return
     * The company
     */
    public Integer getCompany() {
        return company;
    }

    /**
     *
     * @param company
     * The Company
     */
    public void setCompany(Integer company) {
        this.company = company;
    }

    /**
     *
     * @return
     * The tenant
     */
    public Integer getTenant() {
        return tenant;
    }

    /**
     *
     * @param tenant
     * The Tenant
     */
    public void setTenant(Integer tenant) {
        this.tenant = tenant;
    }

    /**
     *
     * @return
     * The _class
     */
    public String getClass_() {
        return _class;
    }

    /**
     *
     * @param _class
     * The Class
     */
    public void setClass_(String _class) {
        this._class = _class;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The Type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The category
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category
     * The Category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @return
     * The resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     *
     * @param resourceId
     * The ResourceId
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     *
     * @return
     * The resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     *
     * @param resourceName
     * The ResourceName
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     *
     * @return
     * The loginTasks
     */
    public List<String> getLoginTasks() {
        return loginTasks;
    }

    /**
     *
     * @param loginTasks
     * The LoginTasks
     */
    public void setLoginTasks(List<String> loginTasks) {
        this.loginTasks = loginTasks;
    }

    /**
     *
     * @return
     * The otherInfo
     */
    public Object getOtherInfo() {
        return otherInfo;
    }

    /**
     *
     * @param otherInfo
     * The OtherInfo
     */
    public void setOtherInfo(Object otherInfo) {
        this.otherInfo = otherInfo;
    }

}
