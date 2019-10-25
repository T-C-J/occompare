package com.counect.cube.ocrcomparejpa.repository.daservice.far;

import com.counect.cube.ocrcomparejpa.domain.daservice.CubeCallbackField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarCubeCallbackFieldRepository extends JpaRepository<CubeCallbackField,String> {

    CubeCallbackField queryByCptid(String cptid);

}
