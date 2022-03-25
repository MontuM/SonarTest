package com.digitalroute.input;

import java.io.InputStream;

import com.digitalroute.output.BillingGateway;

/**
 * Defines a type for reading a particular type for file containg Call Data Records from a system.
 * (Based on a filetype or different data set in a file, there can be more than one implementation for this)
 * 
 * @author Mehul
 *
 */
public interface CallDataReader {

	/**
	 * Reads the input stream and scans the files for all the CDRs
	 * 
	 * @param ipStream -- Input Stream
	 * @param billingGateway -- Billing Gateway
	 */
	public void readCDRData(InputStream ipStream, BillingGateway billingGateway);
}
