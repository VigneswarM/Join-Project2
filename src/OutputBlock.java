import java.io.*;
import java.util.ArrayList;
/**
 * This class is abstraction of block of memory that is written to the
 * merged output file.
 * 
 * Whenever a tuple is sorted from list of sorted chunks, It will be updated to
 * this block (OutputBlock)
 * 
 * This class is also responsible for writing to the output file whenever the 
 * output block is full (i;e 4K of data or 40 tuples of data) and frees the output block
 * to hold the next set of Sorted tuples.
 */
public class OutputBlock extends Block{

    private int counter = -1;
    ArrayList<String> _opBuffer=new ArrayList<>();
    
    private String _currentStudentId = "";
	CgpaComputer cgpaComputer = new CgpaComputer(Constants.GPA_SBJ);

    public OutputBlock(int tupleCountInBlock) {
    	super(tupleCountInBlock);
    }

    /**
     * Adds a Sorted Student tuple to the output block.
     * 
     * It Also dumps the block to the output file when the
     * block is full
     * 	
     * @param data (Tuple of type Student)
     */
    public void add(String data) {
    	String StudentId = data.substring(0,8);
    	
    	if(!StudentId.equals(_currentStudentId)) {
    		if(!_currentStudentId.equals("")) {
    			cgpaComputer.compute(_currentStudentId);
    		}
    		_currentStudentId = StudentId;
    	}
    	
    	String grade = data.substring(data.length() - 4).trim();
    	
    	//System.out.println(grade + "temp");
    	cgpaComputer.addGrade(grade);
    	//System.out.println("Block has added to buffer");
        counter = counter + 1;
        addToBuffer(counter, data);
        if(counter == BLOCKSIZE*8 - 1) {
            writeToFile();
            counter = -1;
        }
    }

    private void addToBuffer(int counter, String data) {
        _opBuffer.add(data);
        //System.out.println("Added to output buffer " + data);
    }

    /**
     * Writes the contents of the block to the output file
     */
    private void writeToFile() {
        //System.out.println("Block has been written to File");
        writeFile();
        freeBuffer();
    }

    /**
     * Frees the buffer or block to add the next set of Sorted data.
     */
    private void freeBuffer(){
        _opBuffer.clear();
        //System.out.println("Output Buffer has been freed");
    }

    /**
     * Performs the actual write to file with the contents 
     * present in the Output Block
     */
    private  void writeFile()  {
        BufferedWriter bw=null;
        try {
            try {
                bw = new BufferedWriter(new FileWriter(Constants.DATA_DIR + Constants.OUTPUT_FILE,true));
                for (String student : _opBuffer) {
                    bw.write(student);
                    bw.newLine();
                }
            }finally {
                bw.close();
                //Performance start
                Performance.MergeWriteDiskIO++;
                //Performance end
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void finish() {
    	writeToFile();
    	cgpaComputer.finish(_currentStudentId);
    }
}
