package com.codelemma.mortgagecmp;

import java.math.BigDecimal;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
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
import com.codelemma.mortgagecmp.accounting.MortgageFactory.MortgageFactoryException;

public class FrgInputOne extends SherlockFragment implements OnItemSelectedListener {

	private static final String FIXED_RATE_VARIABLE_PRINCIPAL = "frvp";
	private static final String FIXED_RATE_FIXED_PRINCIPAL = "frfp";
	
	private OnDataInputListener mCallback;
    private MortgageCMP appState;
    private String[] mortgage_type_items = {FIXED_RATE_VARIABLE_PRINCIPAL, FIXED_RATE_FIXED_PRINCIPAL};

    public interface OnDataInputListener {
        public void onDataInput(String key, String value);
        public void addMortgageToAccount(HashMap<String,String> data) throws MortgageFactoryException;   
        public void replaceButtons(Mortgage mortgage);
        public boolean showModifyCloneButtons();
        public void toggleVisibilityFees(View view);
        public void toggleVisibilityExtraPayment(View view);
        public void setMortgageType(String type);
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
		mCallback.setMortgageType(mortgage_type_items[pos]);
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

    	appState = MortgageCMP.getInstance();
    	Mortgage mortgage = appState.getCurrentMortgage();

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
    	}

	    Spinner spinner = (Spinner) getActivity().findViewById(R.id.mortgage_type);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
	         R.array.mortgage_types_spinner, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setSelection(0);
	    spinner.setOnItemSelectedListener(this);
    	
	    Intent intent = getSherlockActivity().getIntent();
		Log.d("intent", String.valueOf(intent));
		if (savedInstanceState != null) {
            Log.d("key", String.valueOf(savedInstanceState.getBoolean("clone_modify_buttons")));
		}
		Log.d("savedInstanceState", String.valueOf(savedInstanceState));

		int mortgage_type_index = intent.getIntExtra("mortgage_type", 0);
		spinner.setSelection(mortgage_type_index);
		
		EditText name = (EditText) getView().findViewById(R.id.mortgage_name);
		String mortgage_name = intent.getStringExtra("mortgage_name");
		if (mortgage_name != null) {
		    name.setText(mortgage_name, TextView.BufferType.EDITABLE);
		}

		EditText price = (EditText) getSherlockActivity().findViewById(R.id.mortgage_purchase_price);
		String mortgage_purchase_price = intent.getStringExtra("mortgage_purchase_price");
		if (mortgage_purchase_price != null) {
			Log.d("puuting here again: mortgage_purchase_price", mortgage_purchase_price);
		    price.setText(mortgage_purchase_price, TextView.BufferType.EDITABLE);
		}

		EditText downpayment = (EditText) getSherlockActivity().findViewById(R.id.mortgage_downpayment);
		String mortgage_downpayment = intent.getStringExtra("mortgage_downpayment");
		if (mortgage_downpayment != null) {
		    downpayment.setText(mortgage_downpayment, TextView.BufferType.EDITABLE);
		}

		EditText interest = (EditText) getSherlockActivity().findViewById(R.id.mortgage_interest_rate);
		String mortgage_interest_rate = intent.getStringExtra("mortgage_interest_rate");
		if (mortgage_interest_rate != null) {
		    interest.setText(mortgage_interest_rate, TextView.BufferType.EDITABLE);
		}

		EditText termy = (EditText) getSherlockActivity().findViewById(R.id.mortgage_term_years);
		String mortgage_term_years = intent.getStringExtra("mortgage_term_years");
		if (mortgage_term_years != null) {
		    termy.setText(mortgage_term_years, TextView.BufferType.EDITABLE);
		}
		
		EditText termm = (EditText) getSherlockActivity().findViewById(R.id.mortgage_term_months);
		String mortgage_term_months = intent.getStringExtra("mortgage_term_months");
		if (mortgage_term_months != null) {
		    termm.setText(mortgage_term_months, TextView.BufferType.EDITABLE);
		}
		
		EditText insurance = (EditText) getSherlockActivity().findViewById(R.id.mortgage_property_insurance);
		String mortgage_property_insurance = intent.getStringExtra("mortgage_property_insurance");
		if (mortgage_property_insurance != null) {
		    insurance.setText(mortgage_property_insurance, TextView.BufferType.EDITABLE);
		}
		
		EditText tax = (EditText) getSherlockActivity().findViewById(R.id.mortgage_property_tax);
		String mortgage_property_tax = intent.getStringExtra("mortgage_property_tax");
		if (mortgage_property_tax != null) {
		    tax.setText(mortgage_property_tax, TextView.BufferType.EDITABLE);
		}
		
		EditText pmi = (EditText) getSherlockActivity().findViewById(R.id.mortgage_pmi);
		String mortgage_pmi = intent.getStringExtra("mortgage_pmi");
		if (mortgage_pmi != null) {
		    pmi.setText(mortgage_pmi, TextView.BufferType.EDITABLE);
		}

		EditText fees = (EditText) getSherlockActivity().findViewById(R.id.mortgage_closing_fees);
		String closing_fees = intent.getStringExtra("mortgage_closing_fees");
		if (mortgage_pmi != null) {
		    fees.setText(closing_fees, TextView.BufferType.EDITABLE);
		}
		
		EditText extra_payment = (EditText) getSherlockActivity().findViewById(R.id.mortgage_extra_payment);
		String mortgage_extra_payment = intent.getStringExtra("mortgage_extra_payment");
		if (mortgage_extra_payment != null) {
			extra_payment.setText(mortgage_extra_payment, TextView.BufferType.EDITABLE);
		}

		int extra_payment_frequency = intent.getIntExtra("mortgage_extra_payment_frequency", 12);
		
		Log.d("extra_payment_frequency", String.valueOf(extra_payment_frequency));
		
		if (extra_payment_frequency == 1) {
			RadioButton rb = (RadioButton) getActivity().findViewById(R.id.radioMonthly);
			rb.setChecked(true);
		}
		
		if (extra_payment_frequency == 12) {
			RadioButton rb = (RadioButton) getActivity().findViewById(R.id.radioYearly);
			rb.setChecked(true);
		}
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);
      savedInstanceState.putBoolean("clone_modify_buttons", true);
      savedInstanceState.putDouble("myDouble", 1.9);      
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