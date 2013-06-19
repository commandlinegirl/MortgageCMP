package com.codelemma.mortgagecmp;

import java.math.BigDecimal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
	    	
			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_term);
			if (tv == null) {
				Log.d("tv null", "a");
			} 
			Log.d("months", String.valueOf(mortg.getTotalTermMonths()));
 	    	tv.setText(String.valueOf(mortg.getTotalTermMonths()));

			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_interest_rate);
	    	tv.setText(formatter.formatNumber(mortg.getInterestRate())+"%");

			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_downpayment);
	    	tv.setText(formatter.formatNumber(mortg.getDownpayment()));

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_total_payment);
	    	tv.setText(formatter.formatNumber(mortg.getTotalPayment()));

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_total_interests);
	    	tv.setText(formatter.formatNumber(mortg.getTotalInterestPaid()));

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_total_insurance);
	    	if (mortg.getTotalInsurance().compareTo(BigDecimal.ZERO) == 1) {
	    	    tv.setText(formatter.formatNumber(mortg.getTotalInsurance()));
	    	} else {
	    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
	    	}

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_total_pmi);
	    	if (mortg.getTotalPMI().compareTo(BigDecimal.ZERO) == 1) {
	    	    tv.setText(formatter.formatNumber(mortg.getTotalPMI()));
	    	} else {
	    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
	    	}
	    	
	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_total_property_tax);
	    	if (mortg.getTotalPropertyTax().compareTo(BigDecimal.ZERO) == 1) {
	    	    tv.setText(formatter.formatNumber(mortg.getTotalPropertyTax()));
	    	} else {
	    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
	    	}
	    	
	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_payment);
	    	tv.setText(formatter.formatNumber(mortg.getBaseMonthlyPayment()));

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_insurance);
	    	if (mortg.getPropertyInsuranceAmount().compareTo(BigDecimal.ZERO) == 1) {
	    	    tv.setText(formatter.formatNumber(mortg.getPropertyInsuranceAmount()));
	    	} else {
	    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
	    	}

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_pmi);
	    	if (mortg.getPMIAmount().compareTo(BigDecimal.ZERO) == 1) {
	    	    tv.setText(formatter.formatNumber(mortg.getPMIAmount()));
	    	} else {
	    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
	    	}

	    	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_tax);
	    	if (mortg.getPropertyTaxAmount().compareTo(BigDecimal.ZERO) == 1) {
	    	    tv.setText(formatter.formatNumber(mortg.getPropertyTaxAmount()));
	    	} else {
	    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
	    	}
		}
    }
}
