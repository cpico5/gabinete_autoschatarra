package mx.gob.cdmx.gabineteautoschatarra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TelefonoAsignado implements Serializable {

    @SerializedName("id_usuario")
    public int idUsuario;
    @SerializedName("telefono")
    public String telefono;
    @SerializedName("fecha")
    public String fecha;
    @SerializedName("id_status")
    public int id_status;

    public TelefonoAsignado() {
    }

    public TelefonoAsignado(int idUsuario, String telefono, String fecha,int id_status) {
        this.idUsuario = idUsuario;
        this.telefono = telefono;
        this.fecha = fecha;
        this.id_status = id_status;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
