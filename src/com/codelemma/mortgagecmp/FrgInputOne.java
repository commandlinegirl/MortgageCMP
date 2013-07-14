package com.codelemma.mortgagecmp;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
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
        public void addMortgageToAccount(Map<String,String> data) throws MortgageFactoryException;   
        public void replaceButtons(Mortgage mortgage);
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
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		parent.setSelection(0);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_input_one, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
		
	    Spinner spinner = (Spinner) getActivity().findViewById(R.id.mortgage_type);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
	         R.array.mortgage_types_spinner, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(this);
	    
    	Mortgage mortgage = MortgageCMP.getInstance().getAccount().getCurrentMortgage();

    	if (mortgage != null) {
    		// set modify and clone buttons
    		mCallback.replaceButtons(mortgage);
    		
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

    		spinner.setSelection(MortgageNameConstants.getTypeInteger(mortgage.getType()));
    	}
    }
}