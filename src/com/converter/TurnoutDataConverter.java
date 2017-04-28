package com.converter;

import java.util.Scanner;

import com.model.turnout.Turnout;
import com.model.turnout.Turnout1;
import com.model.turnout.Turnout2;
import com.model.turnout.Turnout4;

public class TurnoutDataConverter implements IDataConverter{
	static{
		ContextAwareDataConverterFactory.getInstance().register(new Context(Constants.PDF.DECLARED_RESULT_CONTEXT),
									new TurnoutDataConverter(""));
	}
	
	private Turnout turnout;
	private final String data;
	
	public TurnoutDataConverter(String data) {
		this.data = data;
	}
	
	@Override
	public boolean isConvertable() {
		
		if(parseForTurnout1(false) || parseForTurnout1(true))
			return true;
		
		if(parseForTurnout2())
			return true;
		
		if(parseForTurnout4(false) || parseForTurnout4(true))
			return true;
		
		return false;
	}

	private boolean parseForTurnout4(boolean parseForUncontested){
		try{
			String res[] = Utils.splitData(data, Constants.PDF.TURNOUT_TOTAL);
			Scanner scan = new Scanner(res[1]);
			int gen = -1, post = -1, total=-1;
			if(!parseForUncontested){
				gen = scan.nextInt();
				post = scan.nextInt();
				total = scan.nextInt();
				scan.close();
			}else{
				scan.close();
				// in case for uncontested constituency, no value after TOTAL
				if(!"".equals( res[1].trim() ))
					throw new Exception();
			}

			turnout = new Turnout4(total, gen, post);
		}catch(Exception e){
			return false;
		}
		return true;
		
	}
	
	private boolean parseForTurnout1(boolean parseForUncontested){
		try{
			int electors = -1, voters = -1,valid = -1;
			double per = -1;
			
			String[] res = Utils.splitData(data, Constants.PDF.ELECTORS);
			Scanner scanner = new Scanner(res[1]);
			electors = scanner.nextInt();
			scanner.close();
			
			if(!parseForUncontested){
				res = Utils.splitData(data, Constants.PDF.VOTERS); 
				scanner = new Scanner(res[1]);
				voters = scanner.nextInt();
				scanner.close();
				
				res = Utils.splitData(data, Constants.PDF.POLL_PERCENTAGE); 
				scanner = new Scanner(res[1]);
				per = Utils.getPercentage(scanner);
				scanner.close();
				
				res = Utils.splitData(data, Constants.PDF.VALID_VOTES); 
				scanner = new Scanner(res[1]);
				scanner.nextInt();
			}else if(parseForUncontested){
				if(!data.contains(Constants.PDF.POLL_PERCENTAGE) || !data.contains(Constants.PDF.VALID_VOTES) 
						|| !data.contains(Constants.PDF.VOTERS) )
					throw new RuntimeException();
			}
			
			scanner.close();
			
			turnout = new Turnout1(voters,electors,per,valid);
			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	private boolean parseForTurnout2(){
		try{
			String res[];
			if(data.contains(Constants.PDF.TURNOUT1))
				res = Utils.splitData(data, Constants.PDF.TURNOUT1);
			else
				res = Utils.splitData(data, Constants.PDF.TURNOUT2);
			
			res = Utils.splitData(res[1], Constants.PDF.TURNOUT_TOTAL);
			Scanner scanner = new Scanner(res[1]);
			
			int gen = scanner.nextInt();
			int post = scanner.nextInt();
			int total = scanner.nextInt();
			double per = Utils.getPercentage(scanner);
			
			turnout = new Turnout2(total,per,gen,post);			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	@Override
	public Object getObject() {
		return turnout;
	}

	@Override
	public IDataConverter getNewInstance(String data) {
		return new TurnoutDataConverter(data);
	}

}
