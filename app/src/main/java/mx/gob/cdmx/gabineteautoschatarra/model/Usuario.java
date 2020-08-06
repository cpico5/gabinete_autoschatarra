package mx.gob.cdmx.gabineteautoschatarra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import mx.gob.cdmx.gabineteautoschatarra.db.Anotaciones.PrimaryKey;

public class Usuario implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    public int id;
    @SerializedName("access_token")
    public String token;
    @SerializedName("name")
    public String nombre;
    @SerializedName("paterno")
    public String paterno;
    @SerializedName("materno")
    public String materno;
    @SerializedName("email")
    public String email;
    @SerializedName("password")
    public int password;
    @SerializedName("project")
    public String proyecto;
    @SerializedName("details")
    public Detalle detalle;

    //id push notifications
    //@SerializedName("id")
    public int idUsuarioPush;

    public Usuario() {
    }

    public Usuario(int id, String token, String nombre, String paterno, String materno, String email, int password, String proyecto, Detalle detalle, int idUsuarioPush) {
        this.id = id;
        this.token = token;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.email = email;
        this.password = password;
        this.proyecto = proyecto;
        this.detalle = detalle;
        this.idUsuarioPush = idUsuarioPush;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public Detalle getDetalle() {
        return detalle;
    }

    public void setDetalle(Detalle detalle) {
        this.detalle = detalle;
    }

    public int getIdUsuarioPush() {
        return idUsuarioPush;
    }

    public void setIdUsuarioPush(int idUsuarioPush) {
        this.idUsuarioPush = idUsuarioPush;
    }
}
