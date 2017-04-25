package apps.veery.com.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupDetailsResult {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("company")
    @Expose
    private Integer company;
    @SerializedName("tenant")
    @Expose
    private Integer tenant;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("users")
    @Expose
    private List<GroupDetailsUsers> users = new ArrayList<GroupDetailsUsers>();

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The _id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

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
     * The company
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
     * The tenant
     */
    public void setTenant(Integer tenant) {
        this.tenant = tenant;
    }

    /**
     *
     * @return
     * The v
     */
    public Integer getV() {
        return v;
    }

    /**
     *
     * @param v
     * The __v
     */
    public void setV(Integer v) {
        this.v = v;
    }

    /**
     *
     * @return
     * The users
     */
    public List<GroupDetailsUsers> getUsers() {
        return users;
    }

    /**
     *
     * @param users
     * The users
     */
    public void setUsers(List<GroupDetailsUsers> users) {
        this.users = users;
    }

}
