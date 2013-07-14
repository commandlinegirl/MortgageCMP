package com.codelemma.mortgagecmp;

import java.math.BigDecimal;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.mortgagecmp.accounting.FixedRateFixedPrincipalMortgage;
import com.codelemma.mortgagecmp.accounting.FixedRateVariablePrincipalMortgage;
import com.codelemma.mortgagecmp.accounting.MonthlyPaymentListingVisitor;
import com.codelemma.mortgagecmp.accounting.Mortgage;

public class MonthlyPaymentListingMaker implements MonthlyPaymentListingVisitor {

	private SherlockFragmentActivity frgActivity;
	private NumberFormatter formatter;
	private String[] dates;
	
	public MonthlyPaymentListingMaker(SherlockFragmentActivity sherlockFragmentActivity, String[] dates) {
		frgActivity = sherlockFragmentActivity;
		formatter = new NumberFormatter();
		this.dates = dates;
	}

	@Override
	public void listMonthlyPayment(Mortgage mortgage) {}

	@Override
	public void listConstantPayment(
			FixedRateVariablePrincipalMortgage mortgage) {
    	TextView tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment_title);
    	tv.setText(R.string.s_mortgage_monthly_payment_title);
		
    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment);
    	tv.setText(formatter.formatNumber(mortgage.getMonthlyPaymentConstant().add(mortgage.getMonthlyExtraPayment())));

    	LinearLayout max_interest_l = (LinearLayout) frgActivity.findViewById(R.id.s_mortgage_monthly_interest_layout);
    	max_interest_l.setVisibility(View.GONE);
    	
    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_insurance);
    	if (mortgage.getMonthlyPropertyInsurance().compareTo(BigDecimal.ZERO) == 1) {
    	    tv.setText(formatter.formatNumber(mortgage.getMonthlyPropertyInsurance()));
    	} else {
    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
    	}

    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_tax);
    	if (mortgage.getMonthlyPropertyTax().compareTo(BigDecimal.ZERO) == 1) {
    	    tv.setText(formatter.formatNumber(mortgage.getMonthlyPropertyTax()));
    	} else {
    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
    	}
    	
    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment_max_total);
    	if (mortgage.getMonthlyPropertyInsurance()
    			.add(mortgage.getMonthlyPropertyTax())
    			.compareTo(BigDecimal.ZERO) > 0) {
    	    tv.setText(formatter.formatNumber(mortgage.getMonthlyTotalPaymentNonPMI()));
    	} else {
    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
        	LinearLayout hr = (LinearLayout) frgActivity.findViewById(R.id.chart_monthly_one_hr);
        	hr.setVisibility(View.GONE);
    	}

    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_pmi);
    	TextView tv_pmi_title = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_pmi_title);        	
    	if (mortgage.getMonthlyPMI().compareTo(BigDecimal.ZERO) == 1) {
    	    tv.setText(formatter.formatNumber(mortgage.getMonthlyPMI()));
    		String format2 = frgActivity.getResources().getString(R.string.s_mortgage_monthly_pmi_title);
    		String value2 = String.format(format2, dates[mortgage.lastPmiPaymentMonth()]); 
    		tv_pmi_title.setText(value2);
        	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment_total_title);
    		tv.setText(frgActivity.getResources().getString(R.string.s_mortgage_monthly_payment_total_wo_PMI_title));
    	} else {
    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
    	}
	}

	@Override
	public void listConstantPrincipal(FixedRateFixedPrincipalMortgage mortgage) {
    	TextView tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment_title);
    	tv.setText(R.string.principal);

    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment);
    	tv.setText(formatter.formatNumber(mortgage.getMonthlyPaymentConstant().add(mortgage.getMonthlyExtraPayment())));

    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_interest_min);
    	tv.setText(formatter.formatNumber(mortgage.getMinMonthlyInterest()));

    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_interest_max);
    	tv.setText(formatter.formatNumber(mortgage.getMaxMonthlyInterest()));

    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_insurance);
    	if (mortgage.getMonthlyPropertyInsurance().compareTo(BigDecimal.ZERO) == 1) {
    	    tv.setText(formatter.formatNumber(mortgage.getMonthlyPropertyInsurance()));
    	} else {
    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
    	}

    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_tax);
    	if (mortgage.getMonthlyPropertyTax().compareTo(BigDecimal.ZERO) == 1) {
    	    tv.setText(formatter.formatNumber(mortgage.getMonthlyPropertyTax()));
    	} else {
    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
    	}

		BigDecimal constant_payment_wo_interest = mortgage.getMonthlyPaymentConstant()
				.add(mortgage.getMonthlyExtraPayment())
				.add(mortgage.getMonthlyPropertyInsurance())
				.add(mortgage.getMonthlyPropertyTax());

    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment_min_total);
    	if (mortgage.getMonthlyPropertyInsurance()
    			.add(mortgage.getMonthlyPropertyTax())
    			.compareTo(BigDecimal.ZERO) > 0) {
        	tv.setText(formatter.formatNumber(mortgage.getMinMonthlyInterest()
					.add(constant_payment_wo_interest))); 
    	} else {
    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
        	LinearLayout hr = (LinearLayout) frgActivity.findViewById(R.id.chart_monthly_one_hr);
        	hr.setVisibility(View.GONE);
    	}

    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment_max_total);
    	if (mortgage.getMonthlyPropertyInsurance()
    			.add(mortgage.getMonthlyPropertyTax())
    			.compareTo(BigDecimal.ZERO) > 0) {
        	tv.setText(formatter.formatNumber(mortgage.getMaxMonthlyInterest()
					.add(constant_payment_wo_interest))); 
    	} else {
    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
        	LinearLayout hr = (LinearLayout) frgActivity.findViewById(R.id.chart_monthly_one_hr);
        	hr.setVisibility(View.GONE);
    	}

    	
    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment_total_title);
    	tv.setText(R.string.s_mortgage_monthly_payment_total_min_max_title);


    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_pmi);
    	TextView tv_pmi_title = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_pmi_title);        	
    	if (mortgage.getMonthlyPMI().compareTo(BigDecimal.ZERO) == 1) {
    	    tv.setText(formatter.formatNumber(mortgage.getMonthlyPMI()));
    		String format3 = frgActivity.getResources().getString(R.string.s_mortgage_monthly_pmi_title);
    		String value3 = String.format(format3, dates[mortgage.lastPmiPaymentMonth()]); 
    		tv_pmi_title.setText(value3);
        	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment_total_title);
    		tv.setText(frgActivity.getResources().getString(R.string.s_mortgage_monthly_payment_total_wo_PMI_title));
    	} else {
    		((LinearLayout) tv.getParent()).setVisibility(View.GONE);
    	}
	}
}