package com.counect.cube.ocrcomparejpa.controller;

import com.counect.cube.ocrcomparejpa.domain.daservice.CubeCallbackField;
import com.counect.cube.ocrcomparejpa.domain.dps.TblCubeInit;
import com.counect.cube.ocrcomparejpa.service.daservice.DaserviceService;
import com.counect.cube.ocrcomparejpa.service.dps.DpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Rest {

    @Autowired
    DpsService dpsService;
    @Autowired
    DaserviceService daserviceService;

    @RequestMapping("test")
    public List<TblCubeInit> test(){
        List<TblCubeInit> tblCubeInits = dpsService.selectFar();
//        System.out.println(tblCubeInits);
        return tblCubeInits;
    }
    @RequestMapping("test2")
    public List<TblCubeInit> test2(){
        List<TblCubeInit> tblCubeInits = dpsService.selectLocal();
        return tblCubeInits;
//        System.out.println(tblCubeInits);
    }

    @RequestMapping("test3")
    public List<CubeCallbackField> test3(){
        return daserviceService.getCallBackFieldFar();
    }

    @RequestMapping("test4")
    public List<CubeCallbackField> test4(){
        return daserviceService.getCallBackFieldlocal();
    }

    @RequestMapping("test5")
    public List<CubeCallbackField>  test5(){
        return daserviceService.saveFromFar();
    }
}
