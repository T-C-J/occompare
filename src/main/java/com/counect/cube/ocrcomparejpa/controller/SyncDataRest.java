package com.counect.cube.ocrcomparejpa.controller;

import com.counect.cube.ocrcomparejpa.service.SyncDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *  同步配置信息
 *  同步数据表
 *  syncData 同步全部
 *  syncDataByMsid/{msid} 根据msid同步
 *
 */

@RestController
@RequestMapping("/sync")
public class SyncDataRest {
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
}
