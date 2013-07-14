package com.codelemma.mortgagecmp;

import com.actionbarsherlock.app.SherlockFragment;
import com.codelemma.mortgagecmp.accounting.Mortgage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class FrgInputMulti extends SherlockFragment {

    OnDataInputListener mCallback;

    private OnClickListener onClickShowDetailListener = new OnClickListener() {
    	@Override
	    public void onClick(View v) {
    		Mortgage m = (Mortgage) v.getTag();
        	Intent intent = new Intent(getSherlockActivity(), ResultsOne.class);
            intent.putExtra("edit_mortgage", true);
            MortgageCMP.getInstance().getAccount().setCurrentMortgage(m);
    		startActivity(intent);
	    }
    };

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView.isChecked()) {
				mCallback.onAddItemToCompare(buttonView.getId());
			} else {
				mCallback.onRemoveItemToCompare(buttonView.getId());
			}
		}
    };

    private OnClickListener onClickDeleteListener = new OnClickListener() {
    	@Override
	    public void onClick(final View v) {
    		final Mortgage mortgage = (Mortgage) v.getTag();
	        new AlertDialog.Builder(getSherlockActivity())
            .setTitle("Delete")
            .setMessage("Do you want to delete this item?")                
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   MortgageCMP.getInstance().getAccount().removeMortgage(mortgage); 
            	   MortgageCMP.getInstance().getAccount().clearComparisonList();
            	   
            	   // remove views from currently active Summary Fragment
           		   ScrollView ll = (ScrollView) getActivity().findViewById(R.id.frg_summary_multi);
        		   ll.removeAllViews();
                   LinearLayout subLayout = (LinearLayout) getSherlockActivity().findViewById(1000+mortgage.getId());
                   subLayout.setVisibility(View.GONE);
               }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   dialog.cancel();
               }
           })
          .show();
	    }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, 
    		                 ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_input_multi, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	showMortgageList();
    }

    public interface OnDataInputListener {
        public void onAddItemToCompare(int item_id);
        public void onRemoveItemToCompare(int item_id);
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

	@SuppressWarnings("deprecation")
	private void showMortgageList() {
		LinearLayout layout = (LinearLayout) getSherlockActivity().findViewById(R.id.mortgage_listing);
		layout.removeAllViews();
		
	 	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	 			LinearLayout.LayoutParams.FILL_PARENT,
	 			LinearLayout.LayoutParams.WRAP_CONTENT);
	 	LinearLayout.LayoutParams paramsL = new LinearLayout.LayoutParams(
	 			LinearLayout.LayoutParams.FILL_PARENT,
	 			LinearLayout.LayoutParams.WRAP_CONTENT);
	 	paramsL.weight = 1;
	 	LinearLayout.LayoutParams paramsR = new LinearLayout.LayoutParams(
	 			LinearLayout.LayoutParams.FILL_PARENT,
	 			LinearLayout.LayoutParams.WRAP_CONTENT);
	 	paramsR.weight = 2;
	 	params.setMargins(0, 5, 0, 5); // left, top, right, bottom

	 	LinearLayout.LayoutParams view_params = new LinearLayout.LayoutParams(
	 			LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
	 	view_params.gravity = Gravity.CENTER;

		for (Mortgage mortgage : MortgageCMP.getInstance().getAccount().getMortgages()) {						

			LinearLayout subLayout = new LinearLayout(getSherlockActivity());	
			subLayout.setId(1000+mortgage.getId()); // mortgage_id is already given to checkbox, here some other id needs to chosen

			subLayout.setLayoutParams(params);
			subLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.mortgage_item));
			subLayout.setPadding(10, 10, 10, 10);
			subLayout.setGravity(Gravity.CENTER_VERTICAL);

			LinearLayout subLayoutLeft = new LinearLayout(getSherlockActivity());
			subLayoutLeft.setLayoutParams(paramsL);
			subLayoutLeft.setGravity(Gravity.LEFT);

			CheckBox chk = new CheckBox(getSherlockActivity());		    
		    chk.setText(mortgage.getName());	
		    chk.setId(mortgage.getId());
		    chk.setTag(mortgage);
		    chk.setOnCheckedChangeListener(onCheckedChangeListener);
		    subLayoutLeft.addView(chk);

			LinearLayout subLayoutRight = new LinearLayout(getSherlockActivity());
			subLayoutRight.setLayoutParams(paramsR);
			subLayoutRight.setGravity(Gravity.RIGHT);

		    ImageView iv = new ImageView(getSherlockActivity());
		    iv.setImageDrawable(getResources().getDrawable(R.drawable.ico_chart));
		    iv.setTag(mortgage);
		    iv.setOnClickListener(onClickShowDetailListener);
		    iv.setPadding(0, 0, 10, 0);
		    subLayoutRight.addView(iv);

		    iv = new ImageView(getSherlockActivity());
		    iv.setImageDrawable(getResources().getDrawable(R.drawable.ico_delete));
		    iv.setTag(mortgage);
		    iv.setOnClickListener(onClickDeleteListener);
		    subLayoutRight.addView(iv);

		    subLayout.addView(subLayoutLeft);
		    subLayout.addView(subLayoutRight);
		    layout.addView(subLayout);
		}
	}
}