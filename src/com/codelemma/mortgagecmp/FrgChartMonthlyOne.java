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
import com.codelemma.mortgagecmp.accounting.HistogramVisitor;
import com.codelemma.mortgagecmp.accounting.HistoryMortgage;
import com.codelemma.mortgagecmp.accounting.Mortgage;

public class FrgChartMonthlyOne extends SherlockFragment {
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgChartTwo.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_chart_monthly_one, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState); 
    	Mortgage mortgage = MortgageCMP.getInstance().getAccount().getCurrentMortgage();  
    	
        if (mortgage != null) {
        	HistoryMortgage historyMortgage = mortgage.getHistory();
        	String[] dates = historyMortgage.getDates(
        			MortgageCMP.getInstance().getSimulationStartYear(), 
        			MortgageCMP.getInstance().getSimulationStartMonth());
            
            HistogramVisitor histogramVisitor = new HistogramMaker(getSherlockActivity(), dates);
            mortgage.makeHistogram(histogramVisitor);
        	NumberFormatter formatter = new NumberFormatter();

        	TextView tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_payment);
        	tv.setText(formatter.formatNumber(mortgage.getMonthlyPaymentConstant()));

        	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_extra_payment);
        	if (mortgage.getMonthlyExtraPayment().compareTo(BigDecimal.ZERO) == 1) {
        	    tv.setText(formatter.formatNumber(mortgage.getMonthlyExtraPayment()));
        	} else {
        		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
        	}
        	      	
        	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_insurance);
        	if (mortgage.getMonthlyPropertyInsurance().compareTo(BigDecimal.ZERO) == 1) {
        	    tv.setText(formatter.formatNumber(mortgage.getMonthlyPropertyInsurance()));
        	} else {
        		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
        	}

        	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_tax);
        	if (mortgage.getMonthlyPropertyTax().compareTo(BigDecimal.ZERO) == 1) {
        	    tv.setText(formatter.formatNumber(mortgage.getMonthlyPropertyTax()));
        	} else {
        		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
        	}
        	
        	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_payment_total);
        	if (mortgage.getMonthlyExtraPayment()
        			.add(mortgage.getMonthlyPropertyInsurance())
        			.add(mortgage.getMonthlyPropertyTax())
        			.compareTo(BigDecimal.ZERO) > 0) {
        	    tv.setText(formatter.formatNumber(mortgage.getMonthlyTotalPaymentNonPMI()));
        	} else {
        		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
        	}

        	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_pmi);
        	TextView tv_pmi_title = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_pmi_title);        	
        	if (mortgage.getMonthlyPMI().compareTo(BigDecimal.ZERO) == 1) {
        	    tv.setText(formatter.formatNumber(mortgage.getMonthlyPMI()));
        		String format2 = getResources().getString(R.string.s_mortgage_monthly_pmi_title);
        		String value2 = String.format(format2, dates[mortgage.lastPmiPaymentMonth()]); 
        		tv_pmi_title.setText(value2);
            	tv = (TextView) getActivity().findViewById(R.id.s_mortgage_monthly_payment_total_title);
        		tv.setText(getResources().getString(R.string.s_mortgage_monthly_payment_total_wo_PMI_title));
        	} else {
        		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
        	}
		} else {
			LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.frg_chart_two);
	    	ll.removeAllViews();
		}
    }
}
