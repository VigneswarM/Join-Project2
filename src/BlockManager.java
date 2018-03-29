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

    OutputBlock _outputBlock;
    
    private int _fileCount;

    public BlockManager(int fileCount,int tupleCountInBlock) {
        _fileCount = fileCount;
        for (int i = 0; i < fileCount; i++) {
            InputBlock ip = new InputBlock(Constants.SORTED_FILE_PREFIX + String.valueOf(i) + ".txt",tupleCountInBlock);
            _inputBlocks.add(ip);
        }
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
        List<InputBlock> blocks = _inputBlocks.stream().filter(x -> x.isDataAvailable()).collect(Collectors.toList());
        //System.out.println(blocks.size());
        InputBlock minInputBlock = blocks.get(0);
        for (InputBlock inputBlock : blocks) {
            if (inputBlock.getCurrentData().ID < minInputBlock.getCurrentData().ID) {
                minInputBlock = inputBlock;
            }
        }
        _outputBlock.add(minInputBlock.getCurrentData().toString());
        minInputBlock.getData();
    }
    
    
    public void loadBlocks() {
    	for(InputBlock inputBlock:_inputBlocks) {
    		inputBlock.load();
    	}
    }
}