package pw.aristos.arnylib.database;

import android.content.ContentValues;
import android.content.Context;

public interface DBObject {
    ContentValues getObjectValues();
    boolean removeObj(Context context);
    boolean addObj(Context context);
    boolean updateObg(Context context);
}
