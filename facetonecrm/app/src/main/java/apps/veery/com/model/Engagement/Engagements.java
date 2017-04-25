
package apps.veery.com.model.Engagement;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Engagements {

    @SerializedName("Exception")
    @Expose
    private Object exception;
    @SerializedName("CustomMessage")
    @Expose
    private String customMessage;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Result")
    @Expose
    private Result result;

    /**
     * 
     * @return
     *     The exception
     */
    public Object getException() {
        return exception;
    }

    /**
     * 
     * @param exception
     *     The Exception
     */
    public void setException(Object exception) {
        this.exception = exception;
    }

    /**
     * 
     * @return
     *     The customMessage
     */
    public String getCustomMessage() {
        return customMessage;
    }

    /**
     * 
     * @param customMessage
     *     The CustomMessage
     */
    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    /**
     * 
     * @return
     *     The isSuccess
     */
    public Boolean getIsSuccess() {
        return isSuccess;
    }

    /**
     * 
     * @param isSuccess
     *     The IsSuccess
     */
    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * 
     * @return
     *     The result
     */
    public Result getResult() {
        return result;
    }

    /**
     * 
     * @param result
     *     The Result
     */
    public void setResult(Result result) {
        this.result = result;
    }

}
