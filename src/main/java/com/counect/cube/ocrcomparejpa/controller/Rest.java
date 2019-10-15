package com.counect.cube.ocrcomparejpa.controller;

import com.counect.cube.ocrcomparejpa.domain.dps.TblCubeInit;
import com.counect.cube.ocrcomparejpa.service.dps.DpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Rest {

    @Autowired
    DpsService dpsService;

    @RequestMapping("test")
    public void test(){
        List<TblCubeInit> tblCubeInits = dpsService.selectFar();
        System.out.println(tblCubeInits);
    }
}
