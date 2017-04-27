package com.converter;

import com.model.Electors;
import com.model.Voters;


public class TypeDataConverter implements IDataConverter{

	static{
		ContextAwareDataConverterFactory.getInstance().register(new Context(Constants.PDF.CONSTITUENCY_DATA_CONTEXT),
									new TypeDataConverter(""));
	}
	
	private final String data;
	
	public TypeDataConverter(String data) {
		this.data = data;
	}
	
	@Override
	public boolean isConvertable() {
		if(data.endsWith(Constants.PDF.ELECTORS) || data.endsWith(Constants.PDF.VOTERS) || data.endsWith(Constants.PDF.ELECTORS_WHO_VOTED))
			return true;
		return false;
	}

	@Override
	public Object getObject() {
		if(data.endsWith(Constants.PDF.ELECTORS) )
			return new Electors();
		else if(data.endsWith(Constants.PDF.VOTERS)|| data.endsWith(Constants.PDF.ELECTORS_WHO_VOTED))
			return new Voters();
		throw new RuntimeException("TypeDataConverter getObject null");
	}

	@Override
	public IDataConverter getNewInstance(String data) {
		return new TypeDataConverter(data);
	}

}
