package com.codelemma.mortgagecmp;

import java.math.BigDecimal;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.mortgagecmp.accounting.AdjustableRateMortgage;
import com.codelemma.mortgagecmp.accounting.FixedRateFixedPrincipalMortgage;
import com.codelemma.mortgagecmp.accounting.FixedRateVariablePrincipalMortgage;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.MortgageNameConstants;
import com.codelemma.mortgagecmp.accounting.SummaryVisitor;

public class SummaryWriter implements SummaryVisitor {

	private SherlockFragmentActivity frgActivity;
	
	public SummaryWriter(SherlockFragmentActivity frgActivity) {
		this.frgActivity = frgActivity;
	}

	@Override
	public void writeSummary(FixedRateFixedPrincipalMortgage mortgage) {
		commonSummary(mortgage);
	}

	@Override
	public void writeSummary(FixedRateVariablePrincipalMortgage mortgage) {
		commonSummary(mortgage);
		
	}

	@Override
	public void writeSummary(AdjustableRateMortgage mortgage) {
		commonSummary(mortgage);
		
        TextView text =  (TextView) frgActivity.findViewById(R.id.s_interest_rate_title);
        text.setText(frgActivity.getResources().getString(R.string.initial_interest_rate_input));
	}

	private void commonSummary(Mortgage mortg) {
		
		NumberFormatter formatter = new NumberFormatter();
		
		TextView tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_name);
		tv.setText(mortg.getName());

		tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_type);
		int type_r_id = MortgageNameConstants.getTypeInfo(mortg.getType());
		if (frgActivity.getResources().getString(type_r_id) != null) {
		    tv.setText(type_r_id);
		} else {
			tv.setText("-");
		}

		tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_purchase_price);
		tv.setText(formatter.formatNumber(mortg.getPurchasePrice()));

		tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_downpayment);
		tv.setText(formatter.formatNumber(mortg.getDownpayment()));

		tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_amount);
		tv.setText(formatter.formatNumber(mortg.getLoanAmount()));

		tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_term);
	 	tv.setText(String.valueOf(mortg.getNumberOfPayments()));

		tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_interest_rate);
		tv.setText(formatter.formatNumber(mortg.getInterestRate())+"%");

		String[] dates = mortg.getHistory().getDates(
				MortgageCMP.getInstance().getSimulationStartYear(), 
				MortgageCMP.getInstance().getSimulationStartMonth()); 
		
		tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_last_payment_date);
		if (mortg.getNumberOfPayments() > 0) {
			tv.setText(dates[mortg.getNumberOfPayments()-1]);
		} else {
			tv.setText("-");
		}
		
		if (mortg.getExtraPayment().compareTo(BigDecimal.ZERO) > 0) {
			tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_total_extra_payment);
			tv.setText(formatter.formatNumber(mortg.getTotalExtraPayment()));
			tv = (TextView) frgActivity.findViewById(R.id.s_mortgage_term_reduction);
	    	tv.setText(String.valueOf(mortg.getTotalTermMonths() - mortg.getNumberOfPayments())+" months");

		} else {
	        LinearLayout d = (LinearLayout) frgActivity.findViewById(R.id.s_mortgage_term_reduction_l);
	        d.setVisibility(View.GONE);
	        d = (LinearLayout) frgActivity.findViewById(R.id.s_mortgage_total_extra_payment_l);
	        d.setVisibility(View.GONE);
	        TextView hrl =  (TextView) frgActivity.findViewById(R.id.s_hrline1);
	        hrl.setVisibility(View.GONE);
	        hrl =  (TextView) frgActivity.findViewById(R.id.s_hrline2);
	        hrl.setVisibility(View.GONE);	        
		}
	}
}
