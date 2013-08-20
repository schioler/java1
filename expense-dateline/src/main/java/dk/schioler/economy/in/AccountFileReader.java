package dk.schioler.economy.in;

import java.io.File;

import dk.schioler.economy.Line;

public interface AccountFileReader {
	 public void openFile(File file);
	 public void closeFile();
	 public Line readNextLine();
	 
	
}
