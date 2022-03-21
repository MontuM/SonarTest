package com.digitalroute.model;

/**
 * Domain class... Defines the Call Data Records and it's fields.
 * 
 * @author Mehul
 *
 */
public class CallDataRecord {

	private String callId;
	private int seqNum;
	private long callingNum;
	private long receivingNum;
	private int outputCause;
	private int duration;
	
	
	public CallDataRecord(String callId, int seqNum, long callingNum, long receivingNum, int outputCause, int duration) {
		this.callId = callId;
		this.seqNum = seqNum;
		this.callingNum = callingNum;
		this.receivingNum = receivingNum;
		this.outputCause = outputCause;
		this.duration = duration;
	}
	
	public CallDataRecord() {
		this.callId = "";
		this.seqNum = -1;
		this.callingNum = 0;
		this.receivingNum = 0;
		this.outputCause = -999;
		this.duration = -999;
	}
	
	public CallDataRecord(String cdrString) {
		initRecordFromString(cdrString);
	}
	
	private void initRecordFromString(String cdrString) {
		//A:1,112233,445566,1,5
		int index = cdrString.indexOf(":");
		if(index != -1) {
			callId = cdrString.substring(0, index);
			String[] cdrSplits = cdrString.substring(index+1, cdrString.length()).split(",");
			if(cdrSplits.length == 5) {
				seqNum = Integer.valueOf(cdrSplits[0].trim());
				callingNum = Long.valueOf(cdrSplits[1].trim());
				receivingNum = Long.valueOf(cdrSplits[2].trim());
				outputCause = Integer.valueOf(cdrSplits[3].trim());
				duration = Integer.valueOf(cdrSplits[4].trim());
			}
		}
	}
	
	
	
	
	public String getCallId() {
		return callId;
	}
	public void setCallId(String callId) {
		this.callId = callId;
	}
	public int getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}
	public long getCallingNum() {
		return callingNum;
	}
	public void setCallingNum(long callingNum) {
		this.callingNum = callingNum;
	}
	public long getReceivingNum() {
		return receivingNum;
	}
	public void setReceivingNum(long receivingNum) {
		this.receivingNum = receivingNum;
	}
	public int getOutputCause() {
		return outputCause;
	}
	public void setOutputCause(int outputCause) {
		this.outputCause = outputCause;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public int hashCode() {
		int result = 19;
		result =  37 * result + callId.hashCode();
		result = 37 * result + seqNum;
		result = 37 * result + (int)callingNum;
		result = 37 * result + (int)receivingNum;
		result = 37 * result + outputCause;
		result = 37 * result + duration;
		
		return result;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(callId);
		buffer.append(":");
		buffer.append(seqNum);
		buffer.append(",");
		buffer.append(callingNum);
		buffer.append(",");
		buffer.append(receivingNum);
		buffer.append(",");
		buffer.append(outputCause);
		buffer.append(",");
		buffer.append(duration);
		
		return buffer.toString();
	}
	
	
	
	
}
