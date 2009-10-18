/*
 * Copyright 2009 James Koval
 * 
 * This file is part of Jedi Mage
 * 
 * Jedi Mage is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * 
 * Jedi Mage is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Jedi Mage. If not, see <http://www.gnu.org/license/>
 */
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

public class filemanager {
	
	/**
	 * Reads file and returns contents;
	 * Ignores empty lines;
	 * Ignore lines starting with '#';
	 * @param filename
	 * @return contents of file; returns null if file cannot be opened
	 */
    public ArrayList<String> read(String filename){
    	//System.out.println("Opening: "+filename);
        BufferedReader inputStream = null;
        ArrayList<String> content = new ArrayList<String>();

        try {
            inputStream = new BufferedReader(new FileReader(filename));
            String l;
            
            //Only takes content from non-commented non-blank lines
            //Comments start with '#' character
            while ((l = inputStream.readLine()) != null){
            	l=l.trim();
            	if(!l.startsWith("#") && !l.isEmpty())
            		content.add(l);
            }
            
        }catch(IOException e){e.printStackTrace(); return null;}//If can't open file, return null
        finally {
        	try{
	            if (inputStream != null)
	                inputStream.close();
        	}catch(IOException e){e.printStackTrace();}//if can't close file, return null
        }
        return content;
    }//read
    
    public void write(String filename, ArrayList<String> content){
        PrintWriter outputStream = null;

        try {
            outputStream = new PrintWriter(new FileWriter(filename));
            for(int i=0; i < content.size(); i++)
                outputStream.println(content.get(i));
            
        }catch(IOException e){e.printStackTrace();}
        finally {
            if (outputStream != null) 
                outputStream.close();
        }
    }//write
    
    public String[] ls(){
    	String[] saDirs;
    	File[] fDirs;
    	File cur = new File("./");
    	
    	// This filter only returns directories
        FileFilter filefilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        
        fDirs = cur.listFiles(filefilter);
        saDirs = new String[fDirs.length];
        for(int i = 0; i < fDirs.length; i++){
        	saDirs[i] = fDirs[i].getName();
        }

    	return saDirs;
    }
    
    public String[] ls(String sDir){
    	File fDir = new File(sDir);
    	
        // This program only uses .txt files
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        };
        
        return fDir.list(filter);

    }
}
