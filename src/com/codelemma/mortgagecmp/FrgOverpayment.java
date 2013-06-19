package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class FrgOverpayment extends SherlockFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, 
			                 ViewGroup container,
	                         Bundle savedInstanceState) {
		Log.d("FrgChartTwo.onCreateView()", "called");
	    return inflater.inflate(R.layout.frg_overpayment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); 
		
	}
}
