package com.digitalroute.input.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.digitalroute.aggregator.impl.DRAggregatedRecord;
import com.digitalroute.input.CallDataReader;
import com.digitalroute.model.CDRCollection;
import com.digitalroute.model.CallDataRecord;
import com.digitalroute.output.BillingGateway;

/**
 * Reader Implementation for a particular type of file with CDRs. 
 * 
 * @author Mehul
 *
 */
public class DRFileReader implements CallDataReader {

	
	/**
	 * Reads the input stream and scans the files for all the CDRs
	 */
	@Override
	public void readCDRData(InputStream ipStream,BillingGateway billingGateway) {
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(ipStream));
		
		Map<String,Map<String, DRAggregatedRecord>> callRecordsMap =  CDRCollection.getInstance().getCallRecordsCollection();
		List<CallDataRecord> incompleteRecords = CDRCollection.getInstance().getIncompleteRecords();
		
		billingGateway.beginBatch();
		
		/*
		 * Reads the file line by line, and process each CDR string to a CallDataRecord instance. 
		 * This instance is added to data structure for further processing. Data structure is defined as below, which aids 
		 * the aggregation process and search/match operation if any in case of incomplete records.
		 * 
		 * Data Structure is a Map of Maps. 
		 * 1. Internal map stores data as (key,value)-> (callId, DRAggregatedRecord). DRAggregatedRecord, holds all 
		 *    the calls with same callId.
		 *    
		 * 2. External map stores the above map with a combination of aNum and bNum keys. Data is stored as
		 *    (key,value) -> (aNUm_bNum, Map of (callId, DRAggregatedRecord))
		 *    
		 *    Example data:
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
		 *     .......
		 *     .....
		 *     and so on...
		 *  
		 *  3. It will also collect all the incomplete records to a separate list to find a matching DRAggregatedRecord. 
		 */
		String line = null;
		try {
			while(((line = fileReader.readLine()) != null)) {
				if(!("".equals(line.trim()))) {
					CallDataRecord callDataRecord = new CallDataRecord(line);
					//key1 is aNum_bNum of callDataRecord
					String key1 = callDataRecord.getCallingNum()+"_"+callDataRecord.getReceivingNum();
					//key2 is callId of callDataRecord
					String key2 = callDataRecord.getCallId();
					
					if("_".equals(key2)) {
						//Collects incomplete record in different list
						incompleteRecords.add(callDataRecord);
					} else if(callRecordsMap.get(key1) != null) { // Check with Key1 if the similar CDR has been read
						if(callRecordsMap.get(key1).get(key2) != null) { // if yes, then check if CDR with same callID is there
						    // if DRAggregatedRecord with key2 exists, add the CDR to it.
							Map<String, DRAggregatedRecord> value1 = callRecordsMap.get(key1);
							DRAggregatedRecord aggregatedRecords = value1.get(key2);
							aggregatedRecords.addRecord(callDataRecord);
							value1.put(key2, aggregatedRecords);
							callRecordsMap.put(key1, value1);
						} else { // if DRAggregatedRecord for a callId doesn't exists, create one
							Map<String, DRAggregatedRecord> value1 = callRecordsMap.get(key1);
							DRAggregatedRecord aggregatedRecord = new DRAggregatedRecord(callDataRecord);
							value1.put(key2, aggregatedRecord);
							callRecordsMap.put(key1, value1);
						}
					} else {
						// If data with key1, doesn't exists in outer map then create one.
						Map<String, DRAggregatedRecord> value1 = new Hashtable<>();
						DRAggregatedRecord aggregatedRecord = new DRAggregatedRecord(callDataRecord);
						value1.put(key2, aggregatedRecord);
						callRecordsMap.put(key1, value1);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error while reading the file. \\n"+ e.getStackTrace());
		} 
		
	}

}
