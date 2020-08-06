package mx.gob.cdmx.gabineteautoschatarra.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import mx.gob.cdmx.gabineteautoschatarra.R;
import mx.gob.cdmx.gabineteautoschatarra.UsuariosSQLiteHelper3;


public class gpsappwidget extends Activity {
/** Called when the activity is first created. */
	UsuariosSQLiteHelper3 usdbh3;
	private SQLiteDatabase db3;
@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	requestWindowFeature(Window.FEATURE_NO_TITLE);
	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		ActivityCompat.requestPermissions(gpsappwidget.this,
				new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE ,
						Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE,
						Manifest.permission.CAMERA,
						Manifest.permission.ACCESS_COARSE_LOCATION,
						Manifest.permission.READ_PHONE_STATE,
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.INTERNET,
						Manifest.permission.ACCESS_NETWORK_STATE,
						Manifest.permission.RECORD_AUDIO,
						Manifest.permission.LOCATION_HARDWARE,
						Manifest.permission.RECEIVE_BOOT_COMPLETED,
						Manifest.permission.ACCESS_WIFI_STATE},
				1);
	}

	setContentView(R.layout.main);


	usdbh3 = new UsuariosSQLiteHelper3(this);
	db3 = usdbh3.getReadableDatabase();

	Intent serviceIntent = new Intent(this, GPSWidgetProvider.GPSWidgetService.class);
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		ContextCompat.startForegroundService(this, serviceIntent);
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
		}, 3000);

	} else {

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
}
