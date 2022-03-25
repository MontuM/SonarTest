package assignment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import com.digitalroute.Application;
import com.digitalroute.input.CallRecordsProcessor;
import com.digitalroute.input.impl.DRCallRecordsProcessor;

public class TestOutput {
	
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	@Before
	public  void setUp() {
	    System.setOut(new PrintStream(outputStreamCaptor));
	    CallRecordsProcessor processor = new DRCallRecordsProcessor();
    	processor.processBatch(Application.class.getClassLoader().getResourceAsStream("INFILE_ascii_big"));
	}
	
	
	@Test
    public void checkFinalAggregatedRecords() {
    	
    	String output = outputStreamCaptor.toString().trim();
    	
    	
    	assertThat(output,containsString("E, 11, 333333, 444444, 1, 55"));
    	assertThat(output,containsString("B, 7, 999999, 888888, 1, 22"));
    	assertThat(output,containsString("3B, 40, 999999, 888888, 0, 32")); 
    	assertThat(output,containsString("D, 28, 111111, 222222, 0, 43"));
    	assertThat(output,containsString("XX, 42, 654321, 123456, 2, 4"));
    	assertThat(output,containsString("X, 38, 654321, 123456, 2, 4"));
    	assertThat(output,containsString("Z, 35, 654321, 123456, 2, 3"));
    	assertThat(output,containsString("G, 32, 654321, 123456, 0, 15"));
    	assertThat(output,containsString("1E, 27, 333333, 444444, 0, 60"));
    	assertThat(output,containsString("A, 14, 112233, 445566, 0, 29"));
    	assertThat(output,containsString("C, 15, 333333, 111111, 0, 120"));
    	assertThat(output,containsString("End Batch :: TotalDuration = 387"));
    	
	}
	
	@Test
	public void checkNoMatchForRecords() {
    	
    	String output = outputStreamCaptor.toString().trim();
    	
    	assertThat(output,containsString("NO_MATCH, _, 12, 222233, 445566"));
    	assertThat(output,containsString("NO_MATCH, _, 17, 991111, 222222"));
    	assertThat(output,containsString("NO_MATCH, _, 18, 331111, 222222"));
    	assertThat(output,containsString("NO_MATCH, _, 19, 441111, 222222"));
    	assertThat(output,containsString("NO_MATCH, _, 21, 436890, 546781"));
    	assertThat(output,containsString("NO_MATCH, _, 22, 121212, 101010"));
    	assertThat(output,containsString("NO_MATCH, _, 23, 747474, 919191"));
    	assertThat(output,containsString("NO_MATCH, _, 24, 123123, 456456"));
    	assertThat(output,containsString("NO_MATCH, _, 29, 222233, 445566"));
	}

	@Test
	public void checkDuplicateRecords() {
    	
    	String output = outputStreamCaptor.toString().trim();
    	
    	assertThat(output,containsString("DUPLICATE_SEQ_NO, G, 32, 654321, 123456"));
    	assertThat(output,containsString("DUPLICATE_SEQ_NO, C, 15, 333333, 111111"));
	}
	
	

}
