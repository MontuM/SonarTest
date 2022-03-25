package com.digitalroute.aggregator.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.digitalroute.model.AggregatedRecord;
import com.digitalroute.model.CDRCollection;
import com.digitalroute.model.CallDataRecord;
import com.digitalroute.output.BillingGateway;

/**
 * (Implementation for AggregatedRecord)
 * 
 * Holds all the related/matching {@link CallDataRecord} together.
 * It will also perform the aggregation based on the rules defined by the system and type of record
 * 
 * @author Mehul
 *
 */
public class DRAggregatedRecord implements AggregatedRecord {
	
	private List<CallDataRecord> cdrList;
	private String key;
	private boolean hasIncompleteRcd;
	
	
	public DRAggregatedRecord(CallDataRecord callDataRecord) {
		key = callDataRecord.getCallId();
		cdrList = new ArrayList<>();
		cdrList.add(callDataRecord);
		hasIncompleteRcd = false;
	}
	
	@Override
	public void getAggregatedRecord(BillingGateway billingGateway) {
		int totalDuration = 0;
		// a quick set to check for any duplicate sequence number in the record list
		TreeSet<CallDataRecord> callRecordSet = new TreeSet<>((CallDataRecord o1, CallDataRecord o2)
				-> o1.getSeqNum() - o2.getSeqNum());
		
		for (CallDataRecord callDataRecord : cdrList) {
			if(!callRecordSet.contains(callDataRecord)) {
				/*
				 * if a record doen't have a duplicate Sequece number than it's time duration is added here.
				 */
				callRecordSet.add(callDataRecord);
				totalDuration += callDataRecord.getDuration();
			} else {
				// Log duplicate Sequence number to BillingGateway
				billingGateway.logError(BillingGateway.ErrorCause.DUPLICATE_SEQ_NO, key, callDataRecord.getSeqNum(),
						Long.toString(callDataRecord.getCallingNum()), Long.toString(callDataRecord.getReceivingNum()));
			}
		}
		//Get the record with highest Sequence number in the session
		CallDataRecord lastSeqRecord = callRecordSet.last();
		
		// Send aggregated record to BillingGateway
		billingGateway.consume(key, lastSeqRecord.getSeqNum(), Long.toString(lastSeqRecord.getCallingNum()), Long.toString(lastSeqRecord.getReceivingNum()),
				(byte)((hasIncompleteRcd)? 0:lastSeqRecord.getOutputCause()), totalDuration);
		
		CDRCollection.getInstance().setTotalCallDurationInFile(totalDuration);
	}
	
	/**
	 * Returns true if aNum & bNum of a record are matching.
	 */
	@Override
	public boolean isMatch(CallDataRecord callDataRecord) {
		CallDataRecord cdrFromList = cdrList.get(0);
		/*
		 * Compares the aNum (calling number) and bNum (receiving number) for the given record.
		 * 
		 */
		
		return (cdrFromList.getCallingNum() == callDataRecord.getCallingNum() && 
				cdrFromList.getReceivingNum() == callDataRecord.getReceivingNum())? true:false;
	}
	
	/*
	 * adds a record to the aggregated list. 
	 */
	@Override
	public void addRecord(CallDataRecord callDataRecord) {
		if(key.equals(callDataRecord.getCallId())) {
			cdrList.add(callDataRecord);
		} else if(callDataRecord.getCallId().equals("_") && isMatch(callDataRecord)) {
			//If a record is inComplete, it checks for a match and then adds it here.
			cdrList.add(callDataRecord);
			hasIncompleteRcd = true;  // also update the flag to indicate list contains in-complete records
		}
	}

	public List<CallDataRecord> getCdrList() {
		return cdrList;
	}

	public String getKey() {
		return key;
	}
	

}
