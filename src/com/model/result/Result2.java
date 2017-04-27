package com.model.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Result2 extends Result1{
	private final double PercentageVote;
	
	public Result2(int id, String name, char gender, String party, int vote, int generalVote, 
			int postalVote, int age, String category, double percentageVote){
		super(id, name, gender, party, vote, generalVote, postalVote, age, category);
		this.PercentageVote = percentageVote;
	}

}
