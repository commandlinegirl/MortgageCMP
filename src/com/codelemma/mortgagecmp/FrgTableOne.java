package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.HistoryMortgage;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.TableVisitor;
import com.google.analytics.tracking.android.EasyTracker;

public class FrgTableOne extends SherlockFragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgTableOne.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_table_one, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);   
    	Mortgage mortgage = MortgageCMP.getInstance().getAccount().getCurrentMortgage();  
    	if (mortgage != null) {
    		HistoryMortgage historyMortgage = mortgage.getHistory();
        	String[] dates = historyMortgage.getDates(
        			MortgageCMP.getInstance().getSimulationStartYear(), 
        			MortgageCMP.getInstance().getSimulationStartMonth());
        	TableVisitor tableMaker = new TableMaker(getSherlockActivity(), dates);
        	historyMortgage.makeTable(tableMaker);
		} else {
			LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.frg_table_one);
	    	ll.removeAllViews();
		}
    }
    
    @Override
    public void onStart() {
      super.onStart();
      EasyTracker.getInstance().activityStart(getActivity());
    }

    @Override
    public void onStop() {
      super.onStop();
      EasyTracker.getInstance().activityStop(getActivity());
    }
}