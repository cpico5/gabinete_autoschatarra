package mx.gob.cdmx.gabineteautoschatarra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import mx.gob.cdmx.gabineteautoschatarra.db.Anotaciones.PrimaryKey;

public class Status implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    public int id;
    @SerializedName("status")
    public String status;

    public Status(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
