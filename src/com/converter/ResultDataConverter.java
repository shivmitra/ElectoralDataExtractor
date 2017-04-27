package com.converter;

import java.util.Scanner;

import lombok.Data;

import com.model.result.Result;
import com.model.result.Result1;
import com.model.result.Result2;
import com.model.result.Result3;

public class ResultDataConverter implements IDataConverter {
	
	static{
		ContextAwareDataConverterFactory.getInstance().register(new Context(Constants.PDF.DECLARED_RESULT_CONTEXT),
									new ResultDataConverter(""));
	}
	
	private final String data;
	private Result result;
	
	public ResultDataConverter(String data) {
		this.data = data;
	}
	
	@Override
	public boolean isConvertable() {
	
		if(parseForResult1(false) || parseForResult1(true))
			return true;
		
		if(parseForResult2(false) || parseForResult2(true))
			return true;
		
		if(parseForResult3(false) || parseForResult3(true))
			return true;
		
		return false;
	}

	@Override
	public Object getObject() {
		return result;
	}
	
	
	@Data private class CommonResult{
		public final int id;
		public final String name;
		public final char sex;
		public final String party;
		public final String cat;
		public final int age;	
	}
	
	@Data private class Pair{
		public final CommonResult res;
		public final Scanner scanner;
	}
	
	public Pair getCommonResult(Boolean doubleId, boolean findAge, boolean findCategory){
		
		String res[] = Utils.splitDataFromSex(data);
		Scanner scanner = new Scanner(res[0]);
		
		int id = scanner.nextInt();
		if(doubleId) id = scanner.nextInt();
		
		String name = Utils.getName(scanner, " ");
		char sex = Utils.getSex(data);
		
		scanner.close();
		scanner = new Scanner(res[1]);
		
		int age = 0;
		if(findAge)
			age = scanner.nextInt();
		
		String cat = "";
		if(findCategory)
			cat = Utils.getString(scanner);
		
		String party = Utils.getString(scanner);
		
		return new Pair(new CommonResult(id,name,sex,party,cat,age),scanner);
	}
	
	private boolean parseForResult1(boolean parseForUncontested){
		try{
			Pair  p = getCommonResult(false,true,true);
			CommonResult res  = p.res;
			Scanner scanner = p.scanner;
			
			// for skipping party symbol
			Utils.skipTillNextInt(scanner);
			
			int genvote = -1, postalvote = -1, total = -1;
			if(!parseForUncontested){
				genvote = scanner.nextInt();
				postalvote = scanner.nextInt();
				total = scanner.nextInt();
			}
			
			scanner.close();
			
			result = new Result1(res.id,res.name,res.sex,res.party,total,genvote,postalvote,res.age,res.cat);
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	private boolean parseForResult2(boolean doubleId){
		try{
			
			//stupid format has two identical id's
			Pair  p = getCommonResult(true,true,true);
			CommonResult res  = p.res;
			Scanner scanner = p.scanner;
			// for skipping party symbol
			Utils.skipTillNextInt(scanner);
			
			int genvote = scanner.nextInt();
			int postalvote = scanner.nextInt();
			int total = scanner.nextInt();
			double per = Utils.getPercentage(scanner);

			scanner.close();
			
			result = new Result2(res.id,res.name,res.sex,res.party,total,genvote,postalvote,res.age,res.cat,per);
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	private boolean parseForResult3(boolean parseForUncontested){
		try{
			
			Pair  p = getCommonResult(false,false,false);
			CommonResult res  = p.res;
			Scanner scanner = p.scanner;
			
			// for skipping party symbol
			Utils.skipTillNextInt(scanner);
			
			int vote = -1;double per=-1;
			if(!parseForUncontested){
				vote = scanner.nextInt();
				per = Utils.getPercentage(scanner);
			}
			scanner.close();
			
			result = new Result3(res.id,res.name,res.sex,res.party,vote,per);
		}catch(Exception e){
			return false;
		}
		
		return true;
	}

	@Override
	public IDataConverter getNewInstance(String data) {
		return new ResultDataConverter(data);
	}
}
