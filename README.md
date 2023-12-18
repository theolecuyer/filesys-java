filesys-java
A Command Line Directory Builder that simulates a PC directory system

Commands usable:

- create fileName This command creates a new file with the given filename. It will then read user input until a ~ is in the input.
- cat fileName Prints out the contents of the given filename.
- rm filename Removes the file with the given name from the current directory.
- mkdir dirName Make a new directory with the given name in the current directory.
- rmdir dirName remove the directory named "dirName" (and all of its contents) from the current directory.
- cd dirName Changes the current directory to the input. It can be strung together using /. The command .. goes into the parent directory and the command / goes into the root directory.
- ls Prints out all files and directories in the current directory.
- du This command prints out the disk usage in bytes of all files in the current directory and its children.
- pwd Prints the full path to the current directory including the root.
- find name Finds all directories and files with the given name and prints out their locations.
- exit Exit the program.
