package com.counect.cube.ocrcomparejpa.domain.dps;

import com.sun.javafx.beans.IDProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tbl_cube_init")
@Data
public class TblCubeInit {
    @Id
    private String sid;
    private String ending;
    private Character pre_decode;
    private Character status;
    private Character cube_status;
    private String decode;
    private String file_type;
    private String exfile;
    private String device_type;
    private String extend;
    private String ending_position;
    private Integer excount;
    private Date update_time;
    private Date create_time;
    private Date last_success_time;
}
