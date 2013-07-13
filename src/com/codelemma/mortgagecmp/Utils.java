package com.codelemma.mortgagecmp;

import android.app.AlertDialog;
import android.content.Context;

public class Utils {
	
    private Utils() {}
       
	static public int px(Context context, float dips) {
    /* Convert from dp into int (px) */
	    float DP = context.getResources().getDisplayMetrics().density;
	    return Math.round(dips * DP);
	}
	
	static public int dip(Context context, int px) {
	    /* Convert from px into dip (density independent pixels) */
		    float DP = context.getResources().getDisplayMetrics().density;
		    return Math.round(px / DP);
		}

	static public int getIndex(int[] a, int x) {
		for (int i = 0; (i < a.length); i++) {
	        if (a[i] == x) {
	            return i;
	        }
	    }
		return -1;
	}
	
	static public boolean alertIfEmpty(Context context, String fieldData, String fieldName) {
	    if (fieldData.trim().length() == 0) {
	    	new AlertDialog.Builder(context).setTitle("Field empty")
	        	                             .setMessage("Please, fill in \""+ fieldName +"\".")
	        	                             .show();
	    	return true;
	    }
	    return false;
	}
	
    static public boolean alertIfNotInBounds(
            Context context,
            String fieldData,
            int min,
            int max,
            String fieldName) {
        double value = Double.parseDouble(fieldData);
    
        if (value < min) {
            showDialog(context, "Value too low", "Please, fill in "+ fieldName +" with value not lower than " +min+".");
            return true;
        } else if(value > max) {
            showDialog(context, "Value too high", "Please, fill in "+ fieldName +" with value not higher than " +max+".");
            return true;
        }   
        return false;
    }   

    static public boolean alertIfIntNotInBounds(
            Context context,
            String fieldData,
            int min,
            int max,
            String fieldName) {
        int value;
        try {
            value = Integer.parseInt(fieldData);
        } catch (NumberFormatException nfe) {
            showDialog(context, 
                    "Value incorrect", 
                    "Please, fill in "+ fieldName +" with a whole number."); //TODO: better mesg, please!
            return true;
        }   
    
        if (value < min) {
            showDialog(context, "Value too low", "Please, fill in "+ fieldName +" with value not lower than " +min+".");
            return true;
        } else if(value > max) {
            showDialog(context, "Value too high", "Please, fill in "+ fieldName +" with value not higher than " +max+".");
            return true;
        }   
        return false;
    }  
    
    static private void showDialog(Context context, String message_title, String message_text) {
        new AlertDialog.Builder(context).setTitle(message_title)
        .setMessage(message_text)
        .setNeutralButton("OK", null)
        .show();
    }   
}