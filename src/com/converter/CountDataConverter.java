package com.converter;

import java.util.Scanner;

import com.model.Count;

public class CountDataConverter implements IDataConverter{

	static{
		ContextAwareDataConverterFactory.getInstance().register(new Context(Constants.PDF.CONSTITUENCY_DATA_CONTEXT),
									new CountDataConverter(""));
	}
	
	private final String data;
	private Count result;
	
	public CountDataConverter(String data){
		this.data = data;
	}
	
	@Override
	public boolean isConvertable() {
		try{
			if(data.contains(Constants.PDF.TURNOUT_TOTAL)){
				Scanner scanner = new Scanner(data.split(Constants.PDF.TURNOUT_TOTAL)[1]);
				int total = Utils.getLastInt(scanner);
				result = new Count(total);
				scanner.close();
				return true;
			}
		}catch(Exception e){
			
		}
		return false;
	}

	@Override
	public Object getObject() {
		return result;
	}

	@Override
	public IDataConverter getNewInstance(String data) {
		return new CountDataConverter(data);
	}

}
