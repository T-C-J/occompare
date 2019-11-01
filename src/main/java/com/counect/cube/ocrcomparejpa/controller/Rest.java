package com.counect.cube.ocrcomparejpa.controller;

import com.alibaba.fastjson.JSONArray;
import com.counect.cube.ocrcomparejpa.service.HDFSService;
import com.counect.cube.ocrcomparejpa.utils.ContainerUtils;
import com.counect.cube.ocrcomparejpa.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class Rest {

    @Autowired
    HDFSService hdfsService;

    @RequestMapping("hello")
    public Set<String> Hello(){
        return ContainerUtils.UsedMsids;
    }


//    @RequestMapping("syncMsid")
//    public String syncMsid(String body){
//        Set<String> usedMsids = new HashSet<>();
//        JSONArray jsonArray = JSONUtil.ArraytoJSONArray(body);
//        Set<Object> collect = jsonArray.stream().collect(Collectors.toSet());
//        for(Object object :collect){
//            usedMsids.add((String)object);
//        }
//        ContainerUtils.UsedMsids = usedMsids;
//        log.info(JSONUtil.toJson(collect));
//        return "123";
//    }

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

    @RequestMapping("addDayReceippt")
    public Boolean addYestdayReceipt(){
        return hdfsService.addLastDayFileToHDFS();
    }


    @RequestMapping("addReceiptAgain")
    public Boolean s(){
        hdfsService.addFileByDateToHDFS("2019-10-18");
        hdfsService.addFileByDateToHDFS("2019-10-19");
        hdfsService.addFileByDateToHDFS("2019-10-20");
        hdfsService.addFileByDateToHDFS("2019-10-21");
        hdfsService.addFileByDateToHDFS("2019-10-22");
        hdfsService.addFileByDateToHDFS("2019-10-23");
        hdfsService.addFileByDateToHDFS("2019-10-24");
        hdfsService.addFileByDateToHDFS("2019-10-25");
        return true;
    }


}
