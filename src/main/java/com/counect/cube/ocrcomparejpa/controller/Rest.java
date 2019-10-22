package com.counect.cube.ocrcomparejpa.controller;

import com.counect.cube.ocrcomparejpa.service.HDFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class Rest {

    @Autowired
    HDFSService hdfsService;

    @RequestMapping("hello")
    public String Hello(){
        return"hello";
    }

    @RequestMapping("init")
    public Boolean init(){
        return hdfsService.init();
    }
    @RequestMapping("addAll")
    public Boolean addAllReceipt(){
        return hdfsService.addAllFileToHDFS();
    }
    @RequestMapping("addByDate/{date}")
    public Boolean addAllReceipt(@PathVariable("date") String date){
        return hdfsService.addFileByDateToHDFS(date);
    }
}
