package com.counect.cube.ocrcomparejpa.domain.daservice;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ocrcompare")
@Data
public class OcrCompare {

    @Id
    private String tsid;
    private String filepath;
    private String text;
    private String url;

    private String createtime;
    private String updatetime;

}
