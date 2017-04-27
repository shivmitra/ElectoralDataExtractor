package com.converter;

public interface IDataConverter {
	public boolean isConvertable();
	public Object getObject();
	IDataConverter getNewInstance(String data);
}
