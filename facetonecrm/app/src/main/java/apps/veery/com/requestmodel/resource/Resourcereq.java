
package apps.veery.com.requestmodel.resource;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resourcereq {

    @SerializedName("ResourceId")
    private String resourceId;
    @SerializedName("HandlingTypes")
    private List<HandlingType> handlingTypes = new ArrayList<HandlingType>();

    /**
     * 
     * @return
     *     The resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * 
     * @param resourceId
     *     The ResourceId
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * 
     * @return
     *     The handlingTypes
     */
    public List<HandlingType> getHandlingTypes() {
        return handlingTypes;
    }

    /**
     * 
     * @param handlingTypes
     *     The HandlingTypes
     */
    public void setHandlingTypes(List<HandlingType> handlingTypes) {
        this.handlingTypes = handlingTypes;
    }

}
