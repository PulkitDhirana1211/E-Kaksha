package com.example.ekaksha.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.core.os.LocaleListCompat.create;

public class JoinClassHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;

     public static String user;
     public  String SQL_CREATE_TABLE;
     public String SQL_DELETE_TABLE;
    public  String SQL_CREATE_TABLE_ASSIGNMENT;
    public String SQL_DELETE_TABLE_ASSIGNMENT;
    public  String SQL_CREATE_TABLE_EXAMINATION;
    public String SQL_DELETE_TABLE_EXAMINATION;
    public  String SQL_CREATE_TABLE_MESSAGE;
    public String SQL_DELETE_TABLE_MESSAGE;



    public JoinClassHelper(@Nullable Context context,String name) {
        super(context, name, null, DATABASE_VERSION);
          user=name;
         SQL_CREATE_TABLE="CREATE TABLE "+ ClassroomContract.joined.JOINED_TABLE_NAME+ user +" (" +
                ClassroomContract.joined._ID+" INTEGER PRIMARY KEY,"+
                ClassroomContract.joined.COLUMN_CLASSROOM_ID+ " TEXT,"+
                ClassroomContract.joined.COLUMN_NAME+ " TEXT,"+
                ClassroomContract.joined.COLUMN_DESCRIPTION+ " TEXT,"+
                ClassroomContract.joined.COLUMN_TEACHER+" TEXT"+
                " )";
           SQL_DELETE_TABLE="DROP TABLE IF EXISTS "+ClassroomContract.joined.JOINED_TABLE_NAME+user;
        SQL_CREATE_TABLE_ASSIGNMENT="CREATE TABLE "+ ClassroomContract.AssignmentList.JOINED_TABLE_NAME+ user +" (" +
                ClassroomContract.AssignmentList._ID+" INTEGER PRIMARY KEY,"+
                ClassroomContract.AssignmentList.COLUMN_CLASSROOM_ID+ " TEXT,"+
                ClassroomContract.AssignmentList.COLUMN_ASSIGNMENT_ID+ " TEXT,"+
                ClassroomContract.AssignmentList.COLUMN_CLASSROOM_NAME+ " TEXT,"+
                ClassroomContract.AssignmentList.COLUMN_NAME+ " TEXT,"+
                ClassroomContract.AssignmentList.COLUMN_DESCRIPTION+ " TEXT,"+
                ClassroomContract.AssignmentList.COLUMN_DEADLINE+" TEXT,"+
                ClassroomContract.AssignmentList.COLUMN_URL+" TEXT"+
                " )";
        SQL_DELETE_TABLE_ASSIGNMENT="DROP TABLE IF EXISTS "+ClassroomContract.AssignmentList.JOINED_TABLE_NAME+user;

        SQL_CREATE_TABLE_EXAMINATION="CREATE TABLE "+ ClassroomContract.ExaminationList.JOINED_TABLE_NAME+ user +" (" +
                ClassroomContract.ExaminationList._ID+" INTEGER PRIMARY KEY,"+
                ClassroomContract.ExaminationList.COLUMN_CLASSROOM_ID+ " TEXT,"+
                ClassroomContract.ExaminationList.COLUMN_CLASSROOM_NAME+ " TEXT,"+
                ClassroomContract.ExaminationList.COLUMN_NAME+ " TEXT,"+
                ClassroomContract.ExaminationList.COLUMN_DESCRIPTION+ " TEXT,"+
                ClassroomContract.ExaminationList.COLUMN_URL+" TEXT,"+
                ClassroomContract.ExaminationList.COLUMN_START+" TEXT,"+
                ClassroomContract.ExaminationList.COLUMN_END+" TEXT,"+
                ClassroomContract.ExaminationList.COLUMN_MAXIMUM_MARKS+" INTEGER"+
                " )";
        SQL_DELETE_TABLE_EXAMINATION="DROP TABLE IF EXISTS "+ClassroomContract.ExaminationList.JOINED_TABLE_NAME+user;
        SQL_CREATE_TABLE_MESSAGE="CREATE TABLE "+ ClassroomContract.MessageList.TABLE_NAME+ user +" (" +
                ClassroomContract.MessageList._ID+" INTEGER PRIMARY KEY,"+
                ClassroomContract.MessageList.COLUMN_CLASSROOM_ID+ " TEXT,"+
                ClassroomContract.MessageList.COLUMN_MESSAGE_ID+ " TEXT,"+
                ClassroomContract.MessageList.COLUMN_NAME+ " TEXT,"+
                ClassroomContract.MessageList.COLUMN_URL+" TEXT,"+
                ClassroomContract.MessageList.COLUMN_message+" TEXT,"+
                ClassroomContract.MessageList.COLUMN_TIME+" INTEGER"+
                " )";
        SQL_DELETE_TABLE_MESSAGE="DROP TABLE IF EXISTS "+ClassroomContract.MessageList.TABLE_NAME+user;



        Log.v(user,SQL_CREATE_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ASSIGNMENT);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_EXAMINATION);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MESSAGE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_ASSIGNMENT);
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_EXAMINATION);
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_MESSAGE);
        onCreate(sqLiteDatabase);

    }
}
