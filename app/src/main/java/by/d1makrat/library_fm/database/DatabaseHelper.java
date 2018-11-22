package by.d1makrat.library_fm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {

    DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseWorker databaseWorker = new DatabaseWorker();
        databaseWorker.createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DatabaseWorker databaseWorker = new DatabaseWorker();
        databaseWorker.upgradeScrobblesTable(db);
    }
}
