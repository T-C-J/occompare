package com.counect.cube.ocrcomparejpa.controller;

import com.counect.cube.ocrcomparejpa.entity.CallDaserviceReq;
import com.counect.cube.ocrcomparejpa.service.HDFSService;
import com.counect.cube.ocrcomparejpa.service.SyncDataService;
import com.counect.cube.ocrcomparejpa.service.daservice.DaserviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  callDaservice  调用daservice
 */
@RestController
@Slf4j
public class Rest {

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

    /**
     *  调用daservice 重跑
     * @param callDaservicereq
     * @return
     */
    @RequestMapping("callDaservice")
    public boolean callDaservice(CallDaserviceReq callDaservicereq){
        daserviceService.callDaserviceRunagain(callDaservicereq);
        return true;
    }

}
