import java.lang.Runtime;

public class Constants {
    public static String DATA_DIR = "C:\\Users\\vaish\\Desktop\\data\\";
    public static String TEMP_DIR = "C:\\Users\\vaish\\Desktop\\temp\\";
    public static String UNSORTED_FILE_PREFIX = "unsorted_chunk_";
    public static String SORTED_FILE_PREFIX = "sorted_chunk_";
    public static String INPUT_FILE = "JoinT";
    public static String OUTPUT_FILE = "joinOutput.txt";
    
    public static String GPA_NLJ="GPA_NLJ.txt";
    public static String GPA_SBJ="GPA_SBJ.txt";
    
    public static String BAG_OUTPUT_FILE = "bag_difference.txt";
    public static int RAM_SIZE=(int)Runtime.getRuntime().freeMemory();
    public static int BLOCK_SIZE=4096;
    public static int BLOCK_COUNT1=(RAM_SIZE/BLOCK_SIZE)/2;
    public static int BLOCK_COUNT2=480;
    public static int NESTED_LOOP_BLOCK_SIZE=1261591;
    public static int TUPLE_COUNT1=40;
    public static int TUPLE_COUNT2=130;
    public static int FILE_COUNT=2;
    
    public static double getMultiplier(){
    	if(RAM_SIZE>8*1024*1024){
    		return 4;
    	}else{
    		return 2;
    	}
    }
    
    public static int getBagDiffMultiplier(){
    	if(RAM_SIZE>8*1024*1024){
    		return 2;
    	}else{
    		return 1;
    	}
    }
}
