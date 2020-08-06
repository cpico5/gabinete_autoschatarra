package mx.gob.cdmx.gabineteautoschatarra.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import mx.gob.cdmx.gabineteautoschatarra.db.Anotaciones.AutoIncrement;
import mx.gob.cdmx.gabineteautoschatarra.db.Anotaciones.PrimaryKey;


public class DaoManager {

    private static SQLiteDatabase db;
    private static final String TAG_LOG     = "DaoManager";

    public DaoManager(SQLiteDatabase dbs){
        db = dbs;
    }

    public static String generateCreateQueryString(Class aClass) {

        String query = "CREATE TABLE " + aClass.getSimpleName() + " (";
        Field[] fields = aClass.getFields();

        for (int i = 0; i < fields.length ; i++) {

            Class<?> type = fields[i].getType();
            String name = fields[i].getName();


            if(!name.equals("$change")){
                if(!name.equals("serialVersionUID")){
                    query += name + " ";
                    if (type == int.class) {
                        query += "INTEGER";
                    } else if (type == float.class) {
                        query += "REAL";
                    } else if (type == String.class) {
                        query += "TEXT";
                    } else if (type == Boolean.class) {
                        query += "INTEGER";
                    } else if (type == Double.class) {
                        query += "TEXT";
                    }

                    if(fields[i].isAnnotationPresent(PrimaryKey.class))
                        query += " PRIMARY KEY ";

                    if(fields[i].isAnnotationPresent(AutoIncrement.class))
                        query += " AUTOINCREMENT ";

                    query += ",";

                }
            }
        }

        if(query.length()>0)
            query = query.substring(0,query.length()-1);

        query += ")";
        return query;
    }

    public static String generateDropIfExistsQueryString(Class aClass) {
        return "DROP TABLE IF EXISTS " + aClass.getSimpleName();
    }


    public <T> List<T> find(Class aClass, String selection,
                            String[] selectionArgs, String groupBy, String having, String orderBy) {
        return find(aClass, selection, selectionArgs, groupBy, having, orderBy, null);
    }

    public <T> List<T> find(Class aClass, String selection,
                            String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = db.query(aClass.getSimpleName(), null, selection, selectionArgs, groupBy, having, orderBy, limit);
        List a = new ArrayList();
        int id = 0;

        while (cursor.moveToNext()) {
            try {
                Object o = Class.forName(aClass.getName()).getConstructor().newInstance();

                Field[] fields = aClass.getFields();

                for (int i = 0; i < fields.length; i++) {

                    String name = fields[i].getName();

                    if(!name.equals("$change")) {
                        if (!name.equals("serialVersionUID")) {

                            if (fields[i].getType() == int.class)
                                fields[i].set(o, cursor.getInt(cursor.getColumnIndex(fields[i].getName())));
                            else if (fields[i].getType() == String.class)
                                fields[i].set(o, cursor.getString(cursor.getColumnIndex(fields[i].getName())));
                            else if (fields[i].getType() == boolean.class)
                                fields[i].set(o, cursor.getInt(cursor.getColumnIndex(fields[i].getName())) == 1 ? true : false );
                            else if (fields[i].getType() == float.class)
                                fields[i].set(o, cursor.getFloat(cursor.getColumnIndex(fields[i].getName())));
                            else if (fields[i].getType() == double.class)
                                fields[i].set(o, cursor.getDouble(cursor.getColumnIndex(fields[i].getName())));
                            else if(fields[i].getType() == List.class){

                                /*String nombreLista = fields[i].getName();
                                if(nombreLista.equals(LISTA_BITACORA)){
                                    List<Usuario> listaAux = find(BitacoraVisita.class,"idVisita=?",ids,null,null,null);
                                    if(listaAux != null && !listaAux.isEmpty()){
                                        fields[i].set(o, listaAux);
                                    }
                                }  */


                            }else{
                                fields[i].set(o, null);
                            }

                        }
                    }
                }

                a.add(o);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(cursor != null && !cursor.isClosed())
            cursor.close();

        return a;
    }

    public <T> Object findOne(Class aClass, String selection,
                              String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = db.query(aClass.getSimpleName(), null, selection, selectionArgs, groupBy, having, orderBy, limit);
        Object a = null;
        int id = 0;
        if (cursor.moveToFirst()) {
            try {
                Object o = Class.forName(aClass.getName()).getConstructor().newInstance();

                Field[] fields = aClass.getFields();
                for (int i = 0; i < fields.length; i++) {

                    String name = fields[i].getName();

                    if(!name.equals("$change")) {
                        if (!name.equals("serialVersionUID")) {

                            if(fields[i].isAnnotationPresent(PrimaryKey.class))
                                id = cursor.getInt(cursor.getColumnIndex(fields[i].getName()));

                            if (fields[i].getType() == int.class)
                                fields[i].set(o, cursor.getInt(cursor.getColumnIndex(fields[i].getName())));
                            else if (fields[i].getType() == String.class)
                                fields[i].set(o, cursor.getString(cursor.getColumnIndex(fields[i].getName())));
                            else if (fields[i].getType() == boolean.class)
                                fields[i].set(o, cursor.getInt(cursor.getColumnIndex(fields[i].getName())) == 1 ? true : false );
                            else if (fields[i].getType() == float.class)
                                fields[i].set(o, cursor.getFloat(cursor.getColumnIndex(fields[i].getName())));
                            else if (fields[i].getType() == double.class)
                                fields[i].set(o, cursor.getDouble(cursor.getColumnIndex(fields[i].getName())));
                            else if(fields[i].getType() == List.class){


                            }
                            else{
                                fields[i].set(o, null);
                            }


                        }
                    }
                }
                a = o;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //if(cursor != null && !cursor.isClosed())
          //  cursor.close();

        return a;
    }


    public long insert(Object object) {
        try {
            ContentValues c = new ContentValues();
            Field[] fields = object.getClass().getFields();
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                if(!name.equals("$change")){
                    if(!name.equals("serialVersionUID")){
                        if(!fields[i].isAnnotationPresent(AutoIncrement.class)) {
                            if (fields[i].getType() == int.class && fields[i].get(object) != null)
                                c.put(fields[i].getName(), fields[i].getInt(object));
                            if (fields[i].getType() == String.class && fields[i].get(object) != null)
                                c.put(fields[i].getName(), fields[i].get(object).toString());
                            if (fields[i].getType() == float.class && fields[i].get(object) != null)
                                c.put(fields[i].getName(), fields[i].getFloat(object));
                            if (fields[i].getType() == double.class && fields[i].get(object) != null)
                                c.put(fields[i].getName(), fields[i].getDouble(object));
                            if (fields[i].getType() == boolean.class && fields[i].get(object) != null)
                                c.put(fields[i].getName(), fields[i].getBoolean(object) == false ? 0 : 1);
                            if (fields[i].getType() == Long.class && fields[i].get(object) != null)
                                c.put(fields[i].getName(), fields[i].get(object).toString());
                        }
                    }
                }
            }

            return db.insert(object.getClass().getSimpleName(), null, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long update(Object object,String clausula,  String[] id) {
        try {
            ContentValues c = new ContentValues();
            Field[] fields = object.getClass().getFields();
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();

                if(!name.equals("$change")){
                    if(!name.equals("serialVersionUID")){

                        if(!fields[i].isAnnotationPresent(PrimaryKey.class)) {
                            if (fields[i].getType() == int.class && fields[i].get(object) != null)
                                c.put(fields[i].getName(), fields[i].getInt(object));
                            if (fields[i].getType() == String.class && fields[i].get(object) != null)
                                c.put(fields[i].getName(), fields[i].get(object).toString());
                            if (fields[i].getType() == float.class && fields[i].get(object) != null)
                                c.put(fields[i].getName(), fields[i].getFloat(object));
                            if (fields[i].getType() == double.class && fields[i].get(object) != null)
                                c.put(fields[i].getName(), fields[i].getDouble(object));
                            if (fields[i].getType() == boolean.class && fields[i].get(object) != null)
                                c.put(fields[i].getName(), fields[i].getBoolean(object) == false ? 0 : 1);
                        }

                    }
                }
            }
            return db.update(object.getClass().getSimpleName(),c, clausula, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List getCursorBuscador(Class aClass, String word, String selection, String[] selectionArgs ){

        Cursor cursor =  db.rawQuery("SELECT * " + " FROM " + aClass.getSimpleName()+
                " WHERE codigoVivienda LIKE '" +word+ "%' ORDER BY codigoVivienda ASC LIMIT 5", null);
        List a = new ArrayList();
        while (cursor.moveToNext()) {
            try {
                Object o = Class.forName(aClass.getName()).getConstructor().newInstance();

                Field[] fields = aClass.getFields();
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].getType() == int.class)
                        fields[i].set(o, cursor.getInt(cursor.getColumnIndex(fields[i].getName())));
                    else if (fields[i].getType() == String.class)
                        fields[i].set(o, cursor.getString(cursor.getColumnIndex(fields[i].getName())));
                    else if (fields[i].getType() == float.class)
                        fields[i].set(o, cursor.getFloat(cursor.getColumnIndex(fields[i].getName())));
                    else if (fields[i].getType() == double.class)
                        fields[i].set(o, cursor.getDouble(cursor.getColumnIndex(fields[i].getName())));
                    else{
                        fields[i].set(o, null);
                    }
                }
                a.add(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(cursor != null && !cursor.isClosed())
            cursor.close();

        return a;
    }

    public Object findByEmailAndPassword(Class aClass, String email, String password, String[] selectionArgs ){

        Cursor cursor =  db.rawQuery("SELECT * " + " FROM " + aClass.getSimpleName()+
                " WHERE email = '" + email + "' and password='" + password + "' ", null);
        Object a = null;
        if (cursor.moveToFirst()) {
            try {
                Object o = Class.forName(aClass.getName()).getConstructor().newInstance();

                Field[] fields = aClass.getFields();
                for (int i = 0; i < fields.length; i++) {

                    String name = fields[i].getName();

                    if(!name.equals("$change")){
                        if(!name.equals("serialVersionUID")){
                            if (fields[i].getType() == int.class)
                                fields[i].set(o, cursor.getInt(cursor.getColumnIndex(fields[i].getName())));
                            else if (fields[i].getType() == String.class)
                                fields[i].set(o, cursor.getString(cursor.getColumnIndex(fields[i].getName())));
                            else if (fields[i].getType() == float.class)
                                fields[i].set(o, cursor.getFloat(cursor.getColumnIndex(fields[i].getName())));
                            else if (fields[i].getType() == double.class)
                                fields[i].set(o, cursor.getDouble(cursor.getColumnIndex(fields[i].getName())));
                            else{
                                fields[i].set(o, null);
                            }
                        }
                    }
                }
                a = o;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(cursor != null && !cursor.isClosed())
            cursor.close();

        return a;
    }

    public void actualizaEnviadoById(String clase, int idDatoReparacion){
        ContentValues cv = new ContentValues();
        cv.put("enviado",1);

        int resp = db.update(clase, cv, "id=" + idDatoReparacion, null);
        if(resp == -1){
            Log.d("DaoManager", "pimc -----------> Error al marcar enviado: " + clase);
        }
    }

    public void actualizaIncidentes(String clase){
        ContentValues cv = new ContentValues();
        cv.put("atendido",1);

        int resp = db.update(clase, cv, null, null);
        if(resp == -1){
            Log.d("DaoManager", "pimc -----------> Error al actualizar atendido: " + clase);
        }
    }

    public void delete(Class aClass){
        long resp = db.delete(aClass.getSimpleName(), null, null);
        if(resp == -1){
            Log.d(TAG_LOG, "pimc -----------> Error al actualizar el token de usuario: " + aClass.getSimpleName());
        }
    }

    public void deleteClausule(Class aClass, String Clausule, String[] valor){
        long resp = db.delete(aClass.getSimpleName(), Clausule, valor);
        if(resp == -1){
            Log.d(TAG_LOG, "pimc -----------> Error al actualizar el token de usuario: " + aClass.getSimpleName());
        }
    }

    public int count(Class aClass){
        Cursor cursor = db.rawQuery("SELECT * FROM " + aClass.getSimpleName(), null);
        int count = cursor.getCount();
        return count;
    }

    public int countDate(Class aClass, String fechaInicial, String fechaFinal){
        Cursor cursor = db.rawQuery("SELECT * FROM " + aClass.getSimpleName() + " " +
                "and fecha", null);
        int count = cursor.getCount();
        return count;
    }

    public int sacaMaximo() {

        int maximo=0;
        String selectQuery = "SELECT count(*) FROM Ubicacion where " +
                "datetime(substr(fecha,0,5)||'-'||substr(fecha,5,2) ||'-'||substr(fecha,7,2)||' '|| hora)  >= datetime('now','localtime','-2 minute')";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                maximo = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return maximo;
    }

    public String[] getArrayNameTableName(String table){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("Selecciona");
        String query = "SELECT * FROM " + table + " order by status";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("status"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return  listCatalogo.toArray(new String[count]);
    }

    public String[] getArrayIdTableName(String table){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("0");
        String query = "SELECT * FROM " + table + " order by status";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("id"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return listCatalogo.toArray(new String[count]);
    }

    public String[] getArrayNameTableNameAlcaldia(String table){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("Selecciona");
        String query = "SELECT * FROM " + table + " ";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("alcaldia"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return  listCatalogo.toArray(new String[count]);
    }

    public String[] getArrayIdTableNameAlcaldia(String table){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("0");
        String query = "SELECT * FROM " + table + " ";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("id_municipio"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return listCatalogo.toArray(new String[count]);
    }

    public String[] getArrayNameTableNameCol(String table, int idAlcaldia){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("Selecciona");
        String query = "SELECT * FROM " + table + " where municipio = " + idAlcaldia + " order by colonia";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("colonia"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return  listCatalogo.toArray(new String[count]);
    }

    public String[] getArrayIdTableNameCol(String table, int idAlcaldia){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("0");
        String query = "SELECT * FROM " + table + "  where municipio = " + idAlcaldia + " order by colonia";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("id"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return listCatalogo.toArray(new String[count]);
    }

    public String[] getArrayNameTableNameTipo(String table){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("Selecciona");
        String query = "SELECT * FROM " + table + " order by tipo_vivienda";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("tipo_vivienda"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return  listCatalogo.toArray(new String[count]);
    }

    public String[] getArrayIdTableNameTipo(String table){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("0");
        String query = "SELECT * FROM " + table + " order by tipo_vivienda";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("id"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return listCatalogo.toArray(new String[count]);
    }

    public String[] getArrayNameTableNameInicio(String table){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("Selecciona");
        String query = "SELECT * FROM " + table + " where id in (1,2,3,4) order by status";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("status"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return  listCatalogo.toArray(new String[count]);
    }

    public String[] getArrayIdTableNameInicio(String table){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("0");
        String query = "SELECT * FROM " + table + " where id in (1,2,3,4) order by status";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("id"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return listCatalogo.toArray(new String[count]);
    }

    public String[] getArrayNameTableNameDos(String table){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("Selecciona");
        String query = "SELECT * FROM " + table + " where id not in (1,2) order by status";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("status"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return  listCatalogo.toArray(new String[count]);
    }

    public String[] getArrayIdTableNameDos(String table){
        List<String> listCatalogo = new ArrayList<>();
        int count = 0;
        listCatalogo.add("0");
        String query = "SELECT * FROM " + table + " where id not in (1,2) order by status";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    listCatalogo.add(cursor.getString(cursor.getColumnIndex("id"))
                    );
                } while (cursor.moveToNext());
                count = cursor.getCount();
            }
        }catch (SQLException ex){
            Log.e(TAG_LOG,ex.getMessage());
        }
        return listCatalogo.toArray(new String[count]);
    }

}
