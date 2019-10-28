package com.counect.cube.ocrcomparejpa.task;

import com.counect.cube.ocrcomparejpa.service.HDFSService;
import com.counect.cube.ocrcomparejpa.service.SyncDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class FileSystemTask {
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    SyncDataService syncDataService;
    @Autowired
    HDFSService hdfsService;

    public static Boolean flag = false;

    /**
     *  每天一点执行
     */
//    @Scheduled(cron = "0 0 1 * * ? ")
    public void pushFile(){
        flag = false;
        String date = getDate();
        boolean b = syncDataService.addFileByDate(date);
        if(b){
            flag = true;
            log.info("同步日期为" + date + "的水单文件成功");
        }else{
            flag = false;
            log.error("定时任务 push 文件到filesystem失败");
        }
    }




    public String getDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        Date time = calendar.getTime();
        String format = df.format(time);
        return format;
    }


    /**
     *  每天两点执行
     */
//    @Scheduled(cron = "0 0 2 * * ? ")
    public void pullFile(){
        String date = getDate();
        if(flag == false){
            log.info("文件上传失败");
            boolean b = syncDataService.addFileByDate(date);
            try {
                Thread.sleep(1000*60*20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("休眠 20 分钟后下载水单");
        }else{
            boolean complete = true;
            int i = 2;
            do{
                complete = pushFileComplete();
                try {
                    Thread.sleep(1000*60*5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
                if(!complete){
                    log.info("文件 push 暂未完成，休眠五分钟后再试一次");
                    if(i < 1){
                        log.info("停止等待，直接同步文件");
                        complete = true;
                    }
                }
            }while (!complete);
        }

        boolean fileByDate = hdfsService.getFileByDate(date);
        String f = fileByDate==true?"成功":"失败";
        log.info("文件下载" + f);
    }


    private boolean pushFileComplete(){
        Integer fileNumber = hdfsService.getFileNumber();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("文件系统共有 " + fileNumber + "个文件");
        if(hdfsService.getFileNumber().compareTo(fileNumber) == 0){
            return true;
        }
        return false;
    }

//    @Scheduled(cron = "0 0/5 * * * ? ")
    public void test(){
        log.info("=================== test task start ====================");
        log.info("123-1-=1-2=-12=-=");
        log.info("123-1-=1-2=-12=-=");
        log.info("123-1-=1-2=-12=-=");
        log.info("123-1-=1-2=-12=-=");
        log.info("=================== test task end ====================");
    }

}
