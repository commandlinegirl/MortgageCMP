package com.codelemma.mortgagecmp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.MortgageNameConstants;
import com.codelemma.mortgagecmp.accounting.MortgageFactory.MortgageFactoryException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

	static final int NUM_ITEMS = 6; // number of slides
    private final int MAX_MORTGAGE_NUMBER = 30;	
    private int extraPaymentFrequency = 12; // 12 means yearly
    private int adjustment_period;
    private int months_between_adjustments;
    private int arm_type = 71;

    MyAdapter mAdapter;
    ViewPager mPager;

    private OnClickListener clickModifyListener = new OnClickListener() {
    	@Override
	    public void onClick(View v) {
	        final Mortgage mortgage = (Mortgage) v.getTag(R.string.mortgage_to_modify);
	        MortgageCMP.getInstance().getAccount().removeMortgage(mortgage);
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
		setupCurrentDate();
		showStartPopup();
	    if (MortgageCMP.getInstance().getAccount() == null) {
	    	MortgageCMP.getInstance().setAccount();
	    	for (Mortgage mort : MortgageCMP.getInstance().getAccount().getMortgages()) {
		    	recalculate(mort);
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
        MortgageCMP.getInstance().saveAccount();
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
	    	Intent intent = new Intent(this, ResultsMulti.class);
	    	startActivity(intent);
		    return true;
	    case R.id.menu_add:
	    	MortgageCMP.getInstance().getAccount().setCurrentMortgage(null);
	    	mPager.setAdapter(mAdapter);
		    mPager.setCurrentItem(0, true);
		    return true;		    
	    case R.id.menu_reset:
	    	resetForm();
		    return true;
	    case R.id.menu_default:
	    	setDefaultFormValues();
		    return true;
	    case R.id.menu_help:
	    	showInfoDialog(R.layout.help_general);
		    return true;			    
	    case R.id.menu_about:
	    	showInfoDialog(R.layout.about);
		    return true;			    
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void addMortgageToAccount(Map<String,String> data) {
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

		Map<String,String> input = new HashMap<String,String>();

		Spinner sp = (Spinner) findViewById(R.id.mortgage_type);
		try {
	        input.put("type", MortgageNameConstants.getTypeNameByIndex(sp.getSelectedItemPosition()));
		} catch (IndexOutOfBoundsException ioobe) {
			return;
		}
		
		if (sp.getSelectedItemPosition() == 2) {

		    input.put("arm_type", String.valueOf(arm_type));
			
		    EditText adjustment_period = (EditText) findViewById(R.id.adjustment_period);
		    String adjustment_periodData = adjustment_period.getText().toString();
		    if (Utils.alertIfEmpty(this, adjustment_periodData, getResources().getString(R.string.adjustment_period_input))) {
		    	return;
		    }
		    input.put("adjustment_period", adjustment_periodData);
		    
		    EditText months_between_adjustments = (EditText) findViewById(R.id.months_between_adjustments);
		    String months_between_adjustmentsData = months_between_adjustments.getText().toString();
		    if (Utils.alertIfEmpty(this, months_between_adjustmentsData, getResources().getString(R.string.months_between_adjustments_input))) {
		    	return;
		    }
		    input.put("months_between_adjustments", months_between_adjustmentsData);
		    
		    EditText max_single_rate_adjustment = (EditText) findViewById(R.id.max_single_rate_adjustment);
		    String max_single_rate_adjustmentData = max_single_rate_adjustment.getText().toString();
		    if (Utils.alertIfEmpty(this, max_single_rate_adjustmentData, getResources().getString(R.string.max_single_rate_adjustment_input))) {
		    	return;
		    }
		    input.put("max_single_rate_adjustment", max_single_rate_adjustmentData);
		    
		    EditText total_interest_cap = (EditText) findViewById(R.id.total_interest_cap);
		    String total_interest_capData = total_interest_cap.getText().toString();
		    if (Utils.alertIfEmpty(this, total_interest_capData, getResources().getString(R.string.total_interest_cap_input))) {
		    	return;
		    }
		    input.put("total_interest_cap", total_interest_capData);
		}
	    
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
	    if (Utils.alertIfNotInBounds(this, interestRateData, 0, 100, getResources().getString(R.string.mortgage_interest_rate_input))) {
	    	return;
	    }
	    input.put("interest_rate", interestRateData);   

	    EditText term_years = (EditText) findViewById(R.id.mortgage_term_years);
	    String termYearsData = term_years.getText().toString();
	    if (Utils.alertIfEmpty(this, termYearsData, getResources().getString(R.string.mortgage_term_years_input))) {
	    	return;	    	
	    }
	    if (Utils.alertIfIntNotInBounds(this, termYearsData, 0, 100, getResources().getString(R.string.mortgage_term_years_input))) {
	    	return;
	    }
	    input.put("term_years", termYearsData);   

	    EditText term_months = (EditText) findViewById(R.id.mortgage_term_months);
	    String termMonthsData = term_months.getText().toString();
	    if (termMonthsData.trim().length() == 0) { // fill default if not provided
	    	termMonthsData = "0";
	    }
	    if (Utils.alertIfIntNotInBounds(this, termMonthsData, 0, 1000, getResources().getString(R.string.mortgage_term_months_input))) {
	    	return;
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
	    if (Utils.alertIfNotInBounds(this, pmiData, 0, 100, getResources().getString(R.string.mortgage_pmi_input))) {
	    	return;
	    }
	    input.put("pmi_rate", pmiData);
        
	    EditText closing_fees = (EditText) findViewById(R.id.mortgage_closing_fees);
	    String closingFeesData = closing_fees.getText().toString();
	    if (closingFeesData.trim().length() == 0) { // fill default if not provided
	    	closingFeesData = "0";
	    }
	    if (Utils.alertIfNotInBounds(this, closingFeesData, 0, 1000000, getResources().getString(R.string.mortgage_closing_fees_input))) {
	    	return;
	    }
	    input.put("closing_fees", closingFeesData);
	    
	    EditText extra_payment = (EditText) findViewById(R.id.mortgage_extra_payment);
	    String extraPaymentData = extra_payment.getText().toString();
	    if (extraPaymentData.trim().length() == 0) { // fill default if not provided
	    	extraPaymentData = "0";
	    }
	    input.put("extra_payment", extraPaymentData);

	    input.put("extra_payment_frequency", String.valueOf(extraPaymentFrequency));
	    
	    if (MortgageCMP.getInstance().getAccount().getMortgagesSize() >= MAX_MORTGAGE_NUMBER) {
			new AlertDialog.Builder(this)
            .setTitle(R.string.too_many_mortgages)
            .setMessage("Max number of mortgages is "+MAX_MORTGAGE_NUMBER+". Please, remove some of the mortgages.")                
            .setPositiveButton(R.string.remove_all, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   removeAllMortgages();
               }
            })
            .setNegativeButton(R.string.remove_selected, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
              	   Intent intent = new Intent(ResultsOne.this, ResultsMulti.class);
               	   startActivity(intent);
               }
            })
            .show();
			return;
	    }
	    
	    // Add

	    addMortgageToAccount(input);
	    
	    mPager.setAdapter(mAdapter);
	    mPager.setCurrentItem(1, true);
	}

	public void removeAllMortgages() {
		new AlertDialog.Builder(ResultsOne.this)
        .setTitle(R.string.delete_all)
        .setMessage(R.string.delete_question)                
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
       	       MortgageCMP.getInstance().getAccount().removeMortgages();
               MortgageCMP.getInstance().saveAccount();
               Toast.makeText(ResultsOne.this, R.string.mortgage_deleted_toast, Toast.LENGTH_SHORT).show();
           }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
        	   dialog.cancel();
           }
       })
      .show();
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

	public void showMortgageTypeMoreInfo(View view) {
		showInfoDialog(R.layout.help_mortgage_type_info);
	}

	public void showInfoDialog(int r_id) {
		Dialog dialog = new Dialog(this, R.style.FullHeightDialog);			
	    dialog.setContentView(r_id);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void replaceButtons(Mortgage mortgage) {

		if (mortgage == null) {
			return;
		}
		
		LinearLayout buttons = (LinearLayout) findViewById(R.id.sumbit_mortgage_buttons);
        buttons.removeAllViews();

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
				                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 0.5f;		
		
        int px = Utils.px(this, 3);

        Button clone = new Button(this);
        clone.setText(R.string.clone);
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
		modify.setText(R.string.modify);
        params.setMargins(px, 0, 0, 0);
        modify.setLayoutParams(params);
        modify.setTextColor(Color.WHITE);
        modify.setTag(R.string.mortgage_to_modify, mortgage);
        modify.setOnClickListener(clickModifyListener);
        modify.setBackgroundResource(R.drawable.button_confirm);            
        buttons.addView(modify);
        
        ((TextView) findViewById(R.id.save_as_new_info)).setText(R.string.save_as_new);
        
        buttons.refreshDrawableState();
	}

	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();

	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radioMonthly:
	            if (checked) {
	                extraPaymentFrequency = 1;
	            }
	            break;
	        case R.id.radioYearly:
	            if (checked) {
	                extraPaymentFrequency = 12;
	            }
	            break;
	    }
	}

	public void setRadioButtonClicked(int id) {
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
	        default:
	        	rby = (RadioButton) findViewById(R.id.radioYearly);
	            rby.setChecked(true);
	            break;	        	
	    }
	}
	
	public void onARMTypeRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();

	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.arm71:
	            if (checked) {
	                arm_type = 71;
	                setARMTypeEditText(84);
	            }
	            break;
	        case R.id.arm51:
	            if (checked) {
	                arm_type = 51;
	                setARMTypeEditText(60);
	            }	                
	            break;
	        case R.id.arm31:
	            if (checked) {
	                arm_type = 31;
	                setARMTypeEditText(36);
	            }	                
	            break;
	        case R.id.arm_other:
	            if (checked) {
	                arm_type = 0;
	                EditText edit_text = (EditText) findViewById(R.id.adjustment_period);
	                edit_text.setEnabled(true);
	                edit_text = (EditText) findViewById(R.id.months_between_adjustments);
	                edit_text.setEnabled(true);
	            }
	            break;
	    }
	}

	private void setARMTypeEditText(int arm_type) {
        EditText edit_text = (EditText) findViewById(R.id.adjustment_period);
        edit_text.setText(String.valueOf(arm_type));
        edit_text.setEnabled(false);
        edit_text = (EditText) findViewById(R.id.months_between_adjustments);
        edit_text.setText("12");
        edit_text.setEnabled(false);
	}

	public void setARMTypeRadioButtonClicked(int id) {
	    switch(id) {
        case 71:
            RadioButton rb71 = (RadioButton) findViewById(R.id.arm71);
            rb71.setChecked(true);
            break;
        case 51:
        	RadioButton rb51 = (RadioButton) findViewById(R.id.arm51);
        	rb51.setChecked(true);
            break;
        case 31:
        	RadioButton rb31 = (RadioButton) findViewById(R.id.arm31);
        	rb31.setChecked(true);
            break;
        default:
        	RadioButton rb_other = (RadioButton) findViewById(R.id.arm_other);
        	rb_other.setChecked(true);
            break;
	    }
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

    private void showStartPopup() {   
        if (MortgageCMP.getInstance().showStartupWindow() == 1) {
            Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
            dialog.setContentView(R.layout.start_popup);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            MortgageCMP.getInstance().setShowStartupWindow(0); //TODO: set to 0!!
        }   
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
		((EditText) findViewById(R.id.mortgage_closing_fees)).setText("");
		((EditText) findViewById(R.id.mortgage_extra_payment)).setText("");
	}

	public void setDefaultFormValues() {
		((EditText) findViewById(R.id.mortgage_name)).setText("Mortgage 1");
		((EditText) findViewById(R.id.mortgage_purchase_price)).setText("250000");
		((EditText) findViewById(R.id.mortgage_downpayment)).setText("20000");
		((EditText) findViewById(R.id.mortgage_interest_rate)).setText("6");
		((EditText) findViewById(R.id.mortgage_term_years)).setText("30");
		((EditText) findViewById(R.id.mortgage_term_months)).setText("");
		((EditText) findViewById(R.id.mortgage_property_insurance)).setText("0");
		((EditText) findViewById(R.id.mortgage_property_tax)).setText("0");
		((EditText) findViewById(R.id.mortgage_pmi)).setText("0");
		((EditText) findViewById(R.id.mortgage_closing_fees)).setText("0");
		((EditText) findViewById(R.id.mortgage_extra_payment)).setText("0");
	}
}