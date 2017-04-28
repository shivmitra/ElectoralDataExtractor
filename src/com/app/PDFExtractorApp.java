package com.app;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import com.builder.ConstituencyWiseResultBuilder;
import com.builder.ConstituencyWiseTurnoutBuilder;
import com.builder.ConstituencyWiseDataBuilder;
import com.converter.Constants;
import com.converter.Context;
import com.converter.Utils;
import com.extractor.PdfObjectExtractor;
import com.model.DBModel.ConstituencyWiseResult;
import com.model.DBModel.ConstituencyWiseTurnout;
import com.model.DBModel.ConstituencyWiseData;
import com.mongodb.MongoClient;
import com.stats.Summary;

import org.apache.commons.io.FileUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;


public class PDFExtractorApp {
	
	public static Datastore connectMongo(){
		MongoClient client = new MongoClient("127.0.0.1",27017);
		client.dropDatabase("test");
		Morphia morphia = new Morphia();
		morphia.map(ConstituencyWiseResult.class);
		morphia.map(ConstituencyWiseTurnout.class);
		morphia.map(ConstituencyWiseData.class);
		
		Datastore ds = morphia.createDatastore(client, "test");
		ds.ensureIndexes();
		
		return ds;
	}
	
	public static void main(String[] args) throws IOException{
		//to insure proper factory registration
		try{
			Class.forName("com.converter.ResultDataConverter");
			Class.forName("com.converter.TurnoutDataConverter");
			Class.forName("com.converter.ConstituencyDataConverter");
			Class.forName("com.converter.ElectionDataConverter");
			Class.forName("com.converter.TypeDataConverter");
			Class.forName("com.converter.CountDataConverter");
		}catch(Exception e){
			
		}
		
		String folderPath = "pdf/";
		String resultPath = "result/";
		String errorPath = "error/";
		
		FileUtils.deleteDirectory(new File(resultPath));
		FileUtils.deleteDirectory(new File(errorPath));
		
		new PDFExtractorApp().run(folderPath, resultPath, errorPath);
		
	}

	private void run(String folderPath, String resultPath, String errorPath) throws IOException{
		
		Datastore ds = null;
		//ds = connectMongo();
		
		Context[] ctxstosearch = {new Context(Constants.PDF.DECLARED_RESULT_CONTEXT),
				new Context(Constants.PDF.CONSTITUENCY_DATA_CONTEXT),
				new Context(Constants.PDF.ELECTION_CONTEXT)};
       	
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
			       	         
       	        			 File result = new File(resultFile);
       	        			 File error = new File(errorFile);
       	        			 PrintStream o = new PrintStream(FileUtils.openOutputStream(new File(resultFile)));
			       	         PrintStream e = new PrintStream(FileUtils.openOutputStream(new File(errorFile)));
			       	         
			       	         // Store current System.out before assigning a new value
			       	         PrintStream console = System.out;
			       	         PrintStream econsole = System.err;
			       	  
			       	         // Assign o to output stream
			       	         System.setOut(o);
			       	         System.setErr(e);
			       	         
			       	         Summary sum = new Summary();
			       	         
			       	         extractAndWriteContextRelatedData(ctxstosearch,filePath.toString(),ds,sum);
       	        		 	 
			       	         try{
			       	        	 sum.writeSummary();
			       	         }catch(Exception ex){
			       	        	 System.err.println(ex);
			       	        	 ex.printStackTrace();
			       	         }
			       	         
			       	         //Use stored value for output stream
			       	         System.setOut(console);
			       	         System.setErr(econsole);
			       	         
			       	         // check if files are empty, trunc them if they are, result and error
			       	         if(result.length() == 0)
			       	        	result.delete();
			       	         if(new File(errorFile).length() == 0)
			       	        	error.delete();
			       	         
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
	
	
	public void writeList(List<?> list,Datastore ds){
		for(Object o : list){
			try{
				ds.save(o);
				//Utils.print(o);
			}catch(Exception e){
				System.err.println(e.toString());
			}
		}
	}
	
	private void extractAndWriteContextRelatedData(Context[] ctxs, String filePath, Datastore ds, Summary sum) throws IOException{
		
         List<Object> list = new PdfObjectExtractor(filePath, ctxs).getObjects();

         List<ConstituencyWiseData> cwdList = new ConstituencyWiseDataBuilder(list).getResult();
         List<ConstituencyWiseResult> cwrList = new ConstituencyWiseResultBuilder(list).getResult();
         List<ConstituencyWiseTurnout> cwtList = new ConstituencyWiseTurnoutBuilder(list).getResult();
         
         cwrList.stream().forEach(sum::add);
         cwdList.stream().forEach(sum::add);
         cwtList.stream().forEach(sum::add);
         
         //writeList(cwrList,ds);
         //writeList(cwtList,ds);
         //writeList(cwdList,ds);
	}
}
