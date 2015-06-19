package ivye.bipin.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vongola on 2015/6/13.
 */
public class ItemCPU {
    // 表格名稱
    public static final String TABLE_NAME = "cpu";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String COMPANY_COLUMN = "Company";
    public static final String NAME_COLUMN = "Name";
    public static final String CLOCK_COLUMN = "Clock";
    public static final String CORE_COLUMN = "Core";
    public static final String THREAD_COLUMN = "Thread";
    public static final String CACHE_COLUMN = "Cache";
    public static final String TDP_COLUMN = "TDP";
    public static final String PRICE_COLUMN = "Price";
    public static final String SCORE_COLUMN = "Score";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COMPANY_COLUMN + "TEXT NOT NULL, " +
                    NAME_COLUMN + " TEXT NOT NULL, " +
                    CLOCK_COLUMN + " REAL NOT NULL, " +
                    CORE_COLUMN + "  INTEGER NOT NULL, " +
                    THREAD_COLUMN + "  INTEGER NOT NULL, " +
                    CACHE_COLUMN + "  INTEGER NOT NULL, " +
                    TDP_COLUMN + "  INTEGER NOT NULL, " +
                    PRICE_COLUMN + "  INTEGER NOT NULL, " +
                    SCORE_COLUMN + " INTEGER NOT NULL)";

    // 資料庫物件
    private static SQLiteDatabase db;


    // 建構子，一般的應用都不需要修改
    public ItemCPU(Context context) {

    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    public static Map<String, String> findCPUByScore(DBHelper dbh, String Category) {
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

    public static HashMap<String, String> findCPUByPrice(DBHelper dbh, int starand) throws NullPointerException, CursorIndexOutOfBoundsException{
        HashMap<String, String> result = new HashMap<String, String>();
        db = dbh.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "Price>0 and Price<?", new String[]{String.valueOf(starand)}, null, null, null);
        int i = cursor.getColumnCount();
        cursor.moveToLast();
        for(int j = 0; j<i; j++){
            result.put(cursor.getColumnName(j), cursor.getString(j));
        }
        cursor.close();
        return result;
    }

}
