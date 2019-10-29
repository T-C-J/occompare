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

    public static Set<String > UsedMsids = new HashSet(){{
        add("ms007000101");
        add("ms007000201");
        add("ms007000301");
        add("ms007000401");
        add("ms007000402");
        add("ms007000403");
        add("ms007000404");
        add("ms007000501");
        add("ms007000601");
        add("ms007000701");
        add("ms007000801");
        add("ms007000901");
        add("ms007000902");
        add("ms007000903");
        add("ms007000904");
        add("ms007001001");
        add("ms007001101");
        add("ms007001201");
        add("ms007001301");
        add("ms007001401");
        add("ms007001501");
        add("ms007001601");
        add("ms007001701");
        add("ms007001801");
        add("ms007001901");
        add("ms007001902");
        add("ms007001903");
        add("ms007001904");
        add("ms007002001");
        add("ms007002101");
        add("ms007002201");
        add("ms007002301");
        add("ms007002401");
        add("ms007002501");
        add("ms007002601");
        add("ms007002701");
        add("ms007002801");
        add("ms007002802");
        add("ms007002804");
        add("ms007002805");
        add("ms007002901");
        add("ms007003001");
        add("ms007003101");
        add("ms007003201");
        add("ms007003301");
        add("ms007003401");
        add("ms007003501");
        add("ms007003601");
        add("ms007003701");
        add("ms007003801");
        add("ms007003901");
        add("ms007004001");
        add("ms007004101");
        add("ms007004201");
        add("ms007004301");
        add("ms007004401");
        add("ms007004501");
        add("ms007004601");
        add("ms007004701");
        add("ms007004801");
        add("ms007004802");
        add("ms007004901");
        add("ms007004902");
        add("ms007004903");
        add("ms007004904");
        add("ms007005001");
        add("ms007005101");
        add("ms007005201");
        add("ms007005301");
        add("ms007005401");
        add("ms007005501");
        add("ms007005601");
        add("ms007005701");
        add("ms007005801");
        add("ms007005901");
        add("ms007006001");
        add("ms007006101");
        add("ms007006201");
        add("ms007006301");
        add("ms007006401");
        add("ms007006501");
        add("ms007006601");
        add("ms007006701");
        add("ms007006801");
        add("ms007006901");
        add("ms007007001");
        add("ms007007101");
        add("ms007007201");
        add("ms007007301");
        add("ms007007401");
        add("ms007007601");
        add("ms007007701");
        add("ms007007801");
        add("ms007007901");
        add("ms007008001");
        add("ms007008101");
        add("ms007008102");
        add("ms007008201");
        add("ms007008301");
        add("ms007008401");
    }};
//    public static Set<String > UsedMsids = new HashSet();
    public static Date UPDATETIME = new Date();


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
