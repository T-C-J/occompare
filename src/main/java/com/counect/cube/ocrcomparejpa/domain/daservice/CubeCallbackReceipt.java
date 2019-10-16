package com.counect.cube.ocrcomparejpa.domain.daservice;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "cube_callback_receipt")
@Data
public class CubeCallbackReceipt {
    @Id
    private String id;
    private String cptid;
    private String general_java_code;
    private String normal_java_code;
    private String error_java_code;
    private String other_java_code;
    private String del_status;
    private String update_id;
    private String create_id;


    private Date update_time;
    private Date create_time;

}
