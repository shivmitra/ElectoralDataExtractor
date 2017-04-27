package com.extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import technology.tabula.Table;

import com.converter.Constants;
import com.converter.Context;
import com.converter.ContextAwareDataConverterFactory;
import com.converter.IDataConverter;
import com.converter.Utils;
import com.model.Election;

public class PdfObjectExtractor {
	private final String path;
	private final Context[] ctxs;
	private final ContextAwareDataConverterFactory factory = ContextAwareDataConverterFactory.getInstance();
	private final PDFTableExtractor ext;
	
	public PdfObjectExtractor(String filepath, Context[] ctxs){
		this.path = filepath;
		this.ctxs = ctxs;
		this.ext = new PDFTableExtractor(filepath);
	}
	
	public List<Object> getObjects() throws IOException{
		
		List<Object> list =  new ArrayList<Object>();
		ContextAwareDataConverterFactory.getInstance().addContexts(ctxs);
		String filename = Utils.getFileNameFromPath(path);
		
		factory.setActiveContext(new Context(Constants.PDF.ELECTION_CONTEXT));
		
		IDataConverter convert = factory.getDataConverter(filename);
		Election election =(Election) convert.getObject();
	
		list.add(election);
		
		factory.clearActiveContext();
		
		int total = ext.getTotalPage();
		
		List<Object> ret = IntStream.rangeClosed(1, total).mapToObj(p->{
												factory.clearActiveContext();
												return this.getTablesFromPage(p);
										})
										.flatMap(l->l.stream())
										.map(ExtractUtils::tableToArrayOfRows)
										.flatMap(Arrays::stream)
										.map(Utils::joinArray)
										//.forEach(System.out::println);
										.map(data -> {// this line removes our capability to parallelize, see if we can work around TODO 
											factory.setActiveContext(data);	 
											return factory.getDataConverter(data);})
										.filter(Objects::nonNull)
										.map(conv -> conv.getObject())
										.collect(Collectors.toList());
		
		factory.clearAllContexts();
		list.addAll(ret);
		return list;
	}
	
	// the work of this function is just to catch exceptions. check if it can rather be done in map. TODO
	private List<Table> getTablesFromPage(int page){
		List<Table> tlist;
		try {
			tlist = ext.getTables(page);
			return tlist;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
