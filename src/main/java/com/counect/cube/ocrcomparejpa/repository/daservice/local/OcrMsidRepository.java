package com.counect.cube.ocrcomparejpa.repository.daservice.local;

import com.counect.cube.ocrcomparejpa.domain.daservice.OcrMsid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OcrMsidRepository extends JpaRepository<OcrMsid,Integer> {

    int deleteByMsid(String msid);

}
