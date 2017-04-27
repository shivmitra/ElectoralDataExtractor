package com.model.constituency;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Constituency1 extends Constituency{
	private final int electors;
	
	public Constituency1(int id, String name, int electors){
		super(id,name);
		this.electors = electors;
	}
}
