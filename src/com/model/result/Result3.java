package com.model.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Result3 extends Result{
	private final double PercentageVote;
	
	public Result3(int id, String name, char gender, String party, int vote, double percentage){
		super(id,name,gender,party,vote);
		this.PercentageVote = percentage;
	}
}
