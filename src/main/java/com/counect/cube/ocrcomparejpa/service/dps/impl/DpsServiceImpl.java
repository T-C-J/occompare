package com.counect.cube.ocrcomparejpa.service.dps.impl;

import com.counect.cube.ocrcomparejpa.domain.dps.TblCubeInit;
import com.counect.cube.ocrcomparejpa.repository.dps.far.FarTblCubeInitRepository;
import com.counect.cube.ocrcomparejpa.repository.dps.local.LocalTblCubeInitRepository;
import com.counect.cube.ocrcomparejpa.service.dps.DpsService;
import com.counect.cube.ocrcomparejpa.utils.ContainerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DpsServiceImpl implements DpsService {

    @Autowired
    FarTblCubeInitRepository farTblCubeInitRepository;
    @Autowired
    LocalTblCubeInitRepository localTblCubeInitRepository;


    @Override
    public boolean syncData() {
        Set<String> usedMsids = ContainerUtils.UsedMsids;

        List<TblCubeInit> tblCubeInits = farTblCubeInitRepository.findAll();
        if(!usedMsids.isEmpty()){
            tblCubeInits = tblCubeInits.stream().filter(tblCubeInit -> usedMsids.contains(tblCubeInit.getSid())).collect(Collectors.toList());
        }
        localTblCubeInitRepository.saveAll(tblCubeInits);
        return true;
    }

    @Override
    public boolean syncDataByMsid(String msid) {
        List<TblCubeInit> tblCubeInits = farTblCubeInitRepository.findAll();
//        TblCubeInit tblCubeInit = farTblCubeInitRepository.queryBySid(msid);
        tblCubeInits = tblCubeInits.stream().filter(tblCubeInit -> msid.equals(tblCubeInit.getSid())).collect(Collectors.toList());
        localTblCubeInitRepository.saveAll(tblCubeInits);
        return true;
    }



}
