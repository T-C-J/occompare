package com.counect.cube.ocrcomparejpa.service.daservice.impl;

import com.counect.cube.ocrcomparejpa.domain.daservice.CubeCallbackField;
import com.counect.cube.ocrcomparejpa.repository.daservice.far.FarCubeCallbackFieldRepository;
import com.counect.cube.ocrcomparejpa.repository.daservice.local.LocalCubeCallbackFieldRepository;
import com.counect.cube.ocrcomparejpa.service.daservice.DaserviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DaserviceServiceImpl implements DaserviceService {
    @Autowired
    FarCubeCallbackFieldRepository farCubeCallbackFieldRepository;
    @Autowired
    LocalCubeCallbackFieldRepository localCubeCallbackFieldRepository;

    @Override
    public List<CubeCallbackField> getCallBackFieldFar() {
        return farCubeCallbackFieldRepository.findAll();
    }

    @Override
    public List<CubeCallbackField> getCallBackFieldlocal() {
        return localCubeCallbackFieldRepository.findAll();
    }

    @Override
    public List<CubeCallbackField> saveFromFar() {
        List<CubeCallbackField> all = farCubeCallbackFieldRepository.findAll();
        List<CubeCallbackField> cubeCallbackFields = localCubeCallbackFieldRepository.saveAll(all);
        return cubeCallbackFields;
    }
}
