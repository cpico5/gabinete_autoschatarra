package mx.gob.cdmx.gabineteautoschatarra.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class UsuarioRepository {

    private static final String TAG_LOG     = "UsuarioRepository";
    private static String TABLE_NAME        = "usuario";
    public static String USUARIO_ID         = "id";
    public static String USUARIO_NOMBRE     = "nombre";
    public static String USUARIO_PATERNO    = "paterno";
    public static String USUARIO_MATERNO    = "materno";
    public static String USUARIO_EMAIL      = "email";
    public static String USUARIO_PASSWORD   = "password";
    public static String USUARIO_CODIGO   = "codigo";
    public static String USUARIO_TOKEN      = "token";
    public static String USUARIO_PATH_IMAGEN= "path_imagen";

    private SQLiteDatabase m_appDB;

    public UsuarioRepository(SQLiteDatabase sqLiteDatabase){
        m_appDB = sqLiteDatabase;
    }


    public void insertListUsuario(List<Usuario> usuarios)
    {
        for(Usuario usuario: usuarios)
        {
            insertUsuario(usuario);
        }
    }

    public long updateUsuario(Usuario usuario){
        return updateUsuario(usuario.getIdUser(), usuario.getNombre(),usuario.getPaterno(),
                usuario.getMaterno(), usuario.getEmail(), usuario.getPassword(), usuario.getToken(),
                usuario.getPathImagen());
    }

    public long updateUsuario(int id, String nombre, String paterno, String materno,
                              String email, String password,String token, String pathFoto){

        ContentValues val = new ContentValues();

        //val.put(COLUMNA_ID,id);
        if(nombre != null)
            val.put(USUARIO_NOMBRE, nombre);
        if(paterno != null)
            val.put(USUARIO_PATERNO, paterno);
        if(materno != null)
            val.put(USUARIO_MATERNO, materno);
        if(email!= null)
            val.put(USUARIO_EMAIL, email);
        if(password != null)
            val.put(USUARIO_PASSWORD, password);
        if(token != null)
            val.put(USUARIO_TOKEN, token);
        if(pathFoto != null)
            val.put(USUARIO_PATH_IMAGEN, pathFoto);

        long resp = m_appDB.update(TABLE_NAME, val, "id=" + id,null);
        if(resp == -1){
            Log.e(TAG_LOG, "error al insertar Aplicacion");
        }

        return resp;

    }


    public void insertUsuario(Usuario usuario){

        insertUsuario(usuario.getIdUser(),usuario.getNombre(),usuario.getPaterno(),
                      usuario.getMaterno(),usuario.getCodigo(),usuario.getEmail(),usuario.getPassword(),
                usuario.getToken(), usuario.getPathImagen());

    }


    public void insertUsuario(int id, String nombre, String paterno, String materno, String codigo, String email,
                              String password, String token, String pathImagen){
//        Log.i(TAG_LOG, "insert catalogo idCatalogo[" + id+ "] " +
//                "idElemento[" + idElemento + "] descripcion[" + descripcion + "]");
        ContentValues val= new ContentValues();
        val.put(USUARIO_ID, id);
        val.put(USUARIO_NOMBRE, nombre);
        val.put(USUARIO_PATERNO,paterno);
        val.put(USUARIO_MATERNO, materno);
        val.put(USUARIO_CODIGO, codigo);
        val.put(USUARIO_EMAIL,email);
        val.put(USUARIO_PASSWORD,password);
        val.put(USUARIO_TOKEN, token);
        val.put(USUARIO_PATH_IMAGEN, pathImagen);

        long resp = m_appDB.insert(TABLE_NAME, null, val);

        if(resp == -1){
            Log.e(TAG_LOG,"error al insertar usuario");
        }
    }

    public List<Usuario> findAll(){
        List<Usuario> listUsuario=  new ArrayList<>();
        try {
            Cursor cursor = m_appDB.rawQuery("SELECT * FROM " + TABLE_NAME , null);
            if (cursor.moveToFirst()) {
                do {
                    listUsuario.add(new Usuario(
                            cursor.getInt(cursor.getColumnIndex(USUARIO_ID)),
                            cursor.getString(cursor.getColumnIndex(USUARIO_NOMBRE)),
                            cursor.getString(cursor.getColumnIndex(USUARIO_PATERNO)),
                            cursor.getString(cursor.getColumnIndex(USUARIO_MATERNO)),
                            cursor.getString(cursor.getColumnIndex(USUARIO_CODIGO)),
                            cursor.getString(cursor.getColumnIndex(USUARIO_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(USUARIO_PASSWORD)),
                            cursor.getString(cursor.getColumnIndex(USUARIO_TOKEN)),
                            cursor.getString(cursor.getColumnIndex(USUARIO_PATH_IMAGEN))
                            )
                    );

                } while (cursor.moveToNext());
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return listUsuario;
    }


    public Usuario findByEmailAndPassword(String email, String password){
        Usuario usuario=  null;
        try {
            Cursor cursor = m_appDB.rawQuery("SELECT * FROM " + TABLE_NAME + " " +
                    "WHERE " + USUARIO_EMAIL + "=? AND " + USUARIO_PASSWORD + "=?" , new String[]{ email, password });
           if (cursor.moveToFirst()) {

                   usuario =  new Usuario(
                           cursor.getInt(cursor.getColumnIndex(USUARIO_ID)),
                           cursor.getString(cursor.getColumnIndex(USUARIO_NOMBRE)),
                           cursor.getString(cursor.getColumnIndex(USUARIO_PATERNO)),
                           cursor.getString(cursor.getColumnIndex(USUARIO_MATERNO)),
                           cursor.getString(cursor.getColumnIndex(USUARIO_CODIGO)),
                           cursor.getString(cursor.getColumnIndex(USUARIO_EMAIL)),
                           cursor.getString(cursor.getColumnIndex(USUARIO_PASSWORD)),
                           cursor.getString(cursor.getColumnIndex(USUARIO_TOKEN)),
                           cursor.getString(cursor.getColumnIndex(USUARIO_PATH_IMAGEN))
                            );
           }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return usuario;
    }

    public Usuario findByCodigo(String codigo){
        Usuario usuario=  null;
        try {
            Cursor cursor = m_appDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + USUARIO_CODIGO + " = '"+codigo+"'" ,null);
            if (cursor.moveToFirst()) {

                usuario =  new Usuario(
                        cursor.getInt(cursor.getColumnIndex(USUARIO_ID)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_PATERNO)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_MATERNO)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_CODIGO)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_PASSWORD)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_TOKEN)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_PATH_IMAGEN))
                );
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return usuario;
    }

    public Usuario findOne(){
        Usuario usuario=  null;
        try {
            Cursor cursor = m_appDB.rawQuery("SELECT * FROM " + TABLE_NAME ,null);
            if (cursor.moveToFirst()) {

                usuario =  new Usuario(
                        cursor.getInt(cursor.getColumnIndex(USUARIO_ID)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_PATERNO)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_MATERNO)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_CODIGO)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_PASSWORD)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_TOKEN)),
                        cursor.getString(cursor.getColumnIndex(USUARIO_PATH_IMAGEN))
                );
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return usuario;
    }

    public int countUsuario(){
        Cursor cursor = m_appDB.rawQuery("SELECT * FROM " + TABLE_NAME, new String[]{});
        int count = cursor.getCount();
        return count;
    }

    public int countByCodigo(String codigo){
        Cursor cursor = m_appDB.rawQuery("SELECT * FROM " + TABLE_NAME + " where " +
                USUARIO_CODIGO + " = " + codigo , null);
        int count = cursor.getCount();
        return count;
    }

    public void actualizaTokenByIdUsuario(String [] idUsuario, String token){
        ContentValues cv = new ContentValues();
        cv.put("token",token);

        int resp = m_appDB.update(TABLE_NAME, cv, "id=?", idUsuario);
        if(resp == -1){
            Log.d(TAG_LOG, "pimc -----------> Error al actualizar el token de usuario: " + TABLE_NAME);
        }
    }

    public void actualizaTokenByEmail(String email, String token){
        ContentValues cv = new ContentValues();
        cv.put("token",token);

        int resp = m_appDB.update(TABLE_NAME, cv, "email=?", new String[]{ email });
        if(resp == -1){
            Log.d(TAG_LOG, "pimc -----------> Error al actualizar el token de usuario: " + TABLE_NAME);
        }
    }

    /**
     * Metodo para eliminar los datos de la tabla catalogos de la BDL
     *
     */
    public void deleteUsuario() {
        m_appDB.delete(TABLE_NAME, null, null);
    }

    /**
     * Método para borrar el listado de empresas de los catálogos
     */
    public void deleteUsuarionById(String [] idEsc){
        m_appDB.delete(TABLE_NAME,"id=?",idEsc);
    }

}
