package com.codelemma.mortgagecmp;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.mortgagecmp.accounting.Mortgage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsOne extends SherlockFragmentActivity  
                        implements FrgInputOne.OnDataInputListener {

	private MortgageCMP appState;
	static final int NUM_ITEMS = 6;
    HashMap<String,String> input_values = new HashMap<String,String>();
    private boolean showModifyCloneButtons = false;
    private int extraPaymentFrequency = 1;
    
    MyAdapter mAdapter;
    ViewPager mPager;
    
    private OnClickListener clickModifyListener = new OnClickListener() {
    	@Override
	    public void onClick(View v) {
	        final Mortgage mortgage = (Mortgage) v.getTag(R.string.mortgage_to_modify);
	        appState.getAccount().removeMortgage(mortgage);
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
        setContentView(R.layout.frg_pager_one);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		appState = MortgageCMP.getInstance();
		setupCurrentDate();
	    if (appState.getAccount() == null) {
	    	appState.setAccount();
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
		appState.setSimulationStartYear(c.get(Calendar.YEAR));		        
		appState.setSimulationStartMonth(c.get(Calendar.MONTH));	
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
        		return new FrgChartOne();
        	} else if (position == 3) {
        		return new FrgChartTwo();
        	} else if (position == 4) {
        		return new FrgChartThree();
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
                return "CUMULATIVE";
            case 3:
                return "MONTHLY";
            case 4:
                return "LOAN BREAKDOWN";                
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
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;		
	    case R.id.menu_add:
	    	Intent intent = new Intent(this, ResultsOne.class);
	    	startActivity(intent);
		    return true;
	    case R.id.menu_list:
	    	Intent intent2 = new Intent(this, ResultsMulti.class);
	    	startActivity(intent2);
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
	public Mortgage addMortgageToAccount(HashMap<String,String> data) {
	  	String name = data.get("mortgage_name");
    	BigDecimal price = new BigDecimal(data.get("mortgage_purchase_price"));
    	BigDecimal downpayment = new BigDecimal(data.get("mortgage_downpayment"));
    	BigDecimal interest_rate = new BigDecimal(data.get("mortgage_interest_rate"));
    	int term_years = Integer.parseInt((data.get("mortgage_term_years")));
    	int term_months = Integer.parseInt((data.get("mortgage_term_months")));
    	BigDecimal property_insurance = new BigDecimal(data.get("mortgage_property_insurance"));
    	BigDecimal property_tax = new BigDecimal(data.get("mortgage_property_tax"));
    	BigDecimal pmi = new BigDecimal(data.get("mortgage_pmi"));
    	BigDecimal extra_payment = new BigDecimal(data.get("mortgage_extra_payment"));
    	int extra_payment_frequency = Integer.parseInt(data.get("mortgage_extra_payment_frequency"));

		Mortgage mortgage = new Mortgage (
				 name, 
	    		 price,
	    		 downpayment,
	    		 interest_rate,
	    		 term_years,
	    		 term_months,
	    		 property_insurance,
	    		 property_tax,
	    		 pmi,
	    		 extra_payment,
	    		 extra_payment_frequency);
		
		recalculate(mortgage);
		appState.getAccount().addMortgage(mortgage);
	    appState.setCurrentMortgage(mortgage);
	    
	    return mortgage;
	}
		
	public void addMortgage(View view) {
		/* Executed when used clicks "Calculate" to add a new mortgage */
	    
		HashMap<String,String> input = new HashMap<String,String>();

	    EditText debtName = (EditText) findViewById(R.id.mortgage_name);
	    String debtNameData = debtName.getText().toString();
	    if (Utils.alertIfEmpty(this, debtNameData, getResources().getString(R.string.mortgage_name_input))) {
	    	return;	    	
	    }
	    input.put("mortgage_name", debtNameData);

	    EditText debtAmount = (EditText) findViewById(R.id.mortgage_purchase_price);
	    String debtAmountData = debtAmount.getText().toString();
	    if (Utils.alertIfEmpty(this, debtAmountData, getResources().getString(R.string.mortgage_purchase_price_input))) {
	    	return;	    	
	    }	
	    input.put("mortgage_purchase_price", debtAmountData);   
        
	    EditText downpayment = (EditText) findViewById(R.id.mortgage_downpayment);
	    String downpaymentData = downpayment.getText().toString();
	    if (downpaymentData.trim().length() == 0) { // fill default if not provided
	    	downpaymentData = "0";
	    }	
	    input.put("mortgage_downpayment", downpaymentData);   
        
	    EditText interestRate = (EditText) findViewById(R.id.mortgage_interest_rate);
	    String interestRateData = interestRate.getText().toString();
	    if (Utils.alertIfEmpty(this, interestRateData, getResources().getString(R.string.mortgage_interest_rate_input))) {
	    	return;	    	
	    }	
	    input.put("mortgage_interest_rate", interestRateData);   
        
	    EditText term_years = (EditText) findViewById(R.id.mortgage_term_years);
	    String termYearsData = term_years.getText().toString();
	    if (Utils.alertIfEmpty(this, termYearsData, getResources().getString(R.string.mortgage_term_input))) {
	    	return;	    	
	    }	
	    input.put("mortgage_term_years", termYearsData);   

	    EditText term_months = (EditText) findViewById(R.id.mortgage_term_months);
	    String termMonthsData = term_months.getText().toString();
	    if (termMonthsData.trim().length() == 0) { // fill default if not provided
	    	termMonthsData = "0";
	    }	
	    input.put("mortgage_term_months", termMonthsData);   
        
	    EditText propertyInsurance = (EditText) findViewById(R.id.mortgage_property_insurance);
	    String propertyInsuranceData = propertyInsurance.getText().toString();
	    if (propertyInsuranceData.trim().length() == 0) { // fill default if not provided
	    	propertyInsuranceData = "0";
	    }	
	    input.put("mortgage_property_insurance", propertyInsuranceData);   
        
	    EditText propertyTax = (EditText) findViewById(R.id.mortgage_property_tax);
	    String propertyTaxData = propertyTax.getText().toString();
	    if (propertyTaxData.trim().length() == 0) { // fill default if not provided
	    	propertyTaxData = "0";
	    }	
	    input.put("mortgage_property_tax", propertyTaxData);   

	    EditText pmi = (EditText) findViewById(R.id.mortgage_pmi);
	    String pmiData = pmi.getText().toString();
	    if (pmiData.trim().length() == 0) { // fill default if not provided
	    	pmiData = "0";
	    }		
	    input.put("mortgage_pmi", pmiData);
        
	    EditText extra_payment = (EditText) findViewById(R.id.mortgage_overpayment);
	    String extraPaymentData = extra_payment.getText().toString();
	    if (extraPaymentData.trim().length() == 0) { // fill default if not provided
	    	extraPaymentData = "0";
	    }
	    input.put("mortgage_extra_payment", extraPaymentData);

	    input.put("mortgage_extra_payment_frequency", String.valueOf(extraPaymentFrequency));
	    
	    input.put("replace_submit_buttons", "yes");
	    //Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.frg_input);

	    Mortgage mortgage = addMortgageToAccount(input);
	    
	    mPager.setAdapter(mAdapter);
	    mPager.setCurrentItem(1, true);
	    replaceButtons(mortgage);
	}

	public void recalculate(Mortgage mortgage) {   
        int month = appState.getSimulationStartMonth();
        int year = appState.getSimulationStartYear();	        	        

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

	@Override
	public void replaceButtons(Mortgage mortgage) {

		LinearLayout buttons = (LinearLayout) findViewById(R.id.sumbit_mortgage_buttons);
		@SuppressWarnings("deprecation")
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
				                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 0.5f;		
		
        int px = Utils.px(this, 5);
        buttons.removeAllViews();

        Button clone = new Button(this);
        clone.setText("CLONE");
        params.setMargins(px, 0, 0, 0);            
        clone.setLayoutParams(params);
        clone.setTextColor(Color.WHITE);
        clone.setOnClickListener(clickCloneListener);
        clone.setBackgroundResource(R.drawable.button_confirm);                      
		buttons.addView(clone);

		Button modify = new Button(this);
		modify.setText("MODIFY");
        params.setMargins(0, 0, px, 0);
        modify.setLayoutParams(params);
        modify.setTextColor(Color.WHITE);
        modify.setTag(R.string.mortgage_to_modify, mortgage);
        modify.setOnClickListener(clickModifyListener);
        modify.setBackgroundResource(R.drawable.button_confirm);            
        buttons.addView(modify);
        
        buttons.refreshDrawableState();
	}

	public void toggleVisibility(View view) {
		LinearLayout v = (LinearLayout) findViewById(R.id.advanced_input);
	    TextView tv = (TextView) view.findViewById(R.id.advanced_input_text);
		if (v.getVisibility() == View.GONE) {
		    v.setVisibility(View.VISIBLE);
		    tv.setText(R.string.advanced_input_visible);
		} else {
			v.setVisibility(View.GONE);
		    tv.setText(R.string.advanced_input_invisible);
		}
	}

	public void toggleVisibilityExtraPayments(View view) {
		LinearLayout v = (LinearLayout) findViewById(R.id.advanced_extra_payments_input);
	    TextView tv = (TextView) view.findViewById(R.id.advanced_extra_payments_input_text);
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
	                Toast.makeText(this, String.valueOf(R.id.radioMonthly), Toast.LENGTH_SHORT).show();
	                extraPaymentFrequency = 1;
	            }
	            break;
	        case R.id.radioYearly:
	            if (checked) {
	            	Toast.makeText(this, String.valueOf(R.id.radioYearly), Toast.LENGTH_SHORT).show();
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