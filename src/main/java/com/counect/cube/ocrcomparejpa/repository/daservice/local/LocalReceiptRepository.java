package com.counect.cube.ocrcomparejpa.repository.daservice.local;

import com.counect.cube.ocrcomparejpa.domain.daservice.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LocalReceiptRepository extends JpaRepository<Receipt,String> , JpaSpecificationExecutor<Receipt> {


}
