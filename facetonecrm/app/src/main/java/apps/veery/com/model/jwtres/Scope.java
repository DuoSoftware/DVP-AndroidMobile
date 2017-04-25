
package apps.veery.com.model.jwtres;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Scope {

    @SerializedName("resource")
    @Expose
    private String resource;
    @SerializedName("actions")
    @Expose
    private List<String> actions = new ArrayList<String>();

    /**
     * 
     * @return
     *     The resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * 
     * @param resource
     *     The resource
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * 
     * @return
     *     The actions
     */
    public List<String> getActions() {
        return actions;
    }

    /**
     * 
     * @param actions
     *     The actions
     */
    public void setActions(List<String> actions) {
        this.actions = actions;
    }

}
