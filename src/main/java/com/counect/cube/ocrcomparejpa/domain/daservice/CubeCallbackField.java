package com.counect.cube.ocrcomparejpa.domain.daservice;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "cube_callback_field" )
@Data
public class CubeCallbackField {
    @Id
    private String id;
    private String cptid;
    private String field;
    private String java_code;
    private String del_status;
    private String update_id;
    private Date update_time;
    private Date create_id;
    private Date create_time;
}
