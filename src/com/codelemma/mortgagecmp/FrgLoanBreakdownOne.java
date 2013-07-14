package com.codelemma.mortgagecmp;

import java.math.BigDecimal;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.PieChartVisitor;

public class FrgLoanBreakdownOne extends SherlockFragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_loan_breakdown_one, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState); 
    	Mortgage mortgage = MortgageCMP.getInstance().getAccount().getCurrentMortgage();  
    	
    	if (mortgage != null) {

        	NumberFormatter nf = new NumberFormatter();
            
            PieChartVisitor piechartVisitor = new PieChartMaker(getSherlockActivity());
            mortgage.makePieChart(piechartVisitor);
                        
            TextView tv = (TextView) getActivity().findViewById(R.id.principal_breakdown_amount);
            tv.setText(nf.formatNumber(mortgage.getLoanAmount()));

            tv = (TextView) getActivity().findViewById(R.id.principal_breakdown_percentage);
            tv.setText(nf.formatNumber(mortgage.getPrincipalFraction())+"%");
            tv.setGravity(Gravity.RIGHT);
            
            tv = (TextView) getActivity().findViewById(R.id.interest_breakdown_amount);
            tv.setText(nf.formatNumber(mortgage.getTotalInterestPaid()));

            tv = (TextView) getActivity().findViewById(R.id.interest_breakdown_percentage);
            tv.setText(nf.formatNumber(mortgage.getInterestFraction())+"%");
            tv.setGravity(Gravity.RIGHT);
            
            tv = (TextView) getActivity().findViewById(R.id.fees_breakdown_amount);
            tv.setText(nf.formatNumber(mortgage.getTotalTaxInsurancePMIClosingFees()));

            tv = (TextView) getActivity().findViewById(R.id.fees_breakdown_percentage);
            tv.setText(nf.formatNumber(mortgage.getTotalTaxInsurancePMIClosingFeesFraction())+"%");
            tv.setGravity(Gravity.RIGHT);

            if (mortgage.getTotalInsurance().compareTo(BigDecimal.ZERO) > 0) {
                tv = (TextView) getActivity().findViewById(R.id.fees_home_insurance_breakdown_amount);
                tv.setText(nf.formatNumber(mortgage.getTotalInsurance()));
            } else {
                LinearLayout a = (LinearLayout) getActivity().findViewById(R.id.fees_home_insurance_breakdown);
                a.setVisibility(View.GONE);
            }

            if (mortgage.getTotalPropertyTax().compareTo(BigDecimal.ZERO) > 0) {
            	tv = (TextView) getActivity().findViewById(R.id.fees_tax_breakdown_amount);
                tv.setText(nf.formatNumber(mortgage.getTotalPropertyTax()));
            } else {
                LinearLayout b = (LinearLayout) getActivity().findViewById(R.id.fees_tax_breakdown);
                b.setVisibility(View.GONE);
            }

            if (mortgage.getTotalPMI().compareTo(BigDecimal.ZERO) > 0) {
                tv = (TextView) getActivity().findViewById(R.id.fees_pmi_breakdown_amount);
                tv.setText(nf.formatNumber(mortgage.getTotalPMI()));
            } else {
                LinearLayout c = (LinearLayout) getActivity().findViewById(R.id.fees_pmi_breakdown);
                c.setVisibility(View.GONE);
            }
            
            if (mortgage.getClosingFees().compareTo(BigDecimal.ZERO) > 0) {
                tv = (TextView) getActivity().findViewById(R.id.fees_closing_fees_breakdown_amount);
                tv.setText(nf.formatNumber(mortgage.getClosingFees()));
            } else {
                LinearLayout d = (LinearLayout) getActivity().findViewById(R.id.fees_closing_fees_breakdown);
                d.setVisibility(View.GONE);
            }
 
            tv = (TextView) getActivity().findViewById(R.id.total_breakdown_amount);
            tv.setText(nf.formatNumber(mortgage.getTotalPayment()));

		} else {
			LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.frg_loanbreakdown_one);
	    	ll.removeAllViews();
		}
    }
}