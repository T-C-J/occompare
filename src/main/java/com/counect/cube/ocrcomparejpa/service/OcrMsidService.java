package com.counect.cube.ocrcomparejpa.service;

import com.counect.cube.ocrcomparejpa.domain.daservice.OcrMsid;

import java.util.List;

public interface OcrMsidService {

    int save(List<OcrMsid> msids);

    int delAll();

    int add(OcrMsid msid);

    int delOne(OcrMsid msid);

    boolean syncMsid();
}
