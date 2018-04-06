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
    	
		
		LineCounter lineCounter=new LineCounter();
        int lineCount_1 =lineCounter.count(Constants.INPUT_FILE + 1 +".txt");
        int lineCount_2 =lineCounter.count(Constants.INPUT_FILE + 2 +".txt");

	    NestedLoopJoin nestedLoopJoin=new NestedLoopJoin(lineCount_1);
	    nestedLoopJoin.execute();
		
	 	ArrayList<String> chunkFileList1=new ArrayList<>();
        ArrayList<String> chunkFileList2=new ArrayList<>();
        for(int fileCount = 1;fileCount<=Constants.FILE_COUNT;fileCount++){
	        if(fileCount==1) {
				ChunkFileSplitter chunkFileSplitter=new ChunkFileSplitter(Constants.INPUT_FILE+fileCount+".txt",false);
	        	chunkFileList1=chunkFileSplitter.execute(Constants.BLOCK_COUNT1, fileCount, Constants.TUPLE_COUNT1);
	        }else {
				ChunkFileSplitter chunkFileSplitter=new ChunkFileSplitter(Constants.INPUT_FILE+fileCount+".txt",true);
	        	chunkFileList2=chunkFileSplitter.execute(Constants.BLOCK_COUNT2, fileCount, Constants.TUPLE_COUNT2);
	        }
        }
        
        Merge merge = new Merge();
		int fileCount = merge.execute(chunkFileList2.size() - 1);

	    SortJoin sortJoin = new SortJoin(chunkFileList1.size(), fileCount, lineCount_1, lineCount_2);
	    sortJoin.execute();

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