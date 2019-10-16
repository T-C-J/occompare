package com.counect.cube.ocrcomparejpa.repository.daservice.far;

import com.counect.cube.ocrcomparejpa.domain.daservice.ReceiptRules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarReceiptRulesRepository extends JpaRepository<ReceiptRules,Integer> {

    ReceiptRules queryByCubeId(String cubeId);
}
