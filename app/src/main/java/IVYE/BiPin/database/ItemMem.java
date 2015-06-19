package ivye.bipin.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

/**
 * Created by Vongola on 2015/6/13.
 */
public class ItemMem {
    // 表格名稱
    public static final String TABLE_NAME = "mem";

    // 資料庫物件
    private static SQLiteDatabase db;


    // 建構子，一般的應用都不需要修改
    public ItemMem(Context context) {

    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    public static HashMap<String, String> findMemByPrice(DBHelper dbh, int starand) throws NullPointerException, CursorIndexOutOfBoundsException {
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
