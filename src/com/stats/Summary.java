package com.stats;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.converter.Utils;
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
					this.c.getName().replace("(SC)","")
					.replace("(ST)", "")
					.replaceAll("\\s+", "")
					.equals(that.c.getName().replace("(SC)","")
							.replace("(ST)", "")
							.replaceAll("\\s+", "")))
				return true;
			return false;
		}
		
		@Override public int hashCode() {
			final int PRIME = 43;
			int result = 1;
			result = (result*PRIME) + this.c.getName().replace("(SC)","").replace("(ST)", "").replaceAll("\\s+", "").hashCode();
			result = (result*PRIME) + this.c.getId();
			result = (result*PRIME) + this.e.hashCode();
			return result;
		}
	}
	
	public void add(ConstituencyWiseResult cwr){
		ECPair pair = new ECPair(cwr.getElection(),cwr.getConstituency());
		resultmap.put(pair, cwr.getResult());
	}
	
	public void add(ConstituencyWiseTurnout cwt){
		ECPair pair = new ECPair(cwt.getElection(),cwt.getConstituency());
		turnoutmap.put(pair, cwt.getTurnout());
	}
	
	public void add(ConstituencyWiseData cwd){
		CData cdata = new CData(cwd);
		ECPair pair = new ECPair(cwd.getElection(),cwd.getConstituency());
		datamap.put(pair, cdata);
	}
	
	
	public void writeSummary(){
		Difference tdiff = new Difference(turnoutmap.keySet(),datamap);
		Difference rdiff = new Difference(resultmap.keySet(),datamap);
		
		if(tdiff.AB.size() != 0){
			System.out.println("in turnout, not in constituency");
			tdiff.AB.stream().forEach(Utils::print);
		}
		
		if(tdiff.BA.size() != 0){
			System.out.println("in constituency, not in turnout");
			tdiff.BA.stream().forEach(Utils::print);
		}
		
		if(rdiff.AB.size() != 0){
			System.out.println("in result, not in constituency");
			rdiff.AB.stream().forEach(Utils::print);
		}
		
		if(rdiff.BA.size() != 0){
			System.out.println("in constituency, not in result");
			rdiff.BA.stream().forEach(Utils::print);
		}

	}
	
	private class Difference{
		public HashSet<Constituency> AB;
		public HashSet<Constituency> BA;
		
		public Difference(Set<ECPair> set, HashMap<ECPair,CData> data){
			AB = new HashSet<Constituency>();
			BA = new HashSet<Constituency>();
			
			set.stream()
			.forEach(e -> {
				if(!data.containsKey(e))
					AB.add(e.c);
			});
			
			data.keySet().stream()
			.forEach(e -> {
				// to check for uncontested constituencies 
				if(!set.contains(e) && data.get(e).voters != -1)
					BA.add(e.c);
			});
		}
	}
}
