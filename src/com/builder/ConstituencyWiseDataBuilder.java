package com.builder;

import java.util.ArrayList;
import java.util.List;

import com.model.Count;
import com.model.Election;
import com.model.Electors;
import com.model.Voters;
import com.model.DBModel.ConstituencyWiseData;
import com.model.constituency.Constituency;

public class ConstituencyWiseDataBuilder {
	private List<Object> list;
	private List<ConstituencyWiseData> result;
	private Election activeE;
	private Constituency activeC;
	private Electors activeEl;
	private Voters activeV;
	
	public ConstituencyWiseDataBuilder(List<Object> list){
		this.list = list;
		this.result = new ArrayList<ConstituencyWiseData>();
		this.process();
	}
	
	private void process(){
		for(Object o : list){
			if(o instanceof Election){
				activeE = (Election) o;
			}else if (o instanceof Constituency){
				build();
				activeC = (Constituency) o;
			}else if(o instanceof Voters){
				if(activeV == null)
					activeV = (Voters) o;
			}else if(o instanceof Electors){
				if(activeEl == null)
					activeEl = (Electors) o;
			}else if(o instanceof Count){
				Count cnt = (Count) o;
				if(activeV != null && activeV.getNumber() == -1)
					activeV.setNumber(cnt.getValue());
				else if(activeEl != null)
					activeEl.setNumber(cnt.getValue());
			}
		}
		build();
	}
	
	private void build(){
		if(activeC  == null || activeV == null || activeEl == null) return;
		
		// sometimes on uncontested seats this data might remain -1
		double turnout  = 0;
		if(activeV.getNumber()!=-1 && activeEl.getNumber()!=-1)
			turnout =  (activeV.getNumber()*100.0)/activeEl.getNumber();
		
		result.add( new ConstituencyWiseData(activeE, activeC, activeEl, activeV, turnout) );
		
		activeC = null;
		activeEl = null;
		activeV = null;
	}
	
	public List<ConstituencyWiseData> getResult(){
		return result;
	}
}
