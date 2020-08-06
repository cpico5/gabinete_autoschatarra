package mx.gob.cdmx.gabineteautoschatarra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.content.Context.TELEPHONY_SERVICE;

public class UnCaughtException implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;
    private Activity app = null;
    final String path = "/mnt/sdcard//Mis_archivos/Mis_Logs/";
    private Context context;

    public UnCaughtException(Activity app, Context ctx) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.app = app;
        context = ctx;
    }

    private StatFs getStatFs() {
        File path = Environment.getDataDirectory();
        return new StatFs(path.getPath());
    }

    private long getAvailableInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    private long getTotalInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public String sacaImei() {

        String szImei;
TelephonyManager TelephonyMgr = (TelephonyManager) app.getSystemService(TELEPHONY_SERVICE);//Telefono

if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
if (szImei == null) {
szImei = Settings.Secure.getString(this.context.getContentResolver(), Settings.Secure.ANDROID_ID);// Tableta
}
return szImei;
}
szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
if (szImei == null) {
szImei = Settings.Secure.getString(this.context.getContentResolver(), Settings.Secure.ANDROID_ID);// Tableta
}
return szImei;
}

private void addInformation(StringBuilder message) {
    message.append("Locale: ").append(Locale.getDefault()).append('\n');
    try {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        pi = pm.getPackageInfo(context.getPackageName(), 0);
        message.append("Version: ").append(pi.versionName).append('\n');
        message.append("Package: ").append(pi.packageName).append('\n');
    } catch (Exception e) {
        Log.e("CustomExceptionHandler", "Error", e);
        message.append("Could not get Version information for ").append(
            context.getPackageName());
    }
    message.append("Phone Model: ").append(android.os.Build.MODEL)
    .append('\n');
    message.append("Android Version: ")
    .append(android.os.Build.VERSION.RELEASE).append('\n');
    message.append("Imei: ")
    .append(sacaImei()).append('\n');
    message.append("Board: ").append(android.os.Build.BOARD).append('\n');
    message.append("Brand: ").append(android.os.Build.BRAND).append('\n');
    message.append("Device: ").append(android.os.Build.DEVICE).append('\n');
    message.append("Host: ").append(android.os.Build.HOST).append('\n');
    message.append("ID: ").append(android.os.Build.ID).append('\n');
    message.append("Model: ").append(android.os.Build.MODEL).append('\n');
    message.append("Product: ").append(android.os.Build.PRODUCT)
    .append('\n');
    message.append("Type: ").append(android.os.Build.TYPE).append('\n');
    StatFs stat = getStatFs();
    message.append("Total Internal memory: ")
    .append(getTotalInternalMemorySize(stat)).append('\n');
    message.append("Available Internal memory: ")
    .append(getAvailableInternalMemorySize(stat)).append('\n');
}

public void uncaughtException(Thread t, Throwable e) {
    StackTraceElement[] arr = e.getStackTrace();
    String report = e.toString()+"\n\n";
    report += "--------- Seguimiento de Pila ---------\n\n";
    for (int i=0; i<arr.length; i++) {
        report += "    "+arr[i].toString()+"\n";
    }
    report += "-------------------------------\n\n";

// If the exception was thrown in a background thread inside
// AsyncTask, then the actual exception can be found with getCause

    report += "--------- Causa ---------\n\n";
    Throwable cause = e.getCause();
    if(cause != null) {
        report += cause.toString() + "\n\n";
        arr = cause.getStackTrace();
        for (int i=0; i<arr.length; i++) {
            report += "    "+arr[i].toString()+"\n";
        }
    }
    report += "-------------------------------\n\n";


    final String logString = new String(report.getBytes());

//create text file in SDCard
    File sdCard = Environment.getExternalStorageDirectory();
    File dir = new File (sdCard.getAbsolutePath() + "/Mis_archivos/Mis_Logs");
    dir.mkdirs();
    Nombre nom= new Nombre();
    String nombreE = nom.nombreEncuesta();
    File file = new File(dir, nombreE+".txt");

    try {
        StringBuilder report_mail = new StringBuilder();
        Date curDate = new Date();
        report_mail.append("Informe de error recopilado : ")
        .append(curDate.toString()).append('\n').append('\n');
        report_mail.append("Información :").append('\n');
        addInformation(report_mail);
        report_mail.append('\n').append('\n');
        report_mail.append("Stack:\n");
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        report_mail.append(result.toString());
        printWriter.close();
        report_mail.append('\n');
        report_mail.append("**** End of current Report ***");
        Log.e(UnCaughtException.class.getName(),
            "Error while sendErrorMail" + report);
//            sendErrorMail(report);
        enviar(report_mail);
    } catch (Throwable ignore) {
        Log.e(UnCaughtException.class.getName(),
            "Error while sending error e-mail", ignore);
    }

    try {
//to write logcat in text file
        FileOutputStream fOut = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fOut);

// Write the string to the file
        osw.write(logString);
        osw.flush();
        osw.close();


// enviar("cpico56@gmail.com", "Reporte", "Esto es un email de fallo",report);
    } catch (FileNotFoundException e1) {
        e1.printStackTrace();
    } catch (IOException e1) {
        e1.printStackTrace();
    }

    defaultUEH.uncaughtException(t, e);
}



private void enviar(final StringBuilder errorContent) {

    Nombre nom= new Nombre();
    String nombreE = nom.nombreEncuesta();
    Intent sendIntent = new Intent(Intent.ACTION_SEND);
    String subject = "Reporte de fallo "+nombreE;
    StringBuilder body = new StringBuilder("Applicacion");
    body.append('\n').append('\n');
    body.append(errorContent).append('\n').append('\n');
    sendIntent.setType("text/plain");
    sendIntent.setType("message/rfc822");
    sendIntent.putExtra(Intent.EXTRA_EMAIL,
        new String[] { "cpico56@gmail.com"});
    sendIntent.putExtra(Intent.EXTRA_TEXT,
        body.toString());
    sendIntent.putExtra(Intent.EXTRA_SUBJECT,
        subject);
    sendIntent.setType("message/rfc822");
    app.startActivity(sendIntent);

//        Intent em = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);// send multiple, para enviar varios archivos.
//        em.setType("plain/text");
//
//        em.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "cpico56@gmail.com" });
//        em.putExtra(android.content.Intent.EXTRA_SUBJECT, "Archivos Fallo");
//        em.putExtra(android.content.Intent.EXTRA_TEXT, "Se envían archivos de fallo");
//
//        File sdCard;
//        sdCard = Environment.getExternalStorageDirectory();
//
//        // para enviar multiples archivos adjuntos
//
//        Nombre nom= new Nombre();
//        String nombreE = nom.nombreEncuesta();
//
//        ArrayList<Uri> uris = new ArrayList<Uri>();
//        uris.add(Uri.parse("file://" + path + "" + nombreE+".txt"));
//        em.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//
//        app.startActivity(Intent.createChooser(em, "Enviar correo electrónico"));

}
}




