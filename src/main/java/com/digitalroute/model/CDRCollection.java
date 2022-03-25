package com.digitalroute.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.digitalroute.aggregator.impl.DRAggregatedRecord;

/**
 * This class defines the internal data structure to hold the CDRs coming from the file.
 * It's a singleton class. Data Structure is a map of maps and will hold the data as follows.
 * 
 * Data Structure is a Map of Maps. 
 *  1. Internal map stores data as (key,value)-> (callId, DRAggregatedRecord). DRAggregatedRecord, holds all 
 *     the calls with same callId.
 *    
 * 2. External map stores the above map with a combination of aNum and bNum keys. Data is stored as
 * 	  (key,value) -> (aNUm_bNum, Map of (callId, DRAggregatedRecord))
 * 
 * Example data:
 *    A:1,112233,445566,1,5 --> CallDataRecord
 *	  A:2,112233,445566,1,6 --> CallDataRecord
 *       Above two records will be part one DRAggregatedRecord
 *	  B:3,999999,888888,1,10 --> CallDataRecord
 *       This will be one more DRAggregatedRecord
 *       
 *    These CDRs will convert to callRecordsMap as below
 *    Key1 = 112233_445566
 *    key2 = A
 *    (112233_445566, (A, DRAggregatedRecord for all callId A))
 *    (999999_888888, (B, DRAggregatedRecord for all callId B))
 *    
 *  More details on how the data is populated in {@code DRFileReader}
 *    
 * @author Mehul
 *
 */
public class CDRCollection {
	
	private  Map<String,Map<String, DRAggregatedRecord>> callRecordsMap;
	private  List<CallDataRecord> incompleteRecords;
	private int totalCallDurationInFile = 0;
	
	private static CDRCollection instance = null;
	
	private CDRCollection() {
		callRecordsMap = new Hashtable<String, Map<String,DRAggregatedRecord>>();
		incompleteRecords = new ArrayList<>();
	}
	
	public static CDRCollection getInstance() {
		if (null == instance) {
			synchronized (CDRCollection.class) {
	        if(null == instance) {
	          instance = new CDRCollection();
	        }
	      }
	    }
	    return instance;
	}
	
	public Map<String,Map<String, DRAggregatedRecord>> getCallRecordsCollection() {
		return callRecordsMap;
	}
	

	public List<CallDataRecord> getIncompleteRecords() {
		return incompleteRecords;
	}
	
	public void setTotalCallDurationInFile(int duration) {
		synchronized (CDRCollection.class) {
			totalCallDurationInFile += duration;
		}
	}
	
	public int getTotalCallDurationInFile() {
		return totalCallDurationInFile;
	}
	
	public void clearCollection() {
		synchronized (CDRCollection.class) {
			callRecordsMap.clear();
			incompleteRecords.clear();
			totalCallDurationInFile = 0;
		}
	}


}
