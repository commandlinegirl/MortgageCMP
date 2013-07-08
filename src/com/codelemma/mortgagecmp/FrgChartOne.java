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
import com.codelemma.mortgagecmp.accounting.PlotVisitor;

public class FrgChartOne extends SherlockFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgChartOne.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_chart_one, container, false);
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

            PlotVisitor plotVisitor = new Plotter(getSherlockActivity(), dates);
            historyMortgage.plot(plotVisitor);
		} else {
			LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.frg_chart_one);
	    	ll.removeAllViews();
		}
    }
}
