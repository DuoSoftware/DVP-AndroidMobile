
package apps.veery.com.requestmodel.resource;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class HandlingType {

    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Contact")
    @Expose
    private Contact contact;

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The Type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * 
     * @param contact
     *     The Contact
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

}
