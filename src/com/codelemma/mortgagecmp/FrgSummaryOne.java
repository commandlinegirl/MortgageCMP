package com.codelemma.mortgagecmp;

import java.math.BigDecimal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.MortgageNameConstants;

public class FrgSummaryOne extends SherlockFragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_summary_one, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	Mortgage mortg = MortgageCMP.getInstance().getAccount().getCurrentMortgage();
    	NumberFormatter formatter = new NumberFormatter();

		if (mortg != null) {
			TextView tv = (TextView) getActivity().findViewById(R.id.s_mortgage_name);
	    	tv.setText(mortg.getName());

			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_type);
			int type_r_id = MortgageNameConstants.getTypeInfo(mortg.getType());
    		if (getResources().getString(type_r_id) != null) {
    		    tv.setText(type_r_id);
    		} else {
    			tv.setText("-");
    		}

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
	    			MortgageCMP.getInstance().getSimulationStartYear(), 
	    			MortgageCMP.getInstance().getSimulationStartMonth()); 
	    	
			tv = (TextView) getActivity().findViewById(R.id.s_mortgage_last_payment_date);
			if (mortg.getNumberOfPayments() > 0) {
				tv.setText(dates[mortg.getNumberOfPayments()-1]);
			} else {
				tv.setText("-");
			}
			
			if (mortg.getExtraPayment().compareTo(BigDecimal.ZERO) > 0) {
				tv = (TextView) getActivity().findViewById(R.id.s_mortgage_total_extra_payment);
				tv.setText(formatter.formatNumber(mortg.getTotalExtraPayment()));
				tv = (TextView) getActivity().findViewById(R.id.s_mortgage_term_reduction);
		    	tv.setText(String.valueOf(mortg.getTotalTermMonths() - mortg.getNumberOfPayments())+" months");
			} else {
                LinearLayout d = (LinearLayout) getActivity().findViewById(R.id.s_mortgage_term_reduction_l);
                d.setVisibility(View.GONE);
                d = (LinearLayout) getActivity().findViewById(R.id.s_mortgage_total_extra_payment_l);
                d.setVisibility(View.GONE);
			}

		} else {
	    	ScrollView ll = (ScrollView) getActivity().findViewById(R.id.frg_summary_one);
	    	ll.removeAllViews();
		}
    }
}