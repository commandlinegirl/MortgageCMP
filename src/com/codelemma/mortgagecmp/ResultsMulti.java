package com.codelemma.mortgagecmp;

import java.util.ArrayList;
import java.util.Calendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.mortgagecmp.accounting.Mortgage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class ResultsMulti extends SherlockFragmentActivity  
                          implements FrgInputMulti.OnDataInputListener {

	static final int NUM_ITEMS = 5;
	private ArrayList<Integer> items_to_compare;

    MyAdapter mAdapter;
    ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_pager_multi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
	    if (MortgageCMP.getInstance().getAccount() == null) {
	    	setupCurrentDate();
	    	MortgageCMP.getInstance().setAccount();
	    	for (Mortgage mort : MortgageCMP.getInstance().getAccount().getMortgages()) {
		    	recalculate(mort);
		    }
	    }
        items_to_compare = new ArrayList<Integer>(MortgageCMP.getInstance().getAccount().getMortgagesSize());
        MortgageCMP.getInstance().getAccount().clearComparisonList();
	}

	private void setupCurrentDate() {
	    final Calendar c = Calendar.getInstance();	        
	    MortgageCMP.getInstance().setSimulationStartYear(c.get(Calendar.YEAR));		        
	    MortgageCMP.getInstance().setSimulationStartMonth(c.get(Calendar.MONTH));	
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
        		return new FrgInputMulti();
        	} else if (position == 1) {
                return new FrgSummaryMulti();        		
        	} else if (position == 2) {
        		return new FrgLoanBreakdownMulti();
        	} else if (position == 3) {
        		return new FrgChartMonthlyMulti();
        	} else if (position == 4) {
        		return new FrgChartCumulativeMulti();        		
        	} else {
                return new FrgInputMulti();       		
        	}
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
            case 0:
                return "MORTGAGE LIST";
            case 1:
                return "SUMMARY";
            case 2:
                return "LOAN BREAKDOWN";
            case 3:
                return "MONTHLY";
            case 4:
                return "CUMULATIVE";
            }
            return "";
        }
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.results_multi, menu);
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
	    	MortgageCMP.getInstance().getAccount().setCurrentMortgage(null);
	    	startActivity(intent);
		    return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void compare(View view) {
		int items_len = items_to_compare.size();
		if (items_len < 1 && MortgageCMP.getInstance().getAccount().getMortgagesSize() > 0) {
	        new AlertDialog.Builder(ResultsMulti.this)
            .setTitle("Nothing to compare")
            .setMessage("Please, check mortgages to compare.")
            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   dialog.cancel();
               }
           })
          .show();
		} else if (items_len < 1 && MortgageCMP.getInstance().getAccount().getMortgagesSize() == 0) {
	        new AlertDialog.Builder(ResultsMulti.this)
            .setTitle("Nothing to compare")
            .setMessage("Please, add mortgages to compare.")                
            .setPositiveButton("Add mortgage", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   Intent intent = new Intent(ResultsMulti.this, ResultsOne.class);		   				   
   			       startActivity(intent);
   			       finish();
               }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   dialog.cancel();
               }
           })
          .show();
		} else if (items_len > 10) {
			new AlertDialog.Builder(ResultsMulti.this)
            .setTitle("Too many items")
            .setMessage("Max number of mortgages to compare is 10. Please, uncheck some of the items.")                
            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   dialog.cancel();
               }
           })
          .show();
	    } else {
	    	// create a list of mortgages to compare in Account
	    	MortgageCMP.getInstance().getAccount().clearComparisonList();
			for (int id : items_to_compare) {
				Mortgage mortgage = MortgageCMP.getInstance().getAccount().getMortgageById(id);
				if (mortgage != null) {
					MortgageCMP.getInstance().getAccount().addToComparisonList(mortgage);
				}
			}
		    mPager.setAdapter(mAdapter); //
		    mPager.setCurrentItem(1, true); //TODO: change fragment programmatically to FrgMultiSummary
		}
	}

	@Override
	public void onAddItemToCompare(int item_id) {
		for (Integer i : items_to_compare) {
			if (i == item_id) {
				return;
			}
		}
		items_to_compare.add(item_id);
		Log.d("items_to_compare (ADD)", String.valueOf(items_to_compare));
	}

	@Override
	public void onRemoveItemToCompare(int item_id) {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (Integer i : items_to_compare) {
			if (item_id != i) {
				temp.add(i);
			}
		}
		items_to_compare = temp;
	}
	

    @Override
    public void onPause() {
    	super.onPause();
    	Log.d("ResultsMulti.onPause", "called");
        MortgageCMP.getInstance().saveAccount();
    }
}