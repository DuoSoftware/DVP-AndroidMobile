package apps.veery.com.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sijith on 10/24/2016.
 */
public class  Address {
    @SerializedName("zipcode")
    private String zipCode;
    @SerializedName("number")
    private String number;
    @SerializedName("street")
    private String street;
    @SerializedName("city")
    private String city;
    @SerializedName("province")
    private String province;
    @SerializedName("country")
    private String country;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
