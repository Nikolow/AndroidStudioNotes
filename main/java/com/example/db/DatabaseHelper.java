package com.example.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper
{

    // Версия на базата данни
    private static final int DATABASE_VERSION = 1;

    // Името на базата данни
    private static final String DATABASE_NAME = "notes_db3";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Създаване на таблицата
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // създаване на таблицата
        db.execSQL(Note.CREATE_TABLE);
    }

    // При ъпдейт на базата данни
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Дропваме таблицата ако съществува
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // И я правим пак
        onCreate(db);
    }

    public long insertNote(String title, String note) // създаване на бележка в DB
    {
        // взимаме база данни за писане, понеже искаме да пишем данни в нея
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` и `timestamp` ще бъдат добавени автоматично и няма нужда да ги добавяме

        // добавяме само данните, които ни трябват (заглавие и текст на бележката)
        values.put(Note.COLUMN_TITLE, title);
        values.put(Note.COLUMN_NOTE, note);

        // правим инсърт с данните
        long id = db.insert(Note.TABLE_NAME, null, values);

        // затваряме кънекцията
        db.close();

        // връщаме ид-то на новия инсърт
        return id;
    }

    public Note getNote(long id) // взимане бележка (като обект)
    {
        // взимаме база данни за четене, понеже нямаме нищо за инсъртване
        SQLiteDatabase db = this.getReadableDatabase();

        // с курсор обхождаме бележките (SELECT) където е нашето ID
        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_TITLE, Note.COLUMN_NOTE, Note.COLUMN_TIMESTAMP},Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // подготвяме обекта с данните
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

        // затваряме връзката
        cursor.close();

        // връщаме обект
        return note;
    }

    public List<Note> getAllNotes() // взимане на всички бележки от BD като List от бележки
    {
        List<Note> notes = new ArrayList<>(); // лист от бележки

        // QUERY - SELECT *
        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " + Note.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase(); // за писане
        Cursor cursor = db.rawQuery(selectQuery, null); // курсор за резултатите

        // преминаваме през всички резултати като ги добавяме в листата
        if (cursor.moveToFirst())
        {
            do
            {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TITLE)));
                note.setNote(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

                notes.add(note); // добавяме поредната бележка
            }
            while (cursor.moveToNext());
        }

        // затваряме връзката
        db.close();

        // връщаме листа от бележки
        return notes;
    }

    public int getNotesCount() // функция която ни връща броя на всички бележки до момента
    {
        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // връщаме count
        return count;
    }

    public int updateNote(Note note) // функция за ъпдейт на бележката в DB
    {
        SQLiteDatabase db = this.getWritableDatabase(); // за записване

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_TITLE, note.getTitle());
        values.put(Note.COLUMN_NOTE, note.getNote());

        // ъпдейтваме данните
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) // функция за триене на бележката в DB
    {
        SQLiteDatabase db = this.getWritableDatabase(); // за записване
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())}); // трием където ID = на нашето
        db.close();
    }
}