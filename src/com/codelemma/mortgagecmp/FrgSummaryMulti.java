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

    private void addTextViewToLayout(int view_id,
    		String value,
    		LinearLayout.LayoutParams params,
    		int color) {
		LinearLayout layout = (LinearLayout) getSherlockActivity().findViewById(view_id);    		
		TextView tv = new TextView(getSherlockActivity());
		tv.setLayoutParams(params);
		tv.setTextSize(11);
    	tv.setText(value);
    	tv.setTextColor(color);
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
		BigDecimal[] diff = new BigDecimal[6];
		int[] colors = new int[6];
		
		int color = Color.BLACK;
		
		int iteration = 0;
    	for (Mortgage mortgage : mortgages) {
    		iteration++;
    		
    		addTextViewToLayout(R.id.smulti_mortgage_name, mortgage.getName(), lp, color);
    		addTextViewToLayout(R.id.smulti_mortgage_loan_amount, formatter.formatNumber(mortgage.getLoanAmount()), lp, color);
    		addTextViewToLayout(R.id.smulti_mortgage_term, String.valueOf(mortgage.getTermMonths()), lp, color);
    		addTextViewToLayout(R.id.smulti_mortgage_interest, formatter.formatNumber(mortgage.getInterestRate()), lp, color);
    		addTextViewToLayout(R.id.smulti_mortgage_downpayment, formatter.formatNumber(mortgage.getDownpayment()), lp, color);
    		addTextViewToLayout(R.id.smulti_mortgage_total_payment, formatter.formatNumber(mortgage.getTotalPayment()), lp, color);
    		addTextViewToLayout(R.id.smulti_mortgage_total_interests, formatter.formatNumber(mortgage.getTotalInterestPaid()), lp, color);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_payment, formatter.formatNumber(mortgage.getBaseMonthlyPayment()), lp, color);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_insurance, formatter.formatNumber(mortgage.getPropertyInsuranceAmount()), lp, color);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_pmi, formatter.formatNumber(mortgage.getPMIAmount()), lp, color);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_tax, formatter.formatNumber(mortgage.getPropertyTaxAmount()), lp, color);

    		if (size == 2) {
    			if (iteration == 1) {
    			    diff[0] = mortgage.getTotalPayment();
    			    diff[1] = mortgage.getTotalInterestPaid();
    			    diff[2] = mortgage.getBaseMonthlyPayment();
    			    diff[3] = mortgage.getPropertyInsuranceAmount();
    			    diff[4] = mortgage.getPMIAmount();
    			    diff[5] = mortgage.getPropertyTaxAmount();
    			} else if  (iteration == 2) {
    			    diff[0] = diff[0].subtract(mortgage.getTotalPayment());
    			    diff[1] = diff[1].subtract(mortgage.getTotalInterestPaid());
    			    diff[2] = diff[2].subtract(mortgage.getBaseMonthlyPayment());
    			    diff[3] = diff[3].subtract(mortgage.getPropertyInsuranceAmount());
    			    diff[4] = diff[4].subtract(mortgage.getPMIAmount());
    			    diff[5] = diff[5].subtract(mortgage.getPropertyTaxAmount());
    			}
    		}
    	}
    	
		// if there are two mortgages to compare, add a column with their differences in values
		if (size == 2) {
			
    		for (int i = 0; i < diff.length; i++) {
        		if (diff[i].compareTo(BigDecimal.ZERO) == 1) {
        			colors[i] = Color.GREEN;
        		} else if (diff[i].compareTo(BigDecimal.ZERO) == -1) {
        			colors[i] = Color.RED;    			
        		} else {
        			colors[i] = Color.BLACK;
        		}
        	}

			LinearLayout.LayoutParams divider = new LinearLayout.LayoutParams(1, 
					LinearLayout.LayoutParams.WRAP_CONTENT);
			divider.setMargins(1, 0, 1, 0); // left, top, right, bottom
			divider.weight = 1;

			color = Color.parseColor("#FFCCCCCC");
			addTextViewToLayout(R.id.smulti_mortgage_name, "|", divider, color);
    		addTextViewToLayout(R.id.smulti_mortgage_total_payment, "|", divider, color);
    		addTextViewToLayout(R.id.smulti_mortgage_total_interests, "|", divider, color);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_payment, "|", divider, color);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_insurance, "|", divider, color);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_pmi, "|", divider, color);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_tax, "|", divider, color);

    		addTextViewToLayout(R.id.smulti_mortgage_name, "Difference", lp, Color.BLACK);
    		addTextViewToLayout(R.id.smulti_mortgage_total_payment, formatter.formatNumber(diff[0]), lp, colors[0]);
    		addTextViewToLayout(R.id.smulti_mortgage_total_interests, formatter.formatNumber(diff[1]), lp, colors[1]);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_payment, formatter.formatNumber(diff[2]), lp, colors[2]);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_insurance, formatter.formatNumber(diff[3]), lp, colors[3]);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_pmi, formatter.formatNumber(diff[4]), lp, colors[4]);
    		addTextViewToLayout(R.id.smulti_mortgage_monthly_tax, formatter.formatNumber(diff[5]), lp, colors[5]);
		}
    }
}
