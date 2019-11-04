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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    @Autowired
    LocalReceiptRepository localReceiptRepository;
    @Autowired
    FarReceiptRepository farReceiptRepository;


    private static final Set<String> res = new HashSet<String>(){{
        add("ms007008102");
        add("ms007008101");
        add("ms007007801");
        add("ms007007601");
        add("ms007007401");
        add("ms007007001");
        add("ms007006701");
        add("ms007006601");
        add("ms007005801");
        add("ms007005101");
        add("ms007004904");
        add("ms007004902");
        add("ms007004901");
        add("ms007003901");
        add("ms007003601");
        add("ms007003501");
        add("ms007003201");
        add("ms007003101");
        add("ms007002804");
        add("ms007002701");
        add("ms007002401");
        add("ms007002301");
        add("ms007001904");
        add("ms007001903");
        add("ms007001801");
        add("ms007001301");
        add("ms007001101");
        add("ms007000301");
    }};

    @Override
    public boolean callDaserviceRunagain(CallDaserviceReq callDaserviceReq) {
        Map paramMap = new HashMap<>();
        paramMap.put("appid","counect");
        paramMap.put("timestamp",String.valueOf(new Date().getTime()));
        paramMap.put("sign_type","md5");
        paramMap.put("body", JSONUtil.toJson(callDaserviceReq));
        paramMap.put("method","batchAnalysis");
        paramMap.put("v","0.0.1");
        paramMap.put("sign", SignUtil.md5Sign(paramMap,"counect"));
        log.info("请求：" + JSONUtil.toJson(paramMap));
        HttpUtilV2.HttpResponse httpResponse = HttpUtilV2.doPost("http://172.16.0.104:84/batchAnalysis.do", paramMap);
        log.info(JSONUtil.toJson(httpResponse));
        return true;
    }


    public boolean analysisReceipt(OcrCompare ocrCompare) {
        Map paramMap = new HashMap<>();
        HashMap<Object, Object> body = new HashMap<>();
        paramMap.put("appid","counect");
        paramMap.put("timestamp",String.valueOf(System.currentTimeMillis()));
        paramMap.put("sign_type","md5");
        paramMap.put("method","analysis");
        paramMap.put("v","0.0.1");

        String filepath = ocrCompare.getFilepath();
        String[] split = filepath.split("/");
        body.put("file_path", filepath.replaceAll("ParserDir_mili","ParserDir"));
        body.put("msid", split[split.length -3]);


        paramMap.put("body", JSONUtil.toJson(body));


        paramMap.put("sign", SignUtil.md5Sign(paramMap,"counect"));
        log.info("请求：" + JSONUtil.toJson(paramMap));
        HttpUtilV2.HttpResponse httpResponse = HttpUtilV2.doPost("http://172.16.0.104:84/analysis.do", paramMap);
        log.info(JSONUtil.toJson(httpResponse));
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
    public boolean syncDataByMsid(String msid) { //todo

        List<CubeConfig> configs = farCubeConfigRepository.findAll();
        List<CubeCallbackField> callbackFields = farCubeCallbackFieldRepository.findAll();
        List<CubeCallbackReceipt> callbackReceipts = farCubeCallbackReceiptRepository.findAll();
        List<ReceiptRules> receiptRules = farReceiptRulesRepository.findAll();
        List<ReceiptDefaultValue> defaultValues = farReceiptDefaultValueRepository.findAll();
//        CubeConfig configs = farCubeConfigRepository.queryByCubeId(msid);
//        CubeCallbackField callbackFields = farCubeCallbackFieldRepository.queryByCptid(msid);
//        CubeCallbackReceipt callbackReceipts = farCubeCallbackReceiptRepository.queryById(msid);
//        ReceiptRules receiptRules = farReceiptRulesRepository.queryByCubeId(msid);
//        ReceiptDefaultValue defaultValues = farReceiptDefaultValueRepository.queryByCubeId(msid);
        configs = configs.stream().filter(cubeConfig -> msid.equals(cubeConfig.getCubeId())).collect(Collectors.toList());
        callbackFields = callbackFields.stream().filter( cubeCallbackField -> msid.equals(cubeCallbackField.getCptid())).collect(Collectors.toList());
        callbackReceipts = callbackReceipts.stream().filter( callbackReceipt -> msid.equals(callbackReceipt.getCptid())).collect(Collectors.toList());
        receiptRules = receiptRules.stream().filter( receiptRules1 -> msid.equals(receiptRules1.getCubeId())).collect(Collectors.toList());
        defaultValues = defaultValues.stream().filter( defaultValue -> msid.equals(defaultValue.getCubeId())).collect(Collectors.toList());



        localCubeConfigRepository.saveAll(configs);
        localCubeCallbackFieldRepository.saveAll(callbackFields);
        localCubeCallbackReceiptRepository.saveAll(callbackReceipts);
        localReceiptRulesRepository.saveAll(receiptRules);
        localReceiptDefaultValueRepository.saveAll(defaultValues);
        return true;
    }

    @Override
    public boolean receiptSync() {
        Set<String> msids = getMsids();
        for(String msid:msids){
            Receipt receipt = new Receipt();
            receipt.setMsid(msid);
            Specification<Receipt> specification = new Specification<Receipt>() {
                @Override
                public Predicate toPredicate(Root<Receipt> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("tsdate").as(String.class),"2019-10-18"));
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("tsdate").as(String.class),"2019-10-25"));
                    predicates.add(criteriaBuilder.like(root.get("msid").as(String.class),"ms0070" + "%"));
//                    predicates.add(criteriaBuilder.like(root.get("file_type").as(String.class),".bmp"));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            };
//            Example<Receipt> example = Example.of(receipt ,matcher);
            List<Receipt> all = farReceiptRepository.findAll(specification);
            List<Receipt> receipts = localReceiptRepository.saveAll(all);
//            System.out.println(all);
        }

        return false;
    }

    private Set<String> getMsids() {
        return res;
    }

    @Override
    public boolean receiptSyncByDate(String dateStr) {
        return false;
    }


}
