package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.Mortgage;

public class FrgSummaryOne extends SherlockFragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgSummaryOne.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_summary_one, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	MortgageCMP appState = MortgageCMP.getInstance(); 	    	    	   
    	Mortgage mortg = appState.getCurrentMortgage();
    	NumberFormatter formatter = new NumberFormatter();
    	
		if (mortg != null) {
			TextView tv = (TextView) getActivity().findViewById(R.id.s_mortgage_name);
	    	tv.setText(mortg.getName());

			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_purchase_price);
	    	tv.setText(formatter.formatNumber(mortg.getPurchasePrice()));

			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_downpayment);
	    	tv.setText(formatter.formatNumber(mortg.getDownpayment()));

			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_amount);
	    	tv.setText(formatter.formatNumber(mortg.getLoanAmount()));

			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_term);
 	    	tv.setText(String.valueOf(mortg.getNumberOfPayments()));

			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_interest_rate);
	    	tv.setText(formatter.formatNumber(mortg.getInterestRate())+"%");

	    	String[] dates = mortg.getHistory().getDates(
	    			appState.getSimulationStartYear(), 
	    			appState.getSimulationStartMonth()); 
	    	
			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_last_payment_date);
			if (mortg.getNumberOfPayments() > 0) {
				tv.setText(dates[mortg.getNumberOfPayments()-1]);
			} else {
				tv.setText("-");
			}
		} else {
	    	ScrollView ll = (ScrollView) getActivity().findViewById(R.id.frg_summary_one);
	    	ll.removeAllViews();
		}
    }
}