package com.counect.cube.ocrcomparejpa.repository.dps.far;

import com.counect.cube.ocrcomparejpa.domain.dps.TblCubeInit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarTblCubeInitRepository extends JpaRepository<TblCubeInit,String> {

    TblCubeInit queryBySid(String sid);

}
