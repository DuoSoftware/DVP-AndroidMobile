
package apps.veery.com.model.ticketviews;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Conditions {

    @SerializedName("any")
    @Expose
    private List<Object> any = new ArrayList<Object>();
    @SerializedName("all")
    @Expose
    private List<Object> all = new ArrayList<Object>();

    /**
     * 
     * @return
     *     The any
     */
    public List<Object> getAny() {
        return any;
    }

    /**
     * 
     * @param any
     *     The any
     */
    public void setAny(List<Object> any) {
        this.any = any;
    }

    /**
     * 
     * @return
     *     The all
     */
    public List<Object> getAll() {
        return all;
    }

    /**
     * 
     * @param all
     *     The all
     */
    public void setAll(List<Object> all) {
        this.all = all;
    }

}
