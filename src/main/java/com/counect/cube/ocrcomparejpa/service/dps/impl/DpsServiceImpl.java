package com.counect.cube.ocrcomparejpa.service.dps.impl;

import com.counect.cube.ocrcomparejpa.domain.dps.TblCubeInit;
import com.counect.cube.ocrcomparejpa.repository.dps.far.FarTblCubeInitRepository;
import com.counect.cube.ocrcomparejpa.service.dps.DpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DpsServiceImpl implements DpsService {

    @Autowired
    FarTblCubeInitRepository farTblCubeInitRepository;


    @Override
    public List<TblCubeInit> selectFar() {
        farTblCubeInitRepository.findAll();
        return null;
    }
}
