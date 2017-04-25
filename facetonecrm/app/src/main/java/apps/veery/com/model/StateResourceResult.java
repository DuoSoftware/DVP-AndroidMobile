package apps.veery.com.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateResourceResult {

    @SerializedName("obj")
    @Expose
    private StateResourceObj obj;
    @SerializedName("vid")
    @Expose
    private String vid;

    /**
     *
     * @return
     * The obj
     */
    public StateResourceObj getObj() {
        return obj;
    }

    /**
     *
     * @param obj
     * The obj
     */
    public void setObj(StateResourceObj obj) {
        this.obj = obj;
    }

    /**
     *
     * @return
     * The vid
     */
    public String getVid() {
        return vid;
    }

    /**
     *
     * @param vid
     * The vid
     */
    public void setVid(String vid) {
        this.vid = vid;
    }

}
