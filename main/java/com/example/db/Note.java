package com.example.db;

public class Note {
    public static final String TABLE_NAME = "notes"; // име на таблицата
    public static final String COLUMN_ID = "id"; // колона ид
    public static final String COLUMN_TITLE = "title"; // колона залгавие
    public static final String COLUMN_NOTE = "note"; // колона за текста на бележката
    public static final String COLUMN_TIMESTAMP = "timestamp"; // колона за времето

    private int id;
    private String title;
    private String note;
    private String timestamp;


    // SQL Query-то за създаване на BD
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Note() { } // default

    public Note(int id, String title, String note, String timestamp) // конструктора за създаване на нова бележка с данни
    {
        this.id = id;
        this.title = title;
        this.note = note;
        this.timestamp = timestamp;
    }

    public int getId()
    {
        return id;
    } // взима ид-то

    public String getTitle()
    {
        return title;
    } // взима заглавието

    public String getNote()
    {
        return note;
    } // взима текста

    public void setTitle(String title)
    {
        this.title = title;
    } // сетва заглавие

    public void setNote(String note)
    {
        this.note = note;
    } // сетва текста

    public String getTimestamp()
    {
        return timestamp;
    } // взема времето

    public void setId(int id)
    {
        this.id = id;
    } // сетва ид-то

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    } // сетва времето
}
