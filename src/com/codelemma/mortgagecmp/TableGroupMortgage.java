package com.codelemma.mortgagecmp;

import java.util.ArrayList;

public class TableGroupMortgage extends TableMortgage {
 
	private ArrayList<TableChildMortgage> items;
	
	
	public ArrayList<TableChildMortgage> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<TableChildMortgage> items) {
		this.items = items;
	}
	
	
}