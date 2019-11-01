package com.counect.cube.ocrcomparejpa;

import com.counect.cube.ocrcomparejpa.domain.daservice.OcrCompare;
import com.counect.cube.ocrcomparejpa.repository.daservice.local.OcrCompareRepository;
import com.counect.cube.ocrcomparejpa.service.HDFSService;
import com.counect.cube.ocrcomparejpa.service.daservice.impl.DaserviceServiceImpl;
import com.counect.cube.ocrcomparejpa.template.HadoopTemplate;
import com.counect.cube.ocrcomparejpa.utils.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OcrcompareJpaApplicationTests {

    @Autowired
    HadoopTemplate hadoopTemplate;
    @Autowired
    HDFSService hdfsService;

    @Autowired
    OcrCompareRepository ocrCompareRepository;

    @Autowired
    DaserviceServiceImpl daserviceService;

    @Test
    public void contextLoads() {
        hdfsService.removeAllFile();
//        hadoopTemplate.uploadFile("F:\\hdfsTest\\ParserDir\\ms017000901\\2018-03-22\\000042137t0001_2018-03-22T10-22-17.bmp");
    }


    @Test
    public void contextLoads2() {
        hdfsService.getAllFile();
    }

    @Test
    public void contextLoads3() {
        hdfsService.getFileByDate("2019-08-20");
//        hdfsService.addFileByDateToHDFS("2018-03-22");

    }
    @Test
    public void contextLoads4() {
        hdfsService.addAllFileToHDFS();
//        hdfsService.addFileByDateToHDFS("2018-03-22");

    }

    @Test
    public void contextLoads5() {
        hdfsService.delDatFile();
    }
    @Test
    public void contextLoads7() {
        daserviceService.receiptSync();
    }

    @Test
    public void contextLoads6() {


        ExecutorService executorService = Executors.newFixedThreadPool(10);

        OcrCompare ocrCompare = new OcrCompare();
        ocrCompare.setText("");
        Example<OcrCompare> example = Example.of(ocrCompare);

        List<OcrCompare> all = ocrCompareRepository.findAll(example);
        System.out.println(all);
        for (OcrCompare receipt:all){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    daserviceService.analysisReceipt(receipt);
                }
            });
        }
        try {
            Thread.sleep(1000*60*60*2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
