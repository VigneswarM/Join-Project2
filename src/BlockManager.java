import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * This Class is responsible for Merging all the Sorted chunk files
 */
public class BlockManager {

    private ArrayList<InputBlock> _inputBlocks = new ArrayList<>();
    
    ArrayList<Integer>sublistCount = new ArrayList<>();
    ArrayList<String> blocksnames1 = new ArrayList<>();
    ArrayList<String> blocksnames2 = new ArrayList<>();
    ArrayList<String> outputblock = new ArrayList<>();
    ArrayList<String> blocks = new ArrayList<>();
    int bag1sublist=0;
    int bag2sublist=0;
	int matching =0;
    String min ="0";
    
    OutputBlock _outputBlock;
    boolean test;
    int k =0;
    
    private int _fileCount;
    private int _currFileNumber;
    public BlockManager(int fileCount,int tupleCountInBlock,int currFileNumber) {
        _fileCount = fileCount;
        _currFileNumber=currFileNumber;
        for (int i = 0; i < fileCount; i++) {
             InputBlock ip = new InputBlock(Constants.SORTED_FILE_PREFIX +currFileNumber+"_"+ i + ".txt",tupleCountInBlock);
             _inputBlocks.add(ip);
        }
        //System.out.println(_inputBlocks.size());
        _outputBlock = new OutputBlock(tupleCountInBlock);

        //System.out.println(_inputBlocks.size());
    }
    
    public void finish() {
    	_outputBlock.finish();
    	for (InputBlock inputblock : _inputBlocks) {
    		inputblock.finish();
    	}
    }

    /**
     * Fetches the top most item in each input block (Each sorted chunk)
     * 
     * Sort the items
     * 
     * Adds the first element in the sorted list in the output block
     * 
     * Fetches the next item only from the block from which the item is added to output
     * 
     */
    public String execute() {
        List<InputBlock> blocks = _inputBlocks.stream().filter(x -> x.isDataAvailable()).collect(Collectors.toList());
        //System.out.println(blocks.size());
        InputBlock minInputBlock = blocks.get(0);
        for (InputBlock inputBlock : blocks) {
        	if(_currFileNumber==1){
	            if (new Student(inputBlock.getCurrentData()).ID < new Student(minInputBlock.getCurrentData()).ID) {
	                minInputBlock = inputBlock;
	            }
        	}else{
        		 if (new StudentCourse(inputBlock.getCurrentData()).ID < new StudentCourse(minInputBlock.getCurrentData()).ID) {
                     minInputBlock = inputBlock;
                 }
        	}
        }
        String currentMin = minInputBlock.getCurrentData().toString();
        //_outputBlock.add(minInputBlock.getCurrentData().toString());
        minInputBlock.getData();
        return currentMin;
    }
    
    
   
    
    
    boolean Tuplecompare(String minOfT1) {
		boolean flag = false;
   		for(InputBlock inputBlock:_inputBlocks){
   			try {
				for(String line:inputBlock._currentBlock){
					if(line.substring(0, 8).equals(minOfT1.substring(0,8))){
						_outputBlock.add(line+" "+minOfT1);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
   		}	
   		return flag;
    }
	

	public void loadBlocks() {
    	for(InputBlock inputBlock:_inputBlocks) {
    		inputBlock.load();
    	}   	  
    }
}