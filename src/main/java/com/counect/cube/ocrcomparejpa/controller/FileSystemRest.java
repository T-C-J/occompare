package com.counect.cube.ocrcomparejpa.controller;

import com.counect.cube.ocrcomparejpa.service.HDFSService;
import com.counect.cube.ocrcomparejpa.service.SyncDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *  主要是对文件的操作
 *  initFileSystem          初始化文件系统
 *  addAllFile              向文件系统添加所有文件
 *  addFileByDate/{date}    根据时间向文件系统添加水单
 *  downlodAllReceipt       下载文件系统中所有的水单
 *  downlodReceipt/{date}   根据日期下载水单
 *  removeAllReceipt        清空文件系统
 *
 */
@RestController
@RequestMapping("/filesystem")
public class FileSystemRest {
    @Autowired
    HDFSService hdfsService;

    @Autowired
    SyncDataService syncDataService;

    @RequestMapping("initFileSystem")
    public boolean initFileSystem(){
        return syncDataService.initFileSystem();
    }

    @RequestMapping("addAllFile")
    public boolean addAllFile(){
        return syncDataService.addAllFile();
    }

    @RequestMapping("addFileByDate/{date}")
    public boolean addFileByDate(@PathVariable("date") String date){
        return syncDataService.addFileByDate(date);
    }


    /**
     *  下载所有文件 这个方法没有msid的限制 filesystem里面的所有文件都会下载
     * @return
     */
    @RequestMapping("downlodAllReceipt")
    public Boolean downlodReceipt(){
        return hdfsService.getAllFile();
    }

    @RequestMapping("downlodReceipt/{date}")
    public Boolean downlodReceipts(@PathVariable("date") String  date){
        return hdfsService.getFileByDate(date);
    }

    /**
     *  删除 filesystem 里所有的文件
     * @return
     */
    @RequestMapping("removeAllReceipt")
    public boolean removeAllReceipt(){
        return hdfsService.removeAllFile();
    }
}
