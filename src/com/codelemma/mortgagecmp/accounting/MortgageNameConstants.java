package com.codelemma.mortgagecmp.accounting;

import java.util.HashMap;
import java.util.Map;

import com.codelemma.mortgagecmp.R;

public class MortgageNameConstants {
	private MortgageNameConstants() {}	
	public static final String FIXED_RATE_VARIABLE_PRINCIPAL = "frvp";
	public static final String FIXED_RATE_FIXED_PRINCIPAL = "frfp";
	public static final String ADJUSTABLE_RATE_MORTGAGE = "arm";	
	private static Map<String, Integer> type_id_map = new HashMap<String, Integer>();
    private static Map<String, Integer> type_info = new HashMap<String, Integer>();
    private static String[] mortgage_type_items = {FIXED_RATE_VARIABLE_PRINCIPAL, FIXED_RATE_FIXED_PRINCIPAL, ADJUSTABLE_RATE_MORTGAGE};

	static {
		type_id_map.put(FIXED_RATE_VARIABLE_PRINCIPAL, 0);
		type_id_map.put(FIXED_RATE_FIXED_PRINCIPAL, 1);
		type_id_map.put(ADJUSTABLE_RATE_MORTGAGE, 2);
		
		type_info.put(FIXED_RATE_VARIABLE_PRINCIPAL, R.string.frvp);
		type_info.put(FIXED_RATE_FIXED_PRINCIPAL, R.string.frfp);
		type_info.put(ADJUSTABLE_RATE_MORTGAGE, R.string.arm);
		
		type_info.put(FIXED_RATE_VARIABLE_PRINCIPAL+"BR", R.string.frvp_br);
		type_info.put(FIXED_RATE_FIXED_PRINCIPAL+"BR", R.string.frfp_br);
		type_info.put(ADJUSTABLE_RATE_MORTGAGE+"BR", R.string.arm_br);		
	}

	public static Map<String, Integer> getTypeIdMap() {
		return type_id_map;
	}

	public static int getTypeInteger(String type_name) {
		if (type_name != FIXED_RATE_VARIABLE_PRINCIPAL 
				&& type_name != FIXED_RATE_FIXED_PRINCIPAL
				&& type_name != ADJUSTABLE_RATE_MORTGAGE) {
			return 0;
		}
		return type_id_map.get(type_name);
	}
	public static Integer getTypeInfo(String type_name) {
		return type_info.get(type_name);
	}

	public static String getTypeNameByIndex(int index) {
		if (index < 0 || index >= mortgage_type_items.length) {
			throw new IndexOutOfBoundsException("No such mortgage type");
		}
		return mortgage_type_items[index];
	}
}