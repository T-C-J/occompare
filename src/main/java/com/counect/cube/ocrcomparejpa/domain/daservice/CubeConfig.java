package com.counect.cube.ocrcomparejpa.domain.daservice;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "cube_config")
@Entity
@Data
public class CubeConfig {

    @Id
    private String id;
    private String cube_id;
    private String file_extension;
    private String data_type;
    private String treat_liner_as_empty;
    private String is_goodtogo;
    private String is_new_dateformat;
    private String charset;
    private String hide_liner_with_many_chars;
    private String auto_status;
    private String ocr_type;
    private String ocr_config;
    private String detail_status;
    private String extend;

    private Date update_time;


    private BigDecimal fuzzy_value_pct;


    private Integer image_rotate;
    private Integer empty_line_pct;
    private Integer spacing_width;
}
