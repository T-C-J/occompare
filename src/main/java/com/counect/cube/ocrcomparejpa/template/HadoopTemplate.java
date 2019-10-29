package com.counect.cube.ocrcomparejpa.template;

import com.counect.cube.ocrcomparejpa.utils.ContainerUtils;
import com.counect.cube.ocrcomparejpa.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@DependsOn("fileSystem")
@Slf4j
public class HadoopTemplate {

    @Autowired
    private FileSystem fileSystem;

    @Value("${hadoop.name-node}")
    private String nameNode;

    @Value("${hadoop.namespace:/}")
    private String nameSpace;

    @PostConstruct
    public void init(){
        existDir(nameSpace,true);
    }

    public void uploadFile(String srcFile){
        copyFileToHDFS(false,true,srcFile,nameSpace);
    }

    public void uploadFile(String srcFile,String destPath){
        copyFileToHDFS(false,true,srcFile,destPath);
    }

    public void uploadFileNotOverWriter(String srcFile,String destPath){
        copyFileToHDFS(false,false,srcFile,destPath);
    }

    public void delFile(String fileName){
        rmdir(nameSpace,fileName) ;
    }

    public void delDir(String path){
        nameSpace = nameSpace + "/" +path;
        rmdir(path,null) ;
    }

    public void download(String fileName,String savePath){
        getFile(nameSpace+"/"+fileName,savePath);
    }


    /**
     * 创建目录
     * @param filePath
     * @param create
     * @return
     */
    public boolean existDir(String filePath, boolean create){
        boolean flag = false;
        if(StringUtils.isEmpty(filePath)){
            throw new IllegalArgumentException("filePath不能为空");
        }
        try{
            Path path = new Path(filePath);
            if (create){
                if (!fileSystem.exists(path)){
                    fileSystem.mkdirs(path);
                }
            }
            if (fileSystem.isDirectory(path)){
                flag = true;
            }
        }catch (Exception e){
            log.error("", e);
        }
        return flag;
    }




    /**
     * 文件上传至 HDFS
     * @param delSrc       指是否删除源文件，true为删除，默认为false
     * @param overwrite
     * @param srcFile      源文件，上传文件路径
     * @param destPath     hdfs的目的路径
     */
    public  void copyFileToHDFS(boolean delSrc, boolean overwrite,String srcFile,String destPath) {
        // 源文件路径是Linux下的路径，如果在 windows 下测试，需要改写为Windows下的路径，比如D://hadoop/djt/weibo.txt
        Path srcPath = new Path(srcFile);

        // 目的路径
        if(StringUtils.isNotBlank(nameNode)){
            destPath = nameNode + destPath;
        }
        Path dstPath = new Path(destPath);
        // 实现文件上传
        try {
            // 获取FileSystem对象
            fileSystem.copyFromLocalFile(srcPath, dstPath);
            fileSystem.copyFromLocalFile(delSrc,overwrite,srcPath, dstPath);
            //释放资源
            //    fileSystem.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }


    /**
     * 删除文件或者文件目录
     *
     * @param path
     */
    public void rmdir(String path,String fileName) {
        try {
            // 返回FileSystem对象
            if(StringUtils.isNotBlank(nameNode)){
                path = nameNode + path;
            }
            if(StringUtils.isNotBlank(fileName)){
                path =  path + "/" +fileName;
            }
            // 删除文件或者文件目录  delete(Path f) 此方法已经弃用
            fileSystem.delete(new Path(path),true);
        } catch (IllegalArgumentException | IOException e) {
            log.error("", e);
        }
    }

    /**
     * 从 HDFS 下载文件
     *
     * @param hdfsFile
     * @param destPath 文件下载后,存放地址
     */
    public void getFile(String hdfsFile,String destPath) {
        // 源文件路径
        if(!hdfsFile.contains(nameNode)){
            hdfsFile = nameNode + hdfsFile;
        }
        Path hdfsPath = new Path(hdfsFile);
        Path dstPath = new Path(destPath);
//        FileUtils.clearDir(new File(destPath));
        FileUtils.clearDirForDownlod(new File(destPath));
        try {
            // 下载hdfs上的文件
            FileStatus[] fileStatuses = fileSystem.listStatus(hdfsPath);
            for(FileStatus fileStatus:fileStatuses){
                fileSystem.copyToLocalFile(fileStatus.getPath(), dstPath);
            }
            // 释放资源
            // fs.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }

    public void getFileForDate(String hdfsFile,String destPath) {
        // 源文件路径
        if(!hdfsFile.contains(nameNode)){
            hdfsFile = nameNode + hdfsFile;
        }
        Path hdfsPath = new Path(hdfsFile);
        Path dstPath = new Path(destPath);
        FileUtils.clearDir(new File(destPath));
//        FileUtils.clearDirForDownlod(new File(destPath));
        try {
            // 下载hdfs上的文件

            fileSystem.copyToLocalFile(hdfsPath, dstPath);
            // 释放资源
            // fs.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 遍历文件夹
     * public FileStatus[] listStatus(Path p)
     * 通常使用HDFS文件系统的listStatus(path)来获取改定路径的子路径。然后逐个判断
     * 值得注意的是：
     *  1.并不是总有文件夹中有文件，有些文件夹是空的，如果仅仅做是否为文件的判断会有问题，必须加文件的长度是否为0的判断
     *  2.使用getPath()方法获取的是FileStatus对象是带URL路径的。使用FileStatus.getPath().toUri().getPath()获取的路径才是不带url的路径
     * @param hdfs
     * @param listPath 传入的HDFS开始遍历的路径
     * @return
     */
    public Set<String> recursiveHdfsPath(FileSystem hdfs, Path listPath){
        Set<String> set = new HashSet<>();
                /*FileStatus[] files = null;
                try {
                    files = hdfs.listStatus(listPath);
                    Path[] paths = FileUtil.stat2Paths(files);
                    for(int i=0;i<files.length;i++){
                        if(files[i].isFile()){
                            // set.add(paths[i].toString());
                            set.add(paths[i].getName());
                        }else {
                            recursiveHdfsPath(hdfs,paths[i]);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e);
                }*/

        FileStatus[] files = null;

        try {
            files = hdfs.listStatus(listPath);
            // 实际上并不是每个文件夹都会有文件的。
            if(files.length == 0){
                // 如果不使用toUri()，获取的路径带URL。
                set.add(listPath.toUri().getPath());
            }else {
                // 判断是否为文件
                for (FileStatus f : files) {
                    if (files.length == 0 || f.isFile()) {
                        set.add(f.getPath().toUri().getPath());
                    } else {
                        // 是文件夹，且非空，就继续遍历
                        recursiveHdfsPath(hdfs, f.getPath());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(""+e);
        }
        return set;
    }


//    /**
//     * 获取hdfs路径下的文件列表
//     *
//     * @param srcpath
//     * @return
//     */
//    public List<String> getFileList(String srcpath) {
//        try {
//
//            List<String> files = new ArrayList<String>();
//            Path path = new Path(srcpath);
//            if (fileSystem.exists(path) && fileSystem.isDirectory(path)) {
//                for (FileStatus status : fileSystem.listStatus(path)) {
//                    files.add(status.getPath().toString());
//                }
//            }
//            return files;
//        } catch (IOException e) {
//        } catch (Exception e) {
//        }
//        return null;
//    }

    public List<String> getMSDirList(String srcpath) {
        Set<String> usedMsids = ContainerUtils.UsedMsids;
        try {
            List<String> files = new ArrayList<String>();
            Path path = new Path(srcpath);
            if (fileSystem.exists(path) && fileSystem.isDirectory(path)) {
                FileStatus[] fileStatuses = fileSystem.listStatus(path);
                for (FileStatus status : fileStatuses) {
                    if(status.isDirectory() && status.getPath().getName().contains("ms")){
                        files.add(status.getPath().toString());
                    }
                }
            }
            if(!usedMsids.isEmpty()){
                files = files.stream().filter(file -> usedMsids.contains(file.substring(file.length()-11))).collect(Collectors.toList());
            }
            return files;
        } catch (IOException e) {
        }
        return null;
    }

    public List<String> getDateDirList(String srcpath) {
        try {
            List<String> files = new ArrayList<String>();
            Path path = new Path(srcpath);
            if (fileSystem.exists(path) && fileSystem.isDirectory(path)) {
                for (FileStatus status : fileSystem.listStatus(path)) {
                    if(status.isDirectory() && status.getPath().getName().contains("-")){
                        files.add(status.getPath().toString());
                    }
                }
            }
            return files;
        } catch (IOException e) {
        }
        return null;
    }


    /**
     * 删除文件，实际上删除的是给定path路径的最后一个
     * 跟java中一样，也需要path对象，不过是hadoop.fs包中的。
     * 实际上delete(Path p)已经过时了，更多使用delete(Path p,boolean recursive)
     * 后面的布尔值实际上是对文件的删除，相当于rm -r
     * @param rootPath 要删除的文件路径
     * @return
     */
    public boolean myDropHdfsPath(String rootPath){
        boolean b = false;
        // drop the last path
        Path path = new Path(rootPath);
        try {
            b = fileSystem.delete(path,true);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(""+e);
        }
        return b;
    }

    public Integer getFileNumber(Path root){
        try {
            FileStatus[] fileStatuses = fileSystem.listStatus(root);
            Collection<Path> msPath = Arrays.stream(fileStatuses).filter(fileStatus -> fileStatus.isDirectory()).collect(Collectors.toMap(fi -> fi, fi -> fi.getPath())).values();
            FileStatus[] dateDir = fileSystem.listStatus(msPath.toArray(new Path[msPath.size()]));
            Collection<Path> datePath = Arrays.stream(dateDir).filter(date -> date.isDirectory()).collect(Collectors.toMap(d -> d, d -> d.getPath())).values();
            FileStatus[] receipt = fileSystem.listStatus(datePath.toArray(new Path[datePath.size()]));
            int size = Arrays.stream(receipt).filter(f -> f.isFile()).collect(Collectors.toList()).size();
            return size;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
