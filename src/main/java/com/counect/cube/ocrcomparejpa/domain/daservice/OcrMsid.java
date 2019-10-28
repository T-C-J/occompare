package com.counect.cube.ocrcomparejpa.domain.daservice;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ocr_msids")
public class OcrMsid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String msid;
}
