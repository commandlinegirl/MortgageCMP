package com.codelemma.mortgagecmp;

import java.util.ArrayList;

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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class ResultsMulti extends SherlockFragmentActivity  
                          implements FrgInputMulti.OnDataInputListener {

	private MortgageCMP appState;
	static final int NUM_ITEMS = 5;
	private ArrayList<Integer> items_to_compare;

    MyAdapter mAdapter;
    ViewPager mPager;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_pager_multi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		appState = MortgageCMP.getInstance();
		items_to_compare = new ArrayList<Integer>(appState.getAccount().getMortgagesSize());
		mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        
        //int[] items = (int[]) getIntent().getIntArrayExtra("items_to_compare");
        //account.setMortgagesToCompare(items);
        //for (int i = 0; i < items.length; i++) {
        //	Log.d("i: "+i, account.getMortgageById(items[i]).getName());
        //}
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
        		return new FrgChartMultiMonthly();
        	} else if (position == 3) {
        		return new FrgChartMultiCumulative();        		
        	} else if (position == 4) {
        		return new FrgTableMulti();
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
                return "MONTHLY";
            case 3:
                return "CUMULATIVE";                
            case 4:
                return "AMORTIZATION";
            }
            return "";
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
	    case R.id.menu_add:
	    	Intent intent = new Intent(this, ResultsOne.class);
	    	startActivity(intent);
		    return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void compare(View view) {
		int items_len = items_to_compare.size();
		if (items_len < 1 && appState.getAccount().getMortgagesSize() > 0) {
	        new AlertDialog.Builder(ResultsMulti.this)
            .setTitle("Nothing to compare")
            .setMessage("Please, check mortgages to compare.")
            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   dialog.cancel();
               }
           })
          .show();
		} else if (items_len < 1 && appState.getAccount().getMortgagesSize() == 0) {
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
	    } else {
	    	// create a list of mortgages to compare in Account
	    	appState.getAccount().clearComparisonList();
			for (int id : items_to_compare) {
				Mortgage mortgage = appState.getAccount().getMortgageById(id);
				if (mortgage != null) {
				    appState.getAccount().addToComparisonList(mortgage);
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
		Log.d("items_to_compare (REMOVE)", String.valueOf(items_to_compare));
	}
}