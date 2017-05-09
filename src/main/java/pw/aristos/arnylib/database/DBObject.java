package pw.aristos.arnylib.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public interface DBObject {
    ContentValues getObjectValues();
    boolean dbRemoveObj(Context context);
    boolean dbAddObj(Context context);
    boolean dbUpdateObg(Context context);
    void setCursorValues(Cursor cursor);
}
