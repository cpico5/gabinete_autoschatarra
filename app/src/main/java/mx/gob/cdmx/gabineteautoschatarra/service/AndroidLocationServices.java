package mx.gob.cdmx.gabineteautoschatarra.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import mx.gob.cdmx.gabineteautoschatarra.R;

import static android.support.v4.app.NotificationCompat.PRIORITY_MIN;

public class AndroidLocationServices extends Service {

    final static String TAG = "AndroidLocationServices";
    public static String mLastUpdateTime;
    static int serverResponseCode = 0;
    static Calendar c = Calendar.getInstance();
    static SimpleDateFormat df3 = new SimpleDateFormat("yyyMMdd");
    static String formattedDateFecha = df3.format(c.getTime());
    private PendingIntent service = null;

    WakeLock wakeLock;

    private LocationManager locationManager;
    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            // TODO Auto-generated method stub

            Log.e("Google", "Location Changed");

            if (location == null)
                return;

            if (isConnectingToInternet(getApplicationContext())) {

                JSONObject jsonObject = new JSONObject();

                try {

                    jsonObject.put("secret_key", Constants.SECRET_KEY);
                    jsonObject.put("latitude", location.getLatitude());
                    jsonObject.put("longitude", location.getLongitude());
                    jsonObject.put("deviceID", getDeviceImei(AndroidLocationServices.this));


                    AppLog.logString("Service.onLocationChanged()");
                    mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());



                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateCoordinates(location.getLatitude(), location.getLongitude());
                        }
                    }, 60*2000 );


//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//                            updateCoordinates(location.getLatitude(), location.getLongitude());
//                            handler.postDelayed(this, 120000); //now is every 2 minutes
//                        }
//                    }, 120000); //Every 120000 ms (2 minutes)

//                    updateCoordinates(location.getLatitude(), location.getLongitude());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    };

    public AndroidLocationServices() {
        // TODO Auto-generated constructor stub
    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressLint("MissingPermission") NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceImei(Context ctx) {
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startServiceOreoCondition(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String CHANNEL_ID = "my_service";
            String CHANNEL_NAME = "My Background Service";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(PRIORITY_MIN).build();
            startForeground(101, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        startServiceOreoCondition();

        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");



    }

    @SuppressLint("MissingPermission")
    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);

//		new ToggleGPS(getApplicationContext()).turnGPSOn();

        Log.e("Google", "Service Started");

        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 1, listener);

        waitForGPSCoordinates();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

//        wakeLock.release();
        try{
            Intent serviceIntent = new Intent(this, AndroidLocationServices.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        } catch(Exception ex){

            Intent serviceIntent = new Intent(this, AndroidLocationServices.class);
            startService(new Intent(this, AndroidLocationServices.class));

            ex.printStackTrace();
        }



        //  startService(new Intent(this, AndroidLocationServices.class));

    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        waitForGPSCoordinates();

        AppLog.logString("Service.onStartCommand()");

//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
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
            e.printStackTrace();
        }

        coder = null;
        addresses = null;

        if (info.length() <= 0) {
            info = "lat " + latitude + ", lon " + longitude;
            direccion = "";
        } else {
            info += ("\n" + "(lat " + latitude + ", lon " + longitude + ")");
            direccion += ("");
        }




        new SignupActivity(this).execute(hora, latitud, longitud, direccion);

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

        final String bestProvider = locationManager.getBestProvider(criteria, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

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
                locationManager.requestLocationUpdates(bestProvider, 30000, 10, listener);
            } else {
                final List<String> providers = locationManager.getProviders(true);

                for (final String provider : providers) {
                    locationManager.requestLocationUpdates(provider, 30000, 10, listener);
                }
            }

        }else{

            if (bestProvider != null && bestProvider.length() > 0) {
                locationManager.requestLocationUpdates(bestProvider, 500, 10, listener);
            } else {
                final List<String> providers = locationManager.getProviders(true);

                for (final String provider : providers) {
                    locationManager.requestLocationUpdates(provider, 500, 10, listener);
                }
            }
        }
    }




}
