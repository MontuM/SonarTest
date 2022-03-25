package com.digitalroute.model;

import com.digitalroute.output.BillingGateway;

/**
 * Defines a type which will hold all the related/matching {@link CallDataRecord} together.
 * It will also perform the aggregation based on the rules defined by the system and type of record.
 * 
 * @author Mehul
 *
 */
public interface AggregatedRecord {
	
	/**
	 * Performs the aggregation of the all the CallDataRecord clubbed together as per the rules and
	 * sends it to the Billing Gateway. 
	 * 
	 * @param billingGateway Gateway where the records gets consumed.
	 */
	public void getAggregatedRecord(BillingGateway billingGateway);
	
	/**
	 * Add a new {@link CallDataRecord}n to the list.
	 * 
	 * @param callDataRecord record to be added
	 */
	public void addRecord(CallDataRecord callDataRecord);
	
	/**
	 * Returns true if aNum & bNum of a record are matching.
	 * 
	 * @param callDataRecord  Record to be compared
	 * @return true/false
	 */
	public boolean isMatch(CallDataRecord callDataRecord);

}
