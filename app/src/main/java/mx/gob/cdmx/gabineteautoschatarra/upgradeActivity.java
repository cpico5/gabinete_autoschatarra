package mx.gob.cdmx.gabineteautoschatarra;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import mx.gob.cdmx.gabineteautoschatarra.model.Aplicacion;
import mx.gob.cdmx.gabineteautoschatarra.model.CheckForSDCard;
import pub.devrel.easypermissions.EasyPermissions;

import static mx.gob.cdmx.gabineteautoschatarra.model.Nombre.APLICACION;

public class upgradeActivity<apkurl> extends AppCompatActivity {
    private static final String TAG = "UPGRADE";
    private static final int WRITE_REQUEST_CODE = 300;
    private static final int ACTIVITY_VIEW_ATTACHMENT = 0;
    private final static int INSTALL = 1;

    ImageButton btnUpgrade;
    private String url;
    public static final String outputFile = "update.apk";

    public static String apkurl = "";
    private Aplicacion aplicacion;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.upgrade);
        btnUpgrade = findViewById(R.id.btnUpgrade);

        Intent startingIntent = getIntent();
        if(startingIntent == null) {
            Log.e(TAG,"No Intent?  We're not supposed to be here...");
            finish();
            return;
        }

        if (savedInstanceState != null) {
            aplicacion = (Aplicacion) savedInstanceState.getSerializable(APLICACION);
        } else {
            aplicacion = (Aplicacion) startingIntent.getSerializableExtra(APLICACION);
        }

        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckForSDCard.isSDCardPresent()) {

                    //check if app has permission to write to the external storage.
                    if (EasyPermissions.hasPermissions(upgradeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Get the URL entered
                        url = aplicacion.getUpgrade();
                        new DownloadFile().execute(url);

                    } else {
                        //If permission is not present request for the same.
                        EasyPermissions.requestPermissions(upgradeActivity.this, getString(R.string.write_file), WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }


                } else {
                    Toast.makeText(getApplicationContext(),
                            "SD Card not found", Toast.LENGTH_LONG).show();

                }
            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, upgradeActivity.this);
    }


    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //Download the file once permission is granted
        url = aplicacion.getUpgrade();
        new DownloadFile().execute(url);
    }


    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }



    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;
        private String connection;
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(upgradeActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                HttpsURLConnection connection = null;
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();
                String typec = connection.getContentType();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                //if (!directory.exists()) {
                directory.mkdirs();
                //}
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Se Descargo en: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Algo salio mal...";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();




            File currentFile = new File(folder + fileName);
            File newFile =  new File(folder + "actualizacion" + ".apk");


            if(newFile.exists()){
                newFile.delete();
            }



            if(rename(currentFile, newFile)){
                //Success
                Log.i(TAG, "Success");
            } else {
                //Fail
                Log.i(TAG, "Fail");
            }

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();

            DownloadSuccess(newFile);

        }


    }
    private void DownloadSuccess(File newFile) {

        int result = Settings.Secure.getInt(getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0);

        Intent downloadIntent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File fileLocation = new File("" + newFile);
            Uri apkUri = FileProvider.getUriForFile(upgradeActivity.this, upgradeActivity.this.getApplicationContext().getPackageName() + ".fileProvider", fileLocation);

            downloadIntent = new Intent(Intent.ACTION_VIEW);
            downloadIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            downloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            downloadIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");

            List<ResolveInfo> resInfoList = upgradeActivity.this.getPackageManager().queryIntentActivities(downloadIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                upgradeActivity.this.grantUriPermission(packageName, apkUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

        } else {
            File fileLocation = new File( "" + newFile);
            downloadIntent = new Intent(Intent.ACTION_VIEW);
            downloadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            downloadIntent.setDataAndType(Uri.fromFile(fileLocation), "application/vnd.android.package-archive");
        }
            upgradeActivity.this.startActivityForResult(downloadIntent,INSTALL);
            int p = android.os.Process.myPid();
            android.os.Process.killProcess(p);




    }

    private boolean rename(File from, File to) {
        return from.getParentFile().exists() && from.exists() && from.renameTo(to);
    }

}
