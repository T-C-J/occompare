package com.counect.cube.ocrcomparejpa.service.daservice;

import com.counect.cube.ocrcomparejpa.domain.daservice.CubeCallbackField;

import java.util.List;

public interface DaserviceService {

    List<CubeCallbackField> getCallBackFieldFar();
    List<CubeCallbackField> getCallBackFieldlocal();


    List<CubeCallbackField> saveFromFar();
}
