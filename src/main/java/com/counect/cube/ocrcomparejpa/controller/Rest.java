package com.counect.cube.ocrcomparejpa.controller;

import com.counect.cube.ocrcomparejpa.entity.CallDaserviceReq;
import com.counect.cube.ocrcomparejpa.service.HDFSService;
import com.counect.cube.ocrcomparejpa.service.SyncDataService;
import com.counect.cube.ocrcomparejpa.service.daservice.DaserviceService;
import com.counect.cube.ocrcomparejpa.service.dps.DpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Rest {

    @Autowired
    DpsService dpsService;
    @Autowired
    DaserviceService daserviceService;
    @Autowired
    SyncDataService syncDataService;

    @Autowired
    HDFSService hdfsService;

    @RequestMapping("hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping("syncData")
    public boolean synchronousData(){
        return syncDataService.syncDataAll();
    }

    @RequestMapping("syncDataByMsid/{msid}")
    public boolean synchronousData(@PathVariable("msid") String msid){
        return syncDataService.syncDataByMsid(msid);
    }

//    @RequestMapping("syncDataByMsids")
//    public boolean synchronousData(Set msids){
//        return syncDataService.syncDataByMsids(msids);
//    }


    @RequestMapping("downlodAllReceipt")
    public Boolean downlodReceipt(){
        return hdfsService.getAllFile();
    }

    @RequestMapping("downlodReceipt/{date}")
    public Boolean downlodReceipts(@PathVariable("date") String  date){
        return hdfsService.getFileByDate(date);
    }


    @RequestMapping("removeAllReceipt")
    public boolean removeAllReceipt(){
        return hdfsService.removeAllFile();
    }


    @RequestMapping("callDaservice")
    public boolean callDaservice(CallDaserviceReq callDaservicereq){
        daserviceService.callDaserviceRunagain(callDaservicereq);
        return true;
    }

}
