package com.model.turnout;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Turnout4 extends Turnout{

	private final int generalVotes;
	private final int postalVotes;
	
	public Turnout4(int total,int general, int postal){
		super(total);
		this.generalVotes = general;
		this.postalVotes = postal;
	}
	
}
