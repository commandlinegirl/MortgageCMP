package com.codelemma.mortgagecmp;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.MortgageNameConstants;
import com.codelemma.mortgagecmp.accounting.MortgageFactory.MortgageFactoryException;

public class FrgInputOne extends SherlockFragment implements OnItemSelectedListener {

	private OnDataInputListener mCallback;

    public interface OnDataInputListener {
        public void onDataInput(String key, String value);
        public void addMortgageToAccount(HashMap<String,String> data) throws MortgageFactoryException;   
        public void replaceButtons(Mortgage mortgage);
        public boolean showModifyCloneButtons();
        public void toggleVisibilityFees(View view);
        public void toggleVisibilityExtraPayment(View view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDataInputListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDataInputListener");
        }
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("FrgInput.onCreate()", "called");
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    //TextView type_description = (TextView) getActivity().findViewById(R.id.type_description);
	    //type_description.setText(getResources().getString(type_descriptions[pos]));	
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		parent.setSelection(0);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgInput.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_input_one, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
	    Log.d("FrgInput.onActivityCreated()", "called");

	    Spinner spinner = (Spinner) getActivity().findViewById(R.id.mortgage_type);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
	         R.array.mortgage_types_spinner, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
		spinner.setSelection(0);	    
	    spinner.setOnItemSelectedListener(this);
	    
    	Mortgage mortgage = MortgageCMP.getInstance().getAccount().getCurrentMortgage();

    	if (mortgage != null) {
    		// set modify and clone buttons
    		mCallback.replaceButtons(mortgage);
    		
    		// toggle visibility of optional input
    		//if (mortgage.getYearlyPropertyTax()
    		//		.add(mortgage.getYearlyPropertyInsurance())
    		//		.add(mortgage.getPMI())
    		//		.add(mortgage.getClosingFees()).compareTo(BigDecimal.ZERO) > 0) {
    		//    mCallback.toggleVisibilityFees(null);
            //}

    		//if (mortgage.getExtraPayment().compareTo(BigDecimal.ZERO) > 0) {
    		//    mCallback.toggleVisibilityExtraPayment(null);
            //}
    		
    		
    		
    		EditText name = (EditText) getView().findViewById(R.id.mortgage_name);
    		name.setText(mortgage.getName(), TextView.BufferType.EDITABLE);

    		EditText price = (EditText) getSherlockActivity().findViewById(R.id.mortgage_purchase_price);
    		price.setText(mortgage.getPurchasePrice().toString(), TextView.BufferType.EDITABLE);

    		EditText downpayment = (EditText) getSherlockActivity().findViewById(R.id.mortgage_downpayment);
    		downpayment.setText(mortgage.getDownpayment().toString(), TextView.BufferType.EDITABLE);

    		EditText interest = (EditText) getSherlockActivity().findViewById(R.id.mortgage_interest_rate);
    		interest.setText(mortgage.getInterestRate().toString(), TextView.BufferType.EDITABLE);

    		EditText termy = (EditText) getSherlockActivity().findViewById(R.id.mortgage_term_years);
    		termy.setText(String.valueOf(mortgage.getTermYears()), TextView.BufferType.EDITABLE);

    		EditText termm = (EditText) getSherlockActivity().findViewById(R.id.mortgage_term_months);
    		termm.setText(String.valueOf(mortgage.getTermMonths()), TextView.BufferType.EDITABLE);

    		EditText insurance = (EditText) getSherlockActivity().findViewById(R.id.mortgage_property_insurance);
    		insurance.setText(mortgage.getYearlyPropertyInsurance().toString(), TextView.BufferType.EDITABLE);

    		EditText tax = (EditText) getSherlockActivity().findViewById(R.id.mortgage_property_tax);
    		tax.setText(mortgage.getYearlyPropertyTax().toString(), TextView.BufferType.EDITABLE);

    		EditText pmi = (EditText) getSherlockActivity().findViewById(R.id.mortgage_pmi);
    		pmi.setText(mortgage.getPMI().toString(), TextView.BufferType.EDITABLE);

    		EditText fees = (EditText) getSherlockActivity().findViewById(R.id.mortgage_closing_fees);
    		fees.setText(mortgage.getClosingFees().toString(), TextView.BufferType.EDITABLE);

    		EditText extra_payment = (EditText) getSherlockActivity().findViewById(R.id.mortgage_extra_payment);
    		extra_payment.setText(mortgage.getExtraPayment().toString(), TextView.BufferType.EDITABLE);

    		int extra_payment_frequency = mortgage.getExtraPaymentFrequency();

    		if (extra_payment_frequency == 1) {
    			RadioButton rb = (RadioButton) getActivity().findViewById(R.id.radioMonthly);
    			rb.setChecked(true);
    		}

    		if (extra_payment_frequency == 12) {
    			RadioButton rb = (RadioButton) getActivity().findViewById(R.id.radioYearly);
    			rb.setChecked(true);
    		}
    		
    		String type = mortgage.getType();
    		if (type == MortgageNameConstants.FIXED_RATE_VARIABLE_PRINCIPAL) {
    			spinner.setSelection(0);
    		} else if (type == MortgageNameConstants.FIXED_RATE_FIXED_PRINCIPAL) {
    			spinner.setSelection(1);
    		}
    		
    	}
    }

    @Override
    public void onPause() {
    	super.onPause();
    	Log.d("FrgInput.onPause", "called");
    }

    @Override
    public void onStart() {
    	super.onStart();
    	Log.d("FrgInput.onStart", "called");
    }

    @Override
    public void onStop() {
    	super.onStop();
    	Log.d("FrgInput.onStop", "called");
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Log.d("FrgInput.onDestroy", "called");
    }
    
	public void resetForm() {
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_name)).setText("");
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_purchase_price)).setText("");
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_downpayment)).setText("");
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_interest_rate)).setText("");
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_term_years)).setText("");
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_term_months)).setText("0");
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_property_insurance)).setText("0");
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_property_tax)).setText("0");
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_pmi)).setText("0");
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_closing_fees)).setText("0");		
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_extra_payment)).setText("0");		
	}
}