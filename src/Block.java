public class Block {

    protected int BLOCKSIZE;

    protected int BLOCK_MAX_INDEX = BLOCKSIZE - 1;
    
    public Block(int blockSize) {
    	this.BLOCKSIZE=blockSize;
    	this.BLOCK_MAX_INDEX=this.BLOCKSIZE-1;
    }
}
