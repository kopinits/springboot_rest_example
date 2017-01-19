package com.markus.app.model.filter;

public enum QueryOption {
	/** Equals*/
	EQ, 
	
	/** Not Equals*/
	NEQ, 
	
	/** Like (just for strings)*/
	LIKE, 
	
	/** Greather than (excludes equals)*/
	GT, 
	
	/** Less than (excludes equals)*/
	LT;
}
