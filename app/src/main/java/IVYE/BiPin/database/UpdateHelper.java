package ivye.bipin.database;

import android.os.Environment;
import android.provider.MediaStore;
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

    public static void checkUpdate() throws IOException {
        final URL url = new URL("http://crux.coder.tw/vongola12324/BiPin/lastupdate.time");
        final DownloadUtil downloadHelper = new DownloadUtil();
        new Thread(new Runnable() {
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
                        nowTS.delete();
                        try {
                            nowTS.createNewFile();
                            PrintWriter writer = new PrintWriter(Environment.getExternalStorageDirectory()+"/BiPin/timestamp");
                            writer.write(timestamp);
                            writer.close();
                            getUpdate(timestamp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                    }
                }
            }
        }).start();
    }

    public static void getUpdate(final String ut) throws MalformedURLException {
        final URL url = new URL("http://crux.coder.tw/vongola12324/BiPin/Update/" + ut + ".db");
        final DownloadUtil downloadHelper = new DownloadUtil();
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloadHelper.downFile(url, Environment.getExternalStorageDirectory()+"/BiPin/", "BiPin.db");
            }
        }).start();
    }


    public static void forceUpdate(){
        try {
            URL url = new URL("http://crux.coder.tw/vongola12324/BiPin/Update/1434296262.db");
            //create the new connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //set up some things on the connection
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //and connect!
            urlConnection.connect();

            //set the path where we want to save the file
            //in this case, going to save it on the root directory of the
            //sd card.
            File SDCardRoot = Environment.getExternalStorageDirectory();
            Log.d("BiPin", "SDCard Path: " + SDCardRoot);
            //create a new file, specifying the path, and the filename
            //which we want to save the file as.
            File file = new File(SDCardRoot, "/BiPin/BiPin.update.db");
            //this will be used to write the downloaded data into the file we created
            FileOutputStream fileOutput = new FileOutputStream(file);

            //this will be used in reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file
            int totalSize = urlConnection.getContentLength();
            Log.d("Test", "File size: " + totalSize);
            //variable to store total downloaded bytes
            int downloadedSize = 0;

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0; //used to store a temporary size of the buffer

            //now, read through the input buffer and write the contents to the file
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                //add the data in the buffer to the file in the file output stream (the file on the sd card
                fileOutput.write(buffer, 0, bufferLength);
                //add up the size so we know how much is downloaded
                downloadedSize += bufferLength;
                //this is where you would do something to report the prgress, like this maybe
                //updateProgress(downloadedSize, totalSize);
                //Log.d("Test", "Download size: "+downloadedSize);

            }
            //close the output stream when done
            fileOutput.close();
            File nfile = new File(SDCardRoot, "/BiPin/BiPin.db");
            file.renameTo(nfile);

            //catch some possible errors...
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}