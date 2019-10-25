package com.counect.cube.ocrcomparejpa.service;


public interface HDFSService {

    /**
     *  生成根路径
     * @return
     */
    boolean init();

    /**
     *  将HDFS上从跟路径开始删除
     * @return
     */
    boolean removeAllFile();

    /**
     *  将HFDFS上的文件按照日期下载到本地
     * @param dateStr 2019-02-02
     * @return
     */
    boolean getFileByDate(String dateStr);

    /**
     *  下载HDFS上所有的水单文件
     * @return
     */
    boolean getAllFile();

    /**
     *  安装日期将本地文件上传到HDFS
     * @param dateStr
     * @return
     */
    boolean addFileByDateToHDFS(String dateStr);

    /**
     *  未实现
     * @param filePath
     * @return
     */
    boolean addFileToHDFS(String filePath);

    /**
     *  将本地所有文件上传到HDFS
     * @return
     */
    boolean addAllFileToHDFS();


    Integer getFileNumber();

}
