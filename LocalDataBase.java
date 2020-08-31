package com.e_bus.e_bus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LocalDataBase extends SQLiteOpenHelper {
    private static final String DATA_BASE_NAME = "data.db";
    public static final String TABLE_NAME = "pendente";
    private static final String ID = "id";
    public static final String CARD_NUMBER = "numero_cartao";
    public static final String VALUE = "valor";
    public static final String DATE = "data";
    private static final int VERSION = 1;

    public LocalDataBase(Context context) {
        super(context, DATA_BASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + "(" + ID + " integer primary key autoincrement," + CARD_NUMBER + " text," + VALUE + " real," + DATE + " text" + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
    public void insert(String numero_cartao,String valor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(LocalDataBase.CARD_NUMBER, numero_cartao);
        valores.put(LocalDataBase.VALUE, valor);

        Date now = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(now);

        valores.put(LocalDataBase.DATE, date);
        db.insert(LocalDataBase.TABLE_NAME, null, valores);
        db.close();
    }
    public void insertDate(String numero_cartao,String valor,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(LocalDataBase.CARD_NUMBER, numero_cartao);
        valores.put(LocalDataBase.VALUE, valor);
        valores.put(LocalDataBase.DATE, date);
        db.insert(LocalDataBase.TABLE_NAME, null, valores);
        db.close();
    }
    public List<Pendente> select(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE_NAME, null);
        List<Pendente> pendentes=new ArrayList<>();
        if (c.moveToFirst()){
            do {
                pendentes.add(new Pendente(c.getInt(0),c.getInt(1),c.getDouble(2),c.getString(3)));
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return pendentes;
    }
    public void delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=" + id, null);
        db.close();
    }
}
