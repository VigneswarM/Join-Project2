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
    ArrayList<String> unsortedLines = new ArrayList<String>();


    public NestedLoopJoin(){
        try {
            File file =new File(Constants.DATA_DIR+"R1.txt");
            File file1 =new File(Constants.DATA_DIR+"R2.txt");
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

        while ((lineCounter2 < 40) && input2.hasNextLine() && (line = input2.nextLine()) != null) {
            unsortedLines1.add(line);
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
            while ((lineCounter1 < 40) && input1.hasNextLine() && ( line =input1.nextLine())!= null ) {
                unsortedLines.add(line);
                lineCounter1++;
            }
            ArrayList<String> blockData=new ArrayList<String>();
            boolean flag=true;
            while(flag){
                blockData= getBlock();
                //System.out.println(blockData.size());
                if(blockData.isEmpty()){
                    unsortedLines.clear();
                   
                    while ((lineCounter1 < 40 ) && input1.hasNextLine() && ( line =input1.nextLine())!= null ) {
                        unsortedLines.add(line);
                        lineCounter1++;
                    }
                    if(unsortedLines.isEmpty()){
                       flag=false;
                    }
                    lineCounter2=0;
                    blockData= getBlock();
                }

                
                List<String> join=unsortedLines.stream().filter(blockData::contains).collect(Collectors.toList());
                for (String s : join) {
                    bw.write(s.trim());
                    bw.newLine();
                }
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




