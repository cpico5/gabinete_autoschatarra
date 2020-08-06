package mx.gob.cdmx.gabineteautoschatarra.service;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import mx.gob.cdmx.gabineteautoschatarra.R;
import mx.gob.cdmx.gabineteautoschatarra.UsuariosSQLiteHelper3;

public class Bienvenida extends Activity {

    private static final String TAG = "Bienvenida";


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
	UsuariosSQLiteHelper3 usdbh3;
	private SQLiteDatabase db3;
    public static final int PERMISSION_REQUEST_CODE = 1;
	
	Boolean bandera=false;

    Calendar c = Calendar.getInstance();

    SimpleDateFormat df3 = new SimpleDateFormat("yyyMMdd");
    String formattedDate3 = df3.format(c.getTime());
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



    protected void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();

    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(Bienvenida.this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE ,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.LOCATION_HARDWARE,
                            Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.ACCESS_WIFI_STATE},
                    1);
        }
        setContentView(R.layout.bienvenida);



        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(Bienvenida.this,this));

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int[] ids = manager.getAppWidgetIds(new ComponentName(this,mx.gob.cdmx.gabineteautoschatarra.service.GPSWidgetProvider.class));


        if(ids.length==0){
            requestPinAppWidget();
        }else{
            // Toast.makeText(this, "Number of widgets: "+ids.length, Toast.LENGTH_LONG).show();
        }
        
        usdbh3 = new UsuariosSQLiteHelper3(this);
        db3 = usdbh3.getReadableDatabase();
        
        File directory;
        File file;
        File sdCard;
        sdCard = Environment.getExternalStorageDirectory();
        FileOutputStream fout = null;
        try {
        	directory = new File(sdCard.getAbsolutePath() + "/loc");
        	directory.mkdirs();
        } catch (Exception e) {
        	String stackTrace = Log.getStackTraceString(e);
        	Log.i("log_tag", "mkdir error" + stackTrace);
        }

        Intent serviceIntent = new Intent(this, GPSWidgetProvider.GPSWidgetService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.startForegroundService(this, serviceIntent );
        } else {
            this.startService(serviceIntent);
        }
        
        if (!verificaConexion(this)) {  //SI NO ES

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
           

                           Toast toast = Toast.makeText(getBaseContext(), "Verifica tu conexion a Internet", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            finish();
//                            pd.dismiss();
//                            dialog.dismiss();

                }
            }, 1000);

        } else {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                                 
                	String dato=sacaMaximo();
                	
                	if(dato.matches("0")){
                	
                		Toast toast = Toast.makeText(getBaseContext(), "No hay datos de Ubicación", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        finish();

                	}else{
                    	
//                        Intent intent = new Intent(Bienvenida.this,MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        Bundle bundle = new Bundle();
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                        finish();
                		
                	}

                	

                }
            }, 1000);
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


    public static int getWidgetId(Intent intent) {
        Bundle extras = intent.getExtras();
        int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        return appWidgetId;
    }


    /////// METODO PARA VERIFICAR LA CONEXION A INTERNET
    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        // No solo wifi, tambien GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            // Tenemos conexin? ponemos a true
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
    			String selectQuery = "SELECT count(*) FROM ubicacion where fecha='" + formattedDate3 + "'";
    			Cursor cursor = db3.rawQuery(selectQuery, null);
    			if (cursor.moveToFirst()) {
    				do {
    					maximo = cursor.getString(0);
    				} while (cursor.moveToNext());
    			}
    			cursor.close();
    			db3.close();
    			return maximo;
    		}

    public void requestPinAppWidget(){

        AppWidgetManager appWidgetManager = this.getSystemService(AppWidgetManager.class);
        ComponentName myProvider = new ComponentName(this, mx.gob.cdmx.gabineteautoschatarra.service.GPSWidgetProvider.class);

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

}
