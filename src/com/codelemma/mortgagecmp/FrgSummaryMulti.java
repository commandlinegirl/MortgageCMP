package com.codelemma.mortgagecmp;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.Mortgage;

public class FrgSummaryMulti extends SherlockFragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgSummaryMulti.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_summary_multi, container, false);
    }

    private void addTextViewToLayout(LinearLayout layout,
    		String value,
    		LinearLayout.LayoutParams params) {
		TextView tv = new TextView(getSherlockActivity());
		tv.setLayoutParams(params);
    	tv.setText(value);
		layout.addView(tv);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	Log.d("FrgSummaryMulti.onActivityCreated()", "called");
    	MortgageCMP appState = MortgageCMP.getInstance(); 
    	NumberFormatter formatter = new NumberFormatter();
    	
    	ArrayList<Mortgage> mortgages = appState.getAccount().getMortgagesToCompare();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 0, 5, 0); // left, top, right, bottom
		lp.weight = 4;
		
		int size = mortgages.size();
		LinearLayout summary_multi_table_loan_amounts = (LinearLayout) getSherlockActivity().findViewById(R.id.summary_multi_table_loan_amounts); 
		LinearLayout summary_multi_table_loan_type = (LinearLayout) getSherlockActivity().findViewById(R.id.summary_multi_table_loan_type); 
		LinearLayout summary_multi_table_loan_paymentsnum = (LinearLayout) getSherlockActivity().findViewById(R.id.summary_multi_table_loan_paymentsnum); 

    	for (Mortgage mortgage : mortgages) {
    		LinearLayout ll = new LinearLayout(getSherlockActivity());
    		
    		addTextViewToLayout(ll, mortgage.getName(), lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getPurchasePrice()), lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getDownpayment()), lp);    		
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getLoanAmount()), lp);
    		summary_multi_table_loan_amounts.addView(ll);

    		ll = new LinearLayout(getSherlockActivity());
    		addTextViewToLayout(ll, mortgage.getName(), lp);
    		addTextViewToLayout(ll, "Fixed rate fixed principal", lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getInterestRate()), lp);    		
    		summary_multi_table_loan_type.addView(ll);

    		ll = new LinearLayout(getSherlockActivity());
    		addTextViewToLayout(ll, mortgage.getName(), lp);
    		addTextViewToLayout(ll, String.valueOf(mortgage.getNumberOfPayments()), lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getDownpayment()), lp);    		
    		summary_multi_table_loan_paymentsnum.addView(ll);

    	}
		
    	if (appState.getAccount().getMortgagesToCompare().size() == 0) {
    		ScrollView ll = (ScrollView) getActivity().findViewById(R.id.frg_summary_multi);
    		ll.removeAllViews();
    	}
    }
}
