package mx.gob.cdmx.gabineteautoschatarra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import mx.gob.cdmx.gabineteautoschatarra.db.Anotaciones.AutoIncrement;
import mx.gob.cdmx.gabineteautoschatarra.db.Anotaciones.PrimaryKey;

public class Alcaldia implements Serializable {

    @PrimaryKey
    @AutoIncrement
    public int id;
    @SerializedName("name")
    public String alcaldia;
    @SerializedName("municipio")
    public String id_municipio;

    public Alcaldia() {
    }

    public Alcaldia(int id, String alcaldia, String id_municipio) {
        this.id = id;
        this.alcaldia = alcaldia;
        this.id_municipio = id_municipio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlcaldia() {
        return alcaldia;
    }

    public void setAlcaldia(String alcaldia) {
        this.alcaldia = alcaldia;
    }

    public String getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(String id_municipio) {
        this.id_municipio = id_municipio;
    }
}
