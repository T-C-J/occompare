package com.counect.cube.ocrcomparejpa.repository.daservice.local;

import com.counect.cube.ocrcomparejpa.domain.daservice.OcrCompare;
import com.counect.cube.ocrcomparejpa.domain.daservice.OcrMsid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcrCompareRepository extends JpaRepository<OcrCompare,String> {



}
