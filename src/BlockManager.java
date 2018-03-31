import java.io.File;
import java.io.FileInputStream;
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
        System.out.println(_inputBlocks.size());
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
    public void execute() {
    	
    	System.out.println("inside execute");
    	System.out.println(sublistCount.size());
    	
    	System.out.println(blocksnames1);
    	System.out.println(blocksnames2);
    	

    	System.out.println("bags");
    	System.out.println(bag1sublist);
    	System.out.println(bag2sublist);
    	
    	
    	
    	
    	/*for(int i=0;i<sublistCount.size();i++)
    	{
    		for(int j=0;j<sublistCount.get(i);j++)
    		{
    			if(i==0)
    				blocks.add(Constants.SORTED_FILE_PREFIX+(i+1)+"_"+j+".txt");
    			if(i==1)
    				blocks.add(Constants.SORTED_FILE_PREFIX+(i+1)+"_"+j+".txt");
    			
    		}
    		
    	}
   	
    	System.out.println(blocks);
    */	
    	
    }
    
    public String Sortjoin()
    {
    
    	System.out.println("inside sort");
     	//finding min val
    	for(int i=0;i<sublistCount.size();i++)
    	{
    		try {

    		   		for(int j=0;j<sublistCount.get(i);j++)
    		{
    			File file1=new File(Constants.DATA_DIR +Constants.SORTED_FILE_PREFIX+(i+1)+"_"+j+".txt");
    			System.out.println(file1);
		   	
					Scanner s1=new Scanner(file1);
					String line1=s1.nextLine();	
    	    			
    		if(min.equals("0"))
    		min = line1;
			else
    		{
					if(Integer.parseInt(min.substring(0,8)) > Integer.parseInt(line1.substring(0,8)))
						min = line1;
				}
    		
    		s1.close();	
    		}
    		
    	}
    	catch (Exception e) {
			System.out.println(e.getMessage());
		}

    		
    	System.out.println("min value"+min);		
    	
    	}
		return min;

        //_outputBlock.add(minInputBlock.getCurrentData().toString());
        //minInputBlock.getData();
		/*   	*/
    }
    
    
    boolean Tuplecompare(String comp) {
		boolean flag = false;
		
   		for(int j=0;j<_inputBlocks.size();j++)
    		{
  	
   			File file1 = new File(Constants.DATA_DIR + Constants.SORTED_FILE_PREFIX +(sublistCount.size()+1)+"_"+j+".txt");
   			System.out.println(file1);
		try
		{
			
			Scanner s1 = new Scanner(file1);
			String line1 = s1.nextLine();
			//System.out.println(line1);

			while (s1.hasNextLine()) {
				//System.out.println(line1);

				if (Integer.parseInt(line1.substring(0, 8)) == Integer.parseInt(comp.substring(0, 8))) {
					System.out.println("match");
					outputblock.add((comp+line1));
					System.out.println(line1);
					matching++;
					
					line1 = s1.nextLine();
					flag = true;
				} else if (Integer.parseInt(line1.substring(0, 8)) > Integer.parseInt(comp.substring(0, 8))) {
					 line1=s1.nextLine();
				} else if (Integer.parseInt(line1.substring(0, 8)) < Integer.parseInt(comp.substring(0, 8))) {
					line1 = s1.nextLine();
				}
			}

			if (Integer.parseInt(line1.substring(0, 8)) == Integer.parseInt(comp.substring(0, 8))) {
				flag = true;
			}
			
			s1.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println("no of matches:" +matching);
    }
		return flag;
	}

	public void loadBlocks() {
		
		sublistCount.add(_inputBlocks.size());
		k++;
    	System.out.println("Loadblocks for :: "+_currFileNumber+" "+_inputBlocks.size());

    	for(InputBlock inputBlock:_inputBlocks) {
    		inputBlock.load();
    	}
    	
   	System.out.println("Sublistcount:"+sublistCount);
   	
    	  	for(int i=0;i<_inputBlocks.size();i++)	
		{
			if(k==0)
			{	
			blocksnames1.add(Constants.SORTED_FILE_PREFIX+_currFileNumber+"_"+i+".txt");
			k++;
			bag1sublist= _inputBlocks.size();

	    	//System.out.println(blocksnames1);   
	    	
			}
			else
			{
			blocksnames2.add(Constants.SORTED_FILE_PREFIX+_currFileNumber+"_"+i+".txt");
			bag2sublist= _inputBlocks.size();

	    	//System.out.println(blocksnames2);   
	    		
			}
		}
    		
    	  
    	  	
    	  
    	
    }
}