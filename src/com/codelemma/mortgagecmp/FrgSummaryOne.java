package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.SummaryVisitor;

public class FrgSummaryOne extends SherlockFragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_summary_one, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	Mortgage mortgage = MortgageCMP.getInstance().getAccount().getCurrentMortgage();
    	SummaryVisitor sv = new SummaryWriter(getSherlockActivity());    	
		if (mortgage != null) {
			mortgage.writeSummary(sv);
		} else {
	    	ScrollView ll = (ScrollView) getActivity().findViewById(R.id.frg_summary_one);
	    	ll.removeAllViews();
		}
    }
}