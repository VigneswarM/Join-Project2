import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class NestedLoopJoin {

    Scanner input1=new Scanner(System.in);
    Scanner input2=new Scanner(System.in);
    int lineCounter1 = 0;
    int lineCounter2 = 0;
    HashMap<Integer,String> unsortedLines = new HashMap<>();

    public NestedLoopJoin(){
        try {
            File file =new File(Constants.DATA_DIR+"JoinT1.txt");
            File file1 =new File(Constants.DATA_DIR+"JoinT2.txt");
            input1=new Scanner(file);
            input2=new Scanner(file1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getBlock(){
        String line;
        ArrayList<String> unsortedLines1 = new ArrayList<String>();
        unsortedLines1.clear();
        int count=lineCounter2+Constants.TUPLE_COUNT2;
        while ((lineCounter2 < count) && input2.hasNextLine()) {
            unsortedLines1.add(input2.nextLine());
            lineCounter2++;
        }
        //System.out.println(lineCounter2);
        return unsortedLines1;
    }

    public void execute() {

        try {
            FileWriter writer = new FileWriter(Constants.DATA_DIR + "output.txt", true);
            BufferedWriter bw = new BufferedWriter(writer);
            String line;
            System.out.println(lineCounter1+(Constants.BLOCK_COUNT1/2)*Constants.TUPLE_COUNT1);
            while (lineCounter1 < (Constants.BLOCK_COUNT1/2)*Constants.TUPLE_COUNT1&& input1.hasNextLine()) {
            	String curr=input1.nextLine();
                unsortedLines.put(new Student(curr).ID,curr);
                lineCounter1++;
            }
            
        	System.out.println(unsortedLines.size());

            ArrayList<String> blockData=new ArrayList<String>();
            boolean flag=true;
            int cnt=0;
            while(flag){
                blockData= getBlock();
                if(blockData.isEmpty()){
                    unsortedLines.clear();
                    int count=lineCounter1+(Constants.BLOCK_COUNT1/2)*Constants.TUPLE_COUNT1;
                    while (lineCounter1 < count  && input1.hasNextLine()) {
                    	String curr=input1.nextLine();
                        unsortedLines.put(new Student(curr).ID,curr);
                        lineCounter1++;
                    }
                    if(unsortedLines.isEmpty()){
                       flag=false;
                    }
                    lineCounter2=0;
                    blockData= getBlock();
                }
                //final ArrayList<String> temp=unsortedLines;
                
//                List<StudentCourse> joinResult=blockData.stream()
//                		.map(StudentCourse::new)
//                		.filter(x->(temp.stream().map(Student::new).filter(y->x.ID==y.ID).count()>1))
//                		.collect(Collectors.toList());
                for(String str:blockData){
                	if(unsortedLines.containsKey(new StudentCourse(str).ID))
                	{
                		bw.write(unsortedLines.get(new StudentCourse(str).ID) + " "+str);
                		bw.newLine();
                	}
                }
                
                System.out.println(cnt);
              //  System.out.println(joinResult.size());
//                for(Student s:joinResult){
//                	bw.write(s.toString());
//                }
                //temp.clear();
                blockData.clear();
            }
            
            bw.close();
            input1.close();
            input2.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}




