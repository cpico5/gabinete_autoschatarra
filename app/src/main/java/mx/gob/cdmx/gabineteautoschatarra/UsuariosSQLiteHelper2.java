package mx.gob.cdmx.gabineteautoschatarra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class UsuariosSQLiteHelper2 extends SQLiteOpenHelper {

    private static final String ENCODING = "ISO-8859-1";


    public static String getHostName(String defValue) {
        try {
            Method getString = Build.class.getDeclaredMethod("getString", String.class);
            getString.setAccessible(true);
            return getString.invoke(null, "net.hostname").toString();
        } catch (Exception ex) {
            return defValue;
        }
    }

    public String tablet;
    InputStream datos, usuarios, colonias, autos, prd, pri, pan, morena, independiente = null;

    static Nombre nom = new Nombre();
    static String nombreD = nom.nombreDatos();

    static String ID = getHostName(null);
    static String prefix = "listado";

    private static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/Mis_archivos/" + nombreD + "_" + prefix + "";
    private static final int DATABASE_VERSION = 16;

    public UsuariosSQLiteHelper2(Context context, String name, CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
// TODO Auto-generated constructor stub
        try {
            datos = context.getAssets().open("datos.sql");
            usuarios = context.getAssets().open("usuarios.sql");
            colonias = context.getAssets().open("colonias.sql");
            autos = context.getAssets().open("autos.sql");

        } catch (Exception ex) {
            Log.i(null, "HORROR-1: " + ex.fillInStackTrace());
        }

    }

//////////////////////TABLA colonias  //////////////////////////////////////////////////////////////


    public static class TablaColonias {
        public static String TABLA_Colonias = "colonias";
        public static String COLUMNA_numero = "numero";
        public static String COLUMNA_alcaldia = "alcaldia";
        public static String COLUMNA_coordinacion_territorial = "coordinacion_territorial";
        public static String COLUMNA_colonia = "colonia";
        public static String COLUMNA_cp = "cp";


    }

    private static final String DATABASE_Colonias = "create table "
            + TablaColonias.TABLA_Colonias + "("
            + TablaColonias.COLUMNA_numero + " INTEGER not null, "
            + TablaColonias.COLUMNA_alcaldia + " text not null, "
            + TablaColonias.COLUMNA_coordinacion_territorial + " text not null, "
            + TablaColonias.COLUMNA_colonia + " integer not null, "
            + TablaColonias.COLUMNA_cp + " text not null); ";


//////////////////////TABLA DATOS  //////////////////////////////////////////////////////////////


    public static class TablaDatos {
        public static String TABLA_DATOS = "datos";
        public static String COLUMNA_alcaldia = "alcaldia";
        public static String COLUMNA_EQUIPOS = "equipo";
        public static String COLUMNA_COORDINADOR = "coordinador";
        public static String COLUMNA_Usuario = "usuario";

    }

    private static final String DATABASE_DATOS = "create table "
    + TablaDatos.TABLA_DATOS + "("
    + TablaDatos.COLUMNA_alcaldia + " integer not null, "
    + TablaDatos.COLUMNA_EQUIPOS + " text not null, "
    + TablaDatos.COLUMNA_COORDINADOR + " text not null, "
    + TablaDatos.COLUMNA_Usuario + " text not null); ";


    //////////////////////TABLA AUTOS  //////////////////////////////////////////////////////////////


    public static class TablaAutos {
        public static String TABLA_Autos = "autos";
        public static String COLUMNA_marca = "marca";
        public static String COLUMNA_submarca = "submarca";

    }

    private static final String DATABASE_Autos = "create table "
            + TablaAutos.TABLA_Autos + "("
            + "id integer primary key autoincrement,"
            + TablaAutos.COLUMNA_marca + " text not null, "
            + TablaAutos.COLUMNA_submarca + " text not null); ";

///////////////////////////  TABLA USUARIOS	 /////////////////////////////////////////////////////////

    public static class TablaUsuarios {
        public static String TABLA_USUARIOS = "usuarios";
        public static String COLUMNA_USUARIO = "usuario";
        public static String COLUMNA_PASSWORD = "password";

    }

    private static final String DATABASE_USUARIOS = "create table "
    + TablaUsuarios.TABLA_USUARIOS + "("
    + TablaUsuarios.COLUMNA_USUARIO + " text not null, "
    + TablaUsuarios.COLUMNA_PASSWORD + " text not null); ";


    @Override
    public void onCreate(SQLiteDatabase db) {
// TODO Auto-generated method stub
        db.execSQL(DATABASE_DATOS);
        db.execSQL(DATABASE_USUARIOS);
        db.execSQL(DATABASE_Colonias);
        db.execSQL(DATABASE_Autos);

//        db.execSQL(DaoManager.generateCreateQueryString(Usuario.class));
//        db.execSQL(DaoManager.generateCreateQueryString(Status.class));
//        db.execSQL(DaoManager.generateCreateQueryString(Alcaldia.class));
//        db.execSQL(DaoManager.generateCreateQueryString(Colonia.class));
//        db.execSQL(DaoManager.generateCreateQueryString(Bitacora.class));
//        db.execSQL(DaoManager.generateCreateQueryString(TelefonoAsignado.class));

        cargaDatos(db);
        cargaUsuarios(db);
        cargaColonias(db);
        cargaAutos(db);

    }


    public void cargaDatos(SQLiteDatabase db) {
        InputStream tabla = datos;
        try {

            if (tabla != null) {
                db.beginTransaction();
                BufferedReader reader = new BufferedReader(new InputStreamReader(tabla, ENCODING));
                String line = reader.readLine();
                while (!TextUtils.isEmpty(line)) {
                    db.execSQL(line);
                    line = reader.readLine();
                }
                db.setTransactionSuccessful();
            }
        } catch (Exception ex) {
            Log.i(null, "HORROR-2: " + ex.getMessage());
        } finally {
            db.endTransaction();
            if (tabla != null) {
                try {
                    tabla.close();
                } catch (IOException e) {
                    Log.i(null, "HORROR-3; " + e.getMessage());
                }
            }
        }

    }

    public void cargaUsuarios(SQLiteDatabase db) {
        InputStream tabla = usuarios;
        try {

            if (tabla != null) {
                db.beginTransaction();
                BufferedReader reader = new BufferedReader(new InputStreamReader(tabla, ENCODING));
                String line = reader.readLine();
                while (!TextUtils.isEmpty(line)) {
                    db.execSQL(line);
                    line = reader.readLine();
                }
                db.setTransactionSuccessful();
            }
        } catch (Exception ex) {
            Log.i(null, "HORROR-2: " + ex.getMessage());
        } finally {
            db.endTransaction();
            if (tabla != null) {
                try {
                    tabla.close();
                } catch (IOException e) {
                    Log.i(null, "HORROR-3; " + e.getMessage());
                }
            }
        }

    }

    public void cargaColonias(SQLiteDatabase db) {
        InputStream tabla = colonias;
        try {

            if (tabla != null) {
                db.beginTransaction();
                BufferedReader reader = new BufferedReader(new InputStreamReader(tabla, ENCODING));
                String line = reader.readLine();
                while (!TextUtils.isEmpty(line)) {
                    db.execSQL(line);
                    line = reader.readLine();
                }
                db.setTransactionSuccessful();
            }
        } catch (Exception ex) {
            Log.i(null, "Error caraga colonias 1: " + ex.getMessage());
        } finally {
            db.endTransaction();
            if (tabla != null) {
                try {
                    tabla.close();
                } catch (IOException e) {
                    Log.i(null, "Error caraga colonias 2: " + e.getMessage());
                }
            }
        }

    }

    public void cargaAutos(SQLiteDatabase db) {
        InputStream tabla = autos;
        try {

            if (tabla != null) {
                db.beginTransaction();
                BufferedReader reader = new BufferedReader(new InputStreamReader(tabla, ENCODING));
                String line = reader.readLine();
                while (!TextUtils.isEmpty(line)) {
                    db.execSQL(line);
                    line = reader.readLine();
                }
                db.setTransactionSuccessful();
            }
        } catch (Exception ex) {
            Log.i(null, "Error caraga autos 1: " + ex.getMessage());
        } finally {
            db.endTransaction();
            if (tabla != null) {
                try {
                    tabla.close();
                } catch (IOException e) {
                    Log.i(null, "Error caraga autos 2: " + e.getMessage());
                }
            }
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub
        db.execSQL("DROP table if exists " + TablaDatos.TABLA_DATOS);
        db.execSQL("DROP table if exists " + TablaUsuarios.TABLA_USUARIOS);
        db.execSQL("DROP table if exists " + TablaColonias.TABLA_Colonias);
        db.execSQL("DROP table if exists " + TablaAutos.TABLA_Autos);

//        db.execSQL(DaoManager.generateDropIfExistsQueryString(Usuario.class));
//        db.execSQL(DaoManager.generateDropIfExistsQueryString(Status.class));
//        db.execSQL(DaoManager.generateDropIfExistsQueryString(Alcaldia.class));
//        db.execSQL(DaoManager.generateDropIfExistsQueryString(Colonia.class));
//        db.execSQL(DaoManager.generateDropIfExistsQueryString(Bitacora.class));
//        db.execSQL(DaoManager.generateDropIfExistsQueryString(TelefonoAsignado.class));

        onCreate(db);
    }
}
