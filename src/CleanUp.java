import java.io.File;

public class CleanUp {
	public CleanUp(){
		
	}
	public void renameOutputFile(String fileName,int fileCount){
		try{
			//Rename file
	        File oldFile = new File(Constants.DATA_DIR+fileName);
	        File newFile=new File(Constants.DATA_DIR+Constants.OUTPUT_FILE+fileCount+".txt");
	        oldFile.renameTo(newFile);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void deleteChunks(){
		 //Delete unsorted and sorted chunks
        File folder = new File(Constants.DATA_DIR);
        for (File currFile : folder.listFiles()) {
	         if (currFile.getName().contains(Constants.UNSORTED_FILE_PREFIX)) {
	        	 currFile.delete();
	         }
        }
        for (File currFile : folder.listFiles()) {
	         if (currFile.getName().contains(Constants.SORTED_FILE_PREFIX)) {
	        	 currFile.delete();
	         }
       }
	}
}
