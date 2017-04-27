package com.extractor;

import java.util.List;

import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;

public class ExtractUtils {
	 	
	    @SuppressWarnings("rawtypes")
		public static  String[][] tableToArrayOfRows(Table table) {
			List<List<RectangularTextContainer>> tableRows = table.getRows();
	        int maxColCount = -Integer.MAX_VALUE;
	        for (int i = 0; i < tableRows.size(); i++) {
	            List<RectangularTextContainer> row = tableRows.get(i);
	            if (maxColCount < row.size()) {
	                maxColCount = row.size();
	            }
	        }
	        String[][] rv = new String[tableRows.size()][maxColCount];
	        for (int i = 0; i < tableRows.size(); i++) {
	            List<RectangularTextContainer> row = tableRows.get(i);
	            for (int j = 0; j < row.size(); j++) {
	                rv[i][j] = table.getCell(i, j).getText();
	            }
	        }
	        return rv;
	    }
	    
}
