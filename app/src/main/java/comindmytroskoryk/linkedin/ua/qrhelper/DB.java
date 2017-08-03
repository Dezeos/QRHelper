package comindmytroskoryk.linkedin.ua.qrhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dem on 31.07.2017.
 */
public class DB {

    DBHelper dbHelper;
    Context context;
    Cursor cursor;
    SQLiteDatabase db;
    List<History> mHistorysList;

    public DB(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    private static final String LOG_TAG = "my_tag";

    // метод для удаления строки по id
    public void deleteDB () {

        db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME , null , null);

    }

    // метод для вставки данных
    public void insertDATA(History history){

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_DATE, history.getDate());
        cv.put(DBHelper.KEY_TEXT, history.getText());
        db.insert(DBHelper.TABLE_NAME, null, cv);

    }








    // метод возвращающий коллекцию всех данных
    public List<History> getHistorys() {

        cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        mHistorysList = new ArrayList<History>();

        if (cursor.moveToFirst()) {

            int dateColId = cursor.getColumnIndex(DBHelper.KEY_DATE);
            int textColId = cursor.getColumnIndex(DBHelper.KEY_TEXT);

            do {

                History History = new History( cursor.getString(dateColId), cursor.getString(textColId));
                mHistorysList.add(History);

            } while (cursor.moveToNext());

        } else {
            Log.d(LOG_TAG, "В базе нет данных!");
        }

        cursor.close();

        return mHistorysList;

    }

    // здесь закрываем все соединения с базой и класс-помощник
    public void closeDB() {
        dbHelper.close();
        db.close();
    }
}
