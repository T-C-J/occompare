package com.counect.cube.ocrcomparejpa.service.daservice.impl;

import com.counect.cube.ocrcomparejpa.domain.daservice.*;
import com.counect.cube.ocrcomparejpa.entity.CallDaserviceReq;
import com.counect.cube.ocrcomparejpa.repository.daservice.far.*;
import com.counect.cube.ocrcomparejpa.repository.daservice.local.*;
import com.counect.cube.ocrcomparejpa.service.daservice.DaserviceService;
import com.counect.cube.ocrcomparejpa.utils.ContainerUtils;
import com.counect.cube.ocrcomparejpa.utils.HttpUtilV2;
import com.counect.cube.ocrcomparejpa.utils.JSONUtil;
import com.counect.cube.ocrcomparejpa.utils.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    public boolean callDaserviceRunagain(CallDaserviceReq callDaserviceReq) {
        Map paramMap = new HashMap<>();
        paramMap.put("appid","counect");
        paramMap.put("time_stamp",String.valueOf(new Date().getTime()/1000));
        paramMap.put("sign_type","md5");
        paramMap.put("body", JSONUtil.toJson(callDaserviceReq));
        paramMap.put("method","batchAnalysis");
        paramMap.put("v","0.0.1");
        paramMap.put("sign", SignUtil.md5Sign(paramMap,"counect"));

        HttpUtilV2.HttpResponse httpResponse = HttpUtilV2.doPost("http://172.16.0.104:84/batchAnalysis.do", paramMap);
        return true;
    }

    @Override
    public boolean syncData() {

        Set<String> usedMsids = ContainerUtils.UsedMsids;

        List<CubeConfig> configs = farCubeConfigRepository.findAll();
        List<CubeCallbackField> callbackFields = farCubeCallbackFieldRepository.findAll();
        List<CubeCallbackReceipt> callbackReceipts = farCubeCallbackReceiptRepository.findAll();
        List<ReceiptRules> receiptRules = farReceiptRulesRepository.findAll();
        List<ReceiptDefaultValue> defaultValues = farReceiptDefaultValueRepository.findAll();

        if(!usedMsids.isEmpty()){
            configs = configs.stream().filter(cubeConfig -> usedMsids.contains(cubeConfig.getCubeId())).collect(Collectors.toList());
            callbackFields = callbackFields.stream().filter( cubeCallbackField -> usedMsids.contains(cubeCallbackField.getCptid())).collect(Collectors.toList());
            callbackReceipts = callbackReceipts.stream().filter( callbackReceipt -> usedMsids.contains(callbackReceipt.getCptid())).collect(Collectors.toList());
            receiptRules = receiptRules.stream().filter( receiptRules1 -> usedMsids.contains(receiptRules1.getCubeId())).collect(Collectors.toList());
            defaultValues = defaultValues.stream().filter( defaultValue -> usedMsids.contains(defaultValue.getCubeId())).collect(Collectors.toList());
        }

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
