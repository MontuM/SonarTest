package com.digitalroute.aggregator;

import com.digitalroute.output.BillingGateway;

public interface CallDataAggregator {
	
	/**
	 * Performs aggregation on call data records, and flushes them to Billing Gateway
	 * 
	 * @param billingGateway
	 */
	public void aggregateRecords(BillingGateway billingGateway);
}
