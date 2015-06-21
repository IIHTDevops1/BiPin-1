package ivye.bipin.database;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import ivye.bipin.util.DownloadUtil;

/**
 * Created by Vongola on 2015/6/13.
 */
public class UpdateHelper extends Thread{

    static boolean success = false;

    public static boolean checkUpdate() throws IOException, InterruptedException, ConnectException {
        Log.d("BiPin", "檢查更新");
        final URL url = new URL("http://crux.coder.tw/vongola12324/BiPin/lastupdate.time");
        final DownloadUtil downloadHelper = new DownloadUtil();
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 檢查本地檔案
                File TS = new File(Environment.getExternalStorageDirectory()+"/BiPin/timestamp");
                File DB = new File(Environment.getExternalStorageDirectory()+"/BiPin/BiPin.db");
                BufferedReader br = null;
                String getLine = null;
                if (TS.exists()) {
                    try {
                        br = new BufferedReader(new FileReader(TS));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert br != null;
                        getLine = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (getLine == null || getLine == "") {
                        if (TS.exists())
                            TS.delete();
                        if (DB.exists())
                            DB.delete();
                        getLine = null;
                    }

                }


                // 下載時間戳
                String timestamp = null;
                boolean needUpdate = false;
                try {
                    timestamp = downloadHelper.downStr(url);
                } catch (ConnectException e) {
                    e.printStackTrace();
                    return;
                }
                if (timestamp == null || timestamp == "") {
                    // 下載失敗
                    needUpdate = false;
                } else {
                    // 下載成功
                    if (getLine != null) {
                        // 表示有找到本地端時戳
                        if (new Integer(getLine) < new Integer(timestamp))
                            needUpdate = true;
                        else
                            needUpdate = false;
                    } else {
                        needUpdate = true;
                    }
                }

                // 下載資料庫
                String localMD5 = null;
                String remoteMD5 = null;
                boolean downloadSuccessfully = false;
                if (needUpdate) {
                    try {
                        getUpdate(timestamp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // 檢驗下載檔案
                    MessageDigest mdEnc = null;
                    try {
                        mdEnc = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    InputStream is = null;
                    try {
                        is = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/BiPin/BiPin.db"));
                    } catch (FileNotFoundException e) {
                        Log.e("BiPin", "Exception while getting FileInputStream", e);
                        e.printStackTrace();
                    }
                    byte[] buffer = new byte[8192];
                    int read;
                    try {
                        assert is != null;
                        while ((read = is.read(buffer)) > 0) {
                            assert mdEnc != null;
                            mdEnc.update(buffer, 0, read);
                        }
                        assert mdEnc != null;
                        byte[] md5sum = mdEnc.digest();
                        BigInteger bigInt = new BigInteger(1, md5sum);
                        localMD5 = bigInt.toString(16);
                        // Fill to 32 chars
                        localMD5 = String.format("%32s", localMD5).replace(' ', '0');
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to process file for MD5", e);
                    } finally {
                        try {
                            assert is != null;
                            is.close();
                        } catch (IOException e) {
                            Log.e("BiPin", "Exception on closing MD5 input stream", e);
                        }
                    }
                    try {
                        remoteMD5 = downloadHelper.downStr(new URL("http://crux.coder.tw/vongola12324/BiPin/Update/" + timestamp + ".md5"));
                    } catch (ConnectException | MalformedURLException e) {
                        e.printStackTrace();
                    }

                    if (remoteMD5 == null || remoteMD5.equals("") || localMD5.equals("") || !remoteMD5.equals(localMD5)) {
                        DB.delete();
                        downloadSuccessfully = false;
                    } else {
                        downloadSuccessfully = true;
                    }
                }

                // 更新時間戳
                PrintWriter writer = null;
                if (downloadSuccessfully) {
                    if (TS.exists())
                        TS.delete();
                    try {
                        TS.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        writer = new PrintWriter(TS);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    assert writer != null;
                    writer.write(timestamp);
                    writer.close();

                }
                passStatus(downloadSuccessfully);

            }
        });
        newThread.start();
        newThread.join();
        return success;
    }

    public static void getUpdate(final String ut) throws IOException, InterruptedException {
        final URL url = new URL("http://crux.coder.tw/vongola12324/BiPin/Update/" + ut + ".db");
        final DownloadUtil downloadHelper = new DownloadUtil();

        Log.d("BiPin", "開始更新資料褲......");
        downloadHelper.downFile(url, Environment.getExternalStorageDirectory() + "/BiPin/", "BiPin.db");
        Log.d("BiPin", "下載完成!");
    }


    public static void passStatus(boolean newSuccess){
        success = newSuccess;
    }
}