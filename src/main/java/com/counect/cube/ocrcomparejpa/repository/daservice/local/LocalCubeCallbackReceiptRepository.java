package com.counect.cube.ocrcomparejpa.repository.daservice.local;

import com.counect.cube.ocrcomparejpa.domain.daservice.CubeCallbackReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalCubeCallbackReceiptRepository extends JpaRepository<CubeCallbackReceipt,String> {
}
