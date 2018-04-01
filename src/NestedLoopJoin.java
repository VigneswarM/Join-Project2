import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.io.*;
import java.util.ArrayList;


public class NestedLoopJoin {
	Scanner input1,input2;
	File relation1,relation2;
    BufferedWriter bw;

    int lineCounter1 = 0,lineCounter2 = 0,dataSizeToBeLoaded=0;
    HashMap<Integer,String> relation1Lines = new HashMap<>();
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
    public NestedLoopJoin(){
        try {
            relation1 =new File(Constants.DATA_DIR+"JoinT1.txt");
            relation2 =new File(Constants.DATA_DIR+"JoinT2.txt");
            input1=new Scanner(relation1);
            input2=new Scanner(relation2);
            
            FileWriter writer = new FileWriter(Constants.DATA_DIR + "output.txt", true);
            bw = new BufferedWriter(writer);
            
            dataSizeToBeLoaded=calcSizeOfDataTobeLoaded();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> loadT2Data(){
        int count=lineCounter2+Constants.TUPLE_COUNT2;
        //System.out.println("Loading R2 :: "+count);
        while ((lineCounter2 < count) && input2.hasNextLine()) {
        	relation2Lines.add(input2.nextLine());
            lineCounter2++;
        }
        return relation2Lines;
    }
    
    private void loadT1Data(){
    	//System.out.println(lineCounter1+" "+dataSizeToBeLoaded+" "+Constants.TUPLE_COUNT1);
        int count=lineCounter1+((dataSizeToBeLoaded/2)*Constants.TUPLE_COUNT1);
        //System.out.println("Loading R1 :: "+count);
    	while (lineCounter1 < count&& input1.hasNextLine()) {
        	String curr=input1.nextLine();
            relation1Lines.put(new Student(curr).ID,curr);
            lineCounter1++;
        }
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
                for(String str:relation2Lines){
                	if(relation1Lines.containsKey(new StudentCourse(str).ID))
                	{
                		bw.write(relation1Lines.get(new StudentCourse(str).ID) + " "+str);
                		bw.newLine();
                	}
                }
                
                relation2Lines.clear();
            }        
            bw.close();
            input1.close();
            input2.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}




