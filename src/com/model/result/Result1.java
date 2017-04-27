package com.model.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Result1 extends Result{
	
	private final int age;
	private final String category;
	private final int postalVote;
	private final int generalVote;
	
	public Result1(int id, String name, char gender, String party, int vote, int generalVote, int postalVote, int age, String category){
		super(id,name,gender,party,vote);
		this.age = age;
		this.category = category;
		this.postalVote = postalVote;
		this.generalVote = generalVote;
	}
}
