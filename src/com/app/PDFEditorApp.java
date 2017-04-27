package com.app;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.converter.Constants;
import com.converter.Context;
import com.converter.Utils;
import com.editor.PDFEditor;


public class PDFEditorApp {
	public static void main(String[] args) throws IOException{
		String folderPath = "pdf/";
		String resultPath = "result/";
		String errorPath = "error/";
		
       	try(Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
       	    paths.forEach(filePath -> {
       	        if (Files.isRegularFile(filePath)) {
       	        	try{
       	        		 String filename = Utils.getFileNameFromPath(filePath.toString());
       	        		 String extension = Utils.getFileExtensionFromPath(filePath.toString());
       	        		 
       	        		 if("pdf".equals(extension)){
			       	         // Creating a File object that represents the disk file.
       	        			 
       	        			 StringBuilder s = new StringBuilder();s.append(resultPath);s.append(filename);s.append(".txt");
       	        			 String resultFile = s.toString();
       	        			 
       	        			 s = new StringBuilder();s.append(errorPath);s.append(filename);s.append(".txt");
       	        			 String errorFile = s.toString();
			       	         
       	        			 PrintStream o = new PrintStream(new File(resultFile));
			       	         PrintStream e = new PrintStream(new File(errorFile));
			       	         
			       	         // Store current System.out before assigning a new value
			       	         PrintStream console = System.out;
			       	         PrintStream econsole = System.err;
			       	  
			       	         // Assign o to output stream
			       	         System.setOut(o);
			       	         System.setErr(e);
			       	         Context ctx = 
			       	        	 new Context(Constants.PDF.DECLARED_RESULT_CONTEXT);
			       	         new PDFEditor(filePath.toString(), ctx).edit();
			       	         
			       	         // Use stored value for output stream
			       	         System.setOut(console);
			       	         System.setErr(econsole);
			       	         
			       	         System.out.println(filename);
       	        		 }
       	        	}catch(Exception e){
       	        		System.err.println(e);
       	        		e.printStackTrace();
       	        	}
       	        }
       	    });
       	    
       	} 
	}
}
