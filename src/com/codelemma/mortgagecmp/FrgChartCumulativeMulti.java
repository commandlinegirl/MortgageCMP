package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.HistoryMortgage;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.PlotVisitor;

public class FrgChartCumulativeMulti extends SherlockFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_chart_cumulative_multi, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState); 
    	
    	/* Check which mortgage has the longest loan term;
    	 * build date list for this term */
    	Mortgage mortgage = MortgageCMP.getInstance().getAccount().getLongestMortgage();
    	
    	if (mortgage != null) {
    	    HistoryMortgage historyMortgage = mortgage.getHistory();
    	    String[] dates = historyMortgage.getDates(
    	    		MortgageCMP.getInstance().getSimulationStartYear(), 
    	    		MortgageCMP.getInstance().getSimulationStartMonth());
        
            PlotVisitor plotVisitor = new Plotter(getSherlockActivity(), dates);
            MortgageCMP.getInstance().getAccount().plotComparisonOutstandingPrincipal(plotVisitor);
            MortgageCMP.getInstance().getAccount().plotComparisonTotalInterest(plotVisitor);

    	} 
    	
    	if (MortgageCMP.getInstance().getAccount().getMortgagesToCompare().size() == 0) {
    		LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.frg_chart_multi_cumulative);
    		ll.removeAllViews();
    	}
    }
}