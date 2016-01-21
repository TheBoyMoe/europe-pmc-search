package com.example.downloaderdemo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "journals.db";
    private static final int VERSION = 1;
    private static volatile DatabaseHelper DATABASE_HELPER = null;

    // TODO table columns
    static final String TABLE_NAME = "journals";
    static final String ARTICLE_ID = "articleId";
    static final String ARTICLE_TITLE = "title";
    static final String JOURNAL_TITLE = "journalTitle";
    static final String AUTHOR_STRING = "authorString";
    static final String JOURNAL_INFO = "journalInfo";
    static final String PAGE_INFO = "pageInfo";
    static final String ABSTRACT_TEXT = "abstractText";
    static final String KEYWORD_LIST = "keywordList";
    static final String VOLUME = "volume";
    static final String ISSUE = "issue";
    static final String YEAR_OF_PUBLICATION = "yearOfPublication";
    static final String CITED = "cited";


    // instantiate an instance of the dbase helper available to any activity/fragment
    synchronized static DatabaseHelper getInstance(Context context) {
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
                ARTICLE_ID + " TEXT," +
                ARTICLE_TITLE + " TEXT," +
                JOURNAL_TITLE + " TEXT," +
                AUTHOR_STRING + " TEXT," +
                JOURNAL_INFO + " TEXT," +
                PAGE_INFO + " TEXT," +
                ABSTRACT_TEXT + " TEXT," +
                KEYWORD_LIST + " TEXT," +
                VOLUME + " TEXT, " +
                ISSUE + " TEXT," +
                YEAR_OF_PUBLICATION + " TEXT," +
                CITED + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
