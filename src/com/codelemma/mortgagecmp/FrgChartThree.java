package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.HistogramVisitor;
import com.codelemma.mortgagecmp.accounting.HistoryMortgage;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.PieChartVisitor;
import com.codelemma.mortgagecmp.accounting.PlotVisitor;

public class FrgChartThree extends SherlockFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("FrgChartThree.onCreateView()", "called");
        return inflater.inflate(R.layout.frg_chart_three, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState); 
    	MortgageCMP appState = MortgageCMP.getInstance(); 	    	
    	Mortgage mortgage = appState.getCurrentMortgage();  
    	
    	if (mortgage != null) {

        	NumberFormatter nf = new NumberFormatter();
            
            PieChartVisitor piechartVisitor = new PieChartMaker(getSherlockActivity());
            mortgage.makePieChart(piechartVisitor);
            
            LinearLayout text = (LinearLayout) getActivity().findViewById(R.id.summary_text);
            TextView tv = new TextView(getActivity());
            tv.setGravity(Gravity.CENTER);
            tv.setText("Principal: "+nf.formatNumber(mortgage.getPrincipalFraction())+"%");
            text.addView(tv);
            tv = new TextView(getActivity());
            tv.setGravity(Gravity.CENTER);
            tv.setText("Interest: "+nf.formatNumber(mortgage.getInterestFraction())+"%");
            text.addView(tv);
            tv = new TextView(getActivity());
            tv.setGravity(Gravity.CENTER);
            tv.setText("Tax, insurance, costs: "+nf.formatNumber(mortgage.getExtraCostFraction())+"%");
            text.addView(tv);
            tv = new TextView(getActivity());
            tv.setGravity(Gravity.CENTER);
            tv.setText("Overpayment: 0.00%");
            text.addView(tv);
    	}
    }
}
