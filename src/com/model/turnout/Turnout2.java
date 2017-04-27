package com.model.turnout;

import com.model.TurnoutBasic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Turnout2 extends TurnoutBasic{
	
	private final int generalVotes;
	private final int postalVotes;
	
	public Turnout2(int voters, double percentageTurnout, int generalVotes, int postalVotes){
		super(voters,(int)Math.ceil((voters*100)/percentageTurnout),percentageTurnout);
		this.generalVotes = generalVotes;
		this.postalVotes = postalVotes;
	}

}
