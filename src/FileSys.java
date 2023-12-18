/*********************************************************
 * filename: FileSys.java
 *
 * A file that imitates a file system from a computer. This
 * terminal based program allows users to create directories
 * and files and manipulate them as necessary.
 *  author:@tlecuyer
 *********************************************************/

//Imports for project
import java.util.ArrayList;
import java.util.Scanner;

/** Class FileSystem
 * 
 * This class is responsible for holding all
 * methods for altering the file system with both
 * directories and files and also holds the subclasses
 * for creating them
 */
class FileSystem{
	
	/** Class Directory
	 * 
	 * 	This class creates a directory and
	 * 	links it to the parent if applicable
	 *  and holds all the children in a List
	 */
	private class Directory {
		String dirName;
		Directory parent;
		ArrayList<Directory> children;
		ArrayList<File> fileChildren;
		
		//Used to create the directory
		Directory(String s) {
			dirName = s;
			parent = null;
			children = new ArrayList<>();
			fileChildren = new ArrayList<>();
		}
		//Returns name of directory
		String name() { return dirName; }
		
		//Adds a child to the list as it can have multiple
		void addChild(Directory childDir) {
            children.add(childDir);
        }
		void addFileChild(File childFile) {
			fileChildren.add(childFile);
		}
		//Returns list of children
        ArrayList<Directory> getChildren() {
            return children;
        }
        ArrayList<File> getFileChildren(){
        	return fileChildren;
        }
	}
	
	/** Class File
	 * 
	 * 	This class creates a file and
	 * 	links it to the parent and holds
	 *  the name and file contents.
	 */
	private class File {
		String fileName;
		String fileContents;
		
		//Used to create the file
		File(String s) {
			fileName = s;
			fileContents = null;
		}
		String fileName() { return fileName; }
		String getContents() { return fileContents; }
	}
	
	Directory rootDir;
	Directory currentDir;
	
	//Initially makes the root directory
	public FileSystem() {
		rootDir = new Directory("root");
		currentDir = rootDir;
	}
	
	//Method to make a directory that takes a String input
	public void mkdir(String input) {
		//Makes the new directory using the input as the name
        Directory n = new Directory(input);
        //If input is blank make sure user enters a name
        if(input.isBlank()) {
        	System.out.println("ERROR: Please enter a name for the directory");
        	return;
        }
        //Otherwise the new directory will be a child of the current directory
        else {
        	//Makes sure no directory is created with a name that matches a current directory
        	for(Directory child : currentDir.getChildren()) {
        		if(n.name().equals(child.name())) {
        			System.out.println("ERROR: Directory with name " + child.name() + " already exists in this directory");
        			return;
        		}
        	}
        	//Makes sure no directory is created with a name that matches a current file
        	for(File child : currentDir.getFileChildren()) {
        		if(n.name().equals(child.fileName())) {
        			System.out.println("ERROR: File with name " + child.fileName() + " already exists in this directory");
        			return;
        		}
        	}
            currentDir.addChild(n);
            n.parent = currentDir;
        }
    }
	
	//Method that lists the children of the current directory
	public void ls() {
        ArrayList<Directory> children = currentDir.getChildren();
        ArrayList<File> fileChildren = currentDir.getFileChildren();
        //For every directory add a "(*)" to the end
        for (Directory child : children) {
            System.out.println(child.name() + " (*)");
        }
        for (File child : fileChildren) {
        	System.out.println(child.fileName);
        }
    }
	
	//Method that changes the current directory to the input
	public void cd(String input) {
		boolean validPath = false;
		//If the input is / it changes CD to the root
		if(input.equals("/")) {
			currentDir = rootDir;
			validPath = true;
			return;
		}
		//If the input is .. it changes it to the parent
		if (input.equals("..")) {
            if (currentDir.parent == null) {
                System.out.println("ERROR: Already at root directory");
                return;
            }
            else {
            	currentDir = currentDir.parent;
            	validPath = true;
            	return;
            }
		}
		
		//Splits the input file path using the / character
		String filePath[] = input.split("/");
		//Makes an array of the found path to check if it is valid
		ArrayList<String> foundPath = new ArrayList<String>();
		//Temporary pointer to not mess with CD
		Directory tempPointer = currentDir;
		//For every element in the file path
		for(String d : filePath) {
			//For every child element of the currentDir
			for(Directory child : tempPointer.getChildren()) {
				//If the child is equal to the file path then the temporary pointer is changed
				if(child.name().equals(d)) {
					//Adds the name to the found path array
					foundPath.add(child.name());
					//Makes the new pointer the child
					tempPointer = child;
					validPath = true;
					break;
				}
			}
		}
		//This loop checks to make sure the path is really valid
		for(int i=0;i<filePath.length;i++) {
			//Makes both arrays the same size
			if(filePath.length > foundPath.size()) {
				foundPath.add(null);
			}
			//If the found path does not equal the entered path then it must be an invalid path entered
			if(!filePath[i].equals(foundPath.get(i))) {
				validPath = false;
			}
		}
		//If the file path was not found
		if(validPath != true) {
			System.out.println("ERROR: Please enter a valid file path");
		}
		//Sets the CD to the tempPointer that was iterated in the loop
		else {
		currentDir = tempPointer;
		}
}
	
	//Method removes the directory from the CD
	public void rmdir(String input) {
		//Goes through every child of the CD
		for(Directory child : currentDir.getChildren()) {
			if(child.name().equals(input)) {
				//Clears all children of the child to be removed
				child.getChildren().clear();
				//Removes the child from the parent
				child.parent.getChildren().remove(child);
				return;
			}
		}
		System.out.println("ERROR: Directory to remove not found");
	}
	
	//This method prints out the working directory
	public void pwd() {
		//Calls the getFilePath method
		String filePath = getFilePath(currentDir);
		System.out.println(filePath);
	}
	//Helper recursive method that prints the current file path to the input
	private String getFilePath(Directory d) {
		if (d == rootDir) {
			return "/" + d.name();
		}
		else {
			//Recursively returns the parent's file path and the name of the next
			return getFilePath(d.parent) + "/" + d.name();
		}
	}
	
	//Method creates a "file" and adds the contents
	public void create(String s, Scanner userInput) {
		//Create the file using the name
		File n = new File(s);
		//If there is no name listed
		if(s.isBlank()) {
			System.out.println("ERROR: Please enter a name for the file");
			return;
		}
		//Makes sure no file is created with a name that matches a current file
		for(File child : currentDir.getFileChildren()) {
    		if(n.fileName().equals(child.fileName())) {
    			System.out.println("ERROR: File with name " + child.fileName() + " already exists in this directory");
    			return;
    		}
    	}
		//Makes sure no file is created with a name that matches a current directory
		for(Directory child : currentDir.getChildren()) {
    		if(n.fileName().equals(child.name())) {
    			System.out.println("ERROR: Directory with name " + child.name() + " already exists in this directory");
    			return;
    		}
    	}
		//Add the file to the parent file children
		currentDir.addFileChild(n);
		
		System.out.println("Enter text for the file, end with a '~'");
		//StringBuilder holds all inputed information
		StringBuilder fileInput = new StringBuilder();
		//Loop runs until user inputs a "~"
			while(userInput.hasNextLine()) {
				String line = userInput.nextLine();
				if(line.contains("~")) {
					//Deletes the ~ from the line
					line = line.substring(0, line.indexOf("~"));
					//Appends the link
					fileInput.append(line);
					//Breaks from the loop
					break;
				}
				fileInput.append(line).append("\n");
			}
			//Puts the inputed information into the file contents string object
			n.fileContents = fileInput.toString();
	}
	
	//This method prints out the file contents of a given file name
	public void cat(String s) {
		//If the file is blank
		if(s.isBlank()) {
			System.out.println("ERROR: Please enter a file to catenate");
			return;
		}
		else {
			Boolean fileFound = false;
			//Loop finds the name of the file that matches input
			for(File f : currentDir.getFileChildren()) {
				if(f.fileName.equals(s)) {
					fileFound = true;
					System.out.println(f.getContents());
				}
			}
			//If no file is found with the matching name
			if(!fileFound) {
				System.out.println("ERROR: File not found");
			}
		}
	}
	
	//This method removes the file if the given name is in the directory
	public void rm(String s) {
		for(File f : currentDir.getFileChildren()) {
			if(f.fileName.equals(s)) {
				//Removes the file from the parent array
				currentDir.fileChildren.remove(f);
				return;
			}
		}
		System.out.println("ERROR: File to remove not found");
	}
	
	//This method calculates the disk usage of the files in the current directory and sub-directories
	public void du() {
		int size = calculateUsage(currentDir);
		System.out.println("The size is " + size + " bytes");
	}
	public int calculateUsage(Directory s) {
	    int size = 0;
	    //Gets the usage of all files in the current directory
	    for (File f : s.getFileChildren()) {
	        size += f.getContents().getBytes().length;
	    }
	    //Gets the usage for all sub-directories of the current directory
	    for (Directory d : s.getChildren()) {
	        size += calculateUsage(d);
	    }
	    return size;
	}
	
	public void find(String s) {
		findHelper(currentDir, s);
	}
	//Made helper method so currentDir doesn't have to change and can call it recursively
	private void findHelper(Directory d, String s) {
		//Checks through every sub-directory if the name matches the input
		for(Directory dir : d.children) {
			if(dir.name().equals(s)) {
				System.out.println(getFilePath(d)+ "/" + dir.dirName);
			}
			//Recursively calls method for every sub directory
			findHelper(dir, s);
		}
		//Checks through every file to see if name matches input
		for(File f : d.getFileChildren()) {
			if(f.fileName.equals(s)) {
				System.out.println(getFilePath(d)+ "/" + f.fileName);
			}
		}
	}
}

/* Class FileSys
 * 
 * This class creates a new instance of the FileSystem
 * class and is responsible for handling user input to
 * call the correct methods.
 */
public class FileSys {

	public static void main(String[] args) {
		FileSystem mySystem = new FileSystem();
		Scanner userInput = new Scanner(System.in);
		//Loop that runs until user wants to exit
		while(true) {
		    System.out.print("prompt> ");
		    String userCommand = userInput.nextLine();
		    String[] userSplit = userCommand.split("\\s+");
		    
		    String userValue = "";

		    if (userSplit.length > 1) {
		        userValue = userSplit[1];
		    }
		    //Switch that matches the user input with the correct method
		    switch(userSplit[0]) {
		        case "mkdir": 
		            mySystem.mkdir(userValue);
		            break;
		        case "rmdir":
		        	mySystem.rmdir(userValue);
		        	break;
		        case "pwd":
		        	mySystem.pwd();
		        	break;
		        case "exit": 
		        	userInput.close();
		            System.exit(0);
		            break;
		        case "ls":
		            mySystem.ls();
		            break;
		        case "cd":
		        	mySystem.cd(userValue);
		        	break;
		        case "create":
		        	mySystem.create(userValue, userInput);
		        	break;
		        case "cat":
		        	mySystem.cat(userValue);
		        	break;
		        case "rm":
		        	mySystem.rm(userValue);
		        	break;
		        case "du":
		        	mySystem.du();
		        	break;
		        case "find":
		        	mySystem.find(userValue);
		        	break;
		        case "help":
		        	break;
		       //If the command is not correct
		        default:
		            System.out.println("ERROR: Invalid command");
		            break;
		    }
		}
	}
}
