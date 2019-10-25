package com.counect.cube.ocrcomparejpa;

import com.counect.cube.ocrcomparejpa.service.HDFSService;
import com.counect.cube.ocrcomparejpa.template.HadoopTemplate;
import com.counect.cube.ocrcomparejpa.utils.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OcrcompareJpaApplicationTests {

    @Autowired
    HadoopTemplate hadoopTemplate;
    @Autowired
    HDFSService hdfsService;

    @Test
    public void contextLoads() {
        hdfsService.removeAllFile();
//        hadoopTemplate.uploadFile("F:\\hdfsTest\\ParserDir\\ms017000901\\2018-03-22\\000042137t0001_2018-03-22T10-22-17.bmp");
    }


    @Test
    public void contextLoads2() {
        System.setProperty("hadoop.home.dir", "F:\\hadoop\\hadoop-2.7.7");
        hadoopTemplate.getFile("/counect","C:\\Users\\TCJ\\Desktop\\test123");
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
        FileUtils.clearErrorFile();
    }

    @Test
    public void contextLoads6() {
        hdfsService.getAllFile();
    }

}
