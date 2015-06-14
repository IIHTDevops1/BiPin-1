package ivye.bipin.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Created by Vongola on 2015/6/13.
 */
public class DBHelper extends SQLiteOpenHelper {
    // 資料庫名稱
    public static final String DATABASE_NAME = "BiPin.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;
    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory()+"BiPin/"+DATABASE_NAME, null, 0);

    public static DBHelper instance = null;


    public static void initialize(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
    }

    // 建構子，在一般的應用都不需要修改
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立應用程式需要的表格
        // 待會再回來完成它
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 刪除原有的表格
        // 待會再回來完成它

        // 呼叫onCreate建立新版的表格
        onCreate(db);
    }
}
