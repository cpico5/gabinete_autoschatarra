package mx.gob.cdmx.gabineteautoschatarra.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import mx.gob.cdmx.gabineteautoschatarra.Nombre;
import mx.gob.cdmx.gabineteautoschatarra.R;
import mx.gob.cdmx.gabineteautoschatarra.UsuariosSQLiteHelper3;



public class SignupActivity extends AsyncTask<String, Void, String> {

	final static String TAG = "SignupActivity";
	private Context context;
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	String imei_num;
	UsuariosSQLiteHelper3 usdbh3;
	private SQLiteDatabase db3;

	Nombre nom = new Nombre();
	String nombreEncuesta = nom.nombreEncuesta();
	String upLoadServerUriBase = "https://opinion.cdmx.gob.mx/cgi-bin/php/recibeBases" + nombreEncuesta + ".php?encuesta=" + nombreEncuesta + "";
	String upLoadServerUriAudio = "https://opinion.cdmx.gob.mx/cgi-bin/php/recibeAudios" + nombreEncuesta + ".php?encuesta=" + nombreEncuesta + "";
	String upLoadServerUriFotos = "https://opinion.cdmx.gob.mx/cgi-bin/php/recibeFotos" + nombreEncuesta + ".php?encuesta=" + nombreEncuesta + "";

    public static final int PERMISSION_REQUEST_CODE = 1;
    private WifiState wifiState;
    private Imei imei;

    private String latitud;
    private String longitud;
	SimpleDateFormat sdFecha = new SimpleDateFormat("yyyy/MM/dd");
	SimpleDateFormat sdHora = new SimpleDateFormat("HH:mm:ss");
	String fechaStr = "";
	String horaStr = "";

	private Usuario usuario;

    private Date yesterday() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	Calendar c = Calendar.getInstance();
	SimpleDateFormat df3 = new SimpleDateFormat("yyyMMdd");
	String formattedDateFecha = df3.format(c.getTime());
	SimpleDateFormat df5 = new SimpleDateFormat("HH:mm");
	String formattedDateHora = df5.format(c.getTime());
	SimpleDateFormat ayer = new SimpleDateFormat("yyyyMMdd");
	String formattedDateAyer = ayer.format(yesterday());

	int serverResponseCode = 0;

	int cuenta = 0;

	static InputStream is2 = null;

	public SignupActivity(Context context) {
		this.context = context;
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
    protected void onPreExecute() {

		cuenta++;

        Intent serviceIntent = new Intent(context, GPSWidgetProvider.GPSWidgetService.class);
//        ContextCompat.startForegroundService(context, serviceIntent );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, serviceIntent );
        } else {
            context.startService(serviceIntent);
        }

        wifiState = new WifiState(context);

        try{
            imei = new Imei(this.context);
            imei_num = imei.getImai().toString();
            Log.i(null, "El chip: " + imei_num);

			usdbh3 = new UsuariosSQLiteHelper3(this.context);
			db3 = usdbh3.getWritableDatabase();
			latitud = "";
			longitud = "";


        }catch(Exception ex){
            ex.printStackTrace();
        }

	}


	@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
	protected String doInBackground(String... arg0) {

		latitud = arg0[0];
		longitud = arg0[1];
		String direccion = arg0[2];

		String newDireccion = direccion.replaceAll("\\s", "_");
		final String laDireccion = newDireccion.trim();

		final String hora = formattedDateHora;
		final String elImei = imei_num;
		final String token = "ZJvI7PooUhZogIarOp8v";
		final String project = this.context.getResources().getString((R.string.app_project));
		final String url;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			url=this.context.getResources().getString(R.string.urls_gps);
		} else {
			url=this.context.getResources().getString(R.string.url_gps);
		}

		if (wifiState.haveNetworkConnection()) {


			fechaStr = sdFecha.format(new Date());
			horaStr = sdHora.format(new Date());



			Calendar calendar = Calendar.getInstance();
			Calendar calendar_envio = Calendar.getInstance();
			Date horaActual= null;
			Date FechaActual= null;

			Log.i("log_tag","---------->> Hora sacada de la Base: "+sacaHora());
			Log.i("log_tag","---------->> Fecha sacada de la Base: "+sacaFecha());

			if(sacaHora()==null||sacaFecha()==null){

				Log.i("log_tag","---------->> Fecha Base entra en  null: "+ sacaFecha());
			Log.i("log_tag","---------->> Hora Base entra en null: "+ sacaHora());


			try {

				Log.i("log_tag", "connection: " + url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);

				RequestParams params = new RequestParams();
				params.put("latitude", latitud);
				params.put("longitude", longitud);
				params.put("data", laDireccion);
				params.put("imei", imei_num);
				params.put("token", token);
				params.put("project", project);

				AsyncHttpClient client = new AsyncHttpClient();
				RequestHandle requestHandle = client.post(url + "/api/location/set", params, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

						Log.d(TAG, "pimc -----------> " + new String(responseBody));
						String json = new String(responseBody);
						try {

							Log.i("log_tag", "Borrando Ubicacion anterior");

							Log.i("log_tag", "Insertando ubicacion actual");

							//ejecuta el background

							if(usuario !=  null){
								String[] enviado = {"0"};

								//encuesta = (Encuesta) objectRepository.findOne(Encuesta.class,"enviado=?",
								//      enviado,null,null,null,null);

							}

							if(json != null && !json.equals("")){

								//cacha el msj del alerta

							}


						} catch (Exception ex) {
							Log.e(TAG, ex.getMessage());
						}


					}

					@Override
					public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
						if (statusCode != 200) {
							Log.e(TAG, "existe un error en la conexión -----> " + error.getMessage());
							if(responseBody != null){
								Log.d(TAG, "e2lira -----------> " + new String(responseBody));
								String json = new String(responseBody);
							}

						}
					}
				});




				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);

				HttpResponse response2 = httpclient.execute(httppost);
				HttpEntity entity2 = response2.getEntity();
				is2 = entity2.getContent();

				Log.i("log_tag", "connection success ");
				Log.i("log_tag", "connection: " + url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);


				if (db3 != null) {

					db3.execSQL("DELETE FROM ubicacion where fecha='" + formattedDateAyer + "'");
					Log.i("log_tag", "connection: " + "DELETE FROM ubicacion where fecha='" + formattedDateAyer + "'");
					db3.execSQL("INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
					Log.i("log_tag", "connection: " + "INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");


					if(usuario !=  null){
                        String[] enviado = {"0"};

					    //encuesta = (Encuesta) objectRepository.findOne(Encuesta.class,"enviado=?",
                          //      enviado,null,null,null,null);

					}

				} else {
					Log.i("log_tag", "mal connection: " + "INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
				}


			} catch (Exception e) {
				String stackTrace = Log.getStackTraceString(e);
				Log.i("log_tag", "connection error" + stackTrace);
				Log.i("log_tag", "Error in http connection " + e.toString());

			}

			}else {


				Calendar c = Calendar.getInstance();

				SimpleDateFormat df1 = new SimpleDateFormat("yyyMMdd");
				String formattedDate1 = df1.format(c.getTime());

				SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
				String formattedDate2 = df2.format(c.getTime());

				SimpleDateFormat df3 = new SimpleDateFormat("yyyMMdd");
				String formattedDate3 = df3.format(c.getTime());

				SimpleDateFormat df4 = new SimpleDateFormat("HH:mm:ss a");
				String formattedDate4 = df4.format(c.getTime());

				SimpleDateFormat df5 = new SimpleDateFormat("HH:mm:ss");
				String formattedDate5 = df5.format(c.getTime());





				//Obtiene la Fecha
				try {
					FechaActual = new SimpleDateFormat("yyyy/MM/dd").parse(fechaStr);
					Log.i("log_tag","---------->> La Fecha actual: "+formattedDate3);
					Log.i("log_tag","---------->> La Fecha base: "+sacaFecha());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Date horaBase = null;
				SimpleDateFormat format = new SimpleDateFormat("HH:mm");
				try {
					horaBase = format.parse(sacaHora());
					Log.i("log_tag","---------->> Hora sacada de la Base: "+horaBase);
				} catch (ParseException e) {
					e.printStackTrace();
					Log.i("log_tag","---------->> Error Hora sacada de la Base: "+horaBase);
				}

				try {
					horaActual = new SimpleDateFormat("HH:mm").parse(horaStr);
					Log.i("log_tag","---------->> Hora actual: "+horaActual);
				} catch (ParseException e) {
					e.printStackTrace();
				}






				calendar.setTime(horaBase);
				calendar.add(Calendar.MINUTE, 2);
				calendar_envio.setTime(horaBase);
				calendar_envio.add(Calendar.MINUTE, 30);
				Date horaActualizacion = calendar.getTime();
				Date horaEnvio = calendar_envio.getTime();
				Log.i("log_tag","---------->> Hora actual: "+horaActual);
				Log.i("log_tag","---------->> Hora de la actualización: "+horaActualizacion);
				Log.i("log_tag","---------->> Hora del envio: "+horaEnvio);



				if(formattedDate3.equals(sacaFecha())){


					Log.i("log_tag","---------->> SON IGUALES ");

					//para mandar despues de media hora
					if(horaActual.after(horaEnvio)) {
						Log.i("log_tag", "---------->> Hora de Envío: " + "esta despues  Si mando");
						Log.i("log_tag","---------->> Hora actual: "+horaActual);
						Log.i("log_tag","---------->> Hora del envio: "+horaEnvio);

						new UpdateBases().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						new UpdateFotos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

						try {

						} catch (Exception e) {
							String stackTrace = Log.getStackTraceString(e);
							Log.i("log_tag", "---------->> connection error" + stackTrace);
							Log.i("log_tag", "---------->> Error in http connection " + e.toString());

						}
					}else{
							Log.i("log_tag","---------->> Hora de Envío: "+ "esta antes NO mando");
						}

					//Finaliza para mandar despues de media hora

					//valida el tiempo
					if(horaActual.after(horaActualizacion)){
						Log.i("log_tag","---------->> Hora actualización: "+ "esta despues  Si mando");

						try {

							Log.i("log_tag", "connection: " + url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);

							RequestParams params = new RequestParams();
							params.put("latitude", latitud);
							params.put("longitude", longitud);
							params.put("data", laDireccion);
							params.put("imei", imei_num);
							params.put("token", token);
							params.put("project", project);

							AsyncHttpClient client = new AsyncHttpClient();
							RequestHandle requestHandle = client.post(url + "/api/location/set", params, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

								@Override
								public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

									Log.d(TAG, "pimc ---------->> " + new String(responseBody));
									String json = new String(responseBody);
									try {

										Log.i("log_tag", "---------->> Borrando Ubicacion anterior");

										Log.i("log_tag", "---------->> Insertando ubicacion actual");

										//ejecuta el background

										if(usuario !=  null){
											String[] enviado = {"0"};

											//encuesta = (Encuesta) objectRepository.findOne(Encuesta.class,"enviado=?",
											//      enviado,null,null,null,null);

										}

										if(json != null && !json.equals("")){

											//cacha el msj del alerta

										}


									} catch (Exception ex) {
										Log.e(TAG, ex.getMessage());
									}


								}

								@Override
								public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
									if (statusCode != 200) {
										Log.e(TAG, "existe un error en la conexión ---------->>  " + error.getMessage());
										if(responseBody != null){
											Log.d(TAG, "e2lira ---------->>  " + new String(responseBody));
											String json = new String(responseBody);
										}

									}
								}
							});




							HttpClient httpclient = new DefaultHttpClient();
							HttpPost httppost = new HttpPost(url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);

							HttpResponse response2 = httpclient.execute(httppost);
							HttpEntity entity2 = response2.getEntity();
							is2 = entity2.getContent();

							Log.i("log_tag", "---------->> connection success ");
							Log.i("log_tag", "---------->> connection: " + url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);


							if (db3 != null) {

								db3.execSQL("DELETE FROM ubicacion where fecha='" + formattedDateAyer + "'");
								Log.i("log_tag", "connection: " + "DELETE FROM ubicacion where fecha='" + formattedDateAyer + "'");
								db3.execSQL("INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
								Log.i("log_tag", "connection: " + "INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");


								if(usuario !=  null){
									String[] enviado = {"0"};

									//encuesta = (Encuesta) objectRepository.findOne(Encuesta.class,"enviado=?",
									//      enviado,null,null,null,null);

								}

							} else {
								Log.i("log_tag", " ---------->> mal connection: " + "INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
							}


						} catch (Exception e) {
							String stackTrace = Log.getStackTraceString(e);
							Log.i("log_tag", "---------->> connection error" + stackTrace);
							Log.i("log_tag", "---------->> Error in http connection " + e.toString());

						}

					}else{
						Log.i("log_tag","---------->> Hora actualización: "+ "esta antes NO mando");

										Log.i("log_tag","---------->> CUENTA: "+ cuenta);
					}




				}else{
					Log.i("log_tag","---------->> SON DIFERENTES ");


					try {

						Log.i("log_tag", "connection: " + url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);

						RequestParams params = new RequestParams();
						params.put("latitude", latitud);
						params.put("longitude", longitud);
						params.put("data", laDireccion);
						params.put("imei", imei_num);
						params.put("token", token);
						params.put("project", project);

						AsyncHttpClient client = new AsyncHttpClient();
						RequestHandle requestHandle = client.post(url + "/api/location/set", params, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

							@Override
							public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

								Log.d(TAG, "pimc -----------> " + new String(responseBody));
								String json = new String(responseBody);
								try {

									Log.i("log_tag", "Borrando Ubicacion anterior");

									Log.i("log_tag", "Insertando ubicacion actual");

									//ejecuta el background

									if(usuario !=  null){
										String[] enviado = {"0"};

										//encuesta = (Encuesta) objectRepository.findOne(Encuesta.class,"enviado=?",
										//      enviado,null,null,null,null);

									}

									if(json != null && !json.equals("")){

										//cacha el msj del alerta

									}


								} catch (Exception ex) {
									Log.e(TAG, ex.getMessage());
								}


							}

							@Override
							public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
								if (statusCode != 200) {
									Log.e(TAG, "existe un error en la conexión -----> " + error.getMessage());
									if(responseBody != null){
										Log.d(TAG, "e2lira -----------> " + new String(responseBody));
										String json = new String(responseBody);
									}

								}
							}
						});




						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);

						HttpResponse response2 = httpclient.execute(httppost);
						HttpEntity entity2 = response2.getEntity();
						is2 = entity2.getContent();

						Log.i("log_tag", "connection success ");
						Log.i("log_tag", "connection: " + url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);


						if (db3 != null) {

							db3.execSQL("DELETE FROM ubicacion where fecha='" + formattedDateAyer + "'");
							Log.i("log_tag", "connection: " + "DELETE FROM ubicacion where fecha='" + formattedDateAyer + "'");
							db3.execSQL("INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
							Log.i("log_tag", "connection: " + "INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");


							if(usuario !=  null){
								String[] enviado = {"0"};

								//encuesta = (Encuesta) objectRepository.findOne(Encuesta.class,"enviado=?",
								//      enviado,null,null,null,null);

							}

						} else {
							Log.i("log_tag", "mal connection: " + "INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
						}


					} catch (Exception e) {
						String stackTrace = Log.getStackTraceString(e);
						Log.i("log_tag", "connection error" + stackTrace);
						Log.i("log_tag", "Error in http connection " + e.toString());

					}



				}


//
//
//				//valida el tiempo
//				if(horaActual.after(horaActualizacion)){
//					Log.i("log_tag","---------->> Hora actualización: "+ "esta despues  Si mando");
//
//					try {
//
//						Log.i("log_tag", "connection: " + url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);
//
//						RequestParams params = new RequestParams();
//						params.put("latitude", latitud);
//						params.put("longitude", longitud);
//						params.put("data", laDireccion);
//						params.put("imei", imei_num);
//						params.put("token", token);
//						params.put("project", project);
//
//						AsyncHttpClient client = new AsyncHttpClient();
//						RequestHandle requestHandle = client.post(url + "/api/location/set", params, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//
//							@Override
//							public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//								Log.d(TAG, "pimc ---------->> " + new String(responseBody));
//								String json = new String(responseBody);
//								try {
//
//									Log.i("log_tag", "---------->> Borrando Ubicacion anterior");
//
//									Log.i("log_tag", "---------->> Insertando ubicacion actual");
//
//									//ejecuta el background
//
//									if(usuario !=  null){
//										String[] enviado = {"0"};
//
//										//encuesta = (Encuesta) objectRepository.findOne(Encuesta.class,"enviado=?",
//										//      enviado,null,null,null,null);
//
//									}
//
//									if(json != null && !json.equals("")){
//
//										//cacha el msj del alerta
//
//									}
//
//
//								} catch (Exception ex) {
//									Log.e(TAG, ex.getMessage());
//								}
//
//
//							}
//
//							@Override
//							public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//								if (statusCode != 200) {
//									Log.e(TAG, "existe un error en la conexión ---------->>  " + error.getMessage());
//									if(responseBody != null){
//										Log.d(TAG, "e2lira ---------->>  " + new String(responseBody));
//										String json = new String(responseBody);
//									}
//
//								}
//							}
//						});
//
//
//
//
//						HttpClient httpclient = new DefaultHttpClient();
//						HttpPost httppost = new HttpPost(url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);
//
//						HttpResponse response2 = httpclient.execute(httppost);
//						HttpEntity entity2 = response2.getEntity();
//						is2 = entity2.getContent();
//
//						Log.i("log_tag", "---------->> connection success ");
//						Log.i("log_tag", "---------->> connection: " + url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);
//
//
//						if (db != null) {
//
//							db.execSQL("DELETE FROM ubicacion where fecha='" + formattedDateAyer + "'");
//							Log.i("log_tag", "connection: " + "DELETE FROM ubicacion where fecha='" + formattedDateAyer + "'");
//							db.execSQL("INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
//							Log.i("log_tag", "connection: " + "INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
//
//
//							if(usuario !=  null){
//								String[] enviado = {"0"};
//
//								//encuesta = (Encuesta) objectRepository.findOne(Encuesta.class,"enviado=?",
//								//      enviado,null,null,null,null);
//
//							}
//
//						} else {
//							Log.i("log_tag", " ---------->> mal connection: " + "INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
//						}
//
//
//					} catch (Exception e) {
//						String stackTrace = Log.getStackTraceString(e);
//						Log.i("log_tag", "---------->> connection error" + stackTrace);
//						Log.i("log_tag", "---------->> Error in http connection " + e.toString());
//
//					}
//
//				}else{
//					Log.i("log_tag","---------->> Hora actualización: "+ "esta antes NO mando");
//				}

			}



//			Log.i("log_tag","---------->> Fecha Base: "+ sacaFecha());
//			Log.i("log_tag","---------->> Hora Base: "+ sacaHora());


//			try {
//
//				Log.i("log_tag", "connection: " + url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);
//
//				RequestParams params = new RequestParams();
//				params.put("latitude", latitud);
//				params.put("longitude", longitud);
//				params.put("data", laDireccion);
//				params.put("imei", imei_num);
//				params.put("token", token);
//				params.put("project", project);
//
//				AsyncHttpClient client = new AsyncHttpClient();
//				RequestHandle requestHandle = client.post(url + "/api/location/set", params, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//
//					@Override
//					public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//						Log.d(TAG, "pimc -----------> " + new String(responseBody));
//						String json = new String(responseBody);
//						try {
//
//							Log.i("log_tag", "Borrando Ubicacion anterior");
//
//							Log.i("log_tag", "Insertando ubicacion actual");
//
//							//ejecuta el background
//
//							if(usuario !=  null){
//								String[] enviado = {"0"};
//
//								//encuesta = (Encuesta) objectRepository.findOne(Encuesta.class,"enviado=?",
//								//      enviado,null,null,null,null);
//
//							}
//
//							if(json != null && !json.equals("")){
//
//								//cacha el msj del alerta
//
//							}
//
//
//						} catch (Exception ex) {
//							Log.e(TAG, ex.getMessage());
//						}
//
//
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//						if (statusCode != 200) {
//							Log.e(TAG, "existe un error en la conexión -----> " + error.getMessage());
//							if(responseBody != null){
//								Log.d(TAG, "e2lira -----------> " + new String(responseBody));
//								String json = new String(responseBody);
//							}
//
//						}
//					}
//				});
//
//
//
//
//				HttpClient httpclient = new DefaultHttpClient();
//				HttpPost httppost = new HttpPost(url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);
//
//				HttpResponse response2 = httpclient.execute(httppost);
//				HttpEntity entity2 = response2.getEntity();
//				is2 = entity2.getContent();
//
//				Log.i("log_tag", "connection success ");
//				Log.i("log_tag", "connection: " + url + "/api/location/set?latitude=" + latitud + "&longitude=" + longitud + "&data=" + laDireccion + "&imei=" + elImei + "&token=" + token + "&project=" + project);
//
//
//				if (db != null) {
//
//					db.execSQL("DELETE FROM ubicacion where fecha='" + formattedDateAyer + "'");
//					Log.i("log_tag", "connection: " + "DELETE FROM ubicacion where fecha='" + formattedDateAyer + "'");
//					db.execSQL("INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
//					Log.i("log_tag", "connection: " + "INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
//
//
//					if(usuario !=  null){
//                        String[] enviado = {"0"};
//
//					    //encuesta = (Encuesta) objectRepository.findOne(Encuesta.class,"enviado=?",
//                          //      enviado,null,null,null,null);
//
//					}
//
//				} else {
//					Log.i("log_tag", "mal connection: " + "INSERT INTO ubicacion (fecha,hora,latitud,longitud,direccion) values (" + "'" + formattedDateFecha + "'" + "," + "'" + hora + "'" + "," + "'" + latitud + "'" + "," + "'" + longitud + "'" + "," + "'" + laDireccion + "');");
//				}
//
//
//			} catch (Exception e) {
//				String stackTrace = Log.getStackTraceString(e);
//				Log.i("log_tag", "connection error" + stackTrace);
//				Log.i("log_tag", "Error in http connection " + e.toString());
//
//			}
		}

		return null;

	}

	@TargetApi(Build.VERSION_CODES.O)
	@RequiresApi(api = Build.VERSION_CODES.O)
	private void cargaEncuestaWS(){

		String json = "";

		try{

			Gson gson  = new Gson();
			//Type collectionType = new TypeToken<Encuesta>() { }.getType();
			//json = gson.toJson(encuesta,collectionType);

			imei = new Imei(context);

		} catch (Exception ex){
			Log.d(TAG, "pimc -----------> " + ex.getMessage());
		}

		Log.d(TAG, "Token JWT: " + usuario.getToken());

		RequestParams params = new RequestParams();
		params.put("data", json);

		params.put("latitude", latitud);
		params.put("longitude", longitud);
		params.put("project", "SituacionCalle");
		params.put("imei", imei.getImai().toString());

		AsyncHttpClient client = new AsyncHttpClient();
		client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
		client.addHeader("Authorization", "Bearer " + usuario.getToken());

		RequestHandle requestHandle = client.post(context.getResources().getString(R.string.url_aplicacion) + "/api/situacion_calle/people", params, new AsyncHttpResponseHandler(Looper.getMainLooper()) {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				Log.d(TAG, "pimc -----------> Realizo la conexión para subir los datos");
				Log.d(TAG, "pimc -----------> " + new String(responseBody));

			/*	try {

					String json = new String(responseBody);
					JSONObject jsonObject = new JSONObject(json);
					Log.d(TAG, "pimc -----------> Data: " + jsonObject.get("data"));
					String idReporte = jsonObject.getJSONObject("data").getString("id");

					encuesta.setIdEncuesta(Integer.valueOf(idReporte));
					encuesta.setEnviado(1);

					String[] idEnc = {String.valueOf(encuesta.getId())};
					objectRepository.update(encuesta,idEnc);

				} catch (JSONException e){
					Log.e(TAG, e.getMessage());
				}  */
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				if (statusCode != 200) {
					Log.e(TAG, "existe un error en la conexión -----> " + error.getMessage());
					if(responseBody != null)
						Log.d(TAG, "e2lira -----------> " + new String(responseBody));

				}

			}
		});
	}


	@Override
	protected void onPostExecute(String result) {
		//db.close();
		System.gc();
    }


	private String sacaFecha() {
		Set<String> set = new HashSet<String>();
		String fecha = null;
		final String F = "File dbfile";
//// Abrimos la base de datos 'DBUsuarios' en modo escritura
//		usdbh = new UsuariosSQLiteHelper(this);
//		db = usdbh.getReadableDatabase();
		String selectQuery = "select fecha from ubicacion order by id desc limit 1";
		Cursor cursor = db3.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				fecha = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
// db.close();

		return fecha;
	}

	private String sacaHora() {
		Set<String> set = new HashSet<String>();
		String hora = null;
		final String F = "File dbfile";
//// Abrimos la base de datos 'DBUsuarios' en modo escritura
//		usdbh = new UsuariosSQLiteHelper(this);
//		db = usdbh.getReadableDatabase();
		String selectQuery = "select hora from ubicacion order by id desc limit 1";
		Cursor cursor = db3.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				hora = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
// db.close();

		return hora;
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
						s[x] = pathBase + "/" + nombreEncuesta+"_"+imei_num;
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

//			Toast.makeText(getApplicationContext(), "Archivo Enviado", Toast.LENGTH_LONG).show();

//					dialogo(fecha_envio);

//					correo(date2, sacaChip());

		}

	}

	/// Enviar Fotos


	public int uploadFotos(String sourceFileUri, String fech) {

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df3 = new SimpleDateFormat("yyyMMdd");
		String formattedDate3 = df3.format(c.getTime());

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


				}

				//close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

//		        dialog.dismiss();
				ex.printStackTrace();


				Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + "Upload file to server " + "error: " + ex.getMessage());

//		        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

//		        dialog.dismiss();
				e.printStackTrace();


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

			Calendar c = Calendar.getInstance();
			SimpleDateFormat df3 = new SimpleDateFormat("yyyMMdd");
			String formattedDate3 = df3.format(c.getTime());

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
