package com.codelemma.mortgagecmp;

import java.util.ArrayList;

import android.graphics.Color;
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
import com.codelemma.mortgagecmp.accounting.Account;
import com.codelemma.mortgagecmp.accounting.HistogramVisitor;
import com.codelemma.mortgagecmp.accounting.Mortgage;

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
    	MortgageCMP appState = MortgageCMP.getInstance();
        Account account = appState.getAccount();	
    	NumberFormatter formatter = new NumberFormatter();

    	if (account != null) {

            HistogramVisitor histogramVisitor = new HistogramMultiMaker(getSherlockActivity());
            account.histogramLoanBreakdownMulti(histogramVisitor);
    	}
    	
    	//loanbreakdown_multi_table
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 8, 0, 8); // left, top, right, bottom
		lp.weight = 4;
		
		LinearLayout loanbreakdown_multi_table = (LinearLayout) getSherlockActivity().findViewById(R.id.loanbreakdown_multi_table); 
		LinearLayout loanbreakdown_total_multi_table = (LinearLayout) getSherlockActivity().findViewById(R.id.loanbreakdown_total_multi_table); 

		// thin gray line
		LinearLayout.LayoutParams lpline = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 1);
		lpline.setMargins(0, 5, 0, 5); // left, top, right, bottom
			
    	ArrayList<Mortgage> mortgages = appState.getAccount().getMortgagesToCompare();
		int size = mortgages.size();

    	for (Mortgage mortgage : mortgages) {
    		LinearLayout ll = new LinearLayout(getSherlockActivity());
    		//ll.setOrientation(orientation);
    		addTextViewToLayout(ll, mortgage.getName(), lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getLoanAmount()), lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getTotalInterestPaid()), lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getTotalTaxInsurancePMIClosingFees()), lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getTotalExtraPayment()), lp);
    		LinearLayout lline = new LinearLayout(getSherlockActivity());		
    		lline.setBackgroundColor(Color.parseColor("#FFCCCCCC"));
    		lline.setLayoutParams(lpline);	    		
    		loanbreakdown_multi_table.addView(lline);
    		loanbreakdown_multi_table.addView(ll);

    		ll = new LinearLayout(getSherlockActivity());
    		lp.gravity = Gravity.CENTER; 
    		addTextViewToLayout(ll, mortgage.getName(), lp, Gravity.CENTER, 0, 0, 16, 0);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgage.getTotalPayment()), lp, Gravity.CENTER, 16, 0, 0, 0);
    		lline = new LinearLayout(getSherlockActivity());		
    		lline.setBackgroundColor(Color.parseColor("#FFCCCCCC"));
    		lline.setLayoutParams(lpline);	    		
    		loanbreakdown_total_multi_table.addView(lline);    		
    		loanbreakdown_total_multi_table.addView(ll);
    	}

    	if (size == 2) {

    		LinearLayout ll = new LinearLayout(getSherlockActivity());
    		addTextViewToLayout(ll, "Difference", lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgages.get(0).getLoanAmount().subtract(mortgages.get(1).getLoanAmount())), lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgages.get(0).getTotalInterestPaid().subtract(mortgages.get(1).getTotalInterestPaid())), lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgages.get(0).getTotalTaxInsurancePMIClosingFees().subtract(mortgages.get(1).getTotalTaxInsurancePMIClosingFees())), lp);
    		addTextViewToLayout(ll, formatter.formatNumber(mortgages.get(0).getTotalExtraPayment().subtract(mortgages.get(1).getTotalExtraPayment())), lp);
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
    		addTextViewToLayout(ll2, "Difference", lp, Gravity.CENTER, 0, 0, 16, 0);
    		addTextViewToLayout(ll2, formatter.formatNumber(mortgages.get(0).getTotalPayment().subtract(mortgages.get(1).getTotalPayment())), lp, Gravity.CENTER, 16, 0, 0, 0);
    		loanbreakdown_total_multi_table.addView(ll2);
    	}
    	
    	if (account.getMortgagesToCompare().size() == 0) {
    		ScrollView ll = (ScrollView) getActivity().findViewById(R.id.frg_loan_breakdown_multi);
    		ll.removeAllViews();
    	}
    }

    private void addTextViewToLayout(LinearLayout layout,
    		String value,
    		LinearLayout.LayoutParams params) {
		TextView tv = new TextView(getSherlockActivity());
		tv.setLayoutParams(params);
		tv.setTextSize(11);
    	tv.setText(value);
		layout.addView(tv);
    }

    private void addTextViewToLayout(LinearLayout layout,
    		String value,
    		LinearLayout.LayoutParams params,
    		int gravity,
    		int padding_left,
    		int padding_top,
    		int padding_right,
    		int padding_bottom) {
		TextView tv = new TextView(getSherlockActivity());
		tv.setLayoutParams(params);
		tv.setTextSize(11);
    	tv.setText(value);
    	tv.setGravity(gravity);
    	tv.setPadding(padding_left, padding_top, padding_right, padding_bottom);
		layout.addView(tv);
    }
}