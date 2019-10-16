package com.counect.cube.ocrcomparejpa.service.daservice;

import com.counect.cube.ocrcomparejpa.domain.daservice.CubeCallbackField;
import com.counect.cube.ocrcomparejpa.entity.CallDaserviceBean;

import java.util.List;

public interface DaserviceService {



    boolean callDaserviceRunagain(CallDaserviceBean callDaserviceBean);


    boolean syncData();
    boolean syncDataByMsid(String msid);

}
