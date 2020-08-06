package mx.gob.cdmx.gabineteautoschatarra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DatoContent implements Serializable {

    @SerializedName("key")
    public String key;
    @SerializedName("value")
    public String value;

    public DatoContent() {
    }

    public DatoContent(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
