package com.extractor;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import technology.tabula.*;
import technology.tabula.extractors.BasicExtractionAlgorithm;

public class PDFTableExtractor{

	private final ObjectExtractor ext;
	
	public PDFTableExtractor(String path){
        try {
			PDDocument document = PDDocument.load(new File(path));
			
			ext = new ObjectExtractor(document);
			
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Table> getTables(int pagenum) throws IOException{
		List<Table> list = new ArrayList<Table>();		
		Page page = getPage(pagenum);
		BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
		List<Table> t = bea.extract(page);
		list.addAll(t);		
		return list;
	}
	
	public int getTotalPage(){
		return ext.getPageCount();
	}
	
    private  Page getPage(int pageNumber) throws IOException {
    	return ext.extract(pageNumber);
    }
    
    @Override
    public void finalize(){
    	try {
			ext.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
