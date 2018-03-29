import java.util.ArrayList;
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
    	 	ArrayList<String> chunkFileList1=new ArrayList<>();
	        ArrayList<String> chunkFileList2=new ArrayList<>();
	        for(int fileCount = 1;fileCount<=Constants.FILE_COUNT;fileCount++){
    		long startTimeSplit = System.nanoTime();		
    		System.out.println(fileCount);
	        ChunkFileSplitter chunkFileSplitter=new ChunkFileSplitter(Constants.INPUT_FILE+fileCount+".txt");
	       
	        if(fileCount==1) {
	        	chunkFileList1=chunkFileSplitter.execute(Constants.BLOCK_COUNT1,fileCount);
	        }else {
	        	chunkFileList2=chunkFileSplitter.execute(Constants.BLOCK_COUNT2,fileCount);
	        }
    		long endTimeSplit   = System.nanoTime();
    		Performance.SplittingTime+=calcTotalTime(startTimeSplit,endTimeSplit);
    	}
    	
    	BlockManager blockManager1=new BlockManager(chunkFileList1.size(),Constants.TUPLE_COUNT1);
    	BlockManager blockManager2=new BlockManager(chunkFileList2.size(),Constants.TUPLE_COUNT2);
    	
    	blockManager1.loadBlocks();
    	blockManager2.loadBlocks();
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
    public  static void printPerformance(){
    	    System.out.println("Disk I/O Performance with RAM size :: "+(Runtime.getRuntime().totalMemory())/(1024*1024)+"MB");
    	 	System.out.println("Disk I/O for Splitting and Sorting files :: "+Performance.SplitterDiskIO);
	        System.out.println("Disk I/O for reading files during Merge :: "+ Performance.MergeReadDiskIO);
	        System.out.println("Disk I/O for writing files during Merge :: "+Performance.MergeWriteDiskIO);
	        System.out.println("Disk I/O for finding Bag difference :: "+Performance.BagDifferenceDiskIO);
	        
	        System.out.println("Time taken by Chunk File Splitter and Sorter :: "+Performance.SplittingTime/1000000000+"seconds");
	        System.out.println("Time taken by Chunk File Merger :: "+Performance.MergingTime/1000000000+"seconds");
	        System.out.println("Time taken for Bag Difference  :: "+Performance.BagDifferenceTime/1000000000+"seconds");
    }
}