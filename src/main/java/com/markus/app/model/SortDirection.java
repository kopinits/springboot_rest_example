package com.markus.app.model;

public enum SortDirection {
	ASC,
	DESC;
	
	public static SortDirection isAsc(final Boolean isAscending){
		if (isAscending == null || isAscending){
			return ASC;
		}else{
			return DESC;
		}
	}

}
