package com.model.constituency;

import lombok.Data;

@Data
public class Constituency{
	private final int id;
	private final String name;
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Constituency)) return false;
		Constituency that = (Constituency) obj;
		
		// sometimes there are spaces in constituency name at constituency data while none at detailed result
		if(that.id == this.id && this.name.replaceAll("\\s+", "").equalsIgnoreCase(that.name.replaceAll("\\s+", "")))
			return true;
		return false;
	}
	
	@Override public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = (result*PRIME) + id;
		result = (result*PRIME) + this.name.replaceAll("\\s+", "").hashCode();
		return result;
	}
}
