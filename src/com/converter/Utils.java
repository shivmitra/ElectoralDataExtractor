package com.converter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Utils {
	
	
	public static char getSex(String data) {
		int fidx = data.lastIndexOf(" F ");
		int midx = data.lastIndexOf(" M ");
		int genidx = Math.max(fidx, midx);
		if (genidx == fidx)
			return 'F';
		return 'M';
	}
	
	public static int getLastInt(Scanner scanner){
		int val = scanner.nextInt();
		while(scanner.hasNextInt())
			val = scanner.nextInt();
		if(scanner.hasNext())
			throw new RuntimeException("values other than int are present in data");
		return val;
	}
	
	public static String[] splitData(String data, String val) {
		if (data.contains(val)) {
			return data.split(val);
		}
		throw new RuntimeException("data " + data + " doesnt contain " + val);
	}

	public static String[] splitDataFromSex(String data) {
		int fidx = data.lastIndexOf(" F ");
		int midx = data.lastIndexOf(" M ");
		int genidx = Math.max(fidx, midx);
		String[] result = new String[2];
		result[0] = data.substring(0, genidx);
		result[1] = data.substring(genidx + 3);
		return result;
	}

	public static String getString(Scanner s) {
		if (s.hasNextInt() || s.hasNextFloat() || s.hasNextDouble())
			throw new RuntimeException("no string found in scanner");
		
		return s.next();
	}

	public static String getName(Scanner s, String seperator) {
		StringBuilder sb = new StringBuilder();
		if(s.hasNext() == false)
			throw new RuntimeException("name cant be null");
		
		while (s.hasNext()) {
			sb.append(getString(s));
			sb.append(seperator);
		}
		return sb.toString().trim();
	}

	public static double getPercentage(Scanner s) {
		if (s.hasNextDouble())
			return s.nextDouble();
		String val = s.next();
		// some pdfs have 0% written as #Num!
		if(val.equalsIgnoreCase("#NUM!"))
			return 0;
		if (val.charAt(val.length() - 1) != '%')
			throw new RuntimeException("no percentage found in scanner");

		return Double.parseDouble(val.substring(0, val.length() - 1));
	}

	public static String joinArray(String[] str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length; i++)
			sb.append(str[i] + " ");
		
		String result = sb.toString().trim();
		result = result.replace(":", " ");
		// replace dot only if it is not between digits else decimal data will corrupt
		result = result.replaceAll("\\.(?!\\d)", " ");
		result = result.replace("-", " ");
		result = result.replaceAll("\\s+", " ");
		return result.toUpperCase();
	}

	public static String getFileNameFromPath(String path) {
		Path p = Paths.get(path);
		String name = p.getFileName().toString();
		String[] res = name.split("\\.");
		return res[0];
	}

	public static String getFileExtensionFromPath(String path) {
		Path p = Paths.get(path);
		String name = p.getFileName().toString();
		String[] res = name.split("\\.");
		return res[1];
	}

	public static void print(Object o) {
		System.out.println(ReflectionToStringBuilder.toString(o));
	}

	public static void print(String[] data) {
		System.out.println(Utils.joinArray(data));
	}

	public static void skipTillNextInt(Scanner scanner) {
		// while scanner has more values which are not ints
		while(scanner.hasNext() && !scanner.hasNextInt())
			scanner.next();
	}
}
