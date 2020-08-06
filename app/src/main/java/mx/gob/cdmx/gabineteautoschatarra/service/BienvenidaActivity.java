package mx.gob.cdmx.gabineteautoschatarra.service;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import mx.gob.cdmx.gabineteautoschatarra.Entrada;
import mx.gob.cdmx.gabineteautoschatarra.R;
import mx.gob.cdmx.gabineteautoschatarra.UsuariosSQLiteHelper3;
import mx.gob.cdmx.gabineteautoschatarra.db.DaoManager;
import mx.gob.cdmx.gabineteautoschatarra.model.Aplicacion;
import mx.gob.cdmx.gabineteautoschatarra.upgradeActivity;

import static mx.gob.cdmx.gabineteautoschatarra.model.Nombre.APLICACION;

public class BienvenidaActivity extends Activity {

    private static final String TAG = "BienvenidaActivity";


    double latitude;
    double longitude;
    static InputStream is2 = null;
    String result3;
    String algo;
    public String seccion, count;
    //    ProgressDialog dialog = null;
//    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    private UsuariosSQLiteHelper3 usdbh3;
    private SQLiteDatabase db3;
    private DaoManager daoManager;

    Boolean bandera=false;

    Calendar c = Calendar.getInstance();

    SimpleDateFormat df3 = new SimpleDateFormat("yyyMMdd");
    String formattedDate3 = df3.format(c.getTime());

    SimpleDateFormat df4 = new SimpleDateFormat("yyyMMdd");
    String formattedDateFecha = df3.format(c.getTime());

    SimpleDateFormat df5 = new SimpleDateFormat("HH:mm a");
    String formattedDate5 = df5.format(c.getTime());

    public static String getHostName(String defValue) {
        try {
            Method getString = Build.class.getDeclaredMethod("getString", String.class);
            getString.setAccessible(true);
            return getString.invoke(null, "net.hostname").toString();
        } catch (Exception ex) {
            return defValue;
        }
    }

    public String sacaChip() {
        String Imei = "";

        String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // Getting IMEI Number of Devide

        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return TODO;
        } else {

            Imei = tManager.getDeviceId();
        }
        return Imei;
    }


    protected void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(BienvenidaActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.LOCATION_HARDWARE,Manifest.permission.ACCESS_WIFI_STATE},
                    1);
        }
        setContentView(R.layout.bienvenida);
        int ids[] = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, GPSWidgetProvider.class));

        if(ids.length==0){
            requestPinAppWidget();
        }else{
            // Toast.makeText(this, "Number of widgets: "+ids.length, Toast.LENGTH_LONG).show();
        }

        usdbh3 = new UsuariosSQLiteHelper3(this);
        db3 = usdbh3.getReadableDatabase();
        daoManager = new DaoManager(db3);

        //usdbh.onUpgrade(db,8,8);

        File directory;
        File file;
        File sdCard;
        sdCard = Environment.getExternalStorageDirectory();
        FileOutputStream fout = null;
        try {

            directory = new File(sdCard.getAbsolutePath() + "/PDFs");
            directory.mkdirs();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//        try {
//            copyReadAssets();
//        }catch (Exception e){
//            Log.e(TAG, e.getMessage());
//        }

        bandera = pregunta(AndroidLocationServices.class);
        if(bandera){
//            Toast.makeText(this, "EL SERVICIO YA EST¡ ARRRIBA", Toast.LENGTH_LONG).show();
//
//            finish();
        }
        else {
            startService(new Intent(this, AndroidLocationServices.class));

//            finish();
        }


        if (!verificaConexion(this)) {  //SI NO ES

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    //validar ubicacion
                    String dato=sacaMaximo();
                    if(dato.equals("0")){
                        showAlertDialog("Aviso","No hay datos de ubicacion",false);
                    }else{
                        showAlertDialog("Aviso","La app trabajará sin conexión a internet",true);
                    }

                }
            }, 3000);

        } else {

            //limpia los token de usuario
//            List<Usuario> listaUsuarios = daoManager.find(Usuario.class,null,null,null, null, null);
//            if(listaUsuarios != null && !listaUsuarios.isEmpty()){
//                for(Usuario usuario : listaUsuarios){
//                    usuario.setToken(null);
//                    daoManager.update(usuario,"idUsuario = " + usuario.getIdUsuario(),null);
//                }
//            }

            String version = getResources().getString(R.string.app_version);
            String project = getResources().getString(R.string.app_project);
            String sdk = String.valueOf(Build.VERSION.SDK_INT);

            RequestParams params = new RequestParams();
            params.put("version", version);
            params.put("project", project);
            params.put("android", sdk);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.setTimeout(20000);

            RequestHandle requestHandle = client.post(getResources().getString(R.string.url_upgrade) + "/api/global/version", params, new AsyncHttpResponseHandler() {
                String jsonHost = "";
                String jsonLatest = "";
                String jsonUpgrade = "";
                String jsonCurrent = "";
                String jsonStatus = "";
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d(TAG, "Realizo la conexión");
                    Log.d(TAG, "cea -----------> " + new String(responseBody));
                    try {

                        String json = new String(responseBody);
                        JSONObject jsonObject = new JSONObject(json);
                        Log.d(TAG, "cea -----------> Data: " + jsonObject.get("data"));

                        jsonHost = jsonObject.getJSONObject("data").getString("host");
                        jsonCurrent = jsonObject.getJSONObject("data").getString("current");
                        jsonLatest = jsonObject.getJSONObject("data").getString("latest");
                        jsonUpgrade = jsonObject.getJSONObject("data").getString("upgrade");
                        jsonStatus = jsonObject.getJSONObject("data").getString("status");

                        if(jsonLatest.toString().equals(jsonCurrent.toString())){
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    String dato=sacaMaximo();

                                    if(dato.equals("0")){

                                        showAlertDialog("Aviso","No hay datos de Ubicación",false);

                                        /*Toast toast = Toast.makeText(getBaseContext(), "No hay datos de Ubicación", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        finish();*/

                                    }else{


                                        Intent intent = new Intent(BienvenidaActivity.this, Entrada.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        Bundle bundle = new Bundle();
                                        intent.putExtras(bundle);

                                        finish();
                                        startActivity(intent);

                                    }



                                }
                            }, 3000);
                        }else{

                            Aplicacion aplicacion = new Aplicacion();
                            aplicacion.setUpgrade(jsonUpgrade);
                            aplicacion.setHost(jsonHost);
                            aplicacion.setStatus(jsonStatus);

                            Intent intent = new Intent(BienvenidaActivity.this, upgradeActivity.class);
                            intent.putExtra(APLICACION, aplicacion);
                            finish();
                            startActivity(intent);
                        }



                    } catch (JSONException e){
                        Log.e(TAG, e.getMessage());
                    }




                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e(TAG, "existe un error en la conexión");
                    if(responseBody != null){
                        Log.d(TAG, "cea -----------> " + new String(responseBody));
                    }

                    String dato=sacaMaximo();

                    if(dato.equals("0")){

                        showAlertDialog("Aviso","No hay datos de ubicacion",false);

                    }else{

                        Intent intent = new Intent(BienvenidaActivity.this, Entrada.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Bundle bundle = new Bundle();
                        intent.putExtras(bundle);

                        finish();
                        startActivity(intent);
                    }


                }
            });
        }

    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Permisos asignados", Toast.LENGTH_SHORT).show();

                    //Intent i = new Intent(this, MainActivity.class); //start activity
                    //startActivity(i);
                    //startService(new Intent(this, AndroidLocationServices.class));
/*                    bandera=    pregunta(AndroidLocationServices.class);
                    if(bandera){
                        Toast.makeText(this, "EL SERVICIO YA ESTÁ ARRRIBA", Toast.LENGTH_LONG).show();

                        finish();
                    }
                    else {
                        Intent intent = new Intent();
                        ComponentName comp = new ComponentName(this.getPackageName(),
                                AndroidLocationServices.class.getName());
                        //  startService(new Intent(this, AndroidLocationServices.class));
                        //  AndroidLocationServices.enqueueWork(this, (intent.setComponent(comp)));

                        ContextCompat.startForegroundService(this,intent.setComponent(comp));
                        finish();
                    }*/

                }

                else {
                    Toast.makeText(this, "Permisos rechazados", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onDestroy() {
//		h.removeCallbacks(r);
//		if (pd.isShowing() ) {
//			pd.dismiss();
//		}
        super.onDestroy();
    }

    public void requestPinAppWidget(){

        AppWidgetManager appWidgetManager =
                this.getSystemService(AppWidgetManager.class);
        ComponentName myProvider =
                new ComponentName(this, GPSWidgetProvider.class);

        if (appWidgetManager.isRequestPinAppWidgetSupported()) {
            // Create the PendingIntent object only if your app needs to be notified
            // that the user allowed the widget to be pinned. Note that, if the pinning
            // operation fails, your app isn't notified.
            Intent pinnedWidgetCallbackIntent = new Intent(this, mx.gob.cdmx.gabineteautoschatarra.service.GPSWidgetProvider.class);

            // Configure the intent so that your app's broadcast receiver gets
            // the callback successfully. This callback receives the ID of the
            // newly-pinned widget (EXTRA_APPWIDGET_ID).
            PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                    pinnedWidgetCallbackIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            appWidgetManager.requestPinAppWidget(myProvider, null, successCallback);
        }
    }

    /////// METODO PARA VERIFICAR LA CONEXION A INTERNET
    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sÔøΩlo wifi, tambiÔøΩn GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle deberÌa no ser tan COMPLICADO
        for (int i = 0; i < 2; i++) {
            // ÔøΩTenemos conexiÔøΩn? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }


    private boolean pregunta(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private String sacaMaximo() {
        String maximo=null;
        Set<String> set = new HashSet<String>();
        final String F = "File dbfile";
        usdbh3 = new UsuariosSQLiteHelper3(this);
        db3 = usdbh3.getReadableDatabase();
        String selectQuery = "SELECT count(*) FROM ubicacion where " +
                "datetime(substr(fecha,0,5)||'-'||substr(fecha,5,2) ||'-'||substr(fecha,7,2)||' '|| hora)  >= datetime('now','localtime','-1 hour')  order by hora";
        Cursor cursor = db3.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int val = cursor.getInt(0);
                maximo = String.valueOf(val);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db3.close();
        return maximo;
    }

//    private void copyFileFromAssets(String in_filename, File out_file){
//        Log.d("copyFileFromAssets", "Copying file '"+in_filename+"' to '"+out_file.toString()+"'");
//        AssetManager assetManager = getApplicationContext().getAssets();
//        FileChannel in_chan = null, out_chan = null;
//        try {
//            AssetFileDescriptor in_afd = assetManager.openFd(in_filename);
//            FileInputStream in_stream = in_afd.createInputStream();
//            in_chan = in_stream.getChannel();
//            Log.d("copyFileFromAssets", "Asset space in file: start = "+in_afd.getStartOffset()+", length = "+in_afd.getLength());
//            FileOutputStream out_stream = new FileOutputStream(out_file);
//            out_chan = out_stream.getChannel();
//            in_chan.transferTo(in_afd.getStartOffset(), in_afd.getLength(), out_chan);
//        } catch (IOException ioe){
//            Log.w("copyFileFromAssets", "Failed to copy file '"+in_filename+"' to external storage:"+ioe.toString());
//        } finally {
//            try {
//                if (in_chan != null) {
//                    in_chan.close();
//                }
//                if (out_chan != null) {
//                    out_chan.close();
//                }
//            } catch (IOException ioe){}
//        }
//    }

//    private void copyReadAssets()
//    {
//        File sdCard;
//        sdCard = Environment.getExternalStorageDirectory();
//        AssetManager assetManager = getAssets();
//
//        InputStream in = null;
//        OutputStream out = null;
//
//        String strDir = sdCard.getAbsolutePath() + "/PDFs"+ File.separator + "Pdfs";
//        File fileDir = new File(strDir);
//        fileDir.mkdirs();   // crear la ruta si no existe
//        File file = new File(fileDir, "SACMEXfaqs_v2.pdf");
//
//
//
//        try
//        {
//
//            in = assetManager.open("SACMEXfaqs_v2.pdf");  //leer el archivo de assets
//            out = new BufferedOutputStream(new FileOutputStream(file)); //crear el archivo
//
//
//            copyFile(in, out);
//            in.close();
//            in = null;
//            out.flush();
//            out.close();
//            out = null;
//        } catch (Exception e)
//        {
//            Log.e("tag", e.getMessage());
//        }
//
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("file://" + sdCard.getAbsolutePath() + "/PDFs" + File.separator + "Pdfs" + "/M_SACMEX.pdf"), "application/pdf");
//
//        try {
//            startActivity(intent);
//        }catch (Exception e){
//
//        }
//
//        startActivity(intent);
//    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }

    public void showAlertDialog(String mensaje, String descripcion, final boolean acceder){

        //builder.setTitle("Éxito");
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(BienvenidaActivity.this);
        //builder.setMessage(mensaje);


        LinearLayout diagLayout = new LinearLayout(this);
        diagLayout.setOrientation(LinearLayout.VERTICAL);

        TextView titulo = new TextView(this);
        titulo.setText(mensaje);
        titulo.setPadding(10, 10, 10, 10);
        titulo.setGravity(Gravity.CENTER);
        titulo.setTextSize(22);
        titulo.setTextColor(Color.parseColor("#FFFFFF"));

        TextView text = new TextView(this);
        text.setText(descripcion);
        text.setPadding(10, 60, 10, 10);
        text.setGravity(Gravity.CENTER);
        text.setTextSize(18);
        text.setTextColor(Color.parseColor("#FFFFFF"));
        //diagLayout.addView();
        builder.setView(diagLayout);


        LayoutInflater inflater = this.getLayoutInflater();

        View titleView = inflater.inflate(R.layout.alert_personalizado, null);

        ImageView imageView = titleView.findViewById(R.id.robotImageView);
//        imageView.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
        LinearLayout msgLinearLayout = titleView.findViewById(R.id.messageLinearLayout);
        msgLinearLayout.setBackground(getResources().getDrawable(R.color.robot_sin_internet));
        msgLinearLayout.addView(titulo);
        msgLinearLayout.addView(text);

        builder.setCustomTitle(titleView);


        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(acceder){
                    Intent intent = new Intent(BienvenidaActivity.this, Entrada.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);

                    finish();
                    startActivity(intent);
                }else{

                    finish();

                    //System.exit(0);
                }


            }
        });
/*		builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});*/
        alertDialog =builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
}
