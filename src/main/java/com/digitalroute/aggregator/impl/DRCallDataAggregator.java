package com.digitalroute.aggregator.impl;

import java.util.List;
import java.util.Map;

import com.digitalroute.aggregator.CallDataAggregator;
import com.digitalroute.model.CDRCollection;
import com.digitalroute.model.CallDataRecord;
import com.digitalroute.output.BillingGateway;

/**
 * Implementation for CallDataAggregator.
 * 
 * @author Mehul
 *
 */
public class DRCallDataAggregator implements CallDataAggregator {

	/**
	 * Gets the groomed call data records from {@link CDRCollection} and performs aggregation on each seesion.
	 * It also finds a match for the incomplete records and adds them to first matching record aggregation.
	 * It then takes each for {@link DRAggregatedRecord}, performs the aggregation and sends it to Billing Gateway
	 */
	@Override
	public void aggregateRecords(BillingGateway billingGateway) {
		
		Map<String,Map<String, DRAggregatedRecord>> callRecordsMap =  CDRCollection.getInstance().getCallRecordsCollection();
		List<CallDataRecord> incompleteRecords = CDRCollection.getInstance().getIncompleteRecords();
		
		/*
		 * This loop will go through each of the incomplete record and finds a match based on aNum+bNum. 
		 * If it doesn't finds any match, it logs as Error to Billing Gateway. 
		 */
		for (CallDataRecord callDataRecord : incompleteRecords) {
			String key1 = callDataRecord.getCallingNum()+"_"+callDataRecord.getReceivingNum();
			if(callRecordsMap.get(key1) != null) {
				/*
				 * If a match with aNum+bNum is found, then get the DRAggregatedRecord for the same and
				 * add it to the list for aggregation
				 */
				Map<String, DRAggregatedRecord> value1 = callRecordsMap.get(key1);
				String key2 = value1.keySet().iterator().next();
				DRAggregatedRecord aggregatedRecords = value1.get(key2);
				aggregatedRecords.addRecord(callDataRecord);
				value1.put(key2, aggregatedRecords);
				callRecordsMap.put(key1, value1);
			} else {
				billingGateway.logError(BillingGateway.ErrorCause.NO_MATCH, callDataRecord.getCallId(), callDataRecord.getSeqNum(),
						Long.toString(callDataRecord.getCallingNum()), Long.toString(callDataRecord.getReceivingNum()));
			}
		}
		
		/*
		 * Final aggregation of each records. It takes each of DRAggregatedRecord and calls getAggregatedRecord() on them.
		 * This will calculate the call duration as per the rules and flushes to Billing Gateway
		 */
		callRecordsMap.values().parallelStream()
			.forEach(entry -> entry.values().parallelStream()
					.forEach(value -> value.getAggregatedRecord(billingGateway)));
		
	}

}
