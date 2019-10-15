package com.counect.cube.ocrcomparejpa.domain.daservice;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "receipt_rules")
@Data
public class ReceiptRules {

    @Id
    private Integer id;
    private Integer receipt_type;
    private Integer run_sequence;

    private String field;
    private String cube_id;
    private String match_type;
    private String match_value;
    private String regex;
    private String regex_extract;
    private String keyword;
    private String keyword_exclude;
    private String prefixed;
    private String string;
    private String date_format;
    private String remarks;
    private String location;
    private String is_active;


    private Date update_time;
}
