package com.model;

import com.model.turnout.Turnout;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TurnoutBasic extends Turnout{

	private final int electors;
	private final double percentageTurnout;
	
	
	public TurnoutBasic(int voters, int electors, double percentageTurnout){
		super(voters);
		this.electors = electors;
		this.percentageTurnout = percentageTurnout;
	}
}
