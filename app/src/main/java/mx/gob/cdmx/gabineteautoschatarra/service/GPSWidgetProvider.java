package mx.gob.cdmx.gabineteautoschatarra.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.gob.cdmx.gabineteautoschatarra.BienvenidaActivity;
import mx.gob.cdmx.gabineteautoschatarra.R;


public class GPSWidgetProvider extends AppWidgetProvider {
	public static String mLastUpdateTime;
	static int serverResponseCode = 0;
	static Calendar c = Calendar.getInstance();
	static SimpleDateFormat df3 = new SimpleDateFormat("yyyMMdd");
	static String formattedDateFecha = df3.format(c.getTime());
	private PendingIntent service = null;

	public static final String NOTIFICATION_CHANNEL_ID_SERVICE = "com.package.MyService";
	public static final String NOTIFICATION_CHANNEL_ID_INFO = "com.package.download_info";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		//se implementa AlarmManager para actualizar divamicamente el widget
		final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		final Intent i = new Intent(context, GPSWidgetService.class);
		if (service == null) {
			service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
		}

		final int N = appWidgetIds.length;

		//Iteramos la lista de widgets en ejecuci�n
		for (int j = 0; j < N; j++) {
			//ID del widget actual
			int appWidgetId = appWidgetIds[j];

			Intent intent = new Intent(context, BienvenidaActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.gpswidget);
			views.setOnClickPendingIntent(R.id.txtInfo, pendingIntent);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}


		Intent serviceIntent = new Intent(context, GPSWidgetService.class);
//		ContextCompat.startForegroundService(context, serviceIntent );
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			ContextCompat.startForegroundService(context, serviceIntent );
		} else {
			context.startService(serviceIntent);
		}



		//tiempo de repeticiones
		m.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 180000, service);

	}



	public static class GPSWidgetService extends Service {
		private static final String NOTIFICATION_CHANNEL_ID_INFO = "INFO";
		private LocationManager manager = null;

		private LocationListener listener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onLocationChanged(final Location location) {
				AppLog.logString("Service.onLocationChanged()");
				mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

//				Handler handler = new Handler(Looper.getMainLooper());
//
//				handler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						updateCoordinates(location.getLatitude(), location.getLongitude());
//					}
//				}, 60*2000 );

				updateCoordinates(location.getLatitude(), location.getLongitude());

				//stopSelf();
			}
		};


		private void startMyOwnForeground(){
			String NOTIFICATION_CHANNEL_ID = "mx.gob.cdmx.seguimiento";
			String channelName = "Mi Servicio";
			NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
			chan.setLightColor(Color.BLUE);
			chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			assert manager != null;
			manager.createNotificationChannel(chan);

			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
			Notification notification = notificationBuilder.setOngoing(true)
					.setSmallIcon(R.drawable.ic_launcher_foreground)
					.setContentTitle("Seguimiento corriendo en segundo plano")
					.setPriority(NotificationManager.IMPORTANCE_MIN)
					.setCategory(Notification.CATEGORY_SERVICE)
					.build();
			startForeground(2, notification);
		}


		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}


		@Override
		public void onCreate(){
			super.onCreate();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
				startMyOwnForeground();
			else
				startForeground(1, new Notification());

			AppLog.logString("Service.onCreate()");
			manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		}


		@Override
		public void onStart(Intent intent, int startId) {
			super.onStart(intent, startId);

			waitForGPSCoordinates();
		}

		@Override
		public void onDestroy() {
//			stopListening();

			AppLog.logString("Service.onDestroy()");

			super.onDestroy();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				stopForeground(true); //true will remove notification
			}

		}

		public int onStartCommand(Intent intent, int flags, int startId) {
			waitForGPSCoordinates();

			AppLog.logString("Service.onStartCommand()");

			return super.onStartCommand(intent, flags, startId);
		}

		private void waitForGPSCoordinates() {
			startListening();
		}

		private void startListening() {
			AppLog.logString("Service.startListening()");

			final Criteria criteria = new Criteria();

			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);

			final String bestProvider = manager.getBestProvider(criteria, true);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

				if (bestProvider != null && bestProvider.length() > 0) {
					if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return;
					}
					manager.requestLocationUpdates(bestProvider, 12000, 10, listener);
				} else {
					final List<String> providers = manager.getProviders(true);

					for (final String provider : providers) {
						manager.requestLocationUpdates(provider, 12000, 10, listener);
					}
				}

			}else{

				if (bestProvider != null && bestProvider.length() > 0) {
					manager.requestLocationUpdates(bestProvider, 12000, 10, listener);
				} else {
					final List<String> providers = manager.getProviders(true);

					for (final String provider : providers) {
						manager.requestLocationUpdates(provider, 12000, 10, listener);
					}
				}
			}
		}

		private void stopListening() {
			try {
				if (manager != null && listener != null) {
					manager.removeUpdates(listener);
				}

				manager = null;
			} catch (final Exception ex) {

			}
		}

		private void updateCoordinates(double latitude, double longitude) {
			String hora = mLastUpdateTime.toString();
			String latitud = String.valueOf(latitude);
			String longitud = String.valueOf(longitude);
			String direccion = "";

			Geocoder coder = new Geocoder(this);
			List<Address> addresses = null;
			String info = "";

			AppLog.logString("Service.updateCoordinates()");
			AppLog.logString(info);

			try {
				addresses = coder.getFromLocation(latitude, longitude, 2);

				if (null != addresses && addresses.size() > 0) {
					int addressCount = addresses.get(0).getMaxAddressLineIndex();

					if (-1 != addressCount) {
						for (int index = 0; index <= addressCount; ++index) {
							info += addresses.get(0).getAddressLine(index);
							direccion += addresses.get(0).getAddressLine(index);

							if (index < addressCount)
								info += ", ";
							direccion += ", ";
						}
					} else {
						info += addresses.get(0).getFeatureName() + ", " + addresses.get(0).getSubAdminArea() + ", "
								+ addresses.get(0).getAdminArea();
						direccion += addresses.get(0).getFeatureName() + ", " + addresses.get(0).getSubAdminArea() + ", "
								+ addresses.get(0).getAdminArea();
					}
				}

				AppLog.logString(addresses.get(0).toString());
			} catch (Exception e) {
				String stackTrace = Log.getStackTraceString(e);
				Log.i("GPSWidget", "==================> Error GPS: " + stackTrace);
				Log.i("GPSWidget", "==================> Latitud: " +latitud );
				Log.i("GPSWidget", "==================> Longitud: " +longitud );
				Log.i("GPSWidget", "==================> Dirección: " +direccion );

				String laDir="Sin dato";
				new SignupActivity(this).execute(latitud,longitud,laDir);
			}

			coder = null;
			addresses = null;

			if (info.length() <= 0) {
				info = "lat " + latitude + ", lon " + longitude;
				direccion="";
			} else {
				info += ("\n" + "(lat " + latitude + ", lon " + longitude + ")");
				direccion += ("");
			}



			RemoteViews views = new RemoteViews(getPackageName(), R.layout.gpswidget);

			views.setTextViewText(R.id.txtInfo, info);
			new SignupActivity(this).execute(latitud,longitud,direccion);

			ComponentName thisWidget = new ComponentName(this, GPSWidgetProvider.class);
			AppWidgetManager.getInstance(this).updateAppWidget( thisWidget, views );

//				sube.subeArchivo();


		}


	}


}