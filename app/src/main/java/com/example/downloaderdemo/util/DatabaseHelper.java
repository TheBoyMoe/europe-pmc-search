package com.example.downloaderdemo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "journals.db";
    private static final int VERSION = 1;
    private static volatile DatabaseHelper DATABASE_HELPER = null;

    // table columns
    public static final String TABLE_NAME = "journals";
    public static final String ROW_ID = "_id";
    public static final String ARTICLE_ID = "articleId";
    public static final String ARTICLE_TITLE = "title";
    public static final String JOURNAL_TITLE = "journalTitle";
    public static final String AUTHOR_STRING = "authorString";
    public static final String PAGE_INFO = "pageInfo";
    public static final String ABSTRACT_TEXT = "abstractText";
    public static final String KEYWORD_LIST = "keywordList";
    public static final String VOLUME = "volume";
    public static final String ISSUE = "issue";
    public static final String YEAR_OF_PUBLICATION = "yearOfPublication";
    public static final String CITED = "cited";


    // instantiate an instance of the dbase helper available to any activity/fragment
    public synchronized static DatabaseHelper getInstance(Context context) {
        if(DATABASE_HELPER == null) {
            DATABASE_HELPER = new DatabaseHelper(context.getApplicationContext());
        }
        return DATABASE_HELPER;
    }


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement that creates dbase table
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ARTICLE_ID + " TEXT, " +
                ARTICLE_TITLE + " TEXT, " +
                JOURNAL_TITLE + " TEXT, " +
                AUTHOR_STRING + " TEXT, " +
                PAGE_INFO + " TEXT, " +
                ABSTRACT_TEXT + " TEXT, " +
                KEYWORD_LIST + " TEXT, " +
                VOLUME + " TEXT, " +
                ISSUE + " TEXT, " +
                YEAR_OF_PUBLICATION + " TEXT, " +
                CITED + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



}
