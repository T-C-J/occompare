package com.counect.cube.ocrcomparejpa.controller;

import com.counect.cube.ocrcomparejpa.entity.MsidsBean;
import com.counect.cube.ocrcomparejpa.repository.daservice.local.OcrMsidRepository;
import com.counect.cube.ocrcomparejpa.service.SyncDataService;
import com.counect.cube.ocrcomparejpa.service.impl.OcrMsidServiceImpl;
import com.counect.cube.ocrcomparejpa.utils.ContainerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 *
 *  项目配置相关
 *
 *  uploadMsids             上传文件从中读取要处理的msid
 *  getMsids                返回所有要处理的msid
 *  addMsid/{msid}          添加msid
 *  removeMsid/{msid}       移除msid
 *  emptyMsids              移除所有msid
 *
 */
@RestController
@RequestMapping("/init")
public class InitRest {
    @Autowired
    SyncDataService syncDataService;
    @Autowired
    OcrMsidServiceImpl ocrMsidServiceImpl;


    @RequestMapping("uploadMsids")
    public String uploadMsids(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        ContainerUtils.analysisFile(file);
        syncDataService.syncMsids();
        ocrMsidServiceImpl.syncMsid();
        ContainerUtils.UPDATETIME = new Date();
        return "success";
    }

    @RequestMapping("getMsids")
    public MsidsBean getConfigureMsids(){
        MsidsBean msidsBean = new MsidsBean();
        msidsBean.setMsids(ContainerUtils.UsedMsids);
        msidsBean.setUpdateTime(ContainerUtils.UPDATETIME);
        return msidsBean;
    }

    @RequestMapping("addMsid/{msid}")
    public boolean addMsid(@PathVariable("msid") String msid){
        boolean add = ContainerUtils.UsedMsids.add(msid);
        syncDataService.syncMsids();
        ocrMsidServiceImpl.syncMsid();
        ContainerUtils.UPDATETIME = new Date();
        return add;
    }

    @RequestMapping("removeMsid/{msid}")
    public boolean removeMsid(@PathVariable("msid") String msid){
        boolean add = ContainerUtils.UsedMsids.remove(msid);
        syncDataService.syncMsids();
        ocrMsidServiceImpl.syncMsid();
        ContainerUtils.UPDATETIME = new Date();
        return add;
    }

    @RequestMapping("emptyMsids")
    public boolean emptyMsids(){
        ContainerUtils.UsedMsids = new HashSet<>();
        if(ContainerUtils.UsedMsids.isEmpty())
            return true;
        syncDataService.syncMsids();
        ocrMsidServiceImpl.delAll();
        ContainerUtils.UPDATETIME = new Date();
        return false;
    }
}
