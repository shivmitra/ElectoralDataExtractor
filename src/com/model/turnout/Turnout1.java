package com.model.turnout;

import com.model.TurnoutBasic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Turnout1 extends TurnoutBasic{
	private final int validVotes;
	
	public Turnout1(int voters, int electors, double percentageTurnout, int validVotes){
		super(voters,electors,percentageTurnout);
		this.validVotes = validVotes;
	}
}
