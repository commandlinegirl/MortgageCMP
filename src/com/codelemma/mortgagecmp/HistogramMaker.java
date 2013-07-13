package com.codelemma.mortgagecmp;

import java.math.BigDecimal;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.mortgagecmp.accounting.Account;
import com.codelemma.mortgagecmp.accounting.HistogramVisitor;
import com.codelemma.mortgagecmp.accounting.HistoryMortgage;
import com.codelemma.mortgagecmp.accounting.Money;
import com.codelemma.mortgagecmp.accounting.Mortgage;

public class HistogramMaker implements HistogramVisitor {

	private SherlockFragmentActivity frgActivity;
    private String[] dates;

	public HistogramMaker(SherlockFragmentActivity sherlockFragmentActivity, String[] dates) {
		this.frgActivity = sherlockFragmentActivity;
		this.dates = dates;
	}
		
	public void histogram(BigDecimal[] principalHistory,
			BigDecimal[] interestHistory,
			BigDecimal[] extraCostHistory,
			double maxValue,
			String[] titles, 
			String[] colors) {

    	int rightMargin = Utils.px(frgActivity, 12);
	    int leftMargin = Utils.px(frgActivity, 7);
    	int screenWidthDips = Utils.dip(frgActivity, frgActivity.getResources().getDisplayMetrics().widthPixels);
        int chartSize = screenWidthDips  - leftMargin - rightMargin;
        int numOfLabels = Math.round(chartSize / 20);
        int step = Math.max(1, principalHistory.length / numOfLabels);
        
        // Build renderer with three series renderers
        XYMultipleSeriesRenderer mRenderer = buildBarRenderer(colors, principalHistory.length, maxValue, step);
        SimpleSeriesRenderer rendererExtraCost = getSeriesRenderer(colors[0]);
        SimpleSeriesRenderer rendererPrincipal = getSeriesRenderer(colors[1]);
        SimpleSeriesRenderer rendererInterest = getSeriesRenderer(colors[2]);
        mRenderer.addSeriesRenderer(rendererExtraCost);
        mRenderer.addSeriesRenderer(rendererInterest);
        mRenderer.addSeriesRenderer(rendererPrincipal);
        
        // Build dataset with three series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYSeries seriesExtraCost = getSeries(extraCostHistory, titles[2], step);
        XYSeries seriesInterest = getSeries(interestHistory, titles[1], step);
        XYSeries seriesPrincipal = getSeries(principalHistory, titles[0], step);
        dataset.addSeries(seriesExtraCost);
        dataset.addSeries(seriesInterest);
        dataset.addSeries(seriesPrincipal);

        final GraphicalView grfv = ChartFactory.getBarChartView(frgActivity, 
        		dataset, 
        		mRenderer, 
        		Type.STACKED);
   
        LinearLayout rl = (LinearLayout) frgActivity.findViewById(R.id.chart_one_histogram);
        rl.addView(grfv);
	}

    protected XYMultipleSeriesRenderer buildBarRenderer(
    		String[] colors, 
    		int length,
    		double maxValue,
    		int step) {
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    	mRenderer.setBarSpacing(1);
    	mRenderer.setAxesColor(Color.WHITE);
        mRenderer.setAxisTitleTextSize(Utils.px(frgActivity, 10));    	
    	mRenderer.setMarginsColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));    	
    	mRenderer.setPanEnabled(false, false);
    	mRenderer.setZoomEnabled(false, false);
    	mRenderer.setShowGridX(true);
        mRenderer.setGridColor(0x66CCCCCC);                       
        mRenderer.setLabelsColor(Color.DKGRAY);
        mRenderer.setXLabels(0);       
        mRenderer.setYLabels(7);        
        mRenderer.setXLabelsAlign(Align.CENTER);
        mRenderer.setYLabelsAlign(Align.RIGHT, 0);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);              
        mRenderer.setPointSize(Utils.px(frgActivity, 2));          
    	mRenderer.setLabelsTextSize(Utils.px(frgActivity, 8));         	
    	mRenderer.setShowLegend(true);
    	mRenderer.setLegendTextSize(Utils.px(frgActivity, 8));
    	mRenderer.setInScroll(true);
    	mRenderer.setYAxisMin(0);
    	mRenderer.setXAxisMin(0);

		for (int i = 0; i < length; i += step) { //TODO: check if size == values.size()
            String date = dates[i];
            String label = (i % 5 == 0) ? date.replaceFirst(" ", "\n") : "";
	            mRenderer.addXTextLabel(i, label);
        }
    	
	    mRenderer.setMargins(new int[] {Utils.px(frgActivity, 8), 
	        Utils.px(frgActivity, 8) + Utils.px(frgActivity, 4) * String.valueOf(maxValue).length(), 
			Utils.px(frgActivity, 25), 
			Utils.px(frgActivity, 22)}); // top, left, bottom, right
        return mRenderer;
    }
    
	private SimpleSeriesRenderer getSeriesRenderer(String color) {
		SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
	   	renderer.setColor(Color.parseColor(color)); //TODO: random (light) color	   	
		return renderer;
	}
   
    private XYSeries getSeries(BigDecimal[] values, String title, int step) {
    	XYSeries series = new XYSeries(title);        
        for (int i = 0; i < values.length; i += step) {
            series.add(i, values[i].setScale(0, Money.ROUNDING_MODE).doubleValue());
        }
    	return series;
    }
	
	@Override
	public void histogramMortgage(Mortgage mortgage) {

		HistoryMortgage history = mortgage.getHistory();
		BigDecimal[] principalHistory = history.getPrincipalPaidHistory();
		BigDecimal[] interestHistory = history.getInterestsPaidHistory();
		BigDecimal[] extraCostHistory = history.getAdditionalCostHistory();
		
		BigDecimal[] interestHistoryAdjusted = new BigDecimal[interestHistory.length];
		BigDecimal[] extraCostHistoryAdjusted = new BigDecimal[extraCostHistory.length];
		double maxValue = mortgage.getTotalPayment().setScale(0, Money.ROUNDING_MODE).doubleValue();
		
		for (int i = 0; i < principalHistory.length; i++) {
			interestHistoryAdjusted[i] = principalHistory[i].add(interestHistory[i]);
			extraCostHistoryAdjusted[i] = interestHistoryAdjusted[i].add(extraCostHistory[i]);
		}

		String[] titles = new String[] {
				frgActivity.getResources().getString(R.string.principal),
				frgActivity.getResources().getString(R.string.interest),
				frgActivity.getResources().getString(R.string.s_mortgage_tax_fees_insurance)};
		
		String[] colors = {"#ffE95D22", "#FF06A2CB", "#ffBEF243" }; 

		histogram(principalHistory,
				interestHistoryAdjusted,
				extraCostHistoryAdjusted,
				maxValue,
				titles,
				colors); 
    }

	@Override
	public void plotLoanBreakdownComparison(Account account) {
	}
}