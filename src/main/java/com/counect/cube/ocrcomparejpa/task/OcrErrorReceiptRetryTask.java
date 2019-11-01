package com.counect.cube.ocrcomparejpa.task;

import com.counect.cube.ocrcomparejpa.domain.daservice.OcrCompare;
import com.counect.cube.ocrcomparejpa.repository.daservice.local.OcrCompareRepository;
import com.counect.cube.ocrcomparejpa.service.daservice.impl.DaserviceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class OcrErrorReceiptRetryTask {
    @Autowired
    OcrCompareRepository ocrCompareRepository;
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    DaserviceServiceImpl daserviceService;

    @Scheduled(cron = "0 0 0/2 * * ? ")
    public void run(){
        log.info("================================= 定时重跑ocr解析失败的水单 =================================");
        OcrCompare ocrCompare = new OcrCompare();
        ocrCompare.setText("");
        Example<OcrCompare> example = Example.of(ocrCompare);
        List<OcrCompare> all = ocrCompareRepository.findAll(example);
        for (OcrCompare receipt:all){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    daserviceService.analysisReceipt(receipt);
                }
            });
        }
        log.info("================================= END TASK =================================");
    }

}
