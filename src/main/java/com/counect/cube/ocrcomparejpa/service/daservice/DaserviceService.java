package com.counect.cube.ocrcomparejpa.service.daservice;

import com.counect.cube.ocrcomparejpa.entity.CallDaserviceReq;

public interface DaserviceService {



    boolean callDaserviceRunagain(CallDaserviceReq callDaserviceReq);


    boolean syncData();
    boolean syncDataByMsid(String msid);



    boolean receiptSync();
    boolean receiptSyncByDate( String dateStr);

}
