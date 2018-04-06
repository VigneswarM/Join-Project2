import java.util.*;
import java.io.*;
import java.util.ArrayList;


public class NestedLoopJoin {
	Scanner input2;
	File relation1,relation2;
    BufferedWriter bw;
	InputStream in=null;

    int lineCounter1 = 0,lineCounter2 = 0,dataSizeToBeLoaded=0,totalLineCount;
    Map<Integer,byte[]> relation1Lines = new HashMap<>();
    ArrayList<String> relation2Lines=new ArrayList<String>();    
    
    private int calcSizeOfDataTobeLoaded(){
    	File smallRelation;
    	int availableRam=(int)Runtime.getRuntime().freeMemory();
    	if(relation1.length()<relation2.length()){
    		smallRelation=relation1;
    	}else{
    		smallRelation=relation2;
    	}
    	if(smallRelation.length()<availableRam/2){
    		return (int)smallRelation.length()/Constants.BLOCK_SIZE;
    	}else{
    		return (int)smallRelation.length()/(2*Constants.BLOCK_SIZE);
    	}
    }
    
    
    public NestedLoopJoin(int totCnt){
    	this.totalLineCount=totCnt;
        try {
            relation1 =new File(Constants.DATA_DIR+"JoinT1.txt");
            relation2 =new File(Constants.DATA_DIR+"JoinT2.txt");
            input2=new Scanner(relation2);
            
            in=new BufferedInputStream(new FileInputStream(relation1));

            
            FileWriter writer = new FileWriter(Constants.DATA_DIR + "output.txt", true);
            bw = new BufferedWriter(writer);
            
            dataSizeToBeLoaded=calcSizeOfDataTobeLoaded();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> loadT2Data(){
        int count=lineCounter2+Constants.TUPLE_COUNT2;
        while ((lineCounter2 < count) && input2.hasNextLine()) {
        	relation2Lines.add(input2.nextLine());
            lineCounter2++;
        }
        return relation2Lines;
    }
    
    int curLineCnt=0;
    
    
    private void readFile(int dataSize){
    	System.gc();
        byte[] temp=new byte[dataSize];
        try{
            in.read(temp,0,dataSize);
       
	        loadToMap(temp);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    private void loadToMap(byte[] temp){
    	String line;
        InputStream is = new ByteArrayInputStream(temp);
        BufferedReader bfReader = new BufferedReader(new InputStreamReader(is));
		try {
			while(curLineCnt!=totalLineCount&&(line = bfReader.readLine()) != null){
				//r1.add(new Student(line).ID);
				relation1Lines.put(new Student(line).ID,line.getBytes());
				curLineCnt++;
			}
			is.close();
			bfReader.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
    }
    private void loadT1Data(){
    	readFile(Constants.NESTED_LOOP_BLOCK_SIZE);
    }

    public void execute() {
    	
        boolean flag=true;
        try {
        	loadT1Data();
            while(flag){
                loadT2Data();
                
                if(relation2Lines.isEmpty()){
                    relation1Lines.clear();
                    loadT1Data();
                    if(relation1Lines.isEmpty()){
                       flag=false;
                    }
                    lineCounter2=0;
                    input2.close();
                    input2=new Scanner(relation2);
                    loadT2Data();
                }
                
                writeToFile();
        		bw.flush();
                relation2Lines.clear();
            }    
            relation1Lines.clear();
            System.out.println("Done!");
            closeFilePointers();
        } catch (Exception e) {
			System.out.println(e.getMessage());
        }
    }
    
    private void closeFilePointers(){
    	try {
			bw.close();
	    	input2.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	
    }
    
	private void writeToFile(){
		for(String str:relation2Lines){
	    	if(relation1Lines.containsKey(new StudentCourse(str).ID))
	    	{
	    		try {
					bw.write(new String(relation1Lines.get(new StudentCourse(str).ID))+" "+str.substring(8));
		    		bw.newLine();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
	    	}
	    }
	}
}


