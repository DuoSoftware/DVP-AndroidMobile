package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sijith on 9/21/2016.
 */
public class User {
    @SerializedName("_id")
    private String userId;
    @SerializedName("name")
    private String name;
    @SerializedName("username")
    private String userName;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("firstname")
    private String firstName;
    @SerializedName("lastname")
    private String lastName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Override
    public String toString() {
        return getUserName();
    }
}
