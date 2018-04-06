public class SortJoin {

    private int fileCount_1 = 0;
    private int fileCount_2 = 0;

    public SortJoin(int fileCount1, int fileCount2){
        fileCount_1 = fileCount1;
        fileCount_2 = fileCount2;
    }

    public void execute() {
        BlockManager blockManager1=new BlockManager(fileCount_1, Constants.TUPLE_COUNT1, 1);
        BlockManager blockManager2=new BlockManager(fileCount_2, Constants.TUPLE_COUNT2, 2);

        LineCounter lineCounter=new LineCounter();
        int lineCount_1 =lineCounter.count(Constants.INPUT_FILE + 1 +".txt");
        int lineCount_2 =lineCounter.count(Constants.INPUT_FILE + 2 +".txt");

        String min_1 =blockManager1.execute();
        String min_2 =blockManager2.execute();

        int lineCounter_1 = 1;
        int lineCounter_2 = 1;

        OutputBlock outputBlock = new OutputBlock(Constants.TUPLE_COUNT1);

        while(lineCounter_2 < lineCount_2){
            int student_id_min_1 = Integer.parseInt(min_1.substring(0,8));
            int student_id_min_2 = Integer.parseInt(min_2.substring(0,8));
            if(student_id_min_1 < student_id_min_2){
                min_1 = blockManager1.execute();
                lineCounter_1++;
            }
            else if(student_id_min_1  > student_id_min_2){
                min_2 = blockManager2.execute();
                lineCounter_2++;
            }
            else if(student_id_min_1  == student_id_min_2){
                outputBlock.add(min_1 + min_2.substring(8));
                //System.out.println(Runtime.getRuntime().freeMemory());
                min_2 = blockManager2.execute();
                lineCounter_2++;
            }

            if(!(lineCounter_1 < lineCount_1)){
                int student_id_1 = Integer.parseInt(min_1.substring(0,8));
                int student_id_2 = Integer.parseInt(min_2.substring(0,8));
                if(!(student_id_1 == student_id_2)){
                    break;
                }
            }
        }

        outputBlock.finish();
    }
}
