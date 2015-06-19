package ivye.bipin.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;


public class FileUtils {

    private String PATH = Environment.getExternalStorageDirectory() + "/BiPin/";

    public String getSDPATH(){
        return this.PATH;
    };

    public FileUtils() throws FileNotFoundException, IOException {
        this.PATH = Environment.getExternalStorageDirectory() + "/BiPin/";
    }

    /**
     * 在SD卡上創建文件
     */
    public File createFile(String fileName) throws IOException {
        File file = new File(PATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上創建目錄
     */
    public File createDir(String dirName){
        File dir = new File(PATH + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判斷SD卡上的文件夾是否存在
     */
    public boolean isFileExist(String fileName) {
        File file = new File(PATH + fileName);
        return file.exists();
    }

    /**
     * 判斷SD卡上的文件夾是否存在且刪除
     */
    public boolean isFileExistDelete(String fileName) {
        File file = new File(PATH + fileName);
        return file.delete();
    }

    /**
     * 將一個InputStream裡面的數據寫入到SD卡中
     */
    public File writeFileFromStream(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        //InputStream裡面的數據寫入到SD卡中的固定方法
        try {
            if (!isFileExist(path))
                createDir(path);
            file = createFile(fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
