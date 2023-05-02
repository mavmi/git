package mavmi.git.utils;

import java.io.File;

public abstract class Utils {
    public static boolean isLineEmpty(String line){
        if (line.length() == 0) return true;
        for (int i = 0; i < line.length(); i++){
            if (!Character.isSpaceChar(line.charAt(i))) return false;
        }
        return true;
    }
    public static void deleteDir(String dirPath){
        File file = new File(dirPath);
        if (!file.isDirectory()) return;
        File[] files = file.listFiles();
        if (files == null) return;
        for (File f : files){
            if (f.isDirectory()) deleteDir(f.getPath());
            f.delete();
        }
        file.delete();
    }
}
