
package ru.training.apitesting.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecificResponse {

    @SerializedName("_value")
    @Expose
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SpecificResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("value");
        sb.append('=');
        sb.append(((this.value == null)?"<null>":this.value));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.value == null)? 0 :this.value.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if ((other instanceof SpecificResponse) == false) {
            return false;
        }
        SpecificResponse rhs = ((SpecificResponse) other);
        return (this.value.equals(rhs.value));
    }

}
