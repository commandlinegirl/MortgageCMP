package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.HistogramVisitor;
import com.codelemma.mortgagecmp.accounting.HistoryMortgage;
import com.codelemma.mortgagecmp.accounting.MonthlyPaymentListingVisitor;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.google.analytics.tracking.android.EasyTracker;

public class FrgChartMonthlyOne extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_chart_monthly_one, container, false);
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
            HistogramVisitor histogramVisitor = new HistogramMaker(getSherlockActivity(), dates);
            mortgage.makeHistogram(histogramVisitor);
            MonthlyPaymentListingVisitor mplv = new MonthlyPaymentListingMaker(getSherlockActivity(), dates);
            mortgage.listMonthlyPayment(mplv);
		} else {
			LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.frg_chart_monthly_one);
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
