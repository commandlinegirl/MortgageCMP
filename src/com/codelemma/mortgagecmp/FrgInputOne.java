package com.codelemma.mortgagecmp;

import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.FillInputVisitor;
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
	
	public void showInfoDialog(int r_id) {
		Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);			
	    dialog.setContentView(r_id);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_input_one, container, false);
    }

    @Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		// here change view (different for ARM)
	    LinearLayout specific_mortgage_input = (LinearLayout) getActivity().findViewById(R.id.specific_mortgage_type_view);
	    
	    if (pos < 2) {
            specific_mortgage_input.removeAllViews();
	    } else {
	        if (specific_mortgage_input.getChildCount() == 0) {    	
                specific_mortgage_input.removeAllViews();
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
                View v = inflater.inflate(R.layout.arm_layout, null);
                specific_mortgage_input.addView(v);
        		EditText edit_text = (EditText) getActivity().findViewById(R.id.adjustment_period);
                edit_text.setEnabled(false);
                edit_text = (EditText) getActivity().findViewById(R.id.months_between_adjustments);
                edit_text.setEnabled(false);
		    }
        }
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		parent.setSelection(0);
	}
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
	    final Spinner spinner = (Spinner) getActivity().findViewById(R.id.mortgage_type);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
	         R.array.mortgage_types_spinner, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(this);

    	Mortgage mortgage = MortgageCMP.getInstance().getAccount().getCurrentMortgage();
    	if (mortgage != null) {
    		// set modify and clone buttons
    		mCallback.replaceButtons(mortgage);
    		spinner.setSelection(MortgageNameConstants.getTypeInteger(mortgage.getType()));
        	FillInputVisitor fillInputVisitor = new InputFiller(getSherlockActivity());
            mortgage.fillInput(fillInputVisitor);
    	}
    }
}