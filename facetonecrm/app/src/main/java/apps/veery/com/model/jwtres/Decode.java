
package apps.veery.com.model.jwtres;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Decode {

    @SerializedName("iss")
    @Expose
    private String iss;
    @SerializedName("jti")
    @Expose
    private String jti;
    @SerializedName("sub")
    @Expose
    private String sub;
    @SerializedName("exp")
    @Expose
    private Integer exp;
    @SerializedName("tenant")
    @Expose
    private Integer tenant;
    @SerializedName("company")
    @Expose
    private Integer company;
    @SerializedName("aud")
    @Expose
    private String aud;
    @SerializedName("context")
    @Expose
    private Context context;
    @SerializedName("scope")
    @Expose
    private List<Scope> scope = new ArrayList<Scope>();
    @SerializedName("iat")
    @Expose
    private Integer iat;

    /**
     * 
     * @return
     *     The iss
     */
    public String getIss() {
        return iss;
    }

    /**
     * 
     * @param iss
     *     The iss
     */
    public void setIss(String iss) {
        this.iss = iss;
    }

    /**
     * 
     * @return
     *     The jti
     */
    public String getJti() {
        return jti;
    }

    /**
     * 
     * @param jti
     *     The jti
     */
    public void setJti(String jti) {
        this.jti = jti;
    }

    /**
     * 
     * @return
     *     The sub
     */
    public String getSub() {
        return sub;
    }

    /**
     * 
     * @param sub
     *     The sub
     */
    public void setSub(String sub) {
        this.sub = sub;
    }

    /**
     * 
     * @return
     *     The exp
     */
    public Integer getExp() {
        return exp;
    }

    /**
     * 
     * @param exp
     *     The exp
     */
    public void setExp(Integer exp) {
        this.exp = exp;
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
     *     The aud
     */
    public String getAud() {
        return aud;
    }

    /**
     * 
     * @param aud
     *     The aud
     */
    public void setAud(String aud) {
        this.aud = aud;
    }

    /**
     * 
     * @return
     *     The context
     */
    public Context getContext() {
        return context;
    }

    /**
     * 
     * @param context
     *     The context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 
     * @return
     *     The scope
     */
    public List<Scope> getScope() {
        return scope;
    }

    /**
     * 
     * @param scope
     *     The scope
     */
    public void setScope(List<Scope> scope) {
        this.scope = scope;
    }

    /**
     * 
     * @return
     *     The iat
     */
    public Integer getIat() {
        return iat;
    }

    /**
     * 
     * @param iat
     *     The iat
     */
    public void setIat(Integer iat) {
        this.iat = iat;
    }

}
