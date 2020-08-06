package mx.gob.cdmx.gabineteautoschatarra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Aplicacion implements Serializable {

    private int id;
    @SerializedName("status")
    private String status;
    @SerializedName("current")
    private String current;
    @SerializedName("latest")
    private String Latest;
    @SerializedName("upgrade")
    private String upgrade;
    @SerializedName("host")
    private String host;

    public Aplicacion() {
    }

    public Aplicacion(int id, String status, String current, String latest, String upgrade, String host) {
        this.id = id;
        this.status = status;
        this.current = current;
        Latest = latest;
        this.upgrade = upgrade;
        this.host = host;
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

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getLatest() {
        return Latest;
    }

    public void setLatest(String latest) {
        Latest = latest;
    }

    public String getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
