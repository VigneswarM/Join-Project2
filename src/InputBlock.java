import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
/**
 * This class is abstraction for one block (40 Tuples or 4K of memory) from the input file
 * Each instance of this class would point to one of the sorted chunk file.
 */
public class InputBlock extends Block{

    private int counter = -1;
    private String _fileName;
    LineNumberReader _reader;
    ArrayList<String> _currentBlock = new ArrayList<>();
    
    public InputBlock(String fileName,int tupleCountInBlock) {
    	super(tupleCountInBlock);
        try {
            _fileName = fileName;
            String fullPathToFile = Constants.DATA_DIR + fileName;
            _reader = new LineNumberReader(new InputStreamReader(new FileInputStream(fullPathToFile), "UTF-8"));
            load();
        }
        catch (Exception e){
            System.out.println("Exception Occured" + e.getMessage());
        }
    }

    public String getData() {
        int nextDataIndex = computeNextDataIndex();
        return getRecord(nextDataIndex);
    }

    /**
     * @return True or False based on the data availability in the input block
     * This is used to decide whether to reload the Block with the Next block of
     * memory from the respective sorted_chuck file.
     */
    public boolean isDataAvailable() {
        boolean isDataAvailable =  counter < _currentBlock.size();
//        if(!isDataAvailable) {
//            System.out.println("Data not available" + _fileName);
//        }
        return isDataAvailable;
    }

    /**
     * Returns the current data where the pointer currently pointing in the block
     * @return
     */
    public String getCurrentData() {
        if(counter == -1) {
            getData();
        }
        return getRecord(counter);
    }

    /**
     * Loads the input block with the next block of memory from the respective sorted File.
     * Next Block of memory means ==> Next 4K of data from the file which is equivalent to 40 tuples 
     */
    public void load() {
        loadNextBlockIntoMemory();
        counter = -1;
    }

    private void loadNextBlockIntoMemory() {
    	_currentBlock = getBlock();
    	//Performance start
	    Performance.MergeReadDiskIO++;
        //Performance end
    }
    
    /**
     * Closes the file reader and frees the memory
     */
    public void finish() {
    	if(_reader != null) {
    		try {
				_reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

    private int computeNextDataIndex(){
        if(counter < BLOCK_MAX_INDEX) {
            counter = counter + 1;
        }
        else {
            load();
            counter = counter + 1;
        }
        return counter;
    }

    
    /**
     * @param index Index of the data (tuple) in the current block.
     * It searches the tuple for the given index and returns a Student object.
     * @return Returns a Student object if the index is present or returns null
     */
    private String getRecord(int index) {
    	try{
	        if(isDataAvailable()) {
	            return _currentBlock.get(index);
	        }
	        else {
	    		//System.out.println("Requested block :: "+index+"File name :: "+_fileName+"Total number of lines :: "+_currentBlock.size());
	            return null;
	        }
    	}catch(Exception e){
    		System.out.println(e.getMessage() + "Requested block :: "+index+"File name :: "+_fileName+"Total number of lines :: "+_currentBlock.size());
    	}
    	return null;
    }

    /**
     * Reads the next block of data (4k or 40 tuples) from the file.
     * Converts those 40 tuples into list of Student Objects
     * @return List of Student objects
     */
    public ArrayList<String> getBlock()  {
        ArrayList<String> lines = new ArrayList<>();
        int lineCounter = 0;
        try{
            String line;
            while ( lineCounter < BLOCKSIZE  && ((line = _reader.readLine()) != null)) {
                //System.out.println(line);
                //System.out.println("Object Created");
                lines.add(line);
                lineCounter = lineCounter + 1;
            }
        }
        catch (Exception e)
        {
           System.out.println(e.getMessage());
        }
        return lines;
    }
    
}