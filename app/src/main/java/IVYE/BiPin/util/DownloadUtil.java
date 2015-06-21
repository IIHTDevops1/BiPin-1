package ivye.bipin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUtil {
    /**
     * 根據URL下載文件，前提是這個文件當中的內容是文本，函數的返回值就是文件當中的內容
     * 1.創建一個URL對象
     * 2.通過URL對象，創建一個HttpURLConnection對象
     * 3.得到InputStram
     * 4.從InputStream當中讀取數據
     */
    private URL url = null;

    public String downStr(URL urlStr) throws ConnectException//下載字元流的方法
    {
        /**
         * String和StringBuffer他們都可以存儲和操作字元串，即包含多個字元的字元串數據。
         * String類是字元串常量，是不可更改的常量。而StringBuffer是字元串變數，它的對象是可以擴充和修改的。
         */
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;//BufferedReader類用於從緩衝區中讀取內容

        try
        {
            /**
             * 因為直接使用InputStream不好用，多以嵌套了BufferedReader，這個是讀取字元流的固定格式。
             */

            url = new URL(urlStr.toString());// 創建一個URL對象
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();// 創建一個Http連接
            buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));// 使用IO流讀取數據

            while ((line = buffer.readLine()) != null)
            {
                sb.append(line);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                buffer.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * -1：代表下載文件出錯
     * 0：代表下載文件成功
     * 1：代表文件已經存在
     */
    public int downFile(URL urlStr, String path, String fileName) throws IOException//下載文件的方法
    {
        HttpURLConnection urlConnection = (HttpURLConnection) urlStr.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        urlConnection.connect();
        File file = new File(path+fileName);
        FileOutputStream fileOutput = new FileOutputStream(file);
        InputStream fileInput = null;
        fileInput = getInputStreamFromUrl(urlStr);
        byte[] buffer = new byte[1024];
        int bufferLength = 0;
        while ( (bufferLength = fileInput.read(buffer)) > 0 ) {
            fileOutput.write(buffer, 0, bufferLength);
        }
        fileOutput.close();
        return 0;
    }

    /**
     * 根據URL得到輸入流
     * @param urlStr
     */
    public InputStream getInputStreamFromUrl(URL urlStr) throws MalformedURLException, IOException
    {
        url = new URL(urlStr.toString());// 創建一個URL對象
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();// 創建一個Http連接
        InputStream inputStream = urlConn.getInputStream();//得到輸入流

        return inputStream;
    }
}
