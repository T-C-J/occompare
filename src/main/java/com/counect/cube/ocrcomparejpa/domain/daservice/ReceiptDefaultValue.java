package com.counect.cube.ocrcomparejpa.domain.daservice;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "receipt_default_value")
@Data
public class ReceiptDefaultValue {
    @Id
    private Integer id;
    private Integer receipt_type;

    @Column(name = "cube_id")
    private String cubeId;
    private String field;
    private String default_type;
    private String parameters;

    private Date update_time;
}
