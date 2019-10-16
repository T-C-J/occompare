package com.counect.cube.ocrcomparejpa.service.daservice.impl;

import com.counect.cube.ocrcomparejpa.domain.daservice.*;
import com.counect.cube.ocrcomparejpa.entity.CallDaserviceBean;
import com.counect.cube.ocrcomparejpa.repository.daservice.far.*;
import com.counect.cube.ocrcomparejpa.repository.daservice.local.*;
import com.counect.cube.ocrcomparejpa.service.daservice.DaserviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DaserviceServiceImpl implements DaserviceService {
    @Autowired
    FarCubeConfigRepository farCubeConfigRepository;

    @Autowired
    FarCubeCallbackFieldRepository farCubeCallbackFieldRepository;

    @Autowired
    FarCubeCallbackReceiptRepository farCubeCallbackReceiptRepository;

    @Autowired
    FarReceiptDefaultValueRepository farReceiptDefaultValueRepository;

    @Autowired
    FarReceiptRulesRepository farReceiptRulesRepository;


    @Autowired
    LocalCubeConfigRepository localCubeConfigRepository;

    @Autowired
    LocalCubeCallbackFieldRepository localCubeCallbackFieldRepository;

    @Autowired
    LocalCubeCallbackReceiptRepository localCubeCallbackReceiptRepository;

    @Autowired
    LocalReceiptDefaultValueRepository localReceiptDefaultValueRepository;

    @Autowired
    LocalReceiptRulesRepository localReceiptRulesRepository;


    @Override
    public boolean callDaserviceRunagain(CallDaserviceBean callDaserviceBean) {
        return false;
    }

    @Override
    public boolean syncData() {

        List<CubeConfig> configs = farCubeConfigRepository.findAll();
        List<CubeCallbackField> callbackFields = farCubeCallbackFieldRepository.findAll();
        List<CubeCallbackReceipt> callbackReceipts = farCubeCallbackReceiptRepository.findAll();
        List<ReceiptRules> receiptRules = farReceiptRulesRepository.findAll();
        List<ReceiptDefaultValue> defaultValues = farReceiptDefaultValueRepository.findAll();

        localCubeConfigRepository.saveAll(configs);
        localCubeCallbackFieldRepository.saveAll(callbackFields);
        localCubeCallbackReceiptRepository.saveAll(callbackReceipts);
        localReceiptRulesRepository.saveAll(receiptRules);
        localReceiptDefaultValueRepository.saveAll(defaultValues);
        return true;
    }

    @Override
    public boolean syncDataByMsid(String msid) {
        CubeConfig configs = farCubeConfigRepository.queryByCubeId(msid);
        CubeCallbackField callbackFields = farCubeCallbackFieldRepository.queryByCptid(msid);
        CubeCallbackReceipt callbackReceipts = farCubeCallbackReceiptRepository.queryById(msid);
        ReceiptRules receiptRules = farReceiptRulesRepository.queryByCubeId(msid);
        ReceiptDefaultValue defaultValues = farReceiptDefaultValueRepository.queryByCubeId(msid);

        localCubeConfigRepository.save(configs);
        localCubeCallbackFieldRepository.save(callbackFields);
        localCubeCallbackReceiptRepository.save(callbackReceipts);
        localReceiptRulesRepository.save(receiptRules);
        localReceiptDefaultValueRepository.save(defaultValues);
        return true;
    }



}
