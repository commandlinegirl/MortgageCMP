package com.codelemma.mortgagecmp;

import java.util.Calendar;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.MortgageNameConstants;
import com.codelemma.mortgagecmp.accounting.MortgageFactory.MortgageFactoryException;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsOne extends SherlockFragmentActivity
                        implements FrgInputOne.OnDataInputListener {

	static final int NUM_ITEMS = 6;
    private HashMap<String,String> input_values = new HashMap<String,String>();
    private boolean showModifyCloneButtons = false;
    private int extraPaymentFrequency = 12; // 12 means yearly
    private String mortgage_type = MortgageNameConstants.FIXED_RATE_VARIABLE_PRINCIPAL;

    MyAdapter mAdapter;
    ViewPager mPager;

    private OnClickListener clickModifyListener = new OnClickListener() {
    	@Override
	    public void onClick(View v) {
	        final Mortgage mortgage = (Mortgage) v.getTag(R.string.mortgage_to_modify);
	        MortgageCMP.getInstance().getAccount().removeMortgage(mortgage);
            Toast.makeText(ResultsOne.this, mortgage.getName()+" deleted.", Toast.LENGTH_SHORT).show();
            addMortgage(null);
	    }
    };

    private OnClickListener clickCloneListener = new OnClickListener() {
    	@Override
	    public void onClick(View v) {
            addMortgage(null);
	    }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ResultsOne.onCreate()", "called");
		
        setContentView(R.layout.frg_pager_one);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		setupCurrentDate();
	    if (MortgageCMP.getInstance().getAccount() == null) {
	    	MortgageCMP.getInstance().setAccount();
	    	for (Mortgage mort : MortgageCMP.getInstance().getAccount().getMortgages()) {
		    	recalculate(mort);
		    	
		    	Log.d("------", "000000");
		    	Log.d("Mortgage name", mort.getName());
		    	Log.d("Mortgage type", mort.getType());
		    	Log.d("------", "000000");
		    }
	    }
	    
		mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        if (getIntent().getBooleanExtra("edit_mortgage", false)) {
        	mPager.setCurrentItem(1);
        }
	}

	private void setupCurrentDate() {
	    final Calendar c = Calendar.getInstance();	        
	    MortgageCMP.getInstance().setSimulationStartYear(c.get(Calendar.YEAR));		        
	    MortgageCMP.getInstance().setSimulationStartMonth(c.get(Calendar.MONTH));	
	}

    @Override
    public void onPause() {
    	super.onPause();
    	Log.d("ResultsOne.onPause", "called");
        MortgageCMP.getInstance().saveAccount();
    }

    @Override
    public void onStop() {
    	super.onStop();
    	Log.d("ResultsOne.onStop", "called");
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Log.d("ResultsOne.onDestroy", "called");
    }
	
    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

		@Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
        	if (position == 0) {
                return new FrgInputOne();
        	} else if (position == 1) {
                return new FrgSummaryOne();
        	} else if (position == 2) {
        		return new FrgLoanBreakdownOne();
        	} else if (position == 3) {
        		return new FrgChartCumulativeOne();
        	} else if (position == 4) {
        		return new FrgChartMonthlyOne();
        	} else if (position == 5) {
        		return new FrgTableOne();   		
        	} else {
                return new FrgInputOne();       		
        	}
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
            case 0:
                return "INPUT";            
            case 1:
                return "SUMMARY";
            case 2:
                return "LOAN BREAKDOWN";                
            case 3:
                return "CUMULATIVE";            	
            case 4:
            	return "MONTHLY";
            case 5:
                return "AMORTIZATION";               
            }
            return "";
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.results_one, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.menu_list:
	    	startActivity(new Intent(this, ResultsMulti.class));
		    return true;
	    case R.id.menu_reset:
	    	resetForm();
		    return true;		    
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void resetForm() {
		((EditText) findViewById(R.id.mortgage_name)).setText("");
		((EditText) findViewById(R.id.mortgage_purchase_price)).setText("");
		((EditText) findViewById(R.id.mortgage_downpayment)).setText("");
		((EditText) findViewById(R.id.mortgage_interest_rate)).setText("");
		((EditText) findViewById(R.id.mortgage_term_years)).setText("");
		((EditText) findViewById(R.id.mortgage_term_months)).setText("");
		((EditText) findViewById(R.id.mortgage_property_insurance)).setText("");
		((EditText) findViewById(R.id.mortgage_property_tax)).setText("");
		((EditText) findViewById(R.id.mortgage_pmi)).setText("");
	}
	
	public boolean onTouch(View v, MotionEvent event) {
	    switch (event.getAction()) {
	    case MotionEvent.ACTION_MOVE: 
	    	mPager.requestDisallowInterceptTouchEvent(true);
	        break;
	    case MotionEvent.ACTION_UP:
	    case MotionEvent.ACTION_CANCEL:
	    	mPager.requestDisallowInterceptTouchEvent(false);
	        break;
	    }
	    return true;
	}

	@Override
	public void onDataInput(String key, String value) {
		input_values.put(key, value);
	}

	@Override
	public void addMortgageToAccount(HashMap<String,String> data) {
		try {
			Mortgage mortgage = MortgageCMP.getInstance().getUniversalMortgageFactory().createMortgage(data);
			recalculate(mortgage);
			MortgageCMP.getInstance().getAccount().addMortgage(mortgage);
			MortgageCMP.getInstance().getAccount().setCurrentMortgage(mortgage);			
		} catch (MortgageFactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addMortgage(View view) {
		/* Executed when used clicks "Calculate" to add a new mortgage */

		HashMap<String,String> input = new HashMap<String,String>();

	    String[] mortgage_type_items = {
	    		MortgageNameConstants.FIXED_RATE_VARIABLE_PRINCIPAL, 
	    		MortgageNameConstants.FIXED_RATE_FIXED_PRINCIPAL};
		
		Spinner sp = (Spinner) findViewById(R.id.mortgage_type);
		
	    input.put("type", mortgage_type_items[sp.getSelectedItemPosition()]);
	    Log.d("Chosen mortgage type", mortgage_type);
	    
	    EditText debtName = (EditText) findViewById(R.id.mortgage_name);
	    String debtNameData = debtName.getText().toString();
	    if (Utils.alertIfEmpty(this, debtNameData, getResources().getString(R.string.mortgage_name_input))) {
	    	return;
	    }
	    input.put("name", debtNameData);

	    EditText debtAmount = (EditText) findViewById(R.id.mortgage_purchase_price);
	    String debtAmountData = debtAmount.getText().toString();
	    if (Utils.alertIfEmpty(this, debtAmountData, getResources().getString(R.string.mortgage_purchase_price_input))) {
	    	return;	    	
	    }	
	    input.put("purchase_price", debtAmountData);   
        
	    EditText downpayment = (EditText) findViewById(R.id.mortgage_downpayment);
	    String downpaymentData = downpayment.getText().toString();
	    if (downpaymentData.trim().length() == 0) { // fill default if not provided
	    	downpaymentData = "0";
	    }	
	    input.put("downpayment", downpaymentData);   
        
	    EditText interestRate = (EditText) findViewById(R.id.mortgage_interest_rate);
	    String interestRateData = interestRate.getText().toString();
	    if (Utils.alertIfEmpty(this, interestRateData, getResources().getString(R.string.mortgage_interest_rate_input))) {
	    	return;	    	
	    }	
	    input.put("interest_rate", interestRateData);   
        
	    EditText term_years = (EditText) findViewById(R.id.mortgage_term_years);
	    String termYearsData = term_years.getText().toString();
	    if (Utils.alertIfEmpty(this, termYearsData, getResources().getString(R.string.mortgage_term_input))) {
	    	return;	    	
	    }	
	    input.put("term_years", termYearsData);   

	    EditText term_months = (EditText) findViewById(R.id.mortgage_term_months);
	    String termMonthsData = term_months.getText().toString();
	    if (termMonthsData.trim().length() == 0) { // fill default if not provided
	    	termMonthsData = "0";
	    }	
	    input.put("term_months", termMonthsData);   
        
	    EditText propertyInsurance = (EditText) findViewById(R.id.mortgage_property_insurance);
	    String propertyInsuranceData = propertyInsurance.getText().toString();
	    if (propertyInsuranceData.trim().length() == 0) { // fill default if not provided
	    	propertyInsuranceData = "0";
	    }	
	    input.put("property_insurance", propertyInsuranceData);   
        
	    EditText propertyTax = (EditText) findViewById(R.id.mortgage_property_tax);
	    String propertyTaxData = propertyTax.getText().toString();
	    if (propertyTaxData.trim().length() == 0) { // fill default if not provided
	    	propertyTaxData = "0";
	    }	
	    input.put("property_tax", propertyTaxData);   

	    EditText pmi = (EditText) findViewById(R.id.mortgage_pmi);
	    String pmiData = pmi.getText().toString();
	    if (pmiData.trim().length() == 0) { // fill default if not provided
	    	pmiData = "0";
	    }		
	    input.put("pmi_rate", pmiData);
        
	    EditText closing_fees = (EditText) findViewById(R.id.mortgage_closing_fees);
	    String closingFeesData = closing_fees.getText().toString();
	    if (closingFeesData.trim().length() == 0) { // fill default if not provided
	    	closingFeesData = "0";
	    }		
	    input.put("closing_fees", closingFeesData);
	    
	    EditText extra_payment = (EditText) findViewById(R.id.mortgage_extra_payment);
	    String extraPaymentData = extra_payment.getText().toString();
	    if (extraPaymentData.trim().length() == 0) { // fill default if not provided
	    	extraPaymentData = "0";
	    }
	    input.put("extra_payment", extraPaymentData);

	    input.put("extra_payment_frequency", String.valueOf(extraPaymentFrequency));
	    
	    addMortgageToAccount(input);
	    
	    mPager.setAdapter(mAdapter);
	    mPager.setCurrentItem(1, true);
	}

	public void recalculate(Mortgage mortgage) {
        int month = MortgageCMP.getInstance().getSimulationStartMonth();
        int year = MortgageCMP.getInstance().getSimulationStartYear();	        	        

    	mortgage.initialize();

        int i = 0;
        for (i = 0; i < mortgage.getTotalTermMonths(); i++) {
        	mortgage.recalculate(i, year, month); 
            if (month == 11) {
                month = 0;
                year += 1;
            } else {
                month++;
            }
        }
	}

	@Override
	public boolean showModifyCloneButtons() {
		return showModifyCloneButtons;
	}

	public void showMortgageTypeMoreInfo(View view) {
		Dialog dialog = new Dialog(this, R.style.FullHeightDialog);			
	    dialog.setContentView(R.layout.help_mortgage_type_info);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();	
	}
	
	@Override
	public void replaceButtons(Mortgage mortgage) {

		LinearLayout buttons = (LinearLayout) findViewById(R.id.sumbit_mortgage_buttons);
		@SuppressWarnings("deprecation")
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
				                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 0.5f;		
		
        int px = Utils.px(this, 3);
        buttons.removeAllViews();

        Button clone = new Button(this);
        clone.setText("Clone");
        params.setMargins(0, 0, px, 0);            
        clone.setLayoutParams(params);
        clone.setTextColor(Color.WHITE);
        clone.setOnClickListener(clickCloneListener);
        clone.setBackgroundResource(R.drawable.button_confirm);                      
		buttons.addView(clone);

		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 0.5f;
		Button modify = new Button(this);
		modify.setText("Modify");
        params.setMargins(px, 0, 0, 0);
        modify.setLayoutParams(params);
        modify.setTextColor(Color.WHITE);
        modify.setTag(R.string.mortgage_to_modify, mortgage);
        modify.setOnClickListener(clickModifyListener);
        modify.setBackgroundResource(R.drawable.button_confirm);            
        buttons.addView(modify);
        
        buttons.refreshDrawableState();
	}

	public void toggleVisibilityFees(View view) {
		LinearLayout v = (LinearLayout) findViewById(R.id.advanced_input);
	    TextView tv = (TextView) findViewById(R.id.advanced_input_text);
		if (v.getVisibility() == View.GONE) {
		    v.setVisibility(View.VISIBLE);
		    tv.setText(R.string.advanced_input_visible);
		} else {
			v.setVisibility(View.GONE);
		    tv.setText(R.string.advanced_input_invisible);
		}
	}

	public void toggleVisibilityExtraPayment(View view) {
		LinearLayout v = (LinearLayout) findViewById(R.id.advanced_extra_payments_input);
	    TextView tv = (TextView) findViewById(R.id.advanced_extra_payments_input_text);
		if (v.getVisibility() == View.GONE) {
		    v.setVisibility(View.VISIBLE);
		    tv.setText(R.string.advanced_extra_payments_input_visible);
		} else {
			v.setVisibility(View.GONE);
		    tv.setText(R.string.advanced_extra_payments_input_invisible);
		}
	}

	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();

	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radioMonthly:
	            if (checked) {
	                Toast.makeText(this, "monthly", Toast.LENGTH_SHORT).show();
	                extraPaymentFrequency = 1;
	            }
	            break;
	        case R.id.radioYearly:
	            if (checked) {
	            	Toast.makeText(this, "yearly", Toast.LENGTH_SHORT).show();
	                extraPaymentFrequency = 12;
	            }
	            break;
	    }
	}

	public void setRadioButtonClicked(int id) {
	    // Is the button now checked?

	    // Check which radio button was clicked
	    switch(id) {
	        case 1:
	            RadioButton rbm = (RadioButton) findViewById(R.id.radioMonthly);
	            rbm.setChecked(true);
	            break;
	        case 12:
	        	RadioButton rby = (RadioButton) findViewById(R.id.radioYearly);
	            rby.setChecked(true);
	            break;
	    }
	}
}