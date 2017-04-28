package com.builder;

import java.util.List;
import java.util.ArrayList;

import com.model.Election;
import com.model.DBModel.ConstituencyWiseResult;
import com.model.constituency.Constituency;
import com.model.result.Result;

public class ConstituencyWiseResultBuilder {
	private List<Object> list;
	private List<ConstituencyWiseResult> result;
	private Election activeE;
	private Constituency activeC;
	private ArrayList<Result> activeR;
	
	public ConstituencyWiseResultBuilder(List<Object> list){
		this.list = list;
		this.result = new ArrayList<ConstituencyWiseResult>();
		activeR = new ArrayList<Result>();
		this.process();
	}
	
	private void process(){
		for(Object o : list){
			if(o instanceof Election){
				activeE = (Election) o;
			}else if (o instanceof Constituency){
				build();
				activeC = (Constituency) o;
			}else if(o instanceof Result){
				activeR.add((Result) o);
			}
		}
		
		build();
	}
	
	private void build(){
		if(activeR.isEmpty()) return;
		if(activeE != null && activeC != null && activeR != null)
			result.add(new ConstituencyWiseResult(activeE, activeC, activeR));
		activeC = null;
		activeR = new ArrayList<Result>();
	}

	
	public List<ConstituencyWiseResult> getResult(){
		return result;
	}
}
