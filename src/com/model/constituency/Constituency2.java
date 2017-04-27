package com.model.constituency;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Constituency2 extends Constituency{
	private final int numberofSeats;
	
	public Constituency2(int id, String name, int numberofseats){
		super(id,name);
		this.numberofSeats = numberofseats;
	}

}
