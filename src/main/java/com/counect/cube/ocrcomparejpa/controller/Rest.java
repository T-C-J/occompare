package com.counect.cube.ocrcomparejpa.controller;

import com.counect.cube.ocrcomparejpa.domain.daservice.CubeCallbackField;
import com.counect.cube.ocrcomparejpa.domain.dps.TblCubeInit;
import com.counect.cube.ocrcomparejpa.service.SyncDataService;
import com.counect.cube.ocrcomparejpa.service.daservice.DaserviceService;
import com.counect.cube.ocrcomparejpa.service.dps.DpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class Rest {

    @Autowired
    DpsService dpsService;
    @Autowired
    DaserviceService daserviceService;
    @Autowired
    SyncDataService syncDataService;


    @RequestMapping("syncData")
    public boolean synchronousData(){
        return syncDataService.syncDataAll();
    }

    @RequestMapping("syncDataByMsid/{msid}")
    public boolean synchronousData(@PathVariable("msid") String msid){
        return syncDataService.syncDataByMsid(msid);
    }

    @RequestMapping("syncDataByMsids")
    public boolean synchronousData(Set msids){
        return syncDataService.syncDataByMsids(msids);
    }
}
