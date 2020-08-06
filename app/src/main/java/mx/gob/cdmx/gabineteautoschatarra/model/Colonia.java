package mx.gob.cdmx.gabineteautoschatarra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import mx.gob.cdmx.gabineteautoschatarra.db.Anotaciones.PrimaryKey;

public class Colonia implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    public int id;
    @SerializedName("entidad")
    public int entidad;
    @SerializedName("municipio")
    public int municipio;
    @SerializedName("nombre")
    public String colonia;
    @SerializedName("cp")
    public String cp;

    public Colonia() {
    }

    public Colonia(int id, int entidad, int municipio, String colonia, String cp) {
        this.id = id;
        this.entidad = entidad;
        this.municipio = municipio;
        this.colonia = colonia;
        this.cp = cp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntidad() {
        return entidad;
    }

    public void setEntidad(int entidad) {
        this.entidad = entidad;
    }

    public int getMunicipio() {
        return municipio;
    }

    public void setMunicipio(int municipio) {
        this.municipio = municipio;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }
}
