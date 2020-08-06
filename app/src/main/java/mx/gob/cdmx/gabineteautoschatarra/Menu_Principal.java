package mx.gob.cdmx.gabineteautoschatarra;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import mx.gob.cdmx.gabineteautoschatarra.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class Menu_Principal extends Activity {
	
	private static final String LOG_TAG = "Grabadora";          
	private MediaRecorder mediaRecorder;
	private MediaPlayer mediaPlayer;
	
	final Context context = this;
	public MediaRecorder recorder = new MediaRecorder();

	private Handler handler;
	
	public StringBuilder builder0;

    public EditText editUsuario;
//    public Button Salir;
    public String tablet;
    public Button Salir,archivo,fotos,audio,inicio;
    public String maximo="";
    int elMaximo;

    static Nombre nom = new Nombre();
    static String nombreE = nom.nombreEncuesta();

    static String ID = getHostName(null);
    static String prefix = ID;

    private static final String TAG = "menu";

    String upLoadServerUri = null;
    String upLoadServerUriIncidente = null;
    String upLoadServerUriFoto = null;
    String upLoadServerUriFotoIncidente = null;
    String upLoadServerUriVideo = null;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    final String path = "/mnt/sdcard/Mis_archivos/";
    final String pathZip = "/mnt/sdcard/";

    double latitude;
    double longitude;
    UsuariosSQLiteHelper usdbh;
    private SQLiteDatabase db;
    static InputStream is2 = null;
    String result3;
    String algo;
    public String seccion,count;
    public String dipLocal,dipFederal;



    Calendar c = Calendar.getInstance();

    SimpleDateFormat df1 = new SimpleDateFormat("yyyMMdd");
	String formattedDateFecha = df1.format(c.getTime());

	SimpleDateFormat df5 = new SimpleDateFormat("HH:mm:ss");
	String formattedDateHora = df5.format(c.getTime());

    public static String getHostName(String defValue) {
        try {
            Method getString = Build.class.getDeclaredMethod("getString", String.class);
            getString.setAccessible(true);
            return getString.invoke(null, "net.hostname").toString();
        } catch (Exception ex) {
            return defValue;
        }
    }

    public String sacaChip(){
  		String deviceId = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);
  		tablet=deviceId;	
  		return tablet;
  	}

      public String cachaNombre(){
          Bundle datos=this.getIntent().getExtras();
          String Nombre=datos.getString("Nombre");
          return Nombre;
      }
      


      public String cachaNumeracion(){
          Bundle datos=this.getIntent().getExtras();
          String numeracion=datos.getString("numeracion");
          return numeracion;
      }
      public String cachaSeccionGPS(){
          Bundle datos=this.getIntent().getExtras();
          String seccion_gps=datos.getString("seccion_gps");
          return seccion_gps;
      }
      

//      public String cachaDireccion(){
//          Bundle datos=this.getIntent().getExtras();
//          String direccion=datos.getString("Direccion");
//          return direccion;
//      }

      public String cachaNombreE(){
          Bundle datos=this.getIntent().getExtras();
          String nombre_encuesta=datos.getString("nombre_encuesta");
          return nombre_encuesta;
      }

      public String cachaTablet(){
          Bundle datos=this.getIntent().getExtras();
          String tablet=datos.getString("tablet");
          return tablet;
      }
      
////////////////////////  A MANO PARA TEST  ///////////////
      
      
//      
//      public String cachaMax(){
//
//          String max="1";
//          return max;
//      }
//      public String cachaUsuario(){
//          String usuario="1";
//          return usuario;
//      }
//
//      public String cachaId(){
//         
//          String id="49";
//          return id;
//      }
//      public String cachaDelegacion(){
//    	  String delegacion="iZTAPA";
//          return delegacion;
//      }
//      public String cachaLatitud() {
//  		String latitud = "0.0";
//  		return latitud;
//  	}
//  	public String cachaLongitud() {
//  		String longitud = "1.1";
//  		return longitud;
//  	}
      
      
 //////////////////////////////////////     
      
      
      public String cachaMax(){
          Bundle datos=this.getIntent().getExtras();
          String max=datos.getString("max");
          return max;
      }
      public String cachaUsuario(){
          Bundle datos=this.getIntent().getExtras();
          String usuario=datos.getString("usuario");
          return usuario;
      }

      public String cachaId(){
          Bundle datos=this.getIntent().getExtras();
          String id=datos.getString("id");
          return id;
      }
	    public String cachaIdNuevo(){
	        Bundle datos=this.getIntent().getExtras();
	        String id_nuevo=datos.getString("id_nuevo");
	        return id_nuevo;
	    }
      public String cachaDelegacion(){
          Bundle datos=this.getIntent().getExtras();
    	  String delegacion=datos.getString("delegacion");
          return delegacion;
      }
      public String cachaLatitud() {
  		Bundle datos = this.getIntent().getExtras();
  		String latitud = datos.getString("latitud");
  		return latitud;
  	}
  	public String cachaLongitud() {
  		Bundle datos = this.getIntent().getExtras();
  		String longitud = datos.getString("longitud");
  		return longitud;
  	}
      

       public String nombreArchivo(){
        String date=formattedDateFecha.toString();
        String var2=".txt";
        String var3=date+var2;
        final String nombre = date+"_"+sacaChip();// la extensión la da el archivo php del servidor
        return nombre;
    }
       
      

    @Override
    protected void onPause() {
        super.onPause();
//        try {
//        	 Intent intent = new Intent(this, Bienvenida.class);
//             startActivity(intent);
//             this.finish();
//             
//        } catch (Exception e) {
//        	 String stackTrace = Log.getStackTraceString(e);
//             Log.i(null,"Error OnPause"+ stackTrace);
//        }
    }






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        Thread.setDefaultUncaughtExceptionHandler(new Crash(this,this));
        
//        Toast.makeText(getApplicationContext(), "cachaMax: "+cachaMax(),Toast.LENGTH_LONG).show();


        archivo = (Button) findViewById(R.id.btnArchivo);
        fotos = (Button) findViewById(R.id.btnAudios);
        inicio = (Button) findViewById(R.id.btnInicio);
        

       }


    public void subeCenso(View view){

    Intent intent = new Intent(getApplicationContext(), CalendarViewArchivo.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("usuario", cachaUsuario());
    startActivity(intent);
    finish();


    }

//    public void subeFotos(View view){
//
//    	Intent intent = new Intent(getApplicationContext(), CalendarViewFotos.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("usuario", cachaUsuario());
//        startActivity(intent);
//        finish();
//    }

    public void subeFotos(View view){

        Intent intent = new Intent(getApplicationContext(), CalendarViewFotos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("usuario", cachaUsuario());
        startActivity(intent);
        finish();
    }

    public void inicio(View view){


		this.finish();

    }
   

    

}
