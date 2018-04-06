import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;


public class NestedLoopJoin {
	Scanner input2;
	File relation1,relation2;
    BufferedWriter bw;
	InputStream in=null;

    int lineCounter1 = 0,lineCounter2 = 0,dataSizeToBeLoaded=0,totalLineCount;
    
    Map<Integer,byte[]> relation1Lines = new HashMap<>();
    ArrayList<String> relation2Lines=new ArrayList<String>();    

    HashMap<Integer,GradesData> cgpaSet=new HashMap<>();  
    CgpaComputer cgpaComputer=new CgpaComputer();
    
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
                    writeCgpa();
                    cgpaSet.clear();
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
	    			int currStudentID=new StudentCourse(str).ID;
	    			String currGrade=str.substring(str.length()-4).trim();
					bw.write(new String(relation1Lines.get(new StudentCourse(str).ID))+" "+str.substring(8));
		    		bw.newLine();
		    		populateCgpaMap3(currStudentID, currGrade);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
	    	}
	    }
	}
	
	
	private void writeCgpa(){
		for (Map.Entry<Integer, GradesData> entry : cgpaSet.entrySet())
		{
		    //System.out.println(entry.getKey() + "/" + entry.getValue().grade+"/"+entry.getValue().cnt);
		    double cgpa = entry.getValue().grade / entry.getValue().cnt;
		    cgpa=(double)Math.round(cgpa * 100d) / 100d;
		    cgpaComputer.writeToFile(entry.getKey().toString(),cgpa);
		}
	}
	
	private void populateCgpaMap3(Integer studentID,String grade){
		Double currGrade=cgpaComputer.gradesList.get(grade);
		if(!cgpaSet.containsKey(studentID)){
			GradesData gradesData = new GradesData(currGrade,1);
			cgpaSet.put(studentID,gradesData);
		}else{
			GradesData existingGradesData = cgpaSet.get(studentID);
			existingGradesData.grade += currGrade;
			existingGradesData.cnt+=1;
			existingGradesData.grade = (double)Math.round((existingGradesData.grade) * 100d) / 100d;
			cgpaSet.put(studentID,existingGradesData);
		}
	}
	
	

	
	public class GradesData{
		double grade;
		int cnt=0;
		GradesData(double grade,int cnt){
			this.grade=grade;
			this.cnt=cnt;
		}
	}
}


/*******************************TEST CODE******************************************/



// HashMap<String,Integer> gradesList=new HashMap<>();


//private void initGradesList(){
//	gradesList.put("A+", 1);
//	gradesList.put("A", 2);
//	gradesList.put("A-", 3);
//	gradesList.put("B+", 4);
//	gradesList.put("B", 5);
//	gradesList.put("B-", 6);
//	gradesList.put("C+", 7);
//	gradesList.put("C", 8);
//	gradesList.put("C-", 9);
//	gradesList.put("Fail", 0);
//}


//HashMap<Integer,BigInteger> cgpaSet=new HashMap<>();
//HashMap<Integer,String> cgpaSet1=new HashMap<>();
//HashMap<Integer,Double> cgpaSet2=new HashMap<>();

//private void populateCgpaMap2(Integer studentID,String grade){
//	Double currGrade=cgpaComputer.gradesList.get(grade);
//	if(!cgpaSet2.containsKey(studentID)){
//		cgpaSet2.put(studentID,currGrade);
//	}else{
//		Double existingValue=cgpaSet2.get(studentID);
//		Double cgpa = (double)Math.round((currGrade+existingValue) * 100d) / 100d;
//		cgpa = cgpa + 0.00001;
//		cgpaSet2.put(studentID, cgpa);		
//	}
//}
//
//private void populateCgpaMap(Integer studentID,Integer grade){
//	BigInteger currGrade=BigInteger.valueOf(grade);
//	if(!cgpaSet.containsKey(studentID)){
//		cgpaSet.put(studentID,currGrade);
//	}
//	else{
//		BigInteger existingValue=cgpaSet.get(studentID);
//		BigInteger s=new BigInteger(String.valueOf(existingValue) + String.valueOf(currGrade));
//		//System.out.println("================"+s);
//		cgpaSet.put(studentID,existingValue);		
//	}
//}
//
//private void populateCgpaMap1(Integer studentID,String grade){
//	if(!cgpaSet1.containsKey(studentID)){
//		cgpaSet1.put(studentID,grade);
//	}else{
//		String existingValue=cgpaSet1.get(studentID);
//		String s=existingValue+grade;
//		cgpaSet1.put(studentID,s);		
//	}
//}