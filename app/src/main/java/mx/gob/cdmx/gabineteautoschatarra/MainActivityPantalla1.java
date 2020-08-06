package mx.gob.cdmx.gabineteautoschatarra;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import mx.gob.cdmx.gabineteautoschatarra.model.DatoContent;
import mx.gob.cdmx.gabineteautoschatarra.model.Usuario;
import mx.gob.cdmx.gabineteautoschatarra.service.ImageTool;

import static mx.gob.cdmx.gabineteautoschatarra.Nombre.USUARIO;
import static mx.gob.cdmx.gabineteautoschatarra.Nombre.customURL;
import static mx.gob.cdmx.gabineteautoschatarra.Nombre.encuesta;

public class MainActivityPantalla1 extends Activity {

    private static final String LOG_TAG = "Grabadora";
    private static final String TAG = "Pantalla1";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    TelephonyManager t_manager;
    PhoneStateListener p_listener;
    boolean llamada = false;
    public String Galeria;

    private View mProgressView;
    private Usuario usuario;

    final Context context = this;

    private ArrayList<CheckBox> mChecks;
    private ArrayList<CheckBox> mSelectedChecks;

    private ArrayList<CheckBox> mChecks2;
    private ArrayList<CheckBox> mSelectedChecks2;

    public MediaRecorder recorder = new MediaRecorder();
    private String audio;
    private Handler handler;
    public String fotoCorta;
    public String fotoCorta2;

    public StringBuilder builder0;


    private Button btnGuardar;
    private Button btnCamera1;
    private Button btnCamera2;
    private static final int RESULT_LOAD_IMG1 = 2;
    private static final int RESULT_LOAD_IMG2 = 3;
    private Button btnGaleria1;
    private Button btnGaleria2;
    private Button btnRechazo;
    private Button btnAbrir;
    private Button btnSalir;
    public Button uploadButton, emailButton;
    private ImageView imagen;
    private ImageView imagen2;
    private int funcion = 0;

    double latitude;
    double longitude;

    Random random = new java.util.Random();
    public int rand;

    public RadioGroup rdPreguntaOcupacion, rdPreguntaFocos,   rdPreguntaCoche,rdPreguntaCuantosCoches,rdPreguntaCuartos, rdPreguntaCuartosDormir,
    rdPreguntaBanos,rdPreguntaTrabajaron,rdPreguntaInternet,rdPreguntaRegadera,
    rdPreguntaEstufa, rdPreguntaEdad, rdPreguntaGenero, rdPreguntaTipoVivienda, rdPreguntaTipoPiso;




    private static final int READ_BLOCK_SIZE = 100000;

    Nombre nom = new Nombre();
    String nombreEncuesta = nom.nombreEncuesta();

    String upLoadServerUriFotos = "https://opinion.cdmx.gob.mx/cgi-bin/php/recibeFotos" + nombreEncuesta + ".php?encuesta=" + nombreEncuesta + "";

    UsuariosSQLiteHelper usdbh;
    UsuariosSQLiteHelper Udb;
    List<String> list;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    private SQLiteDatabase db;

    UsuariosSQLiteHelper2 usdbh2;
    private SQLiteDatabase db2;

    private Spinner spinnerDelegaciones;
    private Spinner spinner63;
    private Spinner spinnerMeses;
    private Spinner spinnerSemana;
    private Spinner spinnerCalifica;
    private Spinner spinnermarca_vehiculo;
    private Spinner spinnersubmarca;

    private Spinner spinner0;

    private Spinner spinner9;
    Timer timer;

    public EditText txtColonia;

    public String opcolonia="sin datos";
    public String opcalle="sin datos";
    public String opnum_exterior="sin datos";
    public String optiempo_abandono="sin datos";
    public String opmarca_vehiculo="sin datos";
    public String opsubmarca="sin datos";
    public String opplacas="sin datos";
    public String opcolor="sin datos";
    public String oparacteristicas_generales="sin datos";
    public String opfoto1="sin datos";
    public String opfoto2="sin datos";
    public String opcapturo="sin datos";

    public String opAbandono="sin datos";

    public String nombreImagen1;
    public String nombreImagen2;


    public RadioGroup rdPreguntacolonia;	public EditText editPreguntacolonia;	public String capturacolonia;	LinearLayout laycolonia;
    public RadioGroup rdPreguntacalle;	public EditText editPreguntacalle;	public String capturacalle;	LinearLayout laycalle;
    public RadioGroup rdPreguntanum_exterior;	public EditText editPreguntanum_exterior;	public String capturanum_exterior;	LinearLayout laynum_exterior;
    public RadioGroup rdPreguntatiempo_abandono;	public EditText editPreguntatiempo_abandono;	public String capturatiempo_abandono;	LinearLayout laytiempo_abandono;
    public RadioGroup rdPreguntamarca_vehiculo;	public EditText editPreguntamarca_vehiculo;	public String capturamarca_vehiculo;	LinearLayout laymarca_vehiculo;
    public RadioGroup rdPreguntasubmarca;	public EditText editPreguntasubmarca;	public String capturasubmarca;	LinearLayout laysubmarca;
    public RadioGroup rdPreguntaplacas;	public EditText editPreguntaplacas;	public String capturaplacas;	LinearLayout layplacas;
    public RadioGroup rdPreguntacolor;	public EditText editPreguntacolor;	public String capturacolor;	LinearLayout laycolor;
    public RadioGroup rdPreguntacaracteristicas_generales;	public EditText editPreguntacaracteristicas_generales;	public String capturacaracteristicas_generales;	LinearLayout laycaracteristicas_generales;
    public RadioGroup rdPreguntafoto1;	public EditText editPreguntafoto1;	public String capturafoto1;	LinearLayout layfoto1;
    public RadioGroup rdPreguntafoto2;	public EditText editPreguntafoto2;	public String capturafoto2;	LinearLayout layfoto2;
    public RadioGroup rdPreguntacapturo;	public EditText editPreguntacapturo;	public String capturacapturo;	LinearLayout laycapturo;
    public RadioGroup rdPreguntaAbandono;

    public Resources res;

    UsuariosSQLiteHelper3 usdbh3;
    private SQLiteDatabase db3;


    LinearLayout layCuantosCoches;


    public RadioButton radio1_07;
    public RadioButton radio_abandono1;
    public RadioButton radio_abandono2;
    public RadioButton radio_abandono3;
    public RadioButton radio_abandono4;

    public String captura10a, captura11a, captura13a, captura14a, captura14b, captura14c;
    public String captura18a;
    public String capturaMedio;
    public String capturaSemana;
    public String capturaFinSemana, capturaHijos;

    String ochoa_1;
    String ochoa_2;
    String ochoa_3;
    String ochoa_4;
    String ochoa_5;
    String ochoa_6;
    String ochoa_7;
    String ochoa_8;
    String ochoa_9;
    String ochoa_10;
    String ochoa_11;

//    CheckBox checkochoa_1;
//    CheckBox checkochoa_2;
//    CheckBox checkochoa_3;
//    CheckBox checkochoa_4;
//    CheckBox checkochoa_5;
//    CheckBox checkochoa_6;
//    CheckBox checkochoa_7;
//    CheckBox checkochoa_8;
//    CheckBox checkochoa_9;
//    CheckBox checkochoa_10;
//    CheckBox checkochoa_11;

    public String capturaOcupacion, capturaCoche, capturaE3, capturaE4, capturaCuantosCoches, capturaTrabajo, capturaE7,
    capturaFocos, capturaCuartos, capturaCuartosDormir, capturaBanos, capturaInternet, capturaTrabajaron;
    public String capturaRegadera, capturaEstufa, capturaEdad, capturaGenero, capturaTipoVivienda, capturaTipoPiso,
    capturaE17, capturaE18, capturaE19, capturaE20;
    public String capturaJefe, capturaAporta;


    public String maximo = "";
    int elMaximo;
    String tipoEncuesta;

    public String pasoUsuario;

    public String Secc;

    public EditText editUsuario;

    public String str;
    public String variablePrueba;
    public String encuestador;
    public String tablet;
    public String hora;

    public String quien;

    String upLoadServerUri = null;
    ProgressDialog dialog = null;
    final String path = "/mnt/sdcard/Mis_archivos/";

    int serverResponseCode = 0;

    public String tiempo;

    Calendar c = Calendar.getInstance();

    SimpleDateFormat df1 = new SimpleDateFormat("yyy-MM-dd");
    String formattedDate1 = df1.format(c.getTime());

    SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
    String formattedDate2 = df2.format(c.getTime());

    SimpleDateFormat df3 = new SimpleDateFormat("yyyMMdd");
    String formattedDate3 = df3.format(c.getTime());

    SimpleDateFormat df6 = new SimpleDateFormat("MM");
    String formattedDate6 = df6.format(c.getTime());

    SimpleDateFormat df7 = new SimpleDateFormat("dd");
    String formattedDate7 = df7.format(c.getTime());

    SimpleDateFormat df4 = new SimpleDateFormat("HH:mm:ss a");
    String formattedDate4 = df4.format(c.getTime());

    SimpleDateFormat df5 = new SimpleDateFormat("HH:mm:ss");
    String formattedDate5 = df5.format(c.getTime());

    Calendar t1 = Calendar.getInstance();
    long milis1 = t1.getTimeInMillis();

    public static String getHostName(String defValue) {
        try {
            Method getString = Build.class.getDeclaredMethod("getString", String.class);
            getString.setAccessible(true);
            return getString.invoke(null, "net.hostname").toString();
        } catch (Exception ex) {
            return defValue;
        }
    }

    static String ID = getHostName(null);
    static String prefix = ID;

    public String cachaNombre() {
        Bundle datos = this.getIntent().getExtras();
        String Nombre = datos.getString("Nombre");
        return Nombre;
    }

    public String cachaTelefono() {
        Bundle datos = this.getIntent().getExtras();
        String telefono = datos.getString("telefono");
        return telefono;
    }

    public String cachaSeccion() {
        Bundle datos = this.getIntent().getExtras();
        String Seccion = datos.getString("Seccion");
        return Seccion;
    }

    public String cachaAlcaldia() {
        Bundle datos = this.getIntent().getExtras();
        String alcaldia = datos.getString("alcaldia");
        return alcaldia;
    }
    public String cachaColonia() {
        Bundle datos = this.getIntent().getExtras();
        String colonia = datos.getString("colonia");
        return colonia;
    }
    public String cachaDelegacion() {
        Bundle datos = this.getIntent().getExtras();
        String delegacion = datos.getString("delegacion");
        return delegacion;
    }

    public String cachaEquipo() {
        Bundle datos = this.getIntent().getExtras();
        String equipo = datos.getString("equipo");
        return equipo;
    }

//	public long t1() {
//		Bundle datos = this.getIntent().getExtras();
//		long t1 = datos.getLong("t1");
//		return t1;
//	}


    @SuppressLint("MissingPermission")
    public String sacaChip() {
        String szImei;
TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);//Telefono
szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
if (szImei == null) {
szImei = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);// Tableta
}
return szImei;
}

@SuppressLint("MissingPermission")
public String sacaImei() {
    String szImei;
TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);//Telefono
szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
if (szImei == null) {
szImei = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);// Tableta
}
return szImei;
}

public String hora() {

    if (formattedDate5.matches("")) {
        formattedDate5 = df5.format(c.getTime());
    }
    return formattedDate5;
}

public void dialogo() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Desea continuar?").setTitle("IMPORTANTE").setCancelable(false)
    .setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {

            detenerGrabacion();

            Intent i = new Intent(MainActivityPantalla1.this, Entrada.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
System.exit(0); // metodo que se debe implementar
}
}).setPositiveButton("CONTINUAR", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int id) {

            detenerGrabacion();

        Intent i = new Intent(MainActivityPantalla1.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("Nombre", cachaNombre());
        i.putExtra(USUARIO,usuario);
        startActivity(i);
System.exit(0); // metodo que se debe implementar
}
});
AlertDialog alert = builder.create();
alert.show();

}

public void dialogoParoAudio() {
    timer.cancel();
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    MainActivityPantalla1.this.runOnUiThread(new Runnable() {
        public void run() {
            builder.setMessage("¿Se detendrá la grabación y \n se reiniciará la encuesta..?")
            .setTitle("AVISO...!!!").setCancelable(false)
            .setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    detenerGrabacion();

                    Intent i = new Intent(MainActivityPantalla1.this, Entrada.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
System.exit(0); // metodo que se debe
// implementar
}
});
            AlertDialog alert = builder.create();
            alert.show();

        }
    });

}

public void dialogoCierraEncuesta() {
    timer.cancel();

    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    MainActivityPantalla1.this.runOnUiThread(new Runnable() {
        public void run() {
            builder.setMessage("Excediste el tiempo máximo para realizar la encuesta \n"
                + "¡¡¡ Se detendrá la grabación y se reiniciará la Aplicación..!!!").setTitle("AVISO...!!!")
            .setCancelable(false).setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    detenerGrabacion();

                    Intent i = new Intent(MainActivityPantalla1.this, Entrada.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
System.exit(0); // metodo que se debe
// implementar
}
});

            AlertDialog alert = builder.create();

            alert.show();
        }
    });

}

public void dialogoAbandono() {
    timer.cancel();

    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    MainActivityPantalla1.this.runOnUiThread(new Runnable() {
        public void run() {
            builder.setMessage("Deseas abandonar?").setTitle("AVISO...!!!").setCancelable(false)
            .setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    detenerGrabacion();
                }
            }).setPositiveButton("CONTINUAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            AlertDialog alert = builder.create();

            alert.show();
        }
    });

}

// EVENTO AL PULSAR EL BOTON ATRAS

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
// Esto es lo que hace mi botón al pulsar ir a atrás
        Toast.makeText(getApplicationContext(), "No puedo ir hacia atrás!!", Toast.LENGTH_SHORT)
        .show();

// dialogoAbandono();

        return true;
    }
    return super.onKeyDown(keyCode, event);
}

public String nombreArchivo() {
    String date = formattedDate3.toString();
    String var2 = ".txt";
    String var3 = date + var2;

    final String nombre = date + "-" + tablet + "-" + nombreEncuesta + var2;
    return nombre;
}

public String nombreAudio() {

    elMaximo = Integer.parseInt(sacaMaximo().toString()) + 1;
    String date = formattedDate3.toString();
    String var2 = ".mp3";

    int consecutivo = Integer.parseInt(sacaConsecutivo().toString()) + 1;
    String elConsecutivo = String.valueOf(consecutivo);
    int Cons = elConsecutivo.length();

    if (Cons == 1) {
        elConsecutivo = "00" + elConsecutivo;
    } else if (Cons == 2) {
        elConsecutivo = "0" + elConsecutivo;
    } else {
        elConsecutivo = elConsecutivo;
    }

    String usuario;

    int tamanoUsuario = cachaNombre().length();

    if (tamanoUsuario == 1) {
        usuario = "00" + cachaNombre();
    } else if (tamanoUsuario == 2) {
        usuario = "0" + cachaNombre();
    } else {
        usuario = cachaNombre();
    }

// nombreEncuesta_fecha_chip_usuario_consecutivo
//    final String nombreAudio = nombreEncuesta + "_" + date + "_" + sacaImei() + "_" + cachaNombre() + "_" + elConsecutivo + "_" + cachaTelefono() + ".mp3";
    final String nombreAudio = nombreEncuesta + "_" + date + "_" + sacaImei() + "_" + cachaNombre() + "_" + elConsecutivo + ".mp3";
// final String nombreAudio =
// nombreEncuesta+"_"+date+"_"+prefix+"_"+elConsecutivo+".mp3";
    return nombreAudio;
}

public String elTiempo() {
// Para la diferenci entre tiempos
    Calendar t2 = Calendar.getInstance();
    long milis2 = t2.getTimeInMillis();
//		long diff = milis2 - t1();
    long diff = milis2 - milis1;

    long diffSegundos = diff / 1000;

    long diffMinutos = diffSegundos / 60;

    long residuo = diffSegundos % 60;

    System.out.println(diffSegundos);
    System.out.println(diffMinutos);
    System.out.println(residuo);

    tiempo = diffMinutos + ":" + residuo;

    return tiempo;

}

private Integer[] mLinearLayoutIds = {
//            R.layout.activity_pantalla1,
//            R.layout.activity_pantalla2,
//            R.layout.activity_pantalla3,
//            R.layout.activity_pantalla4,
//            R.layout.activity_pantalla5,
//            R.layout.activity_pantalla6,
//            R.layout.activity_pantalla7,
//            R.layout.activity_pantalla8,
//            R.layout.activity_pantalla9,
//            R.layout.activity_pantalla10,
//// R.layout.activity_pantalla11,
//// R.layout.activity_pantalla12,
//// R.layout.activity_pantalla13,
//// R.layout.activity_pantalla14,
//// R.layout.activity_pantalla15,
//// R.layout.activity_pantalla16,
//// R.layout.activity_pantalla17,
//// R.layout.activity_pantalla18,
//// R.layout.activity_pantalla19,
};

//    int[] layouts_c1 = new int[] {
//            R.layout.pregc1a,
//            R.layout.pregc1b,
//            R.layout.pregc1c,
//            R.layout.pregc1d,
//            R.layout.pregc1e,
//            R.layout.pregc1f,
//            R.layout.pregc1g,
//            R.layout.pregc1h,
//              };



@Override
protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
setContentView(R.layout.activity_pantalla1); // COMENTAR ESTA CUANDO ES ALEATORIO

Intent startingIntent = getIntent();
if (startingIntent == null) {
    Log.e(TAG, "No Intent?  We're not supposed to be here...");
    finish();
    return;
}

if (savedInstanceState != null) {
    usuario = (Usuario) savedInstanceState.getSerializable(USUARIO);
} else {
    usuario = (Usuario) startingIntent.getSerializableExtra(USUARIO);
}

// Carga las pantallas aleatoriamente
random = new java.util.Random();
//
/*DESCOMENTAR ESTAS 3 LINEAS CUANDO YA ESTA EL NUMERO DE HOJAS ALEATORIO */
//        rand = random.nextInt(9);
//        setContentView(mLinearLayoutIds[rand]);
//        Log.i(null, "El aleatorio: " + rand); // si rand= 11 en el layoud corresponde a uno mas


/*activity_pantalla12*/

// Crea Log cuando falla la app
Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(MainActivityPantalla1.this,this));


cachaNombre(); // llamado al metodo para obtener el numero del
// encuestador

try {

    handler = new Handler();

    new Thread(new Runnable() {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "Iniciando Grabación");
//                    grabar();
                }

            });

        }
    }).start();

} catch (Exception e) {

}

///////////// EL TIMER PARA PARAR LA ENCUESTA /////////////////

timer = new Timer();
//		timer.schedule(new CierraEncuesta(), 1800000); // 8 Minutos 480000

////////////////////////
mProgressView = findViewById(R.id.login_progressMain);

    txtColonia = (EditText) findViewById(R.id.txtColonia);

    txtColonia.setText(cachaColonia());
    txtColonia.setEnabled(false);

    res = getResources();

//    rdPreguntacolonia = (RadioGroup)findViewById(R.id.rdPreguntacolonia);	capturacolonia =res.getString(R.string.PREGUNTAcolonia);	laycolonia = (LinearLayout) findViewById(R.id.laycolonia);
    rdPreguntacalle = (RadioGroup)findViewById(R.id.rdPreguntacalle);	capturacalle =res.getString(R.string.PREGUNTAcalle);	laycalle = (LinearLayout) findViewById(R.id.laycalle);
    rdPreguntanum_exterior = (RadioGroup)findViewById(R.id.rdPreguntanum_exterior);	capturanum_exterior =res.getString(R.string.PREGUNTAnum_exterior);	laynum_exterior = (LinearLayout) findViewById(R.id.laynum_exterior);
    rdPreguntatiempo_abandono = (RadioGroup)findViewById(R.id.rdPreguntatiempo_abandono);	capturatiempo_abandono =res.getString(R.string.PREGUNTAtiempo_abandono);	laytiempo_abandono = (LinearLayout) findViewById(R.id.laytiempo_abandono);
    rdPreguntamarca_vehiculo = (RadioGroup)findViewById(R.id.rdPreguntamarca_vehiculo);	capturamarca_vehiculo =res.getString(R.string.PREGUNTAmarca_vehiculo);	laymarca_vehiculo = (LinearLayout) findViewById(R.id.laymarca_vehiculo);
    rdPreguntasubmarca = (RadioGroup)findViewById(R.id.rdPreguntasubmarca);	capturasubmarca =res.getString(R.string.PREGUNTAsubmarca);	laysubmarca = (LinearLayout) findViewById(R.id.laysubmarca);
    rdPreguntaplacas = (RadioGroup)findViewById(R.id.rdPreguntaplacas);	capturaplacas =res.getString(R.string.PREGUNTAplacas);	layplacas = (LinearLayout) findViewById(R.id.layplacas);
    rdPreguntacolor = (RadioGroup)findViewById(R.id.rdPreguntacolor);	capturacolor =res.getString(R.string.PREGUNTAcolor);	laycolor = (LinearLayout) findViewById(R.id.laycolor);
    rdPreguntacaracteristicas_generales = (RadioGroup)findViewById(R.id.rdPreguntacaracteristicas_generales);	capturacaracteristicas_generales =res.getString(R.string.PREGUNTAcaracteristicas_generales);	laycaracteristicas_generales = (LinearLayout) findViewById(R.id.laycaracteristicas_generales);
    rdPreguntacapturo = (RadioGroup)findViewById(R.id.rdPreguntacapturo);	capturacapturo =res.getString(R.string.PREGUNTAcapturo);	laycapturo = (LinearLayout) findViewById(R.id.laycapturo);
    rdPreguntaAbandono = (RadioGroup)findViewById(R.id.rdPreguntaAbandono);


    spinnermarca_vehiculo= (Spinner) findViewById(R.id.spinnermarca_vehiculo);
    spinnersubmarca= (Spinner) findViewById(R.id.spinnersubmarca);

//    editPreguntacolonia= (EditText)findViewById(R.id.editPreguntacolonia);
    editPreguntacalle= (EditText)findViewById(R.id.editPreguntacalle);
    editPreguntanum_exterior= (EditText)findViewById(R.id.editPreguntanum_exterior);
    editPreguntatiempo_abandono= (EditText)findViewById(R.id.editPreguntatiempo_abandono);
//    editPreguntamarca_vehiculo= (EditText)findViewById(R.id.editPreguntamarca_vehiculo);
    editPreguntasubmarca= (EditText)findViewById(R.id.editPreguntasubmarca);
    editPreguntaplacas= (EditText)findViewById(R.id.editPreguntaplacas);
    editPreguntacolor= (EditText)findViewById(R.id.editPreguntacolor);
    editPreguntacaracteristicas_generales= (EditText)findViewById(R.id.editPreguntacaracteristicas_generales);
    editPreguntacapturo= (EditText)findViewById(R.id.editPreguntacapturo);



btnGuardar = (Button) findViewById(R.id.btnGuardar);
btnSalir = (Button) findViewById(R.id.btnSalir);
btnSalir.setEnabled(false);
btnSalir.setVisibility(View.GONE);

    CargaMarcaAutos();

    btnCamera1 = findViewById(R.id.btnCamera1);
    btnCamera2 = findViewById(R.id.btnCamera2);
    btnGaleria1 = findViewById(R.id.btnGaleria1);
    btnGaleria2 = findViewById(R.id.btnGaleria2);
    imagen=(ImageView)findViewById(R.id.imagen);
    imagen2=(ImageView)findViewById(R.id.imagen2);

    btnCamera1.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {

             if (layplacas.getVisibility() == View.VISIBLE && editPreguntaplacas.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturaplacas,Toast.LENGTH_LONG).show();}
            else if (laycapturo.getVisibility() == View.VISIBLE && editPreguntacapturo.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturacapturo,Toast.LENGTH_LONG).show();}


            else {

                elMaximo = Integer.parseInt(sacaMaximo().toString()) + 1;

                 nombreImagen1 = formattedDate3 + "_" + sacaImei() + "_" + cachaNombre() + "_" + editPreguntaplacas.getText().toString().toUpperCase() + "_1_" +elMaximo+ ".jpg";
                getCamara1(nombreImagen1);
            }

        }

    });

    btnCamera2.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {

             if (layplacas.getVisibility() == View.VISIBLE && editPreguntaplacas.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturaplacas,Toast.LENGTH_LONG).show();}
            else if (laycapturo.getVisibility() == View.VISIBLE && editPreguntacapturo.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturacapturo,Toast.LENGTH_LONG).show();}


            else {

                elMaximo = Integer.parseInt(sacaMaximo().toString()) + 1;
                 nombreImagen2 = formattedDate3 + "_" + sacaImei() + "_" + cachaNombre() + "_" + editPreguntaplacas.getText().toString().toUpperCase() + "_2_" +elMaximo+ ".jpg";
                getCamara2(nombreImagen2);
            }

        }

    });

    btnGaleria1.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            if (layplacas.getVisibility() == View.VISIBLE && editPreguntaplacas.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturaplacas,Toast.LENGTH_LONG).show();}
            else if (laycapturo.getVisibility() == View.VISIBLE && editPreguntacapturo.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturacapturo,Toast.LENGTH_LONG).show();}


            else {

                loadImagefromGallery1();

            }

        }

    });

    btnGaleria2.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {

             if (layplacas.getVisibility() == View.VISIBLE && editPreguntaplacas.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturaplacas,Toast.LENGTH_LONG).show();}
            else if (laycapturo.getVisibility() == View.VISIBLE && editPreguntacapturo.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturacapturo,Toast.LENGTH_LONG).show();}
                        else {
            loadImagefromGallery2();
        }

        }

    });

//    editPreguntasubmarca.setOnTouchListener(new View.OnTouchListener() {
//        @SuppressLint("ClickableViewAccessibility")
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            v.setFocusable(true)
//            v.setFocusableInTouchMode(true)
//            return false;
//        }
//    });
//


    editPreguntasubmarca.addTextChangedListener(new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {}
        @Override
        public void beforeTextChanged(CharSequence s, int start,int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start,int before, int count) {
            if(s.length() != 0){
               spinnersubmarca.setSelection(0);
            }
        }
    });


    rdPreguntaAbandono.setOnCheckedChangeListener(new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            if (checkedId == R.id.radio_abandono1) {
                opAbandono = "1";
                tipoEncuesta = "COMPLETA";
                btnGuardar.setText("Guardar Completa");
            } else if (checkedId == R.id.radio_abandono2) {
                opAbandono = "2";
                tipoEncuesta = "ABANDONO";
                btnGuardar.setText("Guardar Abandono");
            }


        }
    });



}

////// FIN ONCREATE/////////////////////////////

    public void loadImagefromGallery1() {
        funcion = 3;
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG1);
    }

    public void loadImagefromGallery2() {
        funcion = 4;
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG2);
    }

    public void getCamara1(String nombreImagen) {
        System.gc();
        funcion = 1;
        int numero;
        int n = 1000;
        String fechaInicial;

       fotoCorta = Environment.getExternalStorageDirectory() + "/Fotos/FotosGS_" +formattedDate3+"N"+"/"+ nombreImagen1;

        Uri output = Uri.fromFile(new File(fotoCorta));

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
          Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, 0);

    }

    public void getCamara2(String nombreImagen2) {
        System.gc();
        funcion = 2;
        int numero;
        int n = 1000;
        String fechaInicial;

        fotoCorta2 = Environment.getExternalStorageDirectory() + "/Fotos/FotosGS_" +formattedDate3+"N"+"/"+ nombreImagen2;

            Uri output = Uri.fromFile(new File(fotoCorta2));

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (funcion){

            case 1:
                if(requestCode == 0 && resultCode == Activity.RESULT_OK){
                    setPic();
                }
                break;
            case 2:
                if(requestCode == 1 && resultCode == Activity.RESULT_OK){
                    setPic2();
                }
                break;
            case 3:
                if(requestCode == 2 && resultCode == Activity.RESULT_OK && null != data){
                    elMaximo = Integer.parseInt(sacaMaximo().toString()) + 1;
                    nombreImagen1 = formattedDate3 + "_" + sacaImei() + "_" + cachaNombre() + "_" + editPreguntaplacas.getText().toString().toUpperCase() + "_1_" +elMaximo+ ".jpg";
                    setGal1(data,nombreImagen1);
                }
                break;
            case 4:
                if(requestCode == 3 && resultCode == Activity.RESULT_OK && null != data){
                    elMaximo = Integer.parseInt(sacaMaximo().toString()) + 1;
                    nombreImagen2 = formattedDate3 + "_" + sacaImei() + "_" + cachaNombre() + "_" + editPreguntaplacas.getText().toString().toUpperCase() + "_2_" +elMaximo+ ".jpg";
                    setGal2(data,nombreImagen2);
                }
                break;

        }


    }

    private void setGal1(Intent data,String nombreImagen1) {
        // Get the dimensions of the View

        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        // Get the cursor
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        Galeria = cursor.getString(columnIndex);
        cursor.close();

        //imagen.setImageBitmap(BitmapFactory.decodeFile(foto));

        ImageTool.setPic(imagen, Galeria);

        try {


            File sd = Environment.getExternalStorageDirectory();
            String sourceImagePath = Galeria;

            File file = new File(sourceImagePath);
            Log.i("Galeria", "Foto exists : " + file.exists());

            if (sd.canWrite()) {
                //String destinationImagePath= directorio + file.getName();

                                  fotoCorta = nombreImagen1;
                String destinationImagePath = Environment.getExternalStorageDirectory() + "/Fotos/FotosGS_" + formattedDate3 + "N" + "/" + fotoCorta;


                File source = new File(sourceImagePath);


                File destination = new File(destinationImagePath);
                Log.i("Galeria", ">>>>>>>> destino1: " + destination);
                if (source.exists()) {
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();

                }
            }
        } catch (Exception e) {

            String stackTrace = Log.getStackTraceString(e);
            Log.i("FotoGaleria1", "el error: " + stackTrace);
        }



    }

    private void setGal2(Intent data,String nombreImagen2) {
        // Get the dimensions of the View

        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        // Get the cursor
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        Galeria = cursor.getString(columnIndex);
        cursor.close();

        //imagen.setImageBitmap(BitmapFactory.decodeFile(foto));

        ImageTool.setPic(imagen2, Galeria);

        try {


            File sd = Environment.getExternalStorageDirectory();
            String sourceImagePath = Galeria;

            File file = new File(sourceImagePath);
            Log.i("Galeria", "Foto exists : " + file.exists());

            if (sd.canWrite()) {
                //String destinationImagePath= directorio + file.getName();

                                 fotoCorta2 = nombreImagen2;
                String destinationImagePath = Environment.getExternalStorageDirectory() + "/Fotos/FotosGS_" + formattedDate3 + "N" + "/" + fotoCorta2;


                File source = new File(sourceImagePath);
                File destination = new File(destinationImagePath);
                Log.i("Galeria", ">>>>>>>> destino2: " + destination);
                if (source.exists()) {
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();

                }
            }
        } catch (Exception e) {

            String stackTrace = Log.getStackTraceString(e);
            Log.i("Foto", "el error: " + stackTrace);
        }



    }



    private void setPic() {
        // Get the dimensions of the View
        try {

            int targetW = imagen.getWidth()/2;
            int targetH = imagen.getHeight()/2;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fotoCorta, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(fotoCorta, bmOptions);
            imagen.setImageBitmap(bitmap);

        }catch (Exception e) {
            String stackTrace = Log.getStackTraceString(e);
            Log.i(TAG,"Error toma foto 1"+ stackTrace);
        }


    }

    private void setPic2() {
        // Get the dimensions of the View
        int targetW = imagen2.getWidth()/2;
        int targetH = imagen2.getHeight()/2;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fotoCorta2, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fotoCorta2, bmOptions);
        imagen2.setImageBitmap(bitmap);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    class CierraEncuesta extends TimerTask {

        @Override
        public void run() {

            dialogoCierraEncuesta();

        }

    }

@Override
public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}

public void valores() {

    String str = "";

    String seg = formattedDate5.substring(7);
    System.out.println("El segundo: " + seg);
    System.out.println("El IMEI" + sacaImei());

    String mes = formattedDate6.toString();
    System.out.println("El mes" + mes);

    String dia = formattedDate7.toString();
    System.out.println("El dia" + dia);

    sacaChip();

    cachaNombre();

    txtColonia.setText(cachaColonia());

    String strSecc = txtColonia.getText().toString();
    String strId = String.valueOf(rand + 1);

    elMaximo = Integer.parseInt(sacaMaximo().toString()) + 1;

    String strTextsubmarca;
    if(spinnersubmarca.getSelectedItem().toString().equals(" Selecciona ")){
        strTextsubmarca=editPreguntasubmarca.getText().toString().toUpperCase();
    }else{
        strTextsubmarca=spinnersubmarca.getSelectedItem().toString();
        editPreguntasubmarca.setText("");
    }


    String strcolonia = cachaColonia();
    String strcalle = editPreguntacalle.getText().toString().toUpperCase();
    String strnum_exterior = editPreguntanum_exterior.getText().toString().toUpperCase();
    String strtiempo_abandono = editPreguntatiempo_abandono.getText().toString().toUpperCase();
    String strmarca_vehiculo = spinnermarca_vehiculo.getSelectedItem().toString().toUpperCase();
    String strsubmarca = strTextsubmarca;
    String strplacas = editPreguntaplacas.getText().toString().toUpperCase();
    String strcolor = editPreguntacolor.getText().toString().toUpperCase();
    String strcaracteristicas_generales = editPreguntacaracteristicas_generales.getText().toString().toUpperCase();
    String strfoto1 = nombreImagen1;
    String strfoto2 = nombreImagen2;
    String strcapturo = editPreguntacapturo.getText().toString().toUpperCase();
    String strAbandono = opAbandono;

    if (strAbandono.matches("1")) {
        tipoEncuesta = "COMPLETA";
    }

    String strFinal = "\n";

// Clase que permite grabar texto en un archivo
    FileOutputStream fout = null;
    try {
// INSERTA EN LA BASE DE DATOS //

        final String F = "File dbfile";

        String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/Mis_archivos/" + nombreEncuesta + "_"
        + sacaImei() + "";

// Abrimos la base de datos 'DBUsuarios' en modo escritura
        usdbh = new UsuariosSQLiteHelper(this, "F", null, 1, DATABASE_NAME);

        db = usdbh.getWritableDatabase();

// NORMAL
        Nombre nom = new Nombre();
        String nombreE = nom.nombreEncuesta();

        GPSTracker gps = new GPSTracker(this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        if (latitude == 0.0) {
            if (sacaLatitud() != null) {
                latitude = Double.valueOf(sacaLatitud());
            } else {
                latitude = 0.0;
            }
        }

        if (longitude == 0.0) {
            if (sacaLongitud() != null) {
                longitude = Double.valueOf(sacaLongitud());
            } else {
                longitude = 0.0;
            }
        }

        String strLatitud = String.valueOf(latitude);
        String strLongitud = String.valueOf(longitude);
        long consecutivoObtenido = 0;
        ContentValues values = new ContentValues();
        if (db != null) {

            values.put("consecutivo_diario", elMaximo);
            values.put("usuario", cachaNombre().toUpperCase());
            values.put("nombre_encuesta", nombreE.toUpperCase());
            values.put("fecha", formattedDate1);
            values.put("hora", formattedDate5);
            values.put("imei", sacaImei());
            values.put("latitud", strLatitud);
            values.put("longitud", strLongitud);
            values.put("alcaldia", cachaAlcaldia());

            values.put("colonia",cachaColonia());
            values.put("calle",strcalle);
            values.put("num_exterior",strnum_exterior);
            values.put("tiempo_abandono",strtiempo_abandono);
            values.put("marca_vehiculo",strmarca_vehiculo);
            values.put("submarca",strsubmarca);
            values.put("placas",strplacas);
            values.put("color",strcolor);
            values.put("caracteristicas_generales",strcaracteristicas_generales);
            values.put("foto1",strfoto1);
            values.put("foto2",strfoto2);
            values.put("capturo",strcapturo);
            values.put("tipo_captura",tipoEncuesta);


            if (!verificaConexion(this)) {
                Toast.makeText(getBaseContext(),"Sin conexión",Toast.LENGTH_LONG).show();
                values.put("enviado", "0");
                db.insert("encuestas", null, values);
            }else{
                values.put("enviado", "1");
                consecutivoObtenido = db.insert("encuestas", null, values);
            }
        }
        db.close();

        values.put("consecutivo", consecutivoObtenido);

        guardaEncuestaWS(values);


        System.out.println("consecutivo_diario " + elMaximo);
        System.out.println("usuario " + cachaNombre().toUpperCase());
        System.out.println("nombre_encuesta " + nombreE.toUpperCase());
        System.out.println("fecha " + formattedDate1);
        System.out.println("hora " + formattedDate5);
        System.out.println("imei " + sacaImei());
        System.out.println("Latitud  " + strLatitud);
        System.out.println("Longitud  " + strLongitud);
        System.out.println("alcaldia  " + cachaAlcaldia());

        System.out.println("colonia  " +   strcolonia);
        System.out.println("calle  " +   strcalle);
        System.out.println("num_exterior  " +   strnum_exterior);
        System.out.println("tiempo_abandono  " +   strtiempo_abandono);
        System.out.println("marca_vehiculo  " +   strmarca_vehiculo);
        System.out.println("submarca  " +   strsubmarca);
        System.out.println("placas  " +   strplacas);
        System.out.println("color  " +   strcolor);
        System.out.println("caracteristicas_generales  " +   strcaracteristicas_generales);
        System.out.println("foto1  " +   strfoto1);
        System.out.println("foto2  " +   strfoto2);
        System.out.println("capturo  " +   strcapturo);



// FIN INSERTA BASE DE DATOS //

    } catch (Exception e) {
        String stackTrace = Log.getStackTraceString(e);
        Log.i(TAG,"Error Inserta Base"+ stackTrace);
    }

}

private void guardaEncuestaWS(ContentValues values){

    showProgress(true);

//RECORRE CONTENTVALUES
    DatoContent datoContent = new DatoContent();
    List<DatoContent> listaContenido = new ArrayList();
    Set<Map.Entry<String, Object>> s=values.valueSet();
    Iterator itr = s.iterator();
    while(itr.hasNext()) {
        Map.Entry me = (Map.Entry)itr.next();
        String key = me.getKey().toString();
        Object value =  me.getValue();

        datoContent = new DatoContent();
        datoContent.setKey(key);
        datoContent.setValue(String.valueOf(value));

        listaContenido.add(datoContent);
    }

    Gson gson  = new Gson();
    Type collectionType = new TypeToken<List<DatoContent>>() { }.getType();
    String json = gson.toJson(listaContenido,collectionType);

    RequestParams params = new RequestParams();
    params.put("api", "guarda_encuesta_gabinete");
    params.put("encuesta", encuesta);
    params.put("data", json);

    Log.d(TAG, "pimc -----------> " + json);

    AsyncHttpClient client = new AsyncHttpClient();
    client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
//client.addHeader("Authorization", "Bearer " + usuario.getToken());
    client.setTimeout(60000);

    RequestHandle requestHandle = client.post(customURL, params, new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            showProgress(false);
            Log.d(TAG, "pimc -----------> Respuesta OK ");
            Log.d(TAG, "pimc -----------> " + new String(responseBody));
            try {


                String json = new String(responseBody);

                if (json != null && !json.isEmpty()) {

                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(json);
                    Log.d(TAG, "pimc -----------> Data: " + jsonObject.get("data"));

                    String login = jsonObject.getJSONObject("response").get("code").toString();
                    if (Integer.valueOf(login) == 1) {

/*JSONObject jsonUser = jsonObject.getJSONObject("data").getJSONObject("respuesta");
Type collectionType = new TypeToken<Usuario>() {}.getType();
usuario = gson.fromJson(jsonUser.toString(), collectionType);*/
//
//if(!opAbandono.equals("5")){
//    dialogo();
//}
/*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
intent.putExtra("Nombre", encuestaQuien);
startActivity(intent);
finish();*/
                        new MainActivityPantalla1.UpdateFotos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        dialogo();


} else {
    Toast.makeText(MainActivityPantalla1.this, "Error al subir los datos, Inténtelo de nuevo", Toast.LENGTH_SHORT).show();
    btnGuardar.setEnabled(true);
}
}

} catch (Exception e) {
    showProgress(false);
    Log.e(TAG, e.getMessage());
                Toast.makeText(MainActivityPantalla1.this, "Error al subir los datos, Inténtelo de nuevo", Toast.LENGTH_SHORT).show();
                btnGuardar.setEnabled(true);
}
}

@Override
public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
    showProgress(false);
    try {
        Log.e(TAG, "PIMC-----------------> existe un error en la conexion -----> " + error.getMessage());
        if (responseBody != null)
            Log.d(TAG, "pimc -----------> " + new String(responseBody));

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    if (statusCode != 200) {
        Log.e(TAG, "Existe un error en la conexi?n -----> " + error.getMessage());
        if (responseBody != null)
            Log.d(TAG, "pimc -----------> " + new String(responseBody));

    }

    Toast.makeText(MainActivityPantalla1.this, "Error de conexion, Se guarda en la base interna", Toast.LENGTH_SHORT).show();
    btnGuardar.setEnabled(true);

    dialogo();

}
});


}

public void guardar(View v) {
    System.out.println(cachaDelegacion());

    timer.cancel();

    String str = "";

    if (opAbandono.matches("sin datos")) {
        opAbandono = "1";
    }

    int tipo = Integer.parseInt(opAbandono);

    switch (tipo) {
        case 1:


                 if (laycalle.getVisibility() == View.VISIBLE && editPreguntacalle.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturacalle,Toast.LENGTH_LONG).show();}
            else if (laynum_exterior.getVisibility() == View.VISIBLE && editPreguntanum_exterior.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturanum_exterior,Toast.LENGTH_LONG).show();}
            else if (laytiempo_abandono.getVisibility() == View.VISIBLE && editPreguntatiempo_abandono.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturatiempo_abandono,Toast.LENGTH_LONG).show();}
            else if (laymarca_vehiculo.getVisibility() == View.VISIBLE && spinnermarca_vehiculo.getSelectedItem().equals(" Selecciona ") ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturamarca_vehiculo,Toast.LENGTH_LONG).show();}
            else if (laysubmarca.getVisibility() == View.VISIBLE && spinnersubmarca.getSelectedItem().equals(" Selecciona ") && editPreguntasubmarca.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturasubmarca,Toast.LENGTH_LONG).show();}
            else if (layplacas.getVisibility() == View.VISIBLE && editPreguntaplacas.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturaplacas,Toast.LENGTH_LONG).show();}
            else if (laycolor.getVisibility() == View.VISIBLE  && editPreguntacolor.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturacolor,Toast.LENGTH_LONG).show();}
            else if (laycaracteristicas_generales.getVisibility() == View.VISIBLE && editPreguntacaracteristicas_generales.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturacaracteristicas_generales,Toast.LENGTH_LONG).show();}
//            else if (layfoto1.getVisibility() == View.VISIBLE && opfoto1.matches("sin datos") && editPreguntafoto1.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturafoto1,Toast.LENGTH_LONG).show();}
//            else if (layfoto2.getVisibility() == View.VISIBLE && opfoto2.matches("sin datos") && editPreguntafoto2.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturafoto2,Toast.LENGTH_LONG).show();}
            else if (laycapturo.getVisibility() == View.VISIBLE && editPreguntacapturo.getText().toString().trim().length() == 0 ){Toast.makeText(getBaseContext(),"CAPTURA:  " +  capturacapturo,Toast.LENGTH_LONG).show();}

            else if(imagen.getDrawable() == null){
                     Toast.makeText(MainActivityPantalla1.this, "Debe Tomar la Foto 1", Toast.LENGTH_LONG).show();
                     Log.i("datos f", "No hay Nada");


            }else if(imagen2.getDrawable() == null){
                     Toast.makeText(MainActivityPantalla1.this, "Debe Tomar la Foto 2", Toast.LENGTH_LONG).show();
                     Log.i("datos f", "No hay Nada");


                 }

 else {




                         valores();
                         btnGuardar.setEnabled(false);
//            dialogo();


            } // Finaliza else
            break;

        case 2:

            if (laycapturo.getVisibility() == View.VISIBLE && editPreguntacapturo.getText().toString().trim().length() == 0) {
                Toast.makeText(getBaseContext(), "CAPTURA:  " + capturacapturo, Toast.LENGTH_LONG).show();
            }else {
                valores();
                btnGuardar.setEnabled(false);
//                dialogo();
            }

            break;

            }

}

public void Salir(View view) {
    finish();
}

private String sacaMaximo() {

    Set<String> set = new HashSet<String>();

    final String F = "File dbfile";

// Abrimos la base de datos 'DBUsuarios' en modo escritura
    String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/Mis_archivos/" + nombreEncuesta + "_"
    + sacaImei() + "";
    usdbh = new UsuariosSQLiteHelper(this, "F", null, 1, DATABASE_NAME);

    db = usdbh.getReadableDatabase();

    String selectQuery = "SELECT count(*) FROM encuestas where fecha='" + formattedDate1 + "'";

    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor.moveToFirst()) {
        do {

            maximo = cursor.getString(0);

        } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();

    return maximo;
}

private String sacaConsecutivo() {

    String consecutivo = null;

    Set<String> set = new HashSet<String>();

    final String F = "File dbfile";

// Abrimos la base de datos 'DBUsuarios' en modo escritura

    String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/Mis_archivos/" + nombreEncuesta + "_"
    + sacaImei() + "";
    usdbh = new UsuariosSQLiteHelper(this, "F", null, 1, DATABASE_NAME);

    db = usdbh.getReadableDatabase();

    String selectQuery = "SELECT count(*) FROM encuestas order by id desc limit 1";

    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor.moveToFirst()) {
        do {

            consecutivo = cursor.getString(0);

        } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();

    return consecutivo;
}

//////////////// SPINNER //////////////

    private void CargaMarcaAutos() {

        usdbh2 = new UsuariosSQLiteHelper2(this, "F", null, 1);

        Set<String> set = new HashSet<String>();
        db2 = usdbh2.getWritableDatabase();

        String selectQuery1 = "SELECT * FROM autos";

        Cursor c = db2.rawQuery(selectQuery1, null);

        if (c.moveToFirst()) {
            do {

                set.add(c.getString(1));

                String secc = c.getString(1);

            } while (c.moveToNext());
        }
        c.close();

        Set<String> set1 = set;
        set1.add(" Selecciona ");
        List<String> list = new ArrayList<String>(set1);

        adapter = new ArrayAdapter<String>(MainActivityPantalla1.this, android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Collections.sort(list);
        spinnermarca_vehiculo.setAdapter(adapter);

        // ACCIÓN QUE SE REALIZA CUANDO ES SELECCIOANDO UN ELEMENTO DEL SPINNER
        spinnermarca_vehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

                String marca=spinnermarca_vehiculo.getSelectedItem().toString();
                CargSubmarca(marca);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // txtEquipo.setText("");
            }
        });
        spinnermarca_vehiculo.setWillNotDraw(false);

    }

    private void CargSubmarca(String subm) {

        usdbh2 = new UsuariosSQLiteHelper2(this, "F", null, 1);

        Set<String> set = new HashSet<String>();
        db2 = usdbh2.getWritableDatabase();

        String selectQuery1 = "SELECT submarca FROM autos where marca='"+subm+"'";
        Log.i(TAG,"Query SubMarca: "+selectQuery1);

        Cursor c = db2.rawQuery(selectQuery1, null);

        if (c.moveToFirst()) {
            do {

                set.add(c.getString(0));

                String subma = c.getString(0);

            } while (c.moveToNext());
        }
        c.close();

        Set<String> set1 = set;
        set1.add(" Selecciona ");
        List<String> list = new ArrayList<String>(set1);

        adapter = new ArrayAdapter<String>(MainActivityPantalla1.this, android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Collections.sort(list);
        spinnersubmarca.setAdapter(adapter);

        // ACCIÓN QUE SE REALIZA CUANDO ES SELECCIOANDO UN ELEMENTO DEL SPINNER
        spinnersubmarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

                editPreguntasubmarca.setText("");

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // txtEquipo.setText("");
            }
        });
        spinnersubmarca.setWillNotDraw(false);

    }

///////////// FIN SPINNER /////////////

private String sacaLatitud() {
    Set<String> set = new HashSet<String>();
    String acceso = null;
    final String F = "File dbfile";
// Abrimos la base de datos 'DBUsuarios' en modo escritura
    usdbh3 = new UsuariosSQLiteHelper3(this);
    db3 = usdbh3.getReadableDatabase();
    String selectQuery = "select latitud from ubicacion order by id desc limit 1";
    Cursor cursor = db3.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
        do {
            acceso = cursor.getString(0);
        } while (cursor.moveToNext());
    }
    cursor.close();
// db.close();

    return acceso;
}

private String sacaLongitud() {
    Set<String> set = new HashSet<String>();
    String acceso = null;
    final String F = "File dbfile";
// Abrimos la base de datos 'DBUsuarios' en modo escritura
    usdbh3 = new UsuariosSQLiteHelper3(this);
    db3 = usdbh3.getReadableDatabase();
    String selectQuery = "select longitud from ubicacion order by id desc limit 1";
    Cursor cursor = db3.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
        do {
            acceso = cursor.getString(0);
        } while (cursor.moveToNext());
    }
    cursor.close();
// db.close();

    return acceso;
}


    public int uploadFotos(String sourceFileUri, String fech) {

        File sdCard;
        sdCard = Environment.getExternalStorageDirectory();
        //final String pathFotos = sdCard.getAbsolutePath() + "/"+ nombreEncuesta+"-Audio"+fech;
        final String pathFotos = sdCard.getAbsolutePath() + "/Fortos/FotosGS_" + formattedDate3 + "N";

        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

//		     dialog.dismiss();
            Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + pathFotos + "" + "/" + sourceFileUri);
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            return 0;

        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUriFotos);
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\""
                        + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("Foto", "HTTP Response fotos is : " + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    + " http://www.androidexample.com/media/uploads/"
                                    + "20161124_002_359083065132816_1.jpg";

//		                      Toast.makeText(Entrada.this, "File Upload Complete."+msg,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

//		        dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
//		                messageText.setText("MalformedURLException Exception : check script url.");
//		                Toast.makeText(CalendarViewFotos.this, "MalformedURLException",
//		                                                    Toast.LENGTH_SHORT).show();
                    }
                });

                Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + "Upload file to server " + "error: " + ex.getMessage());

//		        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

//		        dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
//		                messageText.setText("Error de Internet");
//		                Toast.makeText(CalendarViewFotos.this, "Error de Internet",
//		                        Toast.LENGTH_SHORT).show();
                    }
                });
                Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + "Upload file to server Exception " + "Exception : " + e.getMessage());

//		        Log.e("Upload file to server Exception", "Exception : "
//		                                         + e.getMessage(), e);
            }
            return serverResponseCode;

        } // End else block
    }

    class UpdateFotos extends AsyncTask<String, Float, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Foto", "CarlosQ:  Entra ====>");
        }


        @Override
        protected String doInBackground(String... params) {


            final String date2 = formattedDate3;
            File sdCard;
            sdCard = Environment.getExternalStorageDirectory();
            final String pathFotos = sdCard.getAbsolutePath() + "/Fotos/FotosGS_" + date2 + "N";
            final String pathFotosN = sdCard.getAbsolutePath() + "/Fotos/FotosGS_" + date2 + "N/";
            final String pathFotosF = sdCard.getAbsolutePath() + "/Fotos/FotosGS_" + date2 + "/";
            ;

            String sDirectorio = pathFotos;
            final File f = new File(sDirectorio);
            Log.i(null, "lista" + pathFotos);
            final String customURL = "https://opinion.cdmx.gob.mx/fotografias/gabinete_autoschatarra/";

            Log.i("Foto", "CarlosQ =======> lista de fotos: " + pathFotos);
            Log.i("Foto", "CarlosQ =======> pathFotosN: " + pathFotosN);
            Log.i("Foto", "CarlosQ =======> pathFotosF: " + pathFotosF);
            Log.i("Foto", "CarlosQ =======> URL: " + customURL);

            File F = new File(pathFotos);

            try {

                if (F.exists()) {

                    File[] ficheros = F.listFiles();

                    for (int i = 0; i < ficheros.length; i++) {
                        //Simulamos cierto retraso
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }

                        publishProgress(i / (float) (ficheros.length)); //Actualizamos los valores
                    }


                     String[] s = new String[ficheros.length];
                    String[] t = new String[ficheros.length];
                    for (int x = 0; x < ficheros.length; x++) {
                        Log.i("Foto", "CarlosQ ===========> lista: " + ficheros[x].getName());
                        s[x] = pathFotos + "/" + ficheros[x].getName();
//							t[x] = ficheros[x].getName();
                        // solo la foto
                        t[x] = ficheros[x].getName();

                        //	uploadFotos(s[x],date2);
                        URL u = new URL(customURL + t[x]);
                        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                        huc.setRequestMethod("GET");  //OR  huc.setRequestMethod ("HEAD");
                        huc.connect();
                        huc.getResponseCode();
                        Log.i("Foto", "CarlosQ: =====================> Respuesta del servidor " + huc.getResponseCode());
                        if (huc.getResponseCode() == 200) {
                            moveFile(pathFotosN, t[x], pathFotosF);
                            Log.i("Foto", "CarlosQ: ================>  Foto en el servidor y Movida a otra carpeta ====>" + t[x]);
                        } else if (huc.getResponseCode() == 404) {
                            uploadFotos(s[x], date2);
                            Log.i("Foto", "CarlosQ: =================> Foto Enviada y a_n sin Moverse ====>" + t[x]);
                        }
                    }
                    // first parameter is d files second parameter is zip file name

                } else {
                    Log.i(null, "lista 2: " + "No existe el directorio");
                }
                // first parameter is d files second parameter is zip file name

            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.i(null, "error zip: " + "_" + e.getMessage());
            }


            return date2;
        }


        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(100 * valores[0]);

        }


        protected void onPostExecute(String date2) {
            super.onPostExecute(date2);

        }

    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();


        } catch (FileNotFoundException fnfe1) {
            Log.i(null, "Archivos  tag" + fnfe1.getMessage());
        } catch (Exception e) {
            Log.i(null, "Archivos  tag" + e.getMessage());
        }

    }

public void grabar() {
    try {
// sacaMaximo();
        String pathAudio = "/mnt/sdcard/Audio1" + formattedDate3 + "";

        Nombre nom = new Nombre();
        String nombreEncuesta = nom.nombreEncuesta();

        File sdCard = null, directory, file = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
// Obtenemos el directorio de la memoria externa
            sdCard = Environment.getExternalStorageDirectory();
        }
        directory = new File(sdCard.getAbsolutePath() + "/" + nombreEncuesta + "-Audio" + formattedDate3 + "");
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(
            "/mnt/sdcard/" + nombreEncuesta + "-Audio" + formattedDate3 + "/" + nombreAudio() + "");

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            String stackTrace = Log.getStackTraceString(e);
            Log.i(TAG, String.valueOf("Fallo en grabacion: " + e.getMessage()));
        }
    } catch (Exception e) {
        String stackTrace = Log.getStackTraceString(e);
        Log.i(TAG, "Fallo en grabando" + stackTrace);
    }

}

public void detenerGrabacion() {
    Thread thread = new Thread() {
        public void run() {
            if (recorder != null) {

                try {
                    Log.i(TAG, String.valueOf("Grabadora detenida correctamente "));
                    recorder.stop();
recorder.reset(); // You can reuse the object by going back to
// setAudioSource() step
recorder.release();

} catch (Exception e) {
    String stackTrace = Log.getStackTraceString(e);
    Log.i("Manda Audios", "Al detener grabacion" + stackTrace);
}

}
}
};
thread.start();
}

    /////// METODO PARA VERIFICAR LA CONEXIÓN A INTERNET
    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
private void showProgress(final boolean show) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
            show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }



}
