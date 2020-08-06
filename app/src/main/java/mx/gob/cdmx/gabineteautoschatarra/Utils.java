package mx.gob.cdmx.gabineteautoschatarra;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    public String getAlcaldia(int idAlcaldia){
        String nombre = "";

        switch(idAlcaldia){
            case 1:
                nombre = "Azcapotzalco";
                break;
            case 2:
                nombre = "Coyoacan";
                break;
            case 3:
                nombre = "Cuajimalpa";
                break;
            case 4:
                nombre = "Gustavo A. Madero";
                break;
            case 5:
                nombre = "Iztacalco";
                break;
            case 6:
                nombre = "Iztapalapa";
                break;
            case 7:
                nombre = "La Magdalena Contreras";
                break;
            case 8:
                nombre = "Milpa Alta";
                break;
            case 9:
                nombre = "Alvaro Obregon";
                break;
            case 10:
                nombre = "Tlahuac";
                break;
            case 11:
                nombre = "Tlalpan";
                break;
            case 12:
                nombre = "Xochimilco";
                break;
            case 13:
                nombre = "Benito Juarez";
                break;
            case 14:
                nombre = "Cuahutemoc";
                break;
            case 15:
                nombre = "Miguel Hidalgo";
                break;
            case 16:
                nombre = "Venustiano Carranza";
                break;
            default:
                nombre = "";
                break;
        }

        return nombre;
    }

    public String getAlcaldiaUno(int idAlcaldia){
        String nombre = "";

        switch(idAlcaldia){
            case 1:
                nombre = "Azcapotzalco";
                break;
            case 2:
                nombre = "Coyoacan";
                break;
            case 3:
                nombre = "Cuajimalpa";
                break;
            case 4:
                nombre = "Gustavo A. Madero";
                break;
            case 5:
                nombre = "Iztacalco";
                break;
            case 6:
                nombre = "Iztapalapa";
                break;
            case 7:
                nombre = "La Magdalena Contreras";
                break;
            case 8:
                nombre = "Milpa Alta";
                break;
            case 9:
                nombre = "Alvaro Obregon";
                break;
            case 10:
                nombre = "Tlahuac";
                break;
            case 11:
                nombre = "Tlalpan";
                break;
            case 12:
                nombre = "Xochimilco";
                break;
            case 13:
                nombre = "Benito Juarez";
                break;
            case 14:
                nombre = "Cuahutemoc";
                break;
            case 15:
                nombre = "Miguel Hidalgo";
                break;
            case 16:
                nombre = "Venustiano Carranza";
                break;
            default:
                nombre = "";
                break;
        }

        return nombre;
    }

    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }
}
