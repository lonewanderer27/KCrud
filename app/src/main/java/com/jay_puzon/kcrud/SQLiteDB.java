package com.jay_puzon.kcrud;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLiteDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "records.db";
    public static final String PROFILE = "profile", PROF_ID = "prof_id", PROF_FNAME = "prof_fname", PROF_MNAME = "prof_mname", PROF_LNAME = "prof_lname";
    ContentValues VALUES;
    Cursor rs;
    ArrayList<String> Items;
    ArrayList<Integer> ItemsId;
    public SQLiteDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase conn) {
        conn.execSQL("CREATE TABLE " + PROFILE + "(" + PROF_ID + " Integer PRIMARY KEY AUTOINCREMENT, " + PROF_FNAME + " TEXT, " + PROF_MNAME + " TEXT, " + PROF_LNAME + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase conn, int oldVersion, int newVersion) {
        conn.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(conn);
    }

    public boolean NotEmpty() {
        ArrayList<String> ItemList = this.GetRecords();
        return ItemList.size() > 0;
    }

    public boolean RecordExists(String fName, String mName, String lName) {
        SQLiteDatabase conn = this.getReadableDatabase();
        String[] Columns = {PROF_FNAME, PROF_MNAME, PROF_LNAME};
        String selection = PROF_FNAME + " = ? AND " + PROF_MNAME + " = ? AND " + PROF_LNAME + " = ?";
        String[] selectionArgs = {fName, mName, lName};

        rs = conn.query(PROFILE, Columns, selection, selectionArgs, null, null, null);
        return rs.moveToFirst();
    }

    public boolean AddRecord(String fName, String mName, String lName) {
        SQLiteDatabase conn = this.getWritableDatabase();
        VALUES = new ContentValues();
        VALUES.put(PROF_FNAME, fName);
        VALUES.put(PROF_MNAME, mName);
        VALUES.put(PROF_LNAME, lName);
        try {
            conn.insert(PROFILE, null, VALUES);
            return true;
        } catch (SQLException exception) {
            return false;
        }
    }

    public boolean UpdateRecord(int index, String fName, String mName, String lName) {
        SQLiteDatabase conn = this.getWritableDatabase();
        VALUES = new ContentValues();
        VALUES.put(PROF_FNAME, fName);
        VALUES.put(PROF_MNAME, mName);
        VALUES.put(PROF_LNAME, lName);

        String selection = PROF_ID + " = ?";
        String[] selectionArgs = {String.valueOf(index)};

        int rowsAffected = conn.update(PROFILE, VALUES, selection, selectionArgs);

        conn.close();
        return rowsAffected > 0;
    }

    public boolean DeleteRecords() {
        SQLiteDatabase conn = this.getWritableDatabase();
        int rowsDeleted = conn.delete(PROFILE, null, null);
        conn.close();
        return rowsDeleted > 0;
    }

    public boolean DeleteRecord(int index) {
        SQLiteDatabase conn = this.getWritableDatabase();
        String selection = PROF_ID + " = ?";
        String[] selectionArgs = {String.valueOf(index)};

        int rowsDeleted = conn.delete(PROFILE, selection, selectionArgs);
        conn.close();

        return rowsDeleted > 0;
    }

    @SuppressLint("Range")
    public String[] GetRecord(int index) {
        SQLiteDatabase conn = this.getReadableDatabase();
        String[] recordData = null;

        String[] columns = {PROF_FNAME, PROF_MNAME, PROF_LNAME};
        String selection = PROF_ID + " = ?";
        String[] selectionArgs = {String.valueOf(index)};

        rs = conn.query(PROFILE, columns, selection, selectionArgs, null, null, null);
        if (rs.moveToFirst()) {
            recordData = new String[]{
                    rs.getString(rs.getColumnIndex(PROF_FNAME)),
                    rs.getString(rs.getColumnIndex(PROF_MNAME)),
                    rs.getString(rs.getColumnIndex(PROF_LNAME))
            };
        }

        rs.close();
        conn.close();

        return recordData;
    }

    @SuppressLint("Range")
    public String GetRecordsString() {
        SQLiteDatabase conn = this.getReadableDatabase();
        String result = "", sql = "";
        sql = "SELECT * FROM " + PROFILE;
        rs = conn.rawQuery(sql, null);
        rs.moveToFirst();
        while (!rs.isAfterLast()) {
            result += rs.getString(rs.getColumnIndex(PROF_FNAME)) + " " + rs.getString(rs.getColumnIndex(PROF_MNAME)) + " " + rs.getString(rs.getColumnIndex(PROF_LNAME));
            rs.moveToNext();
        }

        rs.close();
        conn.close();

        return result;
    }

    @SuppressLint("Range")
    public ArrayList<String> GetRecords() {
        SQLiteDatabase conn = this.getReadableDatabase();

        Items = new ArrayList<>();
        ItemsId = new ArrayList<>();

        rs = conn.rawQuery("SELECT * FROM " + PROFILE, null);
        rs.moveToFirst();

        while (!rs.isAfterLast()) {
            ItemsId.add(rs.getInt(rs.getColumnIndex(PROF_ID)));
                    Items.add(
                            rs.getString(rs.getColumnIndex(PROF_ID)) + " " +
                            rs.getString(rs.getColumnIndex(PROF_FNAME)) + " " +
                            rs.getString(rs.getColumnIndex(PROF_MNAME)) + " " +
                            rs.getString(rs.getColumnIndex(PROF_LNAME)));
            rs.moveToNext();
        }

        rs.close();
        return Items;
    }
}
