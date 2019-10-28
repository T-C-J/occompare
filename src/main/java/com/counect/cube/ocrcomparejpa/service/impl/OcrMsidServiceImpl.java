package com.counect.cube.ocrcomparejpa.service.impl;

import com.counect.cube.ocrcomparejpa.domain.daservice.OcrMsid;
import com.counect.cube.ocrcomparejpa.repository.daservice.local.OcrMsidRepository;
import com.counect.cube.ocrcomparejpa.service.OcrMsidService;
import com.counect.cube.ocrcomparejpa.utils.ContainerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class OcrMsidServiceImpl implements OcrMsidService {

    @Autowired
    OcrMsidRepository ocrMsidRepository;

    @Override
    public int save(List<OcrMsid> msids) {
        ocrMsidRepository.saveAll(msids);
        return 0;
    }

    @Override
    public int delAll() {
        ocrMsidRepository.deleteAll();
        return 0;
    }

    @Override
    public int add(OcrMsid msid) {
        ocrMsidRepository.save(msid);
        return 0;
    }

    @Override
    public int delOne(OcrMsid msid) {
        ocrMsidRepository.deleteByMsid(msid.getMsid());
        return 0;
    }

    @Override
    public boolean syncMsid() {
        Set<String> usedMsids = ContainerUtils.UsedMsids;
        delAll();
        List<OcrMsid> msids = new ArrayList<>();
        for (String str :usedMsids){
            OcrMsid ocrMsid = new OcrMsid();
            ocrMsid.setMsid(str);
            msids.add(ocrMsid);
        }
        save(msids);
        return true;
    }
}
