package ivye.bipin.database;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ivye.bipin.util.DownloadUtil;

/**
 * Created by Vongola on 2015/6/13.
 */
public class UpdateHelper extends Thread{

    public static void checkUpdate() throws IOException, InterruptedException {
        final URL url = new URL("http://crux.coder.tw/vongola12324/BiPin/lastupdate.time");
        final DownloadUtil downloadHelper = new DownloadUtil();
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String timestamp = downloadHelper.downStr(url);
                File nowTS = new File(Environment.getExternalStorageDirectory()+"/BiPin/timestamp");
                if (nowTS.exists()){
                    BufferedReader br = null;
                    String nowTimestamp = null;
                    try {
                        br = new BufferedReader(new FileReader(nowTS));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        nowTimestamp = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (nowTimestamp.charAt(nowTimestamp.length()-1) == '\n'){
                        nowTimestamp = nowTimestamp.substring(0, nowTimestamp.length()-2);
                    }
                    if (new Integer(nowTimestamp) < new Integer(timestamp)){
                        Log.d("BiPin", "刪除舊有檔案");
                        nowTS.delete();
                        try {
                            nowTS.createNewFile();
                            PrintWriter writer = new PrintWriter(Environment.getExternalStorageDirectory()+"/BiPin/timestamp");
                            writer.write(timestamp);
                            writer.close();
                            getUpdate(timestamp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("BiPin", "無需更新！");
                    }

                } else {
                    try {
                        nowTS.createNewFile();
                        PrintWriter writer = new PrintWriter(Environment.getExternalStorageDirectory()+"/BiPin/timestamp");
                        writer.write(timestamp);
                        writer.close();
                        getUpdate(timestamp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        newThread.start();
        newThread.join();
    }

    public static void getUpdate(final String ut) throws IOException, InterruptedException {
        final URL url = new URL("http://crux.coder.tw/vongola12324/BiPin/Update/" + ut + ".db");
        final DownloadUtil downloadHelper = new DownloadUtil();

        Log.d("BiPin", "開始更新資料褲......");
        downloadHelper.downFile(url, Environment.getExternalStorageDirectory() + "/BiPin/", "BiPin.db");
        Log.d("BiPin", "下載完成!");
    }

}