package com.counect.cube.ocrcomparejpa.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ContainerUtils {

//    public static Set<String > UsedMsids = new HashSet(){{
//        add("ms132000610");
//        add("ms132000112");
//        add("ms132000124");
//        add("ms132000201");
//        add("ms132000210");
//        add("ms132000502");
//    }}
    ;public static Set<String > UsedMsids = new HashSet();

    public static boolean analysisFile(MultipartFile file){
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(file.getInputStream());
            br = new BufferedReader(isr);
            String line = null;
            List<List<String>> strs = new ArrayList<List<String>>();
            while ((line = br.readLine()) != null){
                strs.add(Arrays.asList(line.split(",")));
            }
            JSONArray array = toJsonArray(strs);
            List<Object> collect = array.stream().collect(Collectors.toList());
            for(Object object:collect){
                Map map = (Map) object;
                Object msid = map.getOrDefault("msid", null);
                if(msid != null){
                    UsedMsids.add((String) msid);
                }
            }
        } catch (IOException e) {
        }finally {
            try {
                if (br != null){
                    br.close();
                }
                if (isr != null){
                    isr.close();
                }
            } catch (IOException e) {
                //
            }
        }
        return true;
    }

    private static JSONArray toJsonArray(List<List<String>> strs){
        JSONArray array = new JSONArray();
        for (int i = 1; i < strs.size(); i++) {
            JSONObject object = new JSONObject();

            for (int j = 0; j < strs.get(0).size(); j++) {
                object.put(strs.get(0).get(j),strs.get(i).get(j));
            }
            array.add(object);
        }
        return array;
    }
}
