package mx.gob.cdmx.gabineteautoschatarra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bitacora implements Serializable {

    @SerializedName("id_padron")
    public int id_padron;
    @SerializedName("id_usuario")
    public int id_usuario;
    @SerializedName("fecha_asignado")
    public String fecha;
    @SerializedName("id_status")
    public int id_status;

    public Bitacora() {
    }

    public Bitacora(int id_padron, int id_usuario, String fecha, int id_status) {
        this.id_padron = id_padron;
        this.id_usuario = id_usuario;
        this.fecha = fecha;
        this.id_status = id_status;
    }

    public int getId_padron() {
        return id_padron;
    }

    public void setId_padron(int id_padron) {
        this.id_padron = id_padron;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId_status() {
        return id_status;
    }

    public void setId_status(int id_status) {
        this.id_status = id_status;
    }
}
