package com.codelemma.mortgagecmp;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.mortgagecmp.accounting.Account;
import com.codelemma.mortgagecmp.accounting.HistoryMortgage;
import com.codelemma.mortgagecmp.accounting.Money;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.PlotVisitor;

public class Plotter implements PlotVisitor {

	private SherlockFragmentActivity frgActivity;
    private int currentColor = 0;
    private String[] colors = {"#FFFF9900", "#ff6600CC", "#ff5BC236", "#ff8C489F", 
    		                   "#ff9CAA9C", "#ffffff00", "#ff66CCFF",  "#ffcccccc"};    
    private String[] dates;

	public Plotter(SherlockFragmentActivity sherlockFragmentActivity, String[] dates) {
		this.frgActivity = sherlockFragmentActivity;
		this.dates = dates;
	}

	private void plot(ArrayList<BigDecimal[]> value_lists, 
			ArrayList<String> titles,
			int numberOfMonths, 
			LinearLayout chart_layout) {		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();		  
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        currentColor = 0; 

	    double maxY = 0;
	    double minY = 0;

	    boolean first = true;
	    int j = 0;
		for (BigDecimal[] values: value_lists) {
			String name = titles.get(j);
        					
            TimeSeries series = getSeries(values, 
                    numberOfMonths, 
                    name);
            dataset.addSeries(series);
    
            if (first) {
                minY = series.getMinY();
                maxY = series.getMaxY();
            }   
            first = false;
            maxY = Math.max(series.getMaxY(), maxY);
            minY = Math.min(series.getMinY(), minY);                        
               
            XYSeriesRenderer renderer = getSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);
            incrementCurrentColor();
            j++;
        }   				    
		/* Here set left and right margin for the chart based on max and min values from each series */

    	int rightMargin = Utils.px(frgActivity, 22);
	    int leftMargin = Utils.px(frgActivity, 17);
	    int labelMaxYLen = String.valueOf(maxY).length();
	    int labelMinYLen = String.valueOf(minY).length();
	    if (labelMaxYLen >=  labelMinYLen) {
		    leftMargin += Utils.px(frgActivity, 4) * labelMaxYLen;
	    } else {
	    	leftMargin += Utils.px(frgActivity, 4) * labelMinYLen;
	    }
	    
    	int screenWidthDips = Utils.dip(frgActivity, frgActivity.getResources().getDisplayMetrics().widthPixels);
        int chartSize = screenWidthDips - leftMargin - rightMargin;
        int numOfLabels = Math.round(chartSize / 30);
        int step = Math.max(1, numberOfMonths / numOfLabels);
        
		int bottomMargin = 0;
    	if (numberOfMonths <= 60) {
            for (int i = 0; i < numberOfMonths; i += step) { //TODO: check if size == values.size()
                String date = dates[i];
  	            mRenderer.addXTextLabel(i, date.replaceFirst(" ", "\n"));
            }
            bottomMargin = 25;
    	} else {
            for (int i = 0; i < numberOfMonths; i += step) { //TODO: check if size == values.size()
                String date = dates[i];
    		    mRenderer.addXTextLabel(i, date.substring(4));
            }
            bottomMargin = 10;
    	}
	    mRenderer.setMargins(new int[] {Utils.px(frgActivity, 8), 
			leftMargin, 
			Utils.px(frgActivity, bottomMargin),
			rightMargin}); // top, left, bottom, right

        /* If there is no condition set, negative values will be above positive on the chart
         * (min value will be set to 0, max value will be set to negative number */
        if (maxY > 0) {
    	    mRenderer.setYAxisMin(0.0);
    	    mRenderer.setYAxisMax(maxY + maxY/5);
        }
        
    	customizeMultipleSeriesRenderer(mRenderer);
    	
		GraphicalView mChartView = ChartFactory.getLineChartView(frgActivity, dataset, mRenderer);	    	  		    	
        chart_layout.removeAllViews();
        chart_layout.setBackgroundResource(R.drawable.chart_bkg);
        chart_layout.addView(mChartView); 
    }
	
	private void customizeMultipleSeriesRenderer(XYMultipleSeriesRenderer mRenderer) {
    	mRenderer.setAxesColor(Color.WHITE);
        mRenderer.setAxisTitleTextSize(Utils.px(frgActivity, 10));    	
    	mRenderer.setMarginsColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));    	
    	mRenderer.setPanEnabled(false, false);
    	mRenderer.setZoomEnabled(false, false);
    	mRenderer.setShowGridX(true);
        mRenderer.setGridColor(0x66CCCCCC);                       
        mRenderer.setLabelsColor(Color.DKGRAY);
        mRenderer.setXLabels(0);       
        mRenderer.setYLabels(10);        
        mRenderer.setXLabelsAlign(Align.CENTER);
        mRenderer.setYLabelsAlign(Align.RIGHT, 0);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);              
        mRenderer.setPointSize(Utils.px(frgActivity, 2));     
    	mRenderer.setLabelsTextSize(Utils.px(frgActivity, 8));
        mRenderer.setShowLegend(true);
        //mRenderer.setLegendHeight(Utils.px(frgActivity, 48));
        mRenderer.setLegendTextSize(Utils.px(frgActivity, 8));
        mRenderer.setInScroll(true);
	}
	
	private TimeSeries getSeries(BigDecimal[] values, int item_count, String name) {
	    TimeSeries series = new TimeSeries(name);	    
	    	    
	    try {
	        for(int i = 0; i < item_count; i++) {	        	
	    	    series.add(i, values[i].setScale(0, Money.ROUNDING_MODE).doubleValue());	    	    
	        }
	    } catch (IndexOutOfBoundsException e) {
	        e.printStackTrace();
	    }
	    return series;
	}
	
	private XYSeriesRenderer getSeriesRenderer() {
		XYSeriesRenderer renderer = new XYSeriesRenderer();
	   	renderer.setColor(Color.parseColor(colors[currentColor])); //TODO: random (light) color	   	
    	renderer.setLineWidth(Utils.px(frgActivity, 2));        	
		return renderer;
	}
 	
	private void incrementCurrentColor() {
		if (currentColor == colors.length - 1) {
			currentColor = 0;
		} else {
			currentColor++;
		}		
	}	

	@Override
	public void plotMortgage(HistoryMortgage historyMortgage) {         		        
		ArrayList<BigDecimal[]> value_lists = new ArrayList<BigDecimal[]>();
		ArrayList<String> titles = new ArrayList<String>();
		titles.add("Total Interest");
		value_lists.add(historyMortgage.getTotalInterestsHistory());
		titles.add("Outstanding loan");
		value_lists.add(historyMortgage.getRemainingAmountHistory());
		int size = historyMortgage.getSimLength();
		plot(value_lists, 
				titles,
        		size, 
        		(LinearLayout) frgActivity.findViewById(R.id.pred_chart)); 	
	}	

	@Override
	public void plotComparisonRates(Account account) {         		        
		ArrayList<BigDecimal[]> value_lists = new ArrayList<BigDecimal[]>();
		ArrayList<String> titles = new ArrayList<String>();
		for (Mortgage mortgage : account.getMortgagesToCompare()) {
		    value_lists.add(mortgage.getHistory().getMonthlyPaymentHistory());
		    titles.add(mortgage.getName());	            
		}
		plot(value_lists, 
				titles,
				account.getLongestLoanTerm(), 
        		(LinearLayout) frgActivity.findViewById(R.id.chart_rates)); 
	}

	@Override
	public void plotComparisonOutstandingPrincipal(Account account) {

		ArrayList<BigDecimal[]> value_lists = new ArrayList<BigDecimal[]>();
		ArrayList<String> titles = new ArrayList<String>();
		
		for (Mortgage mortgage : account.getMortgagesToCompare()) {
		    value_lists.add(mortgage.getHistory().getRemainingAmountHistory());
		    titles.add(mortgage.getName());
		}
		plot(value_lists, 
				titles,
				account.getLongestLoanTerm(), 
        		(LinearLayout) frgActivity.findViewById(R.id.chart_outstanding_loan_principal)); 
	}
	
	@Override
	public void plotComparisonTotalInterest(Account account) {
		ArrayList<BigDecimal[]> value_lists = new ArrayList<BigDecimal[]>();
		ArrayList<String> titles = new ArrayList<String>();
		for (Mortgage mortgage : account.getMortgagesToCompare()) {
		    value_lists.add(mortgage.getHistory().getTotalInterestsHistory());
		    titles.add(mortgage.getName());
		}
		plot(value_lists, 
				titles, 
				account.getLongestLoanTerm(), 
        		(LinearLayout) frgActivity.findViewById(R.id.chart_outstanding_loan_interest)); 
	}
}
