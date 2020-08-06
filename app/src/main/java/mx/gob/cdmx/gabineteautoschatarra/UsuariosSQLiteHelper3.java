package mx.gob.cdmx.gabineteautoschatarra;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.InputStream;

public class UsuariosSQLiteHelper3 extends SQLiteOpenHelper {

	private static final String ENCODING = "ISO-8859-1";

	public String telefono;
	InputStream ubicacion= null;



	private static final String DATABASE_NAME = Environment.getExternalStorageDirectory() +"/loc/ubicacion_ZJvI7PooUhZogIarOp8v";
	private static final int DATABASE_VERSION = 4;
	public UsuariosSQLiteHelper3(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
// TODO Auto-generated constructor stub


	}

	public static class TablaUbicacion {
		public static String TABLA_UBICACION = "ubicacion";
		public static String COLUMNA_ID = "id";
		public static String COLUMNA_fecha = "fecha";
		public static String COLUMNA_hora = "hora";
		public static String COLUMNA_LATITUD = "latitud";
		public static String COLUMNA_LONGITUD = "longitud";
		public static String COLUMNA_DIRECCION = "direccion";
	}

	private static final String DATABASE_UBICACION = "create table "
	+ TablaUbicacion.TABLA_UBICACION + "("
	+ "id integer primary key autoincrement,"
	+ TablaUbicacion.COLUMNA_fecha + " text, "
	+ TablaUbicacion.COLUMNA_hora + " text, "
	+ TablaUbicacion.COLUMNA_LATITUD + " text, "
	+ TablaUbicacion.COLUMNA_LONGITUD + " text, "
	+ TablaUbicacion.COLUMNA_DIRECCION + " text); ";

	@Override
	public void onCreate(SQLiteDatabase db) {
// TODO Auto-generated method stub
		db.execSQL(DATABASE_UBICACION);
//	        cargaUbicacion(db);

	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub
		db.execSQL("drop table if exists " + TablaUbicacion.TABLA_UBICACION);
		onCreate(db);
	}
}
