package mx.gob.cdmx.gabineteautoschatarra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Detalle implements Serializable {

    @SerializedName("user_id")
    public int idUsuario;
    @SerializedName("campamento")
    public String campamento;
    @SerializedName("operator_type_id")
    public int tipoOperador;
    @SerializedName("phone_number")
    public Long telefono;
    @SerializedName("imei")
    public Long imei;
    @SerializedName("zona")
    public int zona;

    public Detalle() {
    }

    public Detalle(int idUsuario, String campamento, int tipoOperador, Long telefono, Long imei, int zona) {
        this.idUsuario = idUsuario;
        this.campamento = campamento;
        this.tipoOperador = tipoOperador;
        this.telefono = telefono;
        this.imei = imei;
        this.zona = zona;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCampamento() {
        return campamento;
    }

    public void setCampamento(String campamento) {
        this.campamento = campamento;
    }

    public int getTipoOperador() {
        return tipoOperador;
    }

    public void setTipoOperador(int tipoOperador) {
        this.tipoOperador = tipoOperador;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public Long getImei() {
        return imei;
    }

    public void setImei(Long imei) {
        this.imei = imei;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }
}
