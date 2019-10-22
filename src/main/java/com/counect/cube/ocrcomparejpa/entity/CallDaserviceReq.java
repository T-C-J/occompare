package com.counect.cube.ocrcomparejpa.entity;

import lombok.Data;

@Data
public class CallDaserviceReq {
    private String msid;
    private String mch_id;
    private String receipt_status;
    private String start_time;
    private String end_time;
    private String receipt_type;
}
