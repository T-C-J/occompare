package com.counect.cube.ocrcomparejpa.utils;

import java.io.File;
import java.util.Arrays;

public class FileUtils {

    public static String createDir(String root,String dirName){
        File rootPath = new File(root);
        if(!rootPath.exists()){
            rootPath.mkdirs();
        }
        if(rootPath.isFile()){
            throw new RuntimeException("本地根目录为文件 :"+ rootPath);
        }

        File dir = new File(root + File.separator + dirName);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir.getPath();
    }

    public static boolean clearDir(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                clearDir(f);
            }
        }
        return file.delete();
    }


    public static boolean clearErrorFile() {
        String path = "/data/data_3D/NFS_ts2/ParserDir_mili";
        File file = new File(path);
        Arrays.stream(file.listFiles()).filter(file1 -> file1.isDirectory())
                .forEach(
                        (file1) -> {
                            Arrays.stream(file1.listFiles()).filter(file2 -> file2.getName().contains("-") && file2.isDirectory())
                            .forEach(
                                    file2 -> Arrays.stream(file2.listFiles()).filter(file3 -> file3.getName().endsWith(".crc")).forEach(file3 -> file3.delete())
                            );}
                );
        return true;
    }
}
