package apps.veery.com.requestmodel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class rUser {

    @SerializedName("grant_type")
    @Expose
    private String grantType;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("scope")
    @Expose
    private String scope;

    /**
     *
     * @return
     * The grantType
     */
    public String getGrantType() {
        return grantType;
    }

    /**
     *
     * @param grantType
     * The grant_type
     */
    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     * The scope
     */
    public String getScope() {
        return scope;
    }

    /**
     *
     * @param scope
     * The scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

}