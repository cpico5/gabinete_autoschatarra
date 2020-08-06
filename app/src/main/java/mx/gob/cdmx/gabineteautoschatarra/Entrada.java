package mx.gob.cdmx.gabineteautoschatarra;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Entrada extends Activity {


    private final String TAG = "Entrada";

    public EditText editUsuario;
    public EditText editPaterno;
    public EditText editLat;
    public EditText editLon;
    Geocoder geocoder;
    public EditText editDireccion;
    public Button Salir;
    public String tablet;
    double latitude;
    double longitude;


    Nombre nom = new Nombre();
    String nombreEncuesta = nom.nombreEncuesta();

    String upLoadServerUriBase = "https://opinion.cdmx.gob.mx/cgi-bin/php/recibeBases" + nombreEncuesta + ".php?encuesta=" + nombreEncuesta + "";
    String upLoadServerUriAudio = "https://opinion.cdmx.gob.mx/cgi-bin/php/recibeAudios" + nombreEncuesta + ".php?encuesta=" + nombreEncuesta + "";
    String upLoadServerUriFotos = "https://opinion.cdmx.gob.mx/cgi-bin/php/recibeFotos" + nombreEncuesta + ".php?encuesta=" + nombreEncuesta + "";
    int serverResponseCode = 0;

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


    ArrayAdapter<String> adapter;
    // public static Spinner spinnerTecnico;
    public static EditText editTecnico;

    ProgressDialog dialog = null;

    int elMaximo;
    public String maximo = "";
    UsuariosSQLiteHelper usdbh;
    private SQLiteDatabase db;
    UsuariosSQLiteHelper2 usdbh2;
    private SQLiteDatabase db2;
    UsuariosSQLiteHelper3 usdbh3;
    private SQLiteDatabase db3;
    public Button buscar;
    public Button ubicar;
    public Button continuar;

    List<Address> addresses;

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    Calendar c = Calendar.getInstance();

    SimpleDateFormat df1 = new SimpleDateFormat("yyy-MM-dd");
    String formattedDate1 = df1.format(c.getTime());
    SimpleDateFormat df3 = new SimpleDateFormat("yyyyMMdd");
    String formattedDate3 = df3.format(c.getTime());
    SimpleDateFormat df4 = new SimpleDateFormat("yyy-MM-dd");
    String formattedDateFecha = df4.format(c.getTime());
    SimpleDateFormat df5 = new SimpleDateFormat("yyyyMMdd");
    String formattedDateAyer = df5.format(yesterday());

    Date ayer = yesterday();


//	Envio bases = new Envio();

    public String sacaChip() {
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            String szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
            System.out.println("Mi Número: " + szImei);

            return szImei;
        }
        String szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
        System.out.println("Mi Número: " + szImei);

        return szImei;
    }

//	public String sacaChip(){
//		String deviceId = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);
//		tablet=deviceId;
//		return tablet;
//	}

    public String sacaImei() {
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            String szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
            System.out.println("Mi Número: " + szImei);

            return szImei;
        }
        String szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
        System.out.println("Mi Número: " + szImei);

        return szImei;
    }


    public void dialogoFecha() {
        // timer.cancel();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Entrada.this.runOnUiThread(new Runnable() {
            public void run() {
                builder.setMessage("La fecha del dispositivo es \n" + formattedDateFecha + "\n" + "Es correcto?")
                        .setTitle("IMPORTANTE...!!!").setCancelable(false)
                        .setNegativeButton("CONFIGURAR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                System.exit(0);

                            }
                        }).setPositiveButton("CORRECTO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // entrada();

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

    }

    private String sacaMaximo() {

        Set<String> set = new HashSet<String>();

        final String F = "File dbfile";



        String DATABASE_NAME = Environment.getExternalStorageDirectory() +"/Mis_archivos/" +nombreEncuesta+"_"+sacaImei()+"";

        // Abrimos la base de datos 'DBUsuarios' en modo escritura
        usdbh = new UsuariosSQLiteHelper(this, "F", null, 1,DATABASE_NAME);
        db = usdbh.getReadableDatabase();


        String selectQuery = "SELECT count(*) FROM encuestas where fecha='" + formattedDateFecha + "'";

        Log.i(TAG, "Query maximo"+selectQuery);

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

    void deleteDir(File file) {
        if (file.isDirectory())
            for (String child : file.list())
                deleteDir(new File(file, child));
        file.delete(); // delete child file or empty directory
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(Entrada.this,
                    new String[]{android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.READ_PHONE_STATE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.INTERNET,
                            android.Manifest.permission.ACCESS_NETWORK_STATE,
                            android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.LOCATION_HARDWARE,
                            android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
                            android.Manifest.permission.MODIFY_PHONE_STATE,
                            android.Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.PROCESS_OUTGOING_CALLS,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.ACCESS_WIFI_STATE},
                    1);
        }
        setContentView(R.layout.entrada);
        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(this,this));

        Log.i(TAG, " =====> la latitud: " + latitude);

        /* Abre la app de seguimiento*/
//        try {
//            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("mx.gob.cdmx.seguimiento");
//            startActivity(launchIntent);
//        } catch (Exception e) {
//            String stackTrace = Log.getStackTraceString(e);
//            Log.i(TAG, "Lanza Seguimiento" + stackTrace);
//
//            dialogoSeguimiento();
//
//        }



        elMaximo = Integer.parseInt(sacaMaximo().toString()) + 1;
        String elMaximoFecha = String.valueOf(elMaximo);

        GPSTracker gps = new GPSTracker(this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        if(latitude==0.0){
            latitude=Double.valueOf(sacaLatitud());
        }

        Log.i(TAG, " =====> la latitud: " + latitude);
        Log.i(TAG, " =====> la longitud: " + longitude);

        Log.i(TAG, " =====> la fecha: " + formattedDateFecha);
        Log.i(TAG, " =====> ayer: " + formattedDateAyer);

//			dialog = ProgressDialog.show(Entrada.this, "", "Actualizando...", true);

        if (!verificaConexion(this)) {
            Toast.makeText(getBaseContext(),"Sin conexión",
                    Toast.LENGTH_LONG).show();
            //this.finish();
        }
        else{


            new UpdateBases().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UpdateFotos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            new UpdateAudios().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//				new UpdateBases().execute();


            // retrazo de 3 segundos para ejecutar la subida de audios
//		            new Handler().postDelayed(new Runnable() {
//		                @Override
//		                public void run() {
//
//		                new UpdateAudios().execute();
//		                }
//		            }, 3000);


        }
        /////////////////////////////////////////////////////////////////////// 7

        if (elMaximoFecha.matches("1")) {

            Log.i(TAG, " =====> El numero inicial: " + elMaximoFecha);

            dialogoFecha();

        }



        continuar = (Button) findViewById(R.id.btnE1);

        String DATABASE_NAME = Environment.getExternalStorageDirectory() +"/Mis_archivos/" +nombreEncuesta+"_"+sacaImei()+"";

        // Abrimos la base de datos 'DBUsuarios' en modo escritura
        usdbh = new UsuariosSQLiteHelper(this, "F", null, 1,DATABASE_NAME);
        db = usdbh.getReadableDatabase();

        usdbh2 = new UsuariosSQLiteHelper2(this, "F", null, 1);
        db2 = usdbh2.getReadableDatabase();

        continuar.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("null")
            @Override
            public void onClick(View v) {
//					// TODO Auto-generated method stub Aqui para generar el archivo vacio// hasta la 112
                final EditText usuario = (EditText) findViewById(R.id.editUsuario);
                final EditText pass = (EditText) findViewById(R.id.editPass);
                File directory;
                File file;
                File sdCard;
                sdCard = Environment.getExternalStorageDirectory();
                FileOutputStream fout = null;
                try {
                    directory = new File(sdCard.getAbsolutePath() + "/Mis_archivos");
                    directory.mkdirs();
                    directory = new File(sdCard.getAbsolutePath() + "/Fotos/FotosGS_"+formattedDate3+"N");
                    directory.mkdirs();
                    directory = new File(sdCard.getAbsolutePath() + "/Fotos/FotosGS_"+formattedDate3);
                    directory.mkdirs();
                    Nombre nom = new Nombre();
                    String nombreE = nom.nombreEncuesta();
//                    directory = new File(sdCard.getAbsolutePath() + "/" + nombreE + "-Audio" + formattedDate3);
//                    directory.mkdirs();


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String user = usuario.getText().toString();
                String pssw = pass.getText().toString();
                sacaUsuario(user, pssw);
                String paso = sacaUsuario(user, pssw).toString();


                if (paso.matches("1")) {

                    Intent intent = new Intent(Entrada.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Nombre", usuario.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(v.getContext(), "Acceso OK", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(v.getContext(), "Usuario y/o Contaseña Incorrectos", Toast.LENGTH_SHORT).show();
                }

            }
        });



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

    public void salir() {
        Salir = (Button) findViewById(R.id.btnSalir);
    }

    public void salir(View view) {
        finish();
    }

    public void Sigue(View view) {
        // TODO Auto-generated method stub Aqui para generar el archivo vacio// hasta la 112

        final EditText usuario = (EditText) findViewById(R.id.editUsuario);
        final EditText pass = (EditText) findViewById(R.id.editPass);
        File directory;
        File file;
        File sdCard;
        sdCard = Environment.getExternalStorageDirectory();
        FileOutputStream fout = null;
        try {
            directory = new File(sdCard.getAbsolutePath() + "/Mis_archivos");
            directory.mkdirs();
            Nombre nom = new Nombre();
            String nombreE = nom.nombreEncuesta();
            directory = new File(sdCard.getAbsolutePath() + "/" + nombreE + "-Audio" + formattedDate3);
            directory.mkdirs();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String user = usuario.getText().toString();
        String pssw = pass.getText().toString();
        sacaUsuario(user, pssw);
        String paso = sacaUsuario(user, pssw).toString();


        if (paso.matches("1")) {

            Intent intent = new Intent(Entrada.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Nombre", usuario.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
            Toast.makeText(this, "Acceso OK", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Usuario y/o Contaseña Incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    private String sacaUsuario(String user, String pass) {
        Set<String> set = new HashSet<String>();
        String acceso = null;
        final String F = "File dbfile";
        String DATABASE_NAME = Environment.getExternalStorageDirectory() +"/Mis_archivos/" +nombreEncuesta+"_"+sacaImei()+"";

        // Abrimos la base de datos 'DBUsuarios' en modo escritura
        usdbh2 = new UsuariosSQLiteHelper2(this, "F", null, 1);
        db2 = usdbh2.getReadableDatabase();
        String selectQuery = "select count(*) from usuarios where usuario='" + user + "' and password='" + pass + "'";
        Log.i(TAG,"csq ------------->> Queri saca Usuario  "+selectQuery);
        Cursor cursor = db2.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                acceso = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db2.close();

        return acceso;
    }

    private String sacaLatitud() {
        Set<String> set = new HashSet<String>();
        String acceso = null;
        final String F = "File dbfile";
        // Abrimos la base de datos 'DBUsuarios' en modo escritura
        usdbh3 = new UsuariosSQLiteHelper3(this);
        db3 = usdbh3.getReadableDatabase();
        String selectQuery = "select latitud from ubicacion limit 1";
        Cursor cursor = db3.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                acceso = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return acceso;
    }

    public void continua(View view) {

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Nombre", editUsuario.getText().toString());

        startActivity(i);

        editUsuario.setText("");

    }

    public void dialogoSeguimiento() {
        // timer.cancel();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Entrada.this.runOnUiThread(new Runnable() {
            public void run() {
                builder.setMessage("Instala la aplicacion de Seguimiento")
                        .setTitle("Aviso").setCancelable(false)
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                finish();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

    }

    //Enviar Base
    public int uploadBase(String sourceFileUri) {

        File sdCard;
        sdCard = Environment.getExternalStorageDirectory();
        final String pathBase = sdCard.getAbsolutePath() + "/Mis_archivos";

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

//				             dialog.dismiss();
            Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + pathBase + "" + "/" + "20161124_002_359083065132816_1.jpg");
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            return 0;

        }
        else
        {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUriBase);
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

                Log.i("TAG", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://www.androidexample.com/media/uploads/"
                                    +"20161124_002_359083065132816_1.jpg";

//				                              Toast.makeText(Entrada.this, "File Upload Complete."+msg,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

//				                dialog.dismiss();
                ex.printStackTrace();



                Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + "Upload file to server "+ "error: " + ex.getMessage());

//				                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

//				                dialog.dismiss();
                e.printStackTrace();

                Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + "Upload file to server Exception "+ "Exception : "+ e.getMessage());

//				                Log.e("Upload file to server Exception", "Exception : "
//				                                                 + e.getMessage(), e);
            }
            return serverResponseCode;

        } // End else block
    }

    class UpdateBases extends AsyncTask<String, Float, String> {

        protected void onPreExecute() {
            super.onPreExecute();

//					dialog = new ProgressDialog(Entrada.this);
//		            dialog.setMessage("Enviando Base de Datos...");
//		            dialog.setTitle("Progreso");
//		            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		            dialog.setCancelable(false);
//		            dialog.setProgress(0);
//		            dialog.setMax(100);
//		            dialog.show(); //Mostramos el diálogo antes de comenzar
        }


        @Override
        protected String doInBackground(String... params) {


            File sdCard;
            sdCard = Environment.getExternalStorageDirectory();
            final String pathBase = sdCard.getAbsolutePath() + "/Mis_archivos";

            String sDirectorio = pathBase;
            final File f = new File(sDirectorio);
            Log.i(TAG,"lista"+pathBase);

//						final String customURL = "https://encuestas.sies2018.org/coordinacion/audios/";
            final String customURL = "https://opinion.cdmx.gob.mx/cgi-bin/bases/";


            Log.i(TAG, " =====> lista 1: " + pathBase);

            File F = new File(pathBase);

            try {

                if (F.exists()) {

                    File[] ficheros = F.listFiles();

                    for (int i = 0; i <ficheros.length; i++) {
                        //Simulamos cierto retraso
                        try {Thread.sleep(500); }
                        catch (InterruptedException e) {}

                        publishProgress(i/(float)(ficheros.length)); //Actualizamos los valores
                    }



                    String[] s = new String[ficheros.length];
                    String[] t = new String[ficheros.length];
                    for (int x = 0; x < ficheros.length; x++) {
                        Log.i(TAG, " =====> lista: " + ficheros[x].getName());
                        s[x] = pathBase + "/" + nombreEncuesta+"_"+sacaImei();
//								t[x] = ficheros[x].getName();

                        Log.i(TAG, " =====> Nombre del Archivo: " + s[x]);

                        uploadBase(s[x]);
//								 URL u = new URL (customURL+t[x]);
//	     						HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection ();
//	     						huc.setRequestMethod ("GET");  //OR  huc.setRequestMethod ("HEAD");
//	     						huc.connect () ;
//	     						huc.getResponseCode();
//	     						Log.i(TAG, " =====> Archivo:  lista ==>" + huc.getResponseCode());
//	     						if(huc.getResponseCode()==200 || huc.getResponseCode()==500){
////	     							moveFile(pathFotosN, t[x], pathFotosF);
//	     							Log.i(TAG, " =====> Archivo:  Movido ==>" + t[x] );
//	     						}else{
//	     							Log.i(TAG, " =====> Archivo:  Sin Moverse ==>" + t[x] );
//	     						}
                    }
                    // first parameter is d files second parameter is zip file name

                } else {
                    Log.i(TAG, " =====> lista 2: " + "No existe el directorio");
                }
                // first parameter is d files second parameter is zip file name

            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.i(TAG, " =====> error zip: " + "_" + e.getMessage());
            }



            return null;
        }


//				protected void onProgressUpdate (Float... valores) {
//		              int p = Math.round(100*valores[0]);
//		              dialog.setProgress(p);
//		          }


        //tomo
        protected void onPostExecute(String date2) {
            super.onPostExecute(date2);
//					dialog.dismiss();

            Toast.makeText(getApplicationContext(), "Archivo Enviado", Toast.LENGTH_LONG).show();

//					dialogo(fecha_envio);

//					correo(date2, sacaChip());

        }

    }

    //Enviar audios
    class UpdateAudios extends AsyncTask<String, Float, String> {

        protected void onPreExecute() {
            super.onPreExecute();

//					dialog = new ProgressDialog(CalendarViewFotos.this);
//			        dialog.setMessage("Enviando Fotografías...");
//			        dialog.setTitle("Progreso");
//			        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//			        dialog.setCancelable(false);
//			        dialog.setProgress(0);
//			        dialog.setMax(100);
//			        dialog.show(); //Mostramos el diálogo antes de comenzar
        }


        @Override
        protected String doInBackground(String... params) {


            File sdCard;
            sdCard = Environment.getExternalStorageDirectory();
//						final String pathAudios = sdCard.getAbsolutePath() + "/" + nombreEncuesta+"-Audio"+date2;
            final String pathAudios = sdCard.getAbsolutePath() + "/" + nombreEncuesta +"-Audio" + formattedDate3 + "/";

            String sDirectorio = pathAudios;
            final File f = new File(sDirectorio);
            Log.i(TAG,"lista"+pathAudios);

//						final String customURL = "https://opinion.cdmx.gob.mx/cgi-bin/fotos/programas_sociales/";
            final String customURL = "https://opinion.cdmx.gob.mx/audios/"+nombreEncuesta+ "/";

            Log.i(TAG, " =====> URL audios: " + customURL);
            Log.i(TAG, " =====> lista audios 1: " + pathAudios);

            File F = new File(pathAudios);

            try {

                if (F.exists()) {

                    File[] ficheros = F.listFiles();

                    for (int i = 0; i <ficheros.length; i++) {
                        //Simulamos cierto retraso
                        try {Thread.sleep(500); }
                        catch (InterruptedException e) {}

                        publishProgress(i/(float)(ficheros.length)); //Actualizamos los valores
                    }



                    String[] s = new String[ficheros.length];
                    String[] t = new String[ficheros.length];
                    for (int x = 0; x < ficheros.length; x++) {
                        Log.i(TAG, " =====> lista audios: " + ficheros[x].getName());
                        s[x] = pathAudios + "/" + ficheros[x].getName();
                        t[x] = ficheros[x].getName();

//								 uploadFotos(s[x],date2);


                        URL u = new URL (customURL+t[x]);
                        Log.i(TAG, " =====> Archivo Audios custom: "+customURL+t[x] );
                        HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection ();
                        huc.setRequestMethod ("GET");  //OR  huc.setRequestMethod ("HEAD");
                        huc.connect () ;
                        huc.getResponseCode();
                        Log.i(TAG, " =====> Archivo:  lista De Audios ==>" + huc.getResponseCode());
                        if(huc.getResponseCode()==200){

                            //moveFile(pathFotosN, t[x], pathFotosF);
                            Log.i(TAG, " =====> Archivo:  En el servidor custom no hace nada==>" + t[x] );

                        }else if(huc.getResponseCode()==404){

                            uploadAudios(s[x]);
                            Log.i(TAG, " =====> Archivo:  Enviado al servidor custom==>" + t[x] );


                        }

                    }
                    // first parameter is d files second parameter is zip file name

                } else {
                    Log.i(TAG, " =====> lista 2: " + "No existe el directorio");
                }
                // first parameter is d files second parameter is zip file name

            } catch (Exception e) {
                String stackTrace = Log.getStackTraceString(e);
                Log.i("Manda Audios","Error Manda Audios"+ stackTrace);
            }


            return null;
        }


//				protected void onProgressUpdate (Float... valores) {
//			          int p = Math.round(100*valores[0]);
//			          dialog.setProgress(p);
//			      }


        //tomo
        protected void onPostExecute(String date2) {
            super.onPostExecute(date2);
//					dialog.dismiss();

            //	Toast.makeText(CalendarViewFotos.this, "Envío de Fotografias completo ",Toast.LENGTH_LONG).show();

//					correo(date2, prefix);
//					correo(date2, sacaChip());

        }

    }

    public int uploadAudios(String sourceFileUri) {

        File sdCard;
        sdCard = Environment.getExternalStorageDirectory();
        //final String pathFotos = sdCard.getAbsolutePath() + "/"+ nombreEncuesta+"-Audio"+fech;
        final String pathAudios = sdCard.getAbsolutePath() + nombreEncuesta +"-Audio" + formattedDate3 + "/";

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

//			     dialog.dismiss();
            Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + pathAudios + "" + "/" + "20161124_002_359083065132816_1.jpg");
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            return 0;

        }
        else
        {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUriAudio);
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

                Log.i("uploadFile", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://www.androidexample.com/media/uploads/"
                                    +"20161124_002_359083065132816_1.jpg";

//			                      Toast.makeText(Entrada.this, "File Upload Complete."+msg,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

//			        dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
//			                messageText.setText("MalformedURLException Exception : check script url.");
//			                Toast.makeText(CalendarViewFotos.this, "MalformedURLException",
//			                                                    Toast.LENGTH_SHORT).show();
                    }
                });

                Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + "Upload file to server "+ "error: " + ex.getMessage());

//			        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

//			        dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
//			                messageText.setText("Error de Internet");
//			                Toast.makeText(CalendarViewFotos.this, "Error de Internet",
//			                        Toast.LENGTH_SHORT).show();
                    }
                });
                Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + "Upload file to server Exception "+ "Exception : "+ e.getMessage());

//			        Log.e("Upload file to server Exception", "Exception : "
//			                                         + e.getMessage(), e);
            }
            return serverResponseCode;

        } // End else block
    }

    /// Enviar Fotos


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



}
