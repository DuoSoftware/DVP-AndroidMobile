
package apps.veery.com.model.count;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Exception {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StackTrace")
    @Expose
    private String stackTrace;

    /**
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The Message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return
     *     The stackTrace
     */
    public String getStackTrace() {
        return stackTrace;
    }

    /**
     * 
     * @param stackTrace
     *     The StackTrace
     */
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

}
