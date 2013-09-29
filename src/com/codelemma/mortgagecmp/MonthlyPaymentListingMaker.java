package com.codelemma.mortgagecmp;

import java.math.BigDecimal;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.mortgagecmp.accounting.AdjustableRateMortgage;
import com.codelemma.mortgagecmp.accounting.FixedRateFixedPrincipalMortgage;
import com.codelemma.mortgagecmp.accounting.FixedRateVariablePrincipalMortgage;
import com.codelemma.mortgagecmp.accounting.MonthlyPaymentListingVisitor;

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
	public void listMonthlyPaymentBreakdown(
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
	public void listMonthlyPaymentBreakdown(FixedRateFixedPrincipalMortgage mortgage) {
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
        	tv.setText(formatter.formatNumber(mortgage.getMinMonthlyInterest()
					.add(constant_payment_wo_interest))); 

    	tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment_max_total);
        	tv.setText(formatter.formatNumber(mortgage.getMaxMonthlyInterest()
					.add(constant_payment_wo_interest))); 

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
	
	@Override
	public void listMonthlyPaymentBreakdown(AdjustableRateMortgage mortgage) {
    	TextView tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment_title);
    	tv.setText(R.string.s_mortgage_monthly_payment_title);
		
    	//tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_monthly_payment);
    	//tv.setText(formatter.formatNumber(mortgage.getMonthlyPaymentConstant().add(mortgage.getMonthlyExtraPayment())));

    	LinearLayout principal_interest_l = (LinearLayout) frgActivity.findViewById(R.id.principal_interest);

    	LinearLayout principal_interest_inner0 = new LinearLayout(frgActivity);
    	principal_interest_inner0.setPadding(16, 0, 16, 0);
    	principal_interest_inner0.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
		
    	TextView principal_interest_tv0a = new TextView(frgActivity);
		params.weight = 0.2f;
    	principal_interest_tv0a.setLayoutParams(params);
		principal_interest_tv0a.setText("- until 1st adjustment");

    	TextView principal_interest_tv0b = new TextView(frgActivity);
    	params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
    	params.weight = 0.3f;
    	principal_interest_tv0b.setLayoutParams(params);
    	principal_interest_tv0b.setGravity(Gravity.RIGHT);
    	principal_interest_tv0b.setText(dates[Math.max(mortgage.getTotalTermMonths()-1, Math.max(mortgage.getAdjustmentPeriod()-1, 0))]);

    	TextView principal_interest_tv0c = new TextView(frgActivity);
    	params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
    	params.weight = 0.3f;
    	principal_interest_tv0c.setLayoutParams(params);
    	principal_interest_tv0c.setGravity(Gravity.RIGHT);
    	principal_interest_tv0c.setText(formatter.formatNumber(mortgage.getPrincipalInterestExtraBeforeAdjustment()));
    	
    	principal_interest_inner0.addView(principal_interest_tv0a);
    	principal_interest_inner0.addView(principal_interest_tv0b);  	
    	principal_interest_inner0.addView(principal_interest_tv0c);
    	
    	LinearLayout principal_interest_inner1 = new LinearLayout(frgActivity);
    	principal_interest_inner1.setPadding(16, 0, 16, 0);
    	principal_interest_inner1.setOrientation(LinearLayout.HORIZONTAL);

    	TextView principal_interest_tv1a = new TextView(frgActivity);
    	params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
    	params.weight = 0.2f;
    	principal_interest_tv1a.setLayoutParams(params);
		principal_interest_tv1a.setText("- max");

    	TextView principal_interest_tv1b = new TextView(frgActivity);
    	params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
    	params.weight = 0.3f;
    	principal_interest_tv1b.setLayoutParams(params);
    	principal_interest_tv1b.setGravity(Gravity.RIGHT);
    	principal_interest_tv1b.setText(dates[mortgage.getMonthWithMaxPrincipalInterestPayment()]);

    	TextView principal_interest_tv1c = new TextView(frgActivity);
    	params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
    	params.weight = 0.3f;
    	principal_interest_tv1c.setLayoutParams(params);
    	principal_interest_tv1c.setGravity(Gravity.RIGHT);
    	principal_interest_tv1c.setText(formatter.formatNumber(mortgage.getMaxPrincipalInterestPayment()));
    	
    	principal_interest_inner1.addView(principal_interest_tv1a);
    	principal_interest_inner1.addView(principal_interest_tv1b);
    	principal_interest_inner1.addView(principal_interest_tv1c);    	
    	
    	
    	LinearLayout principal_interest_inner2 = new LinearLayout(frgActivity);
    	principal_interest_inner2.setOrientation(LinearLayout.HORIZONTAL);
    	principal_interest_inner2.setPadding(16, 0, 16, 6);

    	TextView principal_interest_tv2a = new TextView(frgActivity);
    	params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
    	params.weight = 0.2f;
    	principal_interest_tv2a.setLayoutParams(params);    	
		principal_interest_tv2a.setText("- min");

    	TextView principal_interest_tv2b = new TextView(frgActivity);
    	params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
    	params.weight = 0.3f;
    	principal_interest_tv2b.setLayoutParams(params);  
    	principal_interest_tv2b.setGravity(Gravity.RIGHT);
    	principal_interest_tv2b.setText(dates[mortgage.getMonthWithMinPrincipalInterestPayment()]);

    	TextView principal_interest_tv2c = new TextView(frgActivity);
    	params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
    	params.weight = 0.3f;
    	principal_interest_tv2c.setLayoutParams(params);  
    	principal_interest_tv2c.setGravity(Gravity.RIGHT);
    	principal_interest_tv2c.setText(formatter.formatNumber(mortgage.getMinPrincipalInterestPayment()));

    	principal_interest_inner2.addView(principal_interest_tv2a);
    	principal_interest_inner2.addView(principal_interest_tv2b);
    	principal_interest_inner2.addView(principal_interest_tv2c);

    	principal_interest_l.addView(principal_interest_inner0);
    	principal_interest_l.addView(principal_interest_inner1);
    	principal_interest_l.addView(principal_interest_inner2);
    	    	
    	LinearLayout max_interest_l = (LinearLayout) frgActivity.findViewById(R.id.s_mortgage_monthly_interest_layout);
    	max_interest_l.setVisibility(View.GONE);
    	LinearLayout hr = (LinearLayout) frgActivity.findViewById(R.id.chart_monthly_one_hr);
    	hr.setVisibility(View.GONE);
    	
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
    	((LinearLayout) tv.getParent()).setVisibility(View.GONE);

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