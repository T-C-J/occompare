package com.counect.cube.ocrcomparejpa.service.impl;

import com.counect.cube.ocrcomparejpa.service.HDFSService;
import com.counect.cube.ocrcomparejpa.template.HadoopTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class HDFSServiceImpl implements HDFSService {

    @Autowired
    HadoopTemplate hadoopTemplate;

    @Value("${hadoop.far-rootfile-path}")
    private String farRootFilePath;

    @Value("${hadoop.local-root-path}")
    private String localRootFilePath;

    @Override
    public boolean init() {
        hadoopTemplate.init();
        return true;
    }

    @Override
    public boolean addFileByDateToHDFS(String dateStr) {
        File file = new File(localRootFilePath);

        File[] files = file.listFiles();
        for (File file1: files){
            if(file1.isDirectory() && file1.getName().contains("ms")){
                String msid = file1.getName();
                hadoopTemplate.existDir(farRootFilePath + "/" + msid,true);
//                log.info("create path "+ farRootFilePath + "/" + msid);
                Iterator<File> iterator = Arrays.stream(file1.listFiles())
                        .filter(file2 -> file2.isDirectory() && file2.getName().equals(dateStr))
                        .iterator();
                if(iterator.hasNext())
                    hadoopTemplate.existDir(farRootFilePath + "/" + msid + "/" + dateStr,true);
//                log.info("create path "+ farRootFilePath + "/"+ msid + "/" + dateStr);
                while (iterator.hasNext()){
                    File datFile = iterator.next();
                    Iterator<File> receipts = Arrays.stream(datFile.listFiles()).iterator();
                    while (receipts.hasNext()){
                        File datOrBmp = receipts.next();
//                        log.info("copy file from " +datOrBmp.getAbsolutePath()+ " ---> "+farRootFilePath + File.separator + msid + File.separator + dateStr);
                        hadoopTemplate.uploadFile(datOrBmp.getAbsolutePath(),farRootFilePath + "/" + msid + "/" + dateStr);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean addAllFileToHDFS() {
        File file = new File(localRootFilePath);
        File[] files = file.listFiles();
        for (File file1: files){
            if(file1.isDirectory() && file1.getName().contains("ms")){
                String msid = file1.getName();
                hadoopTemplate.existDir(farRootFilePath + "/" + msid,true);
//                log.info("create path "+ farRootFilePath + "/" + msid);
                Iterator<File> iterator = Arrays.stream(file1.listFiles())
                        .filter(file2 -> (file2.isDirectory() && file2.getName().contains("-") && Integer.valueOf(file2.getName().replaceAll("-",""))>20191010))
                        .iterator();
                while (iterator.hasNext()){
                    File datFile = iterator.next();
                    hadoopTemplate.existDir(farRootFilePath + "/" + msid + "/" + datFile.getName(),true);
                    Iterator<File> receipts = Arrays.stream(datFile.listFiles()).iterator();
                    while (receipts.hasNext()){
                        File datOrBmp = receipts.next();
//                        log.info("copy file from " +datOrBmp.getAbsolutePath()+ " ---> "+farRootFilePath + "/" + msid + "/" + datFile.getName());
                        hadoopTemplate.uploadFile(datOrBmp.getAbsolutePath(),farRootFilePath + "/" + msid + "/" + datFile.getName());
                    }
                }
            }
        }
        return true;
    }

}
