package com.counect.cube.ocrcomparejpa.service.impl;

import com.counect.cube.ocrcomparejpa.service.HDFSService;
import com.counect.cube.ocrcomparejpa.template.HadoopTemplate;
import com.counect.cube.ocrcomparejpa.utils.ContainerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

@DependsOn("hadoopTemplate")
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
        Set<String> usedMsids = ContainerUtils.UsedMsids;

        File[] files = file.listFiles();
        for (File file1: files){
            if(file1.isDirectory() && file1.getName().contains("ms")){
                if(!usedMsids.isEmpty()){
                    if(!usedMsids.contains(file1.getName())){
                        log.info("不包含该 msid 尝试下一个目录");
                        continue;
                    }
                }
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

    /**
     *  文件太多了  限制如果执行这个方法则只搬运  2019-10-10 之后的
     * @return
     */
    @Override
    public boolean addAllFileToHDFS() {
        Set<String> usedMsids = ContainerUtils.UsedMsids;
        File file = new File(localRootFilePath);
        File[] files = file.listFiles();
        for (File file1: files){
            if(file1.isDirectory() && file1.getName().contains("ms")){
                if(!usedMsids.isEmpty()){
                    if(!usedMsids.contains(file1.getName())){
                        log.info("不包含该 msid 尝试下一个目录");
                        continue;
                    }
                }
                log.info("搬运 文件");
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
