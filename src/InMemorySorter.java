import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class InMemorySorter {
	public static void bubbleSort(byte[] temp)
    {
        for (int i = 0; i < 12880 -1; i++) {
        	//System.out.println(i);
            for (int j = 0; j < 12880 - i - 1; j++) {
                if (getStudent(temp, j).ID > getStudent(temp, j+1).ID) 
                {
                    byte[] k = Arrays.copyOfRange(temp, j*102, j*102 + 102);
                    swap(temp, j+1, j);
                    copy(temp, k, j+1);
                }
            }
        }
    }
    
    public static void swap(byte[] temp, int src, int dest ) {
    	for(int i = 0; i<102; i++) {
    		temp[dest*102 + i] = temp[src*102 + i];
    	}
    }
    
    public static void copy(byte[] temp, byte[] src, int tempIndex) {
    	for(int i = 0; i<102; i++) {
    		temp[tempIndex*102 + i] = src[i];
    	}
    }
    
    
    public static Student getStudent(byte[] temp , int i) {
    	int start = i*102   ;
    	int end = i*102 + 102;
        byte[] slice = Arrays.copyOfRange(temp, start, end);
        slice = Arrays.copyOfRange(slice, 0, 100);
        String tuple = new String(slice);
        Student student  = new Student(tuple);
        return student;
    }
    
    public static void writeFile(byte[] temp,String fileName){
        File file=new File(fileName);
        try{
            OutputStream op=null;
            try{
                op=new BufferedOutputStream(new FileOutputStream(file,true));
                op.write(temp);
            }finally{
                op.close();
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void readFile(){
		
        int totalBytesRead=0,chunkCount=0;
        File inFilePtr=new File("unsorted_chunk_82.txt");
        InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(inFilePtr));
			long fileSize=inFilePtr.length();
	        System.out.println(fileSize);
	        String chunkFileName="unsorted_chunk_82.txt";
	        byte[] temp=new byte[(int)fileSize];
	        int bytesRead;
	        bytesRead = in.read(temp, 0, (int)fileSize);
//	        for(int i=0;i<7;i++) {
//	        	System.out.println(getStudent(temp, i));
//	        }
//	        swap(temp, 0, 1);
//	        System.out.println(getStudent(temp, 1));
//	        
//	        swap(temp, 3, 2);
//	        System.out.println(getStudent(temp, 2));
	        
	        bubbleSort(temp);
	        writeFile(temp, "op.txt");
	        System.out.println("Done");
	        //List<String> k = Files.readAllLines(Paths.get(chunkFileName));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    }
}
