package com.converter;

import java.util.Scanner;

import com.model.Election;

public class ElectionDataConverter implements IDataConverter{
	
	static{
		ContextAwareDataConverterFactory.getInstance().register(new Context(Constants.PDF.ELECTION_CONTEXT),
									new ElectionDataConverter(""));
	}
	
	private final String data;
	private Election election;
	
	public ElectionDataConverter(String data) {
		this.data = data;
	}
	
	public boolean isConvertable(){
		
		if(parseForElection())
			return true;
		return false;
	}
	
	public Object getObject(){
		return election;
	}

	@Override
	public IDataConverter getNewInstance(String data) {
		return new ElectionDataConverter(data);
	}
	
	private boolean parseForElection(){
		try{
			String[] res = data.split(Constants.ELECTION_IDENTIFIER);
			Scanner scan = new Scanner(res[1]);
			scan.useDelimiter("_");
			String state = scan.next();
			String typeyear = scan.next();
			String type = "";
			if(typeyear.contains(Constants.ASSEMBLY_ELECTION)){
				type = Constants.ASSEMBLY_ELECTION;
				res = data.split(Constants.ASSEMBLY_ELECTION);
			}else if(typeyear.contains(Constants.GENERAL_ELECTION)){
				type = Constants.GENERAL_ELECTION;
				res = data.split(Constants.GENERAL_ELECTION);
			}
			scan.close();
			scan = new Scanner(res[1]);
			String year = scan.next();
			scan.close();
			election = new Election(state,type,year);
			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
}
