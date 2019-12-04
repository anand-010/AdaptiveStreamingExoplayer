package com.adaptive.exoplayer.database;
import android.database.sqlite.SQLiteDatabase;
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//
//import java.util.logging.Logger;
//
//public class DatabaseHelper extends SQLiteOpenHelper {
//
//    private static DatabaseHelper databaseHelper;
//
//    // All Static variables
//    private static final int DATABASE_VERSION = 1;
//
//    // Database Name
//    private static final String DATABASE_NAME = Config.DATABASE_NAME;
//
//    // Constructor
//    private DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    public static synchronized DatabaseHelper getInstance(Context context){
//        if(databaseHelper==null){
//            databaseHelper = new DatabaseHelper(context);
//        }
//        return databaseHelper;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        // Create tables SQL execution
//        String CREATE_STUDENT_TABLE = "CREATE TABLE " + Config.TABLE_STUDENT + "("
//                + Config.COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + Config.COLUMN_STUDENT_NAME + " TEXT NOT NULL, "
//                + Config.COLUMN_STUDENT_REGISTRATION + " INTEGER NOT NULL UNIQUE, "
//                + Config.COLUMN_STUDENT_PHONE + " TEXT, " //nullable
//                + Config.COLUMN_STUDENT_EMAIL + " TEXT " //nullable
//                + ")";
//
//
//        db.execSQL(CREATE_STUDENT_TABLE);
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_STUDENT);
//
//        // Create tables again
//        onCreate(db);
//    }
//
//}
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.adaptive.exoplayer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "channels.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;
    private String TABLE = "iptv";
    private Context context;
    String TAG = "the category is";
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        //InputStream mInput = mContext.getAssets().open(DB_NAME);
        InputStream mInput = mContext.getResources().openRawResource(R.raw.sqlite);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }

    public List<Channel> getAllchannels(String param){
        String selection = "language =?";
        String[] selectionArgs = {param};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(TABLE, null, selection, selectionArgs, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.
             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Channel> studentList = new ArrayList<>();
                    do {
                        String image = cursor.getString(cursor.getColumnIndex("image"));
                        String contry = cursor.getString(cursor.getColumnIndex("contry"));
                        String language =  cursor.getString(cursor.getColumnIndex("language"));
                        String category =  cursor.getString(cursor.getColumnIndex("category"));
                        String name =  cursor.getString(cursor.getColumnIndex("name"));
                        String url =  cursor.getString(cursor.getColumnIndex("url"));
                        studentList.add(new Channel(image, contry, language, category, name, url));
                    }   while (cursor.moveToNext());

                    return studentList;
                }
        } catch (Exception e){
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }
    public List<Channel> getChannelfromCat(String param){
        Log.i(TAG, "getChannelfromCat: "+param);
        String selection = "category =?";
        String[] selectionArgs = {param};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = null;
        try {
            if (param=="All"){
                cursor = sqLiteDatabase.query(TABLE, null, null, null, null, null, null, "500");
            }
            else {
                cursor = sqLiteDatabase.query(TABLE, null, selection, selectionArgs, null, null, null, null);
            }


            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.
             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Channel> studentList = new ArrayList<>();
                    do {
                        String image = cursor.getString(cursor.getColumnIndex("image"));
                        String contry = cursor.getString(cursor.getColumnIndex("contry"));
                        String language =  cursor.getString(cursor.getColumnIndex("language"));
                        String category =  cursor.getString(cursor.getColumnIndex("category"));
                        String name =  cursor.getString(cursor.getColumnIndex("name"));
                        String url =  cursor.getString(cursor.getColumnIndex("url"));
                        studentList.add(new Channel(image, contry, language, category, name, url));
                    }   while (cursor.moveToNext());

                    return studentList;
                }
        } catch (Exception e){
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }
}