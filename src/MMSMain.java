import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
public class MMSMain {
	
	 public static void mainq(String[] args) throws Exception {  
		 //Merge merge = new Merge();
		 //merge.execute();
		 
		Path source = Paths.get(Constants.DATA_DIR + "sorted_chunk_2_" + 88 + ".txt");
		Path destination = Paths.get(Constants.DATA_DIR + "sorted_chunk_3_" + 50 + ".txt");
		try {
			Files.copy(source, destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }

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
    	
		long start = System.nanoTime();
		System.out.println(Constants.BLOCK_COUNT2);
		
	 	ArrayList<String> chunkFileList1=new ArrayList<>();
        ArrayList<String> chunkFileList2=new ArrayList<>();
        for(int fileCount = 1;fileCount<=Constants.FILE_COUNT;fileCount++){
    		long startTimeSplit = System.nanoTime();		
    		System.out.println(fileCount);
	        ChunkFileSplitter chunkFileSplitter=new ChunkFileSplitter(Constants.INPUT_FILE+fileCount+".txt");
	       
	        if(fileCount==1) {
	        	chunkFileList1=chunkFileSplitter.execute(Constants.BLOCK_COUNT1, fileCount, Constants.TUPLE_COUNT1);
	        }else {
	        	chunkFileList2=chunkFileSplitter.execute(Constants.BLOCK_COUNT2, fileCount, Constants.TUPLE_COUNT2);
	        }
    		long endTimeSplit   = System.nanoTime();
    		Performance.SplittingTime+=calcTotalTime(startTimeSplit,endTimeSplit);
        }
        
        Merge merge = new Merge();
		int fileCount = merge.execute(chunkFileList2.size() - 1);
        
        long stop = System.nanoTime();
	    long time = calcTotalTime(start, stop);
	    System.out.println("Splitting Sorting " +time/1000000000 + "seconds");
		    
	    start = System.nanoTime();
	    
    	BlockManager blockManager1=new BlockManager(chunkFileList1.size(), Constants.TUPLE_COUNT1, 1);
    	BlockManager blockManager2=new BlockManager(fileCount, Constants.TUPLE_COUNT2, 3);
  	
    	LineCounter lineCounter=new LineCounter();
        int lineCount_1 =lineCounter.count(Constants.INPUT_FILE + 1 +".txt");
        int lineCount_2 =lineCounter.count(Constants.INPUT_FILE + 2 +".txt");
        
    	String min_1 =blockManager1.execute();
    	String min_2 =blockManager2.execute();
    	
    	int lineCounter_1 = 1;
    	int lineCounter_2 = 1;
    	
    	OutputBlock outputBlock = new OutputBlock(Constants.TUPLE_COUNT1);
    	
        while(lineCounter_2 < lineCount_2){
        	int student_id_min_1 = Integer.parseInt(min_1.substring(0,8));
        	int student_id_min_2 = Integer.parseInt(min_2.substring(0,8));
        	if(student_id_min_1 < student_id_min_2){
        		min_1 = blockManager1.execute();
        		lineCounter_1++;
        	}
        	else if(student_id_min_1  > student_id_min_2){
        		min_2 = blockManager2.execute();
        		lineCounter_2++;
        	}
        	else if(student_id_min_1  == student_id_min_2){
        		outputBlock.add(min_1 + min_2.substring(8));
        		//System.out.println(Runtime.getRuntime().freeMemory());
        		min_2 = blockManager2.execute();
        		lineCounter_2++;
        	}
        	
        	if(!(lineCounter_1 < lineCount_1)){
        		int student_id_1 = Integer.parseInt(min_1.substring(0,8));
            	int student_id_2 = Integer.parseInt(min_2.substring(0,8));
        		if(!(student_id_1 == student_id_2)){
        			break;
        		}
        	}
        }
        
       outputBlock.finish();
        
       stop = System.nanoTime();
	   time = calcTotalTime(start, stop);
        
       System.out.println("Joining ime  " + time/1000000000 + "seconds");
	    
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