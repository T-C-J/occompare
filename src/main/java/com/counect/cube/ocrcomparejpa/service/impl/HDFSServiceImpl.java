package com.counect.cube.ocrcomparejpa.service.impl;

import com.counect.cube.ocrcomparejpa.service.HDFSService;
import com.counect.cube.ocrcomparejpa.template.HadoopTemplate;
import com.counect.cube.ocrcomparejpa.utils.FileUtils;
import com.counect.cube.ocrcomparejpa.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DependsOn("hadoopTemplate")
@Service
@Slf4j
public class HDFSServiceImpl implements HDFSService {

    @Autowired
    HadoopTemplate hadoopTemplate;
    @Value("${hadoop.namespace:/}")
    private String nameSpace;
    @Value("${hadoop.namespace-root:/}")
    private String nameSpaceRoot;


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
    public boolean removeAllFile() {
        return hadoopTemplate.myDropHdfsPath(nameSpaceRoot);
    }

    @Override
    public boolean getFileByDate(String dateStr) {
        List<String> msDirList = hadoopTemplate.getMSDirList(nameSpace);
        log.info(JSONUtil.toJson(msDirList));
        for (String path : msDirList){
            String[] strs = path.split("/");
            String msid = strs[strs.length -1];
            String msidPath = FileUtils.createDir(localRootFilePath, msid);
            List<String> msDirList1 = hadoopTemplate.getDateDirList(path);
            for (String date : msDirList1){
                if(date.contains(dateStr)){
                    String[] strs2 = date.split("/");
                    String dat = strs2[strs2.length -1];
                    String dir = FileUtils.createDir(msidPath, dat);
                    hadoopTemplate.getFile(date,dir);
                    log.info("下载 " +msid + "日期为 : " + dateStr +"的水单成功");
                }
            }
        }
        FileUtils.clearErrorFile();
        return true;
    }

    @Override
    public boolean getAllFile() {
        hadoopTemplate.getFile(farRootFilePath,localRootFilePath);
        FileUtils.clearErrorFile();
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
    public boolean addFileToHDFS(String filePath) {
        return false;
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
                        .filter(file2 -> (file2.isDirectory() && file2.getName().contains("-") && Integer.valueOf(file2.getName().replaceAll("-",""))>20191016))
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

    @Override
    public Integer getFileNumber() {
        Integer number = hadoopTemplate.getFileNumber(new Path(nameSpace));
        return number;
    }


}
