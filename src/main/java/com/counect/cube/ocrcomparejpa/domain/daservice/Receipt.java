package com.counect.cube.ocrcomparejpa.domain.daservice;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "receipt")
@Data
public class Receipt {

    @Id
    private String tsid;
    private String cptid;
    private String msid;
    private String rawcontent;
    private String file_type;
    @Column(name = "CreditNo")
    private String CreditNo;
    private String receiptno;
    private String casher;
    private String cashpos;
    private String receiptType;
    private String paymentType;
    private String remark;
    private String extend_1;
    private String extend_2;
    private String extend_3;
    private String extend_4;
    private String extend_5;
    private String extend_6;
    private String extend_7;
    private String extend_8;


    private Double totalamount;
    private Double TotalIncome;

    private float totalnumber;

    private Integer status;
    private Integer dps_status;
    private Integer storeid;



    private String tsdate;
    private String lastupdatetime;
    private String receiptdate;
    private String receipttime;
    private String syncTime;
}
