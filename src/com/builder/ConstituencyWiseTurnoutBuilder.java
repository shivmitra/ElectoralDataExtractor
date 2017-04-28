package com.builder;

import java.util.ArrayList;
import java.util.List;

import com.model.Election;
import com.model.DBModel.ConstituencyWiseTurnout;
import com.model.constituency.Constituency;
import com.model.turnout.Turnout;

public class ConstituencyWiseTurnoutBuilder {
	private List<Object> list;
	private List<ConstituencyWiseTurnout> result;
	private Election activeE;
	private Constituency activeC;
	private Turnout activeT;
	
	public ConstituencyWiseTurnoutBuilder(List<Object> list){
		this.list = list;
		this.result = new ArrayList<ConstituencyWiseTurnout>();
		this.process();
	}
	
	private void process(){
		for(Object o : list){
			if(o instanceof Election){
				activeE = (Election) o;
			}else if (o instanceof Constituency){
				build();
				activeC = (Constituency) o;
			}else if(o instanceof Turnout){
				activeT = (Turnout) o;
			}	
		}
		build();
	}
	
	private void build(){
		if(activeE != null && activeT != null && activeC != null){
			result.add(new ConstituencyWiseTurnout(activeE, activeC, activeT));
			erase();
		}
	}
	
	private void erase(){
		activeT = null;
		activeC = null;
	}
	
	public List<ConstituencyWiseTurnout> getResult(){
		return result;
	}
}
