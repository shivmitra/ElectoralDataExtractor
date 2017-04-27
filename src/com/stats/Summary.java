package com.stats;

import java.util.HashMap;
import java.util.List;

import com.model.Election;
import com.model.DBModel.ConstituencyWiseData;
import com.model.DBModel.ConstituencyWiseResult;
import com.model.DBModel.ConstituencyWiseTurnout;
import com.model.constituency.Constituency;
import com.model.result.Result;
import com.model.turnout.Turnout;

import lombok.Data;

@Data
public class Summary {

	private HashMap<ECPair, List<Result> > resultmap = new HashMap<>();
	private HashMap<ECPair, CData> datamap = new HashMap<>();
	private HashMap<ECPair, Turnout> turnoutmap = new HashMap<>();
	
	@Data
	private class CData{
		public final int electors;
		public final int voters;
		public CData(ConstituencyWiseData d){
			this.electors = d.getElectors().getNumber();
			this.voters = d.getVoters().getNumber();
		}
	}
	
	@Data
	private class ECPair{
		public Election e;
		public Constituency c;
		public ECPair(Election e, Constituency c){
			this.e = e;
			this.c = c;
		}
		
		public boolean equals(Object obj){
			if(!(obj instanceof ECPair)) return false;
			ECPair that = (ECPair) obj;
			if(this.e.equals(that.e) && this.c.getId() == that.c.getId() && 
					this.c.getName().replaceAll("\\s+", "").equals(that.c.getName().replaceAll("\\s+", "")))
				return true;
			return false;
		}
		
		@Override public int hashCode() {
			final int PRIME = 43;
			int result = 1;
			result = (result*PRIME) + this.c.getName().replaceAll("\\s+", "").hashCode();
			result = (result*PRIME) + this.c.getId();
			result = (result*PRIME) + this.e.hashCode();
			return result;
		}
	}
	
	public void add(List<?> ld){
		
		ld.stream().filter(d-> d instanceof ConstituencyWiseData)
					.forEach(d->{
						ConstituencyWiseData cwd = (ConstituencyWiseData)d;
						CData cdata = new CData(cwd);
						ECPair pair = new ECPair(cwd.getElection(),cwd.getConstituency());
						datamap.put(pair, cdata);
					});
		
		ld.stream().filter(d-> d instanceof ConstituencyWiseResult)
		.forEach(d->{
			ConstituencyWiseResult cwr = (ConstituencyWiseResult)d;
			ECPair pair = new ECPair(cwr.getElection(),cwr.getConstituency());
			resultmap.put(pair, cwr.getResult());
		});
		
		ld.stream().filter(d-> d instanceof ConstituencyWiseTurnout)
		.forEach(d->{
			ConstituencyWiseTurnout cwt = (ConstituencyWiseTurnout)d;
			ECPair pair = new ECPair(cwt.getElection(),cwt.getConstituency());
			turnoutmap.put(pair, cwt.getTurnout());
		});
	}
	
	
	public void writeSummary(){
		if(datamap.size() == resultmap.size() && turnoutmap.size() == datamap.size())
			return;
		
		// check if all constituency data in resultmap are also in datamap 
		System.out.println("Constituency" + " is in resultdata but missing in constituencydata");
		resultmap.entrySet().stream()
								.forEach(e -> {
									if(!datamap.containsKey(e.getKey()))
										System.out.println(e.getKey().c);
								});
		
		System.out.println("Constituency" + " is in constituencydata but missing in resultdata");
		datamap.entrySet().stream()
		.forEach(e -> {
			// to check for uncontested constituencies
			if(!resultmap.containsKey(e.getKey()) && datamap.get(e).voters != -1)
				System.out.println(e.getKey().c);
		});
		
		System.out.println("total constituency data " + datamap.size());
		System.out.println("total result data " + resultmap.size());
		System.out.println("total turnout data " + turnoutmap.size());
	}
}
