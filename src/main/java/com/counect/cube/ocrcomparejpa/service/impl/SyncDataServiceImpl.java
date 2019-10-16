package com.counect.cube.ocrcomparejpa.service.impl;

import com.counect.cube.ocrcomparejpa.service.SyncDataService;
import com.counect.cube.ocrcomparejpa.service.daservice.DaserviceService;
import com.counect.cube.ocrcomparejpa.service.dps.DpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

@Service
@Slf4j
public class SyncDataServiceImpl implements SyncDataService {
    @Autowired
    DaserviceService daserviceService;

    @Autowired
    DpsService dpsService;



    @Override
    public boolean syncDataAll() {
        if(daserviceService.syncData() && dpsService.syncData())
            return true;
        return false;
    }

    @Override
    public boolean syncDataByMsid(String msid) {
        if(daserviceService.syncDataByMsid(msid) && dpsService.syncDataByMsid(msid))
            return true;
        return false;
    }

    @Override
    public boolean syncDataByMsids(Collection msids) {
        Iterator iterator = msids.iterator();
        Boolean flag = true;
        while (iterator.hasNext()){
            flag = syncDataByMsid((String) iterator.next()) & flag;
        }
        return flag;
    }
}
