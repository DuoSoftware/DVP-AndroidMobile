package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sajith on 3/17/17.
 */

public class ImageUploadResponse extends CommentResponse {

    @SerializedName("Result")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
