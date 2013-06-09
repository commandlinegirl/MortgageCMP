package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_amount);
	    	tv.setText(formatter.formatNumber(mortg.getInitAmount()));
	    	
	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_total_payment);
	    	tv.setText(formatter.formatNumber(mortg.getTotalPayment()));

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_total_interests);
	    	tv.setText(formatter.formatNumber(mortg.getTotalInterestPaid()));

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_total_insurance);
	    	tv.setText(formatter.formatNumber(mortg.getTotalInsurance()));

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_total_pmi);
	    	tv.setText(formatter.formatNumber(mortg.getTotalPMI()));
	    	
	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_total_property_tax);
	    	tv.setText(formatter.formatNumber(mortg.getTotalPropertyTax()));
	    	
	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_payment);
	    	tv.setText(formatter.formatNumber(mortg.getBaseMonthlyPayment()));

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_insurance);
	    	tv.setText(formatter.formatNumber(mortg.getPropertyInsuranceAmount()));

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_pmi);
	    	tv.setText(formatter.formatNumber(mortg.getPMIAmount()));

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_tax);
	    	tv.setText(formatter.formatNumber(mortg.getPropertyTaxAmount()));
		}
    }
}
