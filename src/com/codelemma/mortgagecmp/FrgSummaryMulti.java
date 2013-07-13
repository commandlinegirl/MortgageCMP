package com.codelemma.mortgagecmp;

import java.util.ArrayList;

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
import com.google.analytics.tracking.android.EasyTracker;

public class FrgSummaryMulti extends SherlockFragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_summary_multi, container, false);
    }

    private void addTextViewToLayout(int view_id,
    		String value,
    		LinearLayout.LayoutParams params) {
		LinearLayout layout = (LinearLayout) getSherlockActivity().findViewById(view_id);    		
		TextView tv = new TextView(getSherlockActivity());
		tv.setLayoutParams(params);
    	tv.setText(value);
		layout.addView(tv);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	MortgageCMP appState = MortgageCMP.getInstance(); 
    	NumberFormatter formatter = new NumberFormatter();

    	ArrayList<Mortgage> mortgages = appState.getAccount().getMortgagesToCompare();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.weight = 3;

    	for (Mortgage mortgage : mortgages) {
    		addTextViewToLayout(R.id.smulti_mortgage_name, mortgage.getName(), lp);
    		Integer type_r_id = MortgageNameConstants.getTypeInfo(mortgage.getType()+"BR");
    		if (getResources().getString(type_r_id) != null) {
    		    addTextViewToLayout(R.id.s_multi_mortgage_type, getResources().getString(type_r_id), lp);
    		} else {
    			addTextViewToLayout(R.id.s_multi_mortgage_type, "-", lp);
    		}
    		addTextViewToLayout(R.id.smulti_mortgage_loan_amount, formatter.formatNumber(mortgage.getLoanAmount()), lp);
    		addTextViewToLayout(R.id.s_multi_mortgage_term, String.valueOf(mortgage.getNumberOfPayments()), lp);
    		addTextViewToLayout(R.id.smulti_mortgage_interest, formatter.formatNumber(mortgage.getInterestRate()), lp);
    		
	    	String[] dates = mortgage.getHistory().getDates(
	    			MortgageCMP.getInstance().getSimulationStartYear(), 
	    			MortgageCMP.getInstance().getSimulationStartMonth());
	    	
			if (mortgage.getNumberOfPayments() > 0) {
				addTextViewToLayout(R.id.s_multi_mortgage_last_payment_date, dates[mortgage.getNumberOfPayments()-1], lp);
			} else {
				addTextViewToLayout(R.id.s_multi_mortgage_last_payment_date, "-", lp);
			}
    		
    		addTextViewToLayout(R.id.s_multi_mortgage_extra_payments, formatter.formatNumber(mortgage.getTotalExtraPayment()), lp);
    		addTextViewToLayout(R.id.s_mortgage_term_reduction, String.valueOf(mortgage.getTotalTermMonths() - mortgage.getNumberOfPayments())+" months", lp);    		
    	}

    	if (appState.getAccount().getMortgagesToCompare().size() == 0) {
    		ScrollView ll = (ScrollView) getActivity().findViewById(R.id.frg_summary_multi);
    		ll.removeAllViews();
    	}
    }
    
    @Override
    public void onStart() {
      super.onStart();
      EasyTracker.getInstance().activityStart(getActivity());
    }

    @Override
    public void onStop() {
      super.onStop();
      EasyTracker.getInstance().activityStop(getActivity());
    }
}