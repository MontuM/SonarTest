package com.digitalroute.output.impl;

import com.digitalroute.output.BillingGateway;

/**
 * Sample Implementation for BillingGateway
 * 
 * @author Mehul
 *
 */
public class DRBillingGateway implements BillingGateway {

	@Override
	public void beginBatch() {
		System.out.println("Billing Gateway :: Begin Batch");
	}

	@Override
	public void consume(String callId, int seqNum, String aNum, String bNum, byte causeForOutput, int duration) {
		System.out.println("consume: " + callId +", "+  seqNum +", "+ aNum +", "+ bNum +", "+ causeForOutput +", "+ duration);

	}

	@Override
	public void endBatch(long totalDuration) {
		System.out.println("Billing Gateway :: End Batch :: TotalDuration = " + totalDuration);
	}

	@Override
	public void logError(ErrorCause errorCause, String callId, int seqNum, String aNum, String bNum) {
		System.out.println("logError " +errorCause  +", "+ callId +", "+  seqNum +", "+ aNum +", "+ bNum);

	}

}
