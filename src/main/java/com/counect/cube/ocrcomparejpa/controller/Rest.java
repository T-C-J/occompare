package com.counect.cube.ocrcomparejpa.controller;

import com.counect.cube.ocrcomparejpa.entity.CallDaserviceReq;
import com.counect.cube.ocrcomparejpa.service.HDFSService;
import com.counect.cube.ocrcomparejpa.service.SyncDataService;
import com.counect.cube.ocrcomparejpa.service.daservice.DaserviceService;
import com.counect.cube.ocrcomparejpa.service.dps.DpsService;
import com.counect.cube.ocrcomparejpa.utils.ContainerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@RestController
@Slf4j
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

//    @RequestMapping("syncMsid")
//    public String syncMsid(String body){
//        JSONArray jsonArray = JSONUtil.ArraytoJSONArray(body);
//        Set<Object> collect = jsonArray.stream().collect(Collectors.toSet());
//        log.info(JSONUtil.toJson(collect));
//        return "123";
//    }

    // 上传文档接口  要同步文件和解析的设备
    @RequestMapping("uploadMsids")
    public String uploadMsids(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        ContainerUtils.analysisFile(file);
        return "success";
    }


    // 得到所有要同步文件和解析的设备id，如果没有则是全部设备
    @RequestMapping("getMsids")
    public Set<String> getConfigureMsids(){
        return ContainerUtils.UsedMsids;
    }

    @RequestMapping("addMsid/{msid}")
    public boolean addMsid(@PathVariable("msid") String msid){
        boolean add = ContainerUtils.UsedMsids.add(msid);
        return add;
    }

    @RequestMapping("removeMsid/{msid}")
    public boolean removeMsid(@PathVariable("msid") String msid){
        boolean add = ContainerUtils.UsedMsids.remove(msid);
        return add;
    }

    @RequestMapping("emptyMsids")
    public boolean emptyMsids(){
        ContainerUtils.UsedMsids = new HashSet<>();
        if(ContainerUtils.UsedMsids.isEmpty())
            return true;
        return false;
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


    /**
     *  下载所有文件 这个方法没有msid的限制 filesystem里面的所有文件都会下载
     * @return
     */
    @RequestMapping("downlodAllReceipt")
    public Boolean downlodReceipt(){
        return hdfsService.getAllFile();
    }


    @RequestMapping("downlodReceipt/{date}")
    public Boolean downlodReceipts(@PathVariable("date") String  date){
        return hdfsService.getFileByDate(date);
    }


    /**
     *  删除 filesystem 里所有的文件
     * @return
     */
    @RequestMapping("removeAllReceipt")
    public boolean removeAllReceipt(){
        return hdfsService.removeAllFile();
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
