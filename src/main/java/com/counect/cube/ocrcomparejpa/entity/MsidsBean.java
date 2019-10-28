package com.counect.cube.ocrcomparejpa.entity;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class MsidsBean {

    private Set<String> msids;

    private Date updateTime;

}
