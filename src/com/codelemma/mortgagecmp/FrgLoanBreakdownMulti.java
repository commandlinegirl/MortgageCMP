package com.codelemma.mortgagecmp;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.HistogramVisitor;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.google.analytics.tracking.android.EasyTracker;

public class FrgLoanBreakdownMulti extends SherlockFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgChartThree.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_loan_breakdown_multi, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState); 
    	NumberFormatter formatter = new NumberFormatter();

    	if (MortgageCMP.getInstance().getAccount() != null) {
            HistogramVisitor histogramVisitor = new HistogramMultiMaker(getSherlockActivity());
            MortgageCMP.getInstance().getAccount().histogramLoanBreakdownMulti(histogramVisitor);
    	}
    	
    	//loanbreakdown_multi_table
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 8, 0, 8); // left, top, right, bottom
		lp.weight = 4;
		
		LinearLayout loanbreakdown_multi_table = (LinearLayout) getSherlockActivity().findViewById(R.id.loanbreakdown_multi_table); 
		LinearLayout loanbreakdown_total_multi_table = (LinearLayout) getSherlockActivity().findViewById(R.id.loanbreakdown_total_multi_table); 

		// thin gray line
		@SuppressWarnings("deprecation")
		LinearLayout.LayoutParams lpline = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 1);
		lpline.setMargins(0, Utils.px(getActivity(), 2), 0, Utils.px(getActivity(), 2)); // left, top, right, bottom
			
    	ArrayList<Mortgage> mortgages = MortgageCMP.getInstance().getAccount().getMortgagesToCompare();
		int size = mortgages.size();

		Typeface tf = Typeface.DEFAULT;

    	for (Mortgage mortgage : mortgages) {
    		LinearLayout ll = new LinearLayout(getSherlockActivity());
    		//ll.setOrientation(orientation);
    		addTextViewToLayout(ll, mortgage.getName(), lp, tf);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getLoanAmount()), lp, tf);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getTotalInterestPaid()), lp, tf);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getTotalTaxInsurancePMIClosingFees()), lp, tf);
    		LinearLayout lline = new LinearLayout(getSherlockActivity());		
    		lline.setBackgroundColor(Color.parseColor("#FFCCCCCC"));
    		lline.setLayoutParams(lpline);	    		
    		loanbreakdown_multi_table.addView(lline);
    		loanbreakdown_multi_table.addView(ll);

    		ll = new LinearLayout(getSherlockActivity());
    		lp.gravity = Gravity.CENTER; 
    		addTextViewToLayout(ll, mortgage.getName(), lp, Gravity.CENTER, 0, 0, 16, 0, tf);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getTotalPayment()), lp, Gravity.CENTER, 16, 0, 0, 0, tf);
    		lline = new LinearLayout(getSherlockActivity());		
    		lline.setBackgroundColor(Color.parseColor("#FFCCCCCC"));
    		lline.setLayoutParams(lpline);	    		
    		loanbreakdown_total_multi_table.addView(lline);    		
    		loanbreakdown_total_multi_table.addView(ll);
    	}

    	if (size == 2) {
    		tf = Typeface.DEFAULT_BOLD;

    		LinearLayout ll = new LinearLayout(getSherlockActivity());
    		addTextViewToLayout(ll, "Difference", lp, tf);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgages.get(0).getLoanAmount().subtract(mortgages.get(1).getLoanAmount())), lp, tf);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgages.get(0).getTotalInterestPaid().subtract(mortgages.get(1).getTotalInterestPaid())), lp, tf);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgages.get(0).getTotalTaxInsurancePMIClosingFees().subtract(mortgages.get(1).getTotalTaxInsurancePMIClosingFees())), lp, tf);
    		LinearLayout lline = new LinearLayout(getSherlockActivity());		
    		lline.setBackgroundColor(Color.parseColor("#FFCCCCCC"));
    		lline.setLayoutParams(lpline);	    		
    		loanbreakdown_multi_table.addView(lline);    		
    		loanbreakdown_multi_table.addView(ll);

    		lline = new LinearLayout(getSherlockActivity());
    		lline.setBackgroundColor(Color.parseColor("#FFCCCCCC"));
    		lline.setLayoutParams(lpline);
    		loanbreakdown_total_multi_table.addView(lline);

    		LinearLayout ll2 = new LinearLayout(getSherlockActivity());
    		addTextViewToLayout(ll2, "Difference", lp, Gravity.CENTER, 0, 0, 16, 0, tf);
    		addTextViewToLayout(ll2, formatter.formatNumber(mortgages.get(0).getTotalPayment().subtract(mortgages.get(1).getTotalPayment())), 
    				lp, Gravity.CENTER, 16, 0, 0, 0, tf);
    		loanbreakdown_total_multi_table.addView(ll2);
    	}

    	if (MortgageCMP.getInstance().getAccount().getMortgagesToCompare().size() == 0) {
    		ScrollView ll = (ScrollView) getActivity().findViewById(R.id.frg_loan_breakdown_multi);
    		ll.removeAllViews();
    	}
    }

    private void addTextViewToLayout(LinearLayout layout,
    		String value,
    		LinearLayout.LayoutParams params,
    		Typeface tf) {
		TextView tv = new TextView(getSherlockActivity());
		tv.setLayoutParams(params);
    	tv.setText(value);
    	tv.setTypeface(tf);
		layout.addView(tv);
    }

    private void addTextViewToLayout(LinearLayout layout,
    		String value,
    		LinearLayout.LayoutParams params,
    		int gravity,
    		int padding_left,
    		int padding_top,
    		int padding_right,
    		int padding_bottom,
    		Typeface tf) {
		TextView tv = new TextView(getSherlockActivity());
		tv.setLayoutParams(params);
    	tv.setText(value);
    	tv.setGravity(gravity);
    	tv.setTypeface(tf);    	
    	tv.setPadding(padding_left, padding_top, padding_right, padding_bottom);
		layout.addView(tv);
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