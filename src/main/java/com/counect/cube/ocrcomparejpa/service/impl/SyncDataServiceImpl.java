package com.counect.cube.ocrcomparejpa.service.impl;

import com.counect.cube.ocrcomparejpa.service.SyncDataService;
import com.counect.cube.ocrcomparejpa.service.daservice.DaserviceService;
import com.counect.cube.ocrcomparejpa.service.dps.DpsService;
import com.counect.cube.ocrcomparejpa.utils.ContainerUtils;
import com.counect.cube.ocrcomparejpa.utils.HttpUtilV2;
import com.counect.cube.ocrcomparejpa.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class SyncDataServiceImpl implements SyncDataService {
    @Autowired
    DaserviceService daserviceService;

    @Autowired
    DpsService dpsService;

    @Value("${far.httpServer}")
    private String FARHTTPSERVER;



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

    /**
     *  向远端服务同步msid列表
     * @return
     */
    @Override
    public boolean syncMsids() {
        Map paramMap = new HashMap<>();
        paramMap.put("body", JSONUtil.toJson(ContainerUtils.UsedMsids));
        String path = FARHTTPSERVER + "syncMsid";
        HttpUtilV2.HttpResponse httpResponse = HttpUtilV2.doPost(path, paramMap);
        if(httpResponse.getResponse().equals("123"))
            return true;
        return false;
    }

    @Override
    public boolean initFileSystem() {
        String path = FARHTTPSERVER + "init";
        Map paramMap = new HashMap<>();
        HttpUtilV2.HttpResponse httpResponse = HttpUtilV2.doPost(path, paramMap);
        if(httpResponse.getResponse().equals("true"))
            return true;
        return false;
    }

    @Override
    public boolean addAllFile() {
        String path = FARHTTPSERVER + "addAll";
        Map paramMap = new HashMap<>();
        HttpUtilV2.HttpResponse httpResponse = HttpUtilV2.doPost(path, paramMap);
        if(httpResponse.getResponse().equals("true"))
            return true;
        return false;
    }

    @Override
    public boolean addFileByDate(String date) {
        String path = FARHTTPSERVER + "addByDate/" + date;
        Map paramMap = new HashMap<>();
        HttpUtilV2.HttpResponse httpResponse = HttpUtilV2.doPost(path, paramMap);
        if(httpResponse.getResponse().equals("true"))
            return true;
        return false;
    }
}
