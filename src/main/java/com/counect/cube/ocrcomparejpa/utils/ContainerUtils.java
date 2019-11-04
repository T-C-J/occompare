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
        add("ms007005801");
        add("ms007002401");
        add("ms007003201");
        add("ms007008102");
        add("ms007002804");
        add("ms007001904");
        add("ms007007601");
        add("ms007004904");
        add("ms007002301");
        add("ms007004901");
    }};
}
