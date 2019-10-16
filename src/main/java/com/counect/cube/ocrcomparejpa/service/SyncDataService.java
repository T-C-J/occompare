package com.counect.cube.ocrcomparejpa.service;


import java.util.Collection;
import java.util.Set;

public interface SyncDataService {

    boolean syncDataAll();

    boolean syncDataByMsid(String msid);


    boolean syncDataByMsids(Collection msids);
}
