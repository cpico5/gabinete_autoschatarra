package mx.gob.cdmx.gabineteautoschatarra.service;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Usuario implements Serializable {

    @SerializedName("id")
    private int idUser;
    @SerializedName("name")
    private String nombre;
    @SerializedName("paterno")
    private String paterno;
    @SerializedName("materno")
    private String materno;
    @SerializedName("codigo")
    private String codigo;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("token")
    private String token;
    @SerializedName("type")
    private String tipoUsuario;
    @SerializedName("code")
    private String codigoBarras;
    @SerializedName("path_image")
    private String pathImagen;

    public Usuario() {
    }

    /**
     *
     * @param idUser
     * @param nombre
     * @param paterno
     * @param materno
     * @param email
     * @param password
     * @param token
     */

    public Usuario(int idUser, String nombre, String paterno, String materno, String codigo, String email, String password, String token, String pathImagen) {
        this.idUser = idUser;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.codigo = codigo;
        this.email = email;
        this.password = password;
        this.token = token;
        this.tipoUsuario = tipoUsuario;
        this.codigoBarras = codigoBarras;
        this.pathImagen = pathImagen;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getPathImagen() {
        return pathImagen;
    }

    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUser=" + idUser +
                ", nombre='" + nombre + '\'' +
                ", paterno='" + paterno + '\'' +
                ", materno='" + materno + '\'' +
                ", codigo='" + codigo + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
