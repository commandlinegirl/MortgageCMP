package com.codelemma.mortgagecmp;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.Mortgage;

public class FrgInput extends SherlockFragment {

    OnDataInputListener mCallback;
    MortgageCMP appState;

    public interface OnDataInputListener {
        public void onDataInput(String key, String value);
        public Mortgage addMortgageToAccount(HashMap<String,String> data);   
        public void replaceButtons(Mortgage mortgage);
        public boolean showModifyCloneButtons();
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
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgInput.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_input, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	appState = MortgageCMP.getInstance();
    	
	    Intent intent = getSherlockActivity().getIntent();
		Log.d("intent", String.valueOf(intent));
		Log.d("savedInstanceState", String.valueOf(savedInstanceState));

	    Log.d("FrgInput.onActivityCreated()", "called");
		//if (appState.getCurrentMortgage() != null) {
		//	mCallback.replaceButtons(appState.getCurrentMortgage());
		//    Log.d("FrgInput.mCallback.showModifyCloneButtons()", "called");
		//}

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

		EditText overpayment = (EditText) getSherlockActivity().findViewById(R.id.mortgage_overpayment);
		String mortgage_overpayment = intent.getStringExtra("mortgage_overpayment");
		if (mortgage_overpayment != null) {
			overpayment.setText(mortgage_overpayment, TextView.BufferType.EDITABLE);
		}
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
		((EditText) getSherlockActivity().findViewById(R.id.mortgage_overpayment)).setText("0");		
	}
}
