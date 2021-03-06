package com.codelemma.mortgagecmp;

import java.util.ArrayList;
import java.util.Calendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codelemma.mortgagecmp.accounting.Mortgage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ResultsMulti extends SherlockFragmentActivity  
                          implements FrgInputMulti.OnDataInputListener {

	static final int NUM_ITEMS = 5;
	private final int NUM_MORTGAGES_TO_COMPARE = 6;
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
	    	MortgageCMP.getInstance().getAccount().setCurrentMortgage(null);
	    	Intent intent = new Intent(this, ResultsOne.class);
	    	startActivity(intent);
		    return true;
	    case R.id.menu_help:
	    	showInfoDialog(R.layout.help_general);
		    return true;
	    case R.id.menu_about:
	    	showInfoDialog(R.layout.about);
		    return true;
        case R.id.menu_delete_selected_mortgages:
        	removeMortgages("selected");        	
            return true;
        case R.id.menu_delete_all_mortgages:
    	    removeMortgages("all");
	        return true;	        
        }	
		return super.onOptionsItemSelected(item);
	}

	public void showInfoDialog(int r_id) {
		Dialog dialog = new Dialog(this, R.style.FullHeightDialog);			
	    dialog.setContentView(r_id);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
	
	private void removeMortgages(final String which) {
		new AlertDialog.Builder(ResultsMulti.this)
        .setTitle(R.string.delete_all)
        .setMessage(R.string.delete_question)            
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
        	   if (which.equals("all")) {
        		   removeAllMortgages();
        	   } else if (which.equals("selected")) {
        		   removeSelectedMortgages();
        	   }
           }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
        	   dialog.cancel();
           }
       })
      .show();
	}

	private void removeSelectedMortgages() {
		MortgageCMP.getInstance().getAccount().setCurrentMortgage(null);
		for (int id : items_to_compare) {
	        MortgageCMP.getInstance().getAccount().removeMortgage(id);
	        LinearLayout subLayout = (LinearLayout) findViewById(1000+id);
	        subLayout.setVisibility(View.GONE);
		}
	    MortgageCMP.getInstance().getAccount().clearComparisonList();
	    MortgageCMP.getInstance().getAccount().setLongestLoanTerm(0);
	    MortgageCMP.getInstance().getAccount().setLongestMortgage(null);
	    items_to_compare.clear();
 	    // remove views from currently active Summary fragment
	    ScrollView ll = (ScrollView) findViewById(R.id.frg_summary_multi);
		ll.removeAllViews();
		
		// remove from storage, too
		MortgageCMP.getInstance().saveAccount();
	}

	private void removeAllMortgages() {
		MortgageCMP.getInstance().getAccount().removeMortgages();
	    
 	    // remove views from currently active Summary fragment and from InputMulti fragment
	    ScrollView ll = (ScrollView) findViewById(R.id.frg_summary_multi);
		ll.removeAllViews();
        LinearLayout subLayout = (LinearLayout) findViewById(R.id.mortgage_listing);
        subLayout.setVisibility(View.GONE);
        // remove from storage, too
        items_to_compare.clear();
     	MortgageCMP.getInstance().saveAccount();
	}
	
	public void compare(View view) {
		int items_len = items_to_compare.size();
		if (items_len < 1 && MortgageCMP.getInstance().getAccount().getMortgagesSize() > 0) {
	        new AlertDialog.Builder(ResultsMulti.this)
            .setTitle(R.string.nothing_to_compare)
            .setMessage("Please, check mortgages to compare.")
            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   dialog.cancel();
               }
           })
          .show();
		} else if (items_len < 1 && MortgageCMP.getInstance().getAccount().getMortgagesSize() == 0) {
	        new AlertDialog.Builder(ResultsMulti.this)
            .setTitle(R.string.nothing_to_compare)
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
		} else if (items_len > NUM_MORTGAGES_TO_COMPARE) {
			new AlertDialog.Builder(ResultsMulti.this)
            .setTitle("Too many items")
            .setMessage("Max number of mortgages to compare is "+NUM_MORTGAGES_TO_COMPARE+". Please, uncheck some of the items.")                
            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   dialog.cancel();
               }
           })
          .show();			
	    } else {
	    	// create a list of mortgages to compare in Account
	    	MortgageCMP.getInstance().getAccount().clearComparisonList();
	    	int longestTerm = 0;
			for (int id : items_to_compare) {
				Mortgage mortgage = MortgageCMP.getInstance().getAccount().getMortgageById(id);
				if (mortgage != null) {
					MortgageCMP.getInstance().getAccount().addToComparisonList(mortgage);
					if (mortgage.getTotalTermMonths() > longestTerm) {
						longestTerm = mortgage.getTotalTermMonths();
						MortgageCMP.getInstance().getAccount().setLongestLoanTerm(mortgage.getTotalTermMonths());
						MortgageCMP.getInstance().getAccount().setLongestMortgage(mortgage);
					}
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
        MortgageCMP.getInstance().saveAccount();
    }
}