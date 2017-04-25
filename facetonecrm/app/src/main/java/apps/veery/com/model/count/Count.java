
package apps.veery.com.model.count;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Count {

    @SerializedName("Exception")
    @Expose
    private Exception exception;
    @SerializedName("CustomMessage")
    @Expose
    private String customMessage;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;

    @SerializedName("Result")
    @Expose
    private Integer result;

    /**
     * 
     * @return
     *     The exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * 
     * @param exception
     *     The Exception
     */
    public void setException(Exception exception) {
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

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
