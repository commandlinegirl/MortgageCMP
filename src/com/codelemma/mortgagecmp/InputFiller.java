package com.codelemma.mortgagecmp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.mortgagecmp.accounting.AdjustableRateMortgage;
import com.codelemma.mortgagecmp.accounting.FillInputVisitor;
import com.codelemma.mortgagecmp.accounting.FixedRateFixedPrincipalMortgage;
import com.codelemma.mortgagecmp.accounting.FixedRateVariablePrincipalMortgage;
import com.codelemma.mortgagecmp.accounting.Mortgage;

public class InputFiller implements FillInputVisitor {

	private SherlockFragmentActivity frgActivity;

	public InputFiller(SherlockFragmentActivity sherlockFragmentActivity) {
		frgActivity = sherlockFragmentActivity;
	}

	@Override
	public void fillInput(FixedRateVariablePrincipalMortgage mortgage) {
	    LinearLayout specific_mortgage_input = (LinearLayout) frgActivity.findViewById(R.id.specific_mortgage_type_view);
        specific_mortgage_input.removeAllViews();
		commonInput(mortgage);
	}

	@Override
	public void fillInput(FixedRateFixedPrincipalMortgage mortgage) {
	    LinearLayout specific_mortgage_input = (LinearLayout) frgActivity.findViewById(R.id.specific_mortgage_type_view);
        specific_mortgage_input.removeAllViews();		
		commonInput(mortgage);
	}

	@Override
	public void fillInput(AdjustableRateMortgage mortgage) {

		// inflate layout with ARM strategy section
	    LinearLayout specific_mortgage_input = (LinearLayout) frgActivity.findViewById(R.id.specific_mortgage_type_view);
        specific_mortgage_input.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) frgActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.arm_layout, null);
        specific_mortgage_input.addView(v);

        // make the section visible
	    setSectionVisibility(R.id.arm_section_input_text, 
	    		R.id.arm_section_input, 
	    		((ResultsOne) frgActivity).getAdjustmentStrategyVisibility(), 
	    		R.string.arm_section_input_visible, 
	    		R.string.arm_section_input_invisible);

	    // Set previously chosen values
		EditText adjustment_period = (EditText) frgActivity.findViewById(R.id.adjustment_period);
		adjustment_period.setText(String.valueOf(mortgage.getAdjustmentPeriod()), TextView.BufferType.EDITABLE);

		EditText months_between_adjustments = (EditText) frgActivity.findViewById(R.id.months_between_adjustments);
		months_between_adjustments.setText(String.valueOf(mortgage.getMonthsBetweenAdjustments()), TextView.BufferType.EDITABLE);

		EditText max_single_rate_adjustment = (EditText) frgActivity.findViewById(R.id.max_single_rate_adjustment);
		max_single_rate_adjustment.setText(mortgage.getMaxSingleRateAdjustment().toString(), TextView.BufferType.EDITABLE);

		EditText total_interest_cap = (EditText) frgActivity.findViewById(R.id.total_interest_cap);
		total_interest_cap.setText(mortgage.getInterestRateCap().toString(), TextView.BufferType.EDITABLE);

		int arm_type = mortgage.getARMType();
		Log.d("arm_type", String.valueOf(arm_type));

		switch(arm_type) {
		    case 71:
		    	setRadioButton(R.id.arm71);
		    	disableEditText();
				Log.d("arm_type 71", String.valueOf(arm_type));		    	
		    	break;
		    case 51:
		    	setRadioButton(R.id.arm51);
		    	disableEditText();
				Log.d("arm_type 51", String.valueOf(arm_type));		    	
		    	break;
		    case 31:
		    	setRadioButton(R.id.arm31);
		    	disableEditText();
				Log.d("arm_type 31", String.valueOf(arm_type));		    	
		    	break;
		    case 0:
		    	setRadioButton(R.id.arm_other);
				Log.d("arm_type 0", String.valueOf(arm_type));
		    	break;
		    default:
		    	setRadioButton(R.id.arm71);
		    	disableEditText();
				Log.d("arm_type default", String.valueOf(arm_type));		    	
		    	break;
		}

		commonInput(mortgage);
	}

	private void commonInput(Mortgage mortgage) {

	    setSectionVisibility(R.id.basic_section_input_text, 
	    		R.id.basic_section_input, 
	    		((ResultsOne) frgActivity).getBasicDataVisibility(), 
	    		R.string.basic_section_input_visible, 
	    		R.string.basic_section_input_invisible);
	    
	    setSectionVisibility(R.id.advanced_input_text, 
	    		R.id.advanced_input, 
	    		((ResultsOne) frgActivity).getFeesVisibility(), 
	    		R.string.advanced_input_visible, 
	    		R.string.advanced_input_invisible);
	    
	    setSectionVisibility(R.id.advanced_extra_payments_input_text, 
	    		R.id.advanced_extra_payments_input, 
	    		((ResultsOne) frgActivity).getExtraPaymentVisibility(), 
	    		R.string.advanced_extra_payments_input_visible, 
	    		R.string.advanced_extra_payments_input_invisible);	

   		EditText name = (EditText) frgActivity.findViewById(R.id.mortgage_name);
		name.setText(mortgage.getName(), TextView.BufferType.EDITABLE);

		EditText price = (EditText) frgActivity.findViewById(R.id.mortgage_purchase_price);
		price.setText(mortgage.getPurchasePrice().toString(), TextView.BufferType.EDITABLE);

		EditText downpayment = (EditText) frgActivity.findViewById(R.id.mortgage_downpayment);
		downpayment.setText(mortgage.getDownpayment().toString(), TextView.BufferType.EDITABLE);

		EditText termy = (EditText) frgActivity.findViewById(R.id.mortgage_term_years);
		termy.setText(String.valueOf(mortgage.getTermYears()), TextView.BufferType.EDITABLE);
		
		EditText termm = (EditText) frgActivity.findViewById(R.id.mortgage_term_months);
		termm.setText(String.valueOf(mortgage.getTermMonths()), TextView.BufferType.EDITABLE);

		EditText insurance = (EditText) frgActivity.findViewById(R.id.mortgage_property_insurance);
		insurance.setText(mortgage.getYearlyPropertyInsurance().toString(), TextView.BufferType.EDITABLE);

		EditText tax = (EditText) frgActivity.findViewById(R.id.mortgage_property_tax);
		tax.setText(mortgage.getYearlyPropertyTax().toString(), TextView.BufferType.EDITABLE);

		EditText pmi = (EditText) frgActivity.findViewById(R.id.mortgage_pmi);
		pmi.setText(mortgage.getPMI().toString(), TextView.BufferType.EDITABLE);

		EditText fees = (EditText) frgActivity.findViewById(R.id.mortgage_closing_fees);
		fees.setText(mortgage.getClosingFees().toString(), TextView.BufferType.EDITABLE);

		EditText extra_payment = (EditText) frgActivity.findViewById(R.id.mortgage_extra_payment);
		extra_payment.setText(mortgage.getExtraPayment().toString(), TextView.BufferType.EDITABLE);

		EditText interest = (EditText) frgActivity.findViewById(R.id.mortgage_interest_rate);
		interest.setText(mortgage.getInterestRate().toString(), TextView.BufferType.EDITABLE);
		
		int extra_payment_frequency = mortgage.getExtraPaymentFrequency();

		if (extra_payment_frequency == 1) {
			setRadioButton(R.id.radioMonthly);
		}
		if (extra_payment_frequency == 12) {
			setRadioButton(R.id.radioYearly);
		}
	}
	
    private void setRadioButton(int id) {
    	RadioButton rb = (RadioButton) frgActivity.findViewById(id);
    	rb.setChecked(true);
    }

    private void setSectionVisibility(int id_text, int id_layout, boolean current_visibility, int visible, int invisible) {
    	TextView tv = (TextView) frgActivity.findViewById(id_text);
    	LinearLayout v = (LinearLayout) frgActivity.findViewById(id_layout);    	
    	if (current_visibility) {
    		v.setVisibility(View.VISIBLE);
    		tv.setText(visible);
    	} else {
    		v.setVisibility(View.GONE);
    		tv.setText(invisible);
    	}
    }
    
	private void disableEditText() {
		EditText edit_text = (EditText) frgActivity.findViewById(R.id.adjustment_period);
        edit_text.setEnabled(false);
        edit_text = (EditText) frgActivity.findViewById(R.id.months_between_adjustments);
        edit_text.setEnabled(false);
	}
}