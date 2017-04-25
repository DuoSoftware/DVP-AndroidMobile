package apps.veery.com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupDetails extends FirstState {

    @SerializedName("Result")
    @Expose
    private GroupDetailsResult result;

    /**
     * @return The result
     */
    public GroupDetailsResult getResult() {
        return result;
    }

    /**
     * @param result The Result
     */
    public void setResult(GroupDetailsResult result) {
        this.result = result;
    }

}
