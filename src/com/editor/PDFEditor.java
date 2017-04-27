package com.editor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import technology.tabula.Table;

import com.converter.Context;
import com.converter.ContextAwareDataConverterFactory;
import com.converter.Utils;
import com.extractor.ExtractUtils;
import com.extractor.PDFTableExtractor;

public class PDFEditor {
	private final ContextAwareDataConverterFactory factory = ContextAwareDataConverterFactory.getInstance();
	private final List<PDPage> pages;
	private final Context ctx;
	private final PDDocument doc;
	private final String path;
	private final PDFTableExtractor ext;
	
	@SuppressWarnings("unchecked")
	public PDFEditor(String path, Context ctx){
		this.path = path;
		try {
			doc = PDDocument.load(path);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		this.ext = new PDFTableExtractor(path);
		PDDocumentCatalog catalog = doc.getDocumentCatalog();
		pages = (List<PDPage>) catalog.getAllPages();
		this.ctx = ctx;
	}
	
	public void edit(){
		Context[] ctxs = new Context[1];
		ctxs[0] = ctx;
		factory.addContexts(ctxs);
		
		int total = pages.size();
	
		IntStream.rangeClosed(15, total).forEach(pagenum -> {
			
			List<Table> l;
			try {
				l = ext.getTables(pagenum);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			
			//Set active context if for this page
			boolean sawContext = l.stream().map(ExtractUtils::tableToArrayOfRows)
						.flatMap(Arrays::stream)
						.map(Utils::joinArray)
						.reduce(false, (flag,data) -> {
							System.out.println(data);
							if(factory.setActiveContext(data) == null)
								return flag;
							else
								return true;
						},(flag1,flag2) ->  flag1 | flag2);
			
			if(ctx.equals(factory.getActiveContext()) && sawContext == false){
				setText(pagenum,ctx.ctx);
			}
				
		});
			
		factory.clearAllContexts();
		factory.clearActiveContext();
		
		try {
			doc.save("changed/"+path);
			doc.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}									
	
   public void setText(int pagenum, String text){
    	try{
    		PDPage page = pages.get(pagenum-1);
    	
	    	PDFont font = PDType1Font.HELVETICA_BOLD;

	    	PDPageContentStream contentStream = new PDPageContentStream(doc, page,true,false);
	    	contentStream.setFont( font, 12 );

	    	contentStream.beginText(); 
	    	contentStream.moveTextPositionByAmount(0, page.findMediaBox().getHeight()-12);

	    	contentStream.drawString(text);
	    	contentStream.endText();
	    	contentStream.close();
	    		    	
    	}catch(Exception e){
    		System.err.println(e.toString());
    	}
    }
}
