package com.digitalroute.input.impl;

import java.io.InputStream;

import com.digitalroute.aggregator.CallDataAggregator;
import com.digitalroute.aggregator.impl.DRCallDataAggregator;
import com.digitalroute.input.CallDataReader;
import com.digitalroute.input.CallRecordsProcessor;
import com.digitalroute.model.CDRCollection;
import com.digitalroute.output.BillingGateway;
import com.digitalroute.output.impl.DRBillingGateway;

/**
 * Implementation for call records processor.
 * 
 * @author Mehul
 *
 */
public class DRCallRecordsProcessor implements CallRecordsProcessor {

	BillingGateway billingGateway = null;
	
	public DRCallRecordsProcessor() {
		billingGateway = new DRBillingGateway();
	}
	
	@Override
	public void processBatch(InputStream in) {
		// Read the input file
		CallDataReader callDataReader = new DRFileReader();
		callDataReader.readCDRData(in,billingGateway);
		
		//Aggregate the data
		CallDataAggregator callDataAggregator = new DRCallDataAggregator();
		callDataAggregator.aggregateRecords(billingGateway);
		billingGateway.endBatch(CDRCollection.getInstance().getTotalCallDurationInFile());
		
		
		/*
		 * To be called after all the aggregation is done.
		 * If aggregation is done across multiple files, than this has to 
		 * cleared after all the files in a batch are processed.
		 */
		CDRCollection.getInstance().clearCollection();
	}
	
	
	/*
	 * [ #### Upgrade / New requirement changes #### ]
	 * If  there is requirement to read data with a different file type or format,
	 * CallDataReader and CallDataAggregator could be injected,dynamically. A factory 
	 * Pattern can come into here, which will inject the implementation based on type 
	 * or some distinguishing key. 
	 * 
	 */
	

}
