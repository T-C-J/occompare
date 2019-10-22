package com.counect.cube.ocrcomparejpa.service;


public interface HDFSService {

    /**
     *  生成根路径
     * @return
     */
    boolean init();

    /**
     *  安装日期将本地文件上传到HDFS
     * @param dateStr
     * @return
     */
    boolean addFileByDateToHDFS(String dateStr);

    /**
     *  将本地所有文件上传到HDFS
     * @return
     */
    boolean addAllFileToHDFS();

}
