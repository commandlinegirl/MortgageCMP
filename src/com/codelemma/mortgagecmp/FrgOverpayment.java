package com.codelemma.mortgagecmp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.analytics.tracking.android.EasyTracker;

public class FrgOverpayment extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, 
			                 ViewGroup container,
	                         Bundle savedInstanceState) {
	    return inflater.inflate(R.layout.frg_overpayment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); 
		
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
