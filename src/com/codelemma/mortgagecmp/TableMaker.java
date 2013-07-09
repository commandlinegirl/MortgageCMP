package com.codelemma.mortgagecmp;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.util.SparseArray;
import android.view.Gravity;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.mortgagecmp.accounting.HistoryMortgage;
import com.codelemma.mortgagecmp.accounting.TableVisitor;


public class TableMaker implements TableVisitor {

	private SherlockFragmentActivity frgActivity;
	private NumberFormatter formatter;
	private String[] dates;
	
	public TableMaker(SherlockFragmentActivity sherlockFragmentActivity, String[] dates) {
		frgActivity = sherlockFragmentActivity;
		formatter = new NumberFormatter();
		this.dates = dates;
	}
		
	@Override
	public void makeTableMortgage(HistoryMortgage historyMortgage) {
		// TODO Auto-generated method stub
    	// header
		LinearLayout header = (LinearLayout) frgActivity.findViewById(R.id.header);
		header.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);                  
        params.weight = 1;
		TextView tv;
		
		tv = new TextView(frgActivity);
		tv.setText("Date");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv);

		tv = new TextView(frgActivity);
		tv.setText("Principal");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
				
		tv = new TextView(frgActivity);
		tv.setText("Interest");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv);
		
	    tv = new TextView(frgActivity);
	    tv.setText("Tax, PMI,\ninsurance");
	    tv.setTextSize(11);
	    tv.setLayoutParams(params);
	    tv.setGravity(Gravity.CENTER);
	    header.addView(tv);
		
		tv = new TextView(frgActivity);
		tv.setText("Total\npayment");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
		
		tv = new TextView(frgActivity);
		tv.setText("Outstanding \n amount");
		tv.setTextSize(11);
		tv.setLayoutParams(params);
		tv.setGravity(Gravity.CENTER);
		header.addView(tv); 
	

		
    	ExpandableListView expandedList = (ExpandableListView) frgActivity.findViewById(R.id.exp_list);
    	ArrayList<TableGroupMortgage> ExpListItems = setGroupsMortgage(historyMortgage);
    	TableAdapterMortgage ExpAdapter = new TableAdapterMortgage(frgActivity, ExpListItems);
        expandedList.setAdapter(ExpAdapter);
	}
	
    public ArrayList<TableGroupMortgage> setGroupsMortgage(HistoryMortgage historyMortgage) {
    	ArrayList<TableGroupMortgage> list = new ArrayList<TableGroupMortgage>();
    	ArrayList<TableChildMortgage> list2 = new ArrayList<TableChildMortgage>();

    	BigDecimal[] interestsPaid = historyMortgage.getInterestsPaidHistory();
    	BigDecimal[] monthlyTotal = historyMortgage.getMonthlyPaymentHistory();
    	BigDecimal[] principalPaid = historyMortgage.getPrincipalPaidHistory();
    	BigDecimal[] additionalCost = historyMortgage.getAdditionalCostHistory();
    	BigDecimal[] remainingAmount = historyMortgage.getRemainingAmountHistory();
    	
    	SparseArray<BigDecimal> monthlyPaymentYearly = historyMortgage.getMonthlyPaymentYearly();
    	SparseArray<BigDecimal> interestsPaidYearly = historyMortgage.getInterestsPaidYearly();
    	SparseArray<BigDecimal> principalPaidYearly = historyMortgage.getPrincipalPaidYearly();
    	SparseArray<BigDecimal> additionalCostYearly = historyMortgage.getAdditionalCostYearly();
    	SparseArray<BigDecimal> remainingAmountAtYearEnd = historyMortgage.getRemainingAmountAtYearEnd();
    	    	    	    	
    	int datesLen = dates.length;
    	String prevYear = dates[0].substring(4);
    	String currYear;
    	int i = 0;
    	

    	TableGroupMortgage gru1 = new TableGroupMortgage();
    	gru1.setDate(prevYear);
		gru1.setInterestsPaid(formatter.formatNumber(interestsPaidYearly.get(Integer.parseInt(prevYear)))); 
		gru1.setMonthlyPayment(formatter.formatNumber(monthlyPaymentYearly.get(Integer.parseInt(prevYear)))); 
		gru1.setPrincipalPaid(formatter.formatNumber(principalPaidYearly.get(Integer.parseInt(prevYear)))); 
		gru1.setAdditionalCost(formatter.formatNumber(additionalCostYearly.get(Integer.parseInt(prevYear))));
		gru1.setRemainingAmount(formatter.formatNumber(remainingAmountAtYearEnd.get(Integer.parseInt(prevYear)))); 


    	while(i < datesLen) {    		
    		currYear = dates[i].substring(4);
    		if (prevYear.equals(currYear)) {
    		    TableChildMortgage ch1_1 = new TableChildMortgage();
    		    
    		    ch1_1.setDate(dates[i]);
    		    ch1_1.setInterestsPaid(formatter.formatNumber(interestsPaid[i])); 
    		    ch1_1.setMonthlyPayment(formatter.formatNumber(monthlyTotal[i])); 
    		    ch1_1.setPrincipalPaid(formatter.formatNumber(principalPaid[i])); 
    		    ch1_1.setAdditionalCost(formatter.formatNumber(additionalCost[i]));
    		    ch1_1.setRemainingAmount(formatter.formatNumber(remainingAmount[i]));  
	            list2.add(ch1_1);	    			    		    		    
    		} else {
    			gru1.setItems(list2);
    			list.add(gru1);
    			list2 = new ArrayList<TableChildMortgage>();
    			gru1 = new TableGroupMortgage();
    			gru1.setDate(currYear);
    			gru1.setInterestsPaid(formatter.formatNumber(interestsPaidYearly.get(Integer.parseInt(currYear)))); 
    			gru1.setMonthlyPayment(formatter.formatNumber(monthlyPaymentYearly.get(Integer.parseInt(currYear)))); 
    			gru1.setPrincipalPaid(formatter.formatNumber(principalPaidYearly.get(Integer.parseInt(currYear)))); 
    			gru1.setAdditionalCost(formatter.formatNumber(additionalCostYearly.get(Integer.parseInt(currYear))));
    			gru1.setRemainingAmount(formatter.formatNumber(remainingAmountAtYearEnd.get(Integer.parseInt(currYear))));
    			TableChildMortgage ch1_1 = new TableChildMortgage();
    		    ch1_1.setDate(dates[i]);
    		    ch1_1.setInterestsPaid(formatter.formatNumber(interestsPaid[i])); 
    		    ch1_1.setMonthlyPayment(formatter.formatNumber(monthlyTotal[i])); 
    		    ch1_1.setPrincipalPaid(formatter.formatNumber(principalPaid[i])); 
    		    ch1_1.setAdditionalCost(formatter.formatNumber(additionalCost[i]));
    		    ch1_1.setRemainingAmount(formatter.formatNumber(remainingAmount[i]));  
	            list2.add(ch1_1);
    		}
    		i++;
    		prevYear = currYear;
    	}
    	gru1.setItems(list2);
		list.add(gru1);
		list2 = new ArrayList<TableChildMortgage>();
        return list;
    }
}
