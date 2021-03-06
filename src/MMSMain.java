import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
public class MMSMain {

	/**
	 * Loads the Input files
	 * 
	 * Split the input file into Small unsorted chunk files
	 * 
	 * Sort each Chunk file
	 * 
	 * Merge the Sorted Chunk file and create output file
	 * 
	 * Compute the Bag difference between the Sorted output files
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {  
    	
		LineCounter lineCounter=new LineCounter();
        int lineCount_1 =lineCounter.count(Constants.INPUT_FILE + 1 +".txt");
        int lineCount_2 =lineCounter.count(Constants.INPUT_FILE + 2 +".txt");
    	long start = System.nanoTime();
        NLJ(lineCount_1);
        long stop = System.nanoTime();
        System.out.print("NLJ Done! Time Taken:");
        System.out.println(calcTotalTime(start, stop)/1000000000 + "Seconds");

    	start = System.nanoTime();
        SBJ(lineCount_1, lineCount_2);
        stop = System.nanoTime();
        System.out.print("SBJ Done!!! Time Taken:");
        System.out.println(calcTotalTime(start, stop)/1000000000 + "Seconds");
    }
    
    public static void NLJ(int lineCount_1) {
		NestedLoopJoin nestedLoopJoin=new NestedLoopJoin(lineCount_1);
	    nestedLoopJoin.execute();
    }
    
    public static int Merge(int size){
    	long start = System.nanoTime();
    	Merge merge = new Merge();
		int fileCount = merge.execute(size - 1);
		long stop = System.nanoTime();
		/*System.out.print("Merge Done!!! Time Taken:");
		System.out.println(calcTotalTime(start, stop)/1000000000 + "Seconds");*/
		return fileCount;
		
    }
    
    public static void SBJ(int lineCount_1, int lineCount_2){
    	ArrayList<String> chunkFileList1=new ArrayList<>();
        ArrayList<String> chunkFileList2=new ArrayList<>();
        ChunkFileSplitter chunkFileSplitter = null;
        for(int fileCount = 1;fileCount<=Constants.FILE_COUNT;fileCount++){
	        if(fileCount==1) {
				chunkFileSplitter=new ChunkFileSplitter(Constants.INPUT_FILE+fileCount+".txt",false);
	        	chunkFileList1=chunkFileSplitter.execute(Constants.BLOCK_COUNT1, fileCount, Constants.TUPLE_COUNT1);
	        }else {
				chunkFileSplitter=new ChunkFileSplitter(Constants.INPUT_FILE+fileCount+".txt",true);
	        	chunkFileList2=chunkFileSplitter.execute(Constants.BLOCK_COUNT2, fileCount, Constants.TUPLE_COUNT2);
	        }
        }
	    
        int fileCount = Merge(chunkFileList2.size());

	    SortJoin sortJoin = new SortJoin(chunkFileList1.size(), fileCount, lineCount_1, lineCount_2);
	    long start = System.nanoTime();
	    sortJoin.execute();
	    long stop = System.nanoTime();
		System.out.print("Sort Join Done!!! Time Taken:");
		System.out.println(calcTotalTime(start, stop)/1000000000 + "Seconds");
    }
    
    /**
     * Compute the difference between start and end time
     * @param startTime
     * @param endTime
     * @return
     */
    public static long calcTotalTime(long startTime,long endTime){
    	long totalTime = endTime - startTime;
    	return totalTime;
    }
    
    /**
     * Prints the performance metrics
     */
 /*   public  static void printPerformance(){
    	    System.out.println("Disk I/O Performance with RAM size :: "+(Runtime.getRuntime().totalMemory())/(1024*1024)+"MB");
    	 	System.out.println("Disk I/O for Splitting and Sorting files :: "+Performance.SplitterDiskIO);
	        System.out.println("Disk I/O for reading files during Merge :: "+ Performance.MergeReadDiskIO);
	        System.out.println("Disk I/O for writing files during Merge :: "+Performance.MergeWriteDiskIO);
	        System.out.println("Disk I/O for finding Bag difference :: "+Performance.BagDifferenceDiskIO);
	        
	        System.out.println("Time taken by Chunk File Splitter and Sorter :: "+Performance.SplittingTime/1000000000+"seconds");
	        System.out.println("Time taken by Chunk File Merger :: "+Performance.MergingTime/1000000000+"seconds");
	        System.out.println("Time taken for Bag Difference  :: "+Performance.BagDifferenceTime/1000000000+"seconds");
    }*/
}