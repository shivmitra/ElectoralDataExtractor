package com.converter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.model.constituency.Constituency;
import com.model.constituency.Constituency1;
import com.model.constituency.Constituency2;

public class ConstituencyDataConverter implements IDataConverter {
	static{
		ContextAwareDataConverterFactory.getInstance().register(new Context(Constants.PDF.DECLARED_RESULT_CONTEXT),
									new ConstituencyDataConverter(""));
		ContextAwareDataConverterFactory.getInstance().register(new Context(Constants.PDF.CONSTITUENCY_DATA_CONTEXT),
				new ConstituencyDataConverter(""));
	}
	
	private final String data;
	private Constituency constituency;
	private List<String> rejectWords;
	private List<String> constituencyIdentifiers;
	
	public ConstituencyDataConverter(String data) {
		this.data = data;
		this.rejectWords = new ArrayList<>();
		constituencyIdentifiers = new ArrayList<>();
		Arrays.stream(Constants.PDF.REJECT_IDENTIFIERS)
			.forEach(rejectWords::add);
		Arrays.stream(Constants.PDF.CONSTITUENCY)
			.forEach(constituencyIdentifiers::add);
	}
	
	@Override
	public boolean isConvertable() {
		
		if(parseForConstituency())
			return true;
		
		if(parseForConstituency1())
			return true;
		
		if(parseForConstituency2())
			return true;
		
		
		return false;
	}

	
	private boolean parseForConstituency1() {
		try{
			String res[] = splitFromConstituency();
			
			res = Utils.splitData(res[1], Constants.PDF.TOTAL_ELECTOR);
			
			Scanner scanner = new Scanner(res[0]);
			int id = scanner.nextInt();
			String name = Utils.getName(scanner," ");
			scanner.close();
			
			scanner = new Scanner(res[1]);
			int electors = scanner.nextInt();
			scanner.close();
			
			constituency = new Constituency1(id, name,electors);
			checkForRejectedWords();
		}catch(Exception e){
			return false;
		}
		
		return true;
	}

	private boolean parseForConstituency2() {
		try{
			String res[] = splitFromConstituency();
			res = Utils.splitData(res[1], Constants.PDF.NUMBER_OF_SEATS);
			
			Scanner scanner = new Scanner(res[0]);
			int id = scanner.nextInt();
			String name = Utils.getName(scanner," ");
			scanner.close();
			
			scanner = new Scanner(res[1]);
			int seats = scanner.nextInt();
			scanner.close();
			
			constituency = new Constituency2(id, name,seats);
			checkForRejectedWords();
		}catch(Exception e){
			return false;
		}
		
		return true;
	}

	private boolean parseForConstituency() {
		try{
			String res[] = new String[2];
			try{
				res = splitFromConstituency();
			}catch(Exception e){
				res[1] = data;
			}
			
			Scanner scanner = new Scanner(res[1]);
			int id = scanner.nextInt();
			String name = Utils.getName(scanner," ");
			
			constituency = new Constituency(id, name);
			checkForRejectedWords();
			
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public Object getObject() {
		return constituency;
	}

	@Override
	public IDataConverter getNewInstance(String data) {
		return new ConstituencyDataConverter(data);
	}
	
	//TODO this is very ugly, check if we can rather do it with scanner.hasnext?
	private void checkForRejectedWords(){
		if(rejectWords.contains(constituency.getName()))
			throw new RuntimeException("rejected constituency name");
		rejectWords.stream().forEach(d-> {
			if(constituency.getName().contains(d))
				throw new RuntimeException();
		});
	}
	
	private String[] splitFromConstituency(){	
		List<String[]> result = constituencyIdentifiers.stream()
						.map((value) -> {
							if(data.contains(value))
								return data.split(value);
							return null;
						}).filter(Objects::nonNull)
						.collect(Collectors.toList());
		if(result.size() == 1)
			return result.get(0);
		throw new RuntimeException("not a valid constitunecy data");
	}

}
