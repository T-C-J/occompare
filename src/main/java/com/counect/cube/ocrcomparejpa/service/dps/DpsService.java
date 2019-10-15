package com.counect.cube.ocrcomparejpa.service.dps;

import com.counect.cube.ocrcomparejpa.domain.dps.TblCubeInit;

import java.util.List;

public interface DpsService {

    List<TblCubeInit> selectFar();
    List<TblCubeInit> selectLocal();
}
