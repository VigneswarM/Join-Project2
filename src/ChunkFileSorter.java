import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;

/**
 * This class has the functionalities to Sort all the small chunk files
 */

public class ChunkFileSorter {
	private ArrayList<String> _chunkFileList;
	
	/**
	 * @param chunkFileList Chunk filenames that has to be sorted
	 */
	public ChunkFileSorter(ArrayList<String> chunkFileList){
		_chunkFileList=chunkFileList;
	}
	
	/**
	 * Reads each and every unsorted_chunk file and Sort the contents of the files.
	 * 
	 * Writes the Sorted content to a new file (sorted_chunk_1.txt)
	 * 
	 * @return List of names of Sorted file names
	 */
	public ArrayList<String> sort(){
		ArrayList<String> sortedChunkFileList=new ArrayList<>();
		try {
			for(String fileName:_chunkFileList){
					//Open the file
					String fullPathtoFile = Constants.DATA_DIR + fileName;
					File file = new File(fullPathtoFile);
					Scanner sortScan = new Scanner(file);
					List<String> studentList = new ArrayList<>();
					while (sortScan.hasNextLine()) {
						studentList.add(sortScan.nextLine());
					}
					sortScan.close();
					studentList.sort(Comparator.comparing(s -> Integer.parseInt(s.substring(0, 8))));
					writeFile(fileName.replace(Constants.UNSORTED_FILE_PREFIX, Constants.SORTED_FILE_PREFIX), studentList);
					sortedChunkFileList.add(fileName.replace(Constants.UNSORTED_FILE_PREFIX, Constants.SORTED_FILE_PREFIX));
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return sortedChunkFileList;
	}

	/**
	 * Performs the actual write to the file with the Sorted items
	 * @param fileName: Name of the File
	 * @param studentList: Sorted Student objects
	 */
	public void writeFile(String fileName,List<String> studentList){
		BufferedWriter bw=null;
        try {
            try {
            	String fullPathToFile = Constants.DATA_DIR + fileName;
                bw = new BufferedWriter(new FileWriter(fullPathToFile,true));
				for (String student:studentList){
                    bw.write(student);
                    bw.newLine();
                }
            }finally {
                bw.close();
                //Performance start
                Performance.SortDiskIO++;
                //Performance end
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
	}
}

