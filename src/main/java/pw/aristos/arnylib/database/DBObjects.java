package pw.aristos.arnylib.database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public interface DBObjects<E> {
    ArrayList<E> getObjList(Context context, String query, String orderby);

    ArrayList<E> getCursorObjs(Cursor cursor);

    E getCursorObject(Cursor cursor, E obj);

    E getObject(Context context, String query);
}
