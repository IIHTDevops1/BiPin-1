package ivye.bipin.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vongola on 2015/6/13.
 */
public class ItemPower {
    // 表格名稱
    public static final String TABLE_NAME = "power";

    // 資料庫物件
    private static SQLiteDatabase db;


    // 建構子，一般的應用都不需要修改
    public ItemPower(Context context) {

    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    public Map<String, String> findPowerByScore(DBHelper dbh, String Category) {
        Map<String, String> result = new HashMap<String, String>();
        db = dbh.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "Score=?", new String[]{Category}, null, null, null);
        int i = cursor.getColumnCount();
        while (cursor.moveToNext()) {
            for(int j = 0; j<i; j++){
                result.put(cursor.getColumnName(j), cursor.getString(j));
            }
        }
        cursor.close();
        return result;
    }

    public static HashMap<String, String> findPowerByPrice(DBHelper dbh, int starand, int offect) {
        HashMap<String, String> result = new HashMap<String, String>();
        db = dbh.getReadableDatabase();
        Integer min = starand-offect;
        if (min < 0){
            min = 0;
        }
        Integer max = starand+offect;
        Cursor cursor = db.query(TABLE_NAME, null, "Price>? and Price<?", new String[]{new Integer(min).toString(), new Integer(max).toString()}, null, null, null);
        int i = cursor.getColumnCount();
        while (cursor.moveToNext()) {
            for(int j = 0; j<i; j++){
                result.put(cursor.getColumnName(j), cursor.getString(j));
            }
        }
        cursor.close();
        return result;
    }

}
