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
import android.util.Log;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
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
        XYMultipleSeriesRenderer mRenderer = buildBarRenderer(titles, colors, principalHistory.length, maxValue, step);
        SimpleSeriesRenderer rendererExtraCost = getSeriesRenderer(colors[0]);
        SimpleSeriesRenderer rendererPrincipal = getSeriesRenderer(colors[1]);
        SimpleSeriesRenderer rendererInterest = getSeriesRenderer(colors[2]);
        mRenderer.addSeriesRenderer(rendererExtraCost);
        mRenderer.addSeriesRenderer(rendererPrincipal);
        mRenderer.addSeriesRenderer(rendererInterest);

        
        // Build dataset with three series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYSeries seriesExtraCost = getSeries(extraCostHistory, "Extra costs", step);
        XYSeries seriesPrincipal = getSeries(principalHistory, "Principal", step);
        XYSeries seriesInterest = getSeries(interestHistory, "Interest", step);
        dataset.addSeries(seriesExtraCost);
        dataset.addSeries(seriesPrincipal);
        dataset.addSeries(seriesInterest);

        final GraphicalView grfv = ChartFactory.getBarChartView(frgActivity, 
        		dataset, 
        		mRenderer, 
        		Type.STACKED);
   
        LinearLayout rl = (LinearLayout) frgActivity.findViewById(R.id.chart_one_histogram);
        rl.addView(grfv);
	}

    protected XYMultipleSeriesRenderer buildBarRenderer(
    		String[] titles, 
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
        mRenderer.setYLabels(5);        
        mRenderer.setXLabelsAlign(Align.CENTER);
        mRenderer.setYLabelsAlign(Align.RIGHT, 0);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);              
        mRenderer.setPointSize(Utils.px(frgActivity, 2));          
    	mRenderer.setLabelsTextSize(Utils.px(frgActivity, 8));         	
    	mRenderer.setShowLegend(true);
    	mRenderer.setLegendTextSize(Utils.px(frgActivity, 8));
    	
    	int rightMargin = Utils.px(frgActivity, 22);
	    int leftMargin = Utils.px(frgActivity, 17);
		leftMargin += Utils.px(frgActivity, 4) * String.valueOf(maxValue).length();
		
		int bottomMargin = 0;
    	if (length <= 60) {
            for (int i = 0; i < length; i += step) { //TODO: check if size == values.size()
                String date = dates[i];
                String label = (i % 5 == 0) ? date.replaceFirst(" ", "\n") : "";
  	            mRenderer.addXTextLabel(i, label);
            }
            bottomMargin = 25;
    	} else {
            for (int i = 0; i < length; i += step) { //TODO: check if size == values.size()
                String date = dates[i];
    		    String label = (i % 5 == 0) ? date.substring(4) : "";
    		    mRenderer.addXTextLabel(i, label);
            }
            bottomMargin = 10;
    	}
	    mRenderer.setMargins(new int[] {Utils.px(frgActivity, 8), 
			leftMargin, 
			Utils.px(frgActivity, bottomMargin), 
			rightMargin}); // top, left, bottom, right
        return mRenderer;
    }
    
	private SimpleSeriesRenderer getSeriesRenderer(String color) {
		SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
	   	renderer.setColor(Color.parseColor(color)); //TODO: random (light) color	   	
		return renderer;
	}
   
    private XYSeries getSeries(BigDecimal[] values, String title, int step) {
    	XYSeries series = new XYSeries(title);
       // series.add(0, 0); // dummy first null value to add space
        
        int length = values.length;
        int i;
        for (i = 0; i < length; i += step) {
            series.add(i, values[i].setScale(0, Money.ROUNDING_MODE).doubleValue());
        }
        Log.d("i", String.valueOf(i));
        //series.add(i+1, 0); // dummy last null value to add space
    	return series;
    }
	
	@Override
	public void histogramMortgage(Mortgage mortgage) {      		        

		HistoryMortgage history = mortgage.getHistory();
		BigDecimal[] principalHistory = history.getPrincipalPaidHistory();
		BigDecimal[] interestHistory = history.getInterestsPaidHistory();
		BigDecimal[] extraCostHistory = history.getAdditionalCostHistory();

		BigDecimal[] principalHistoryAdjusted = new BigDecimal[principalHistory.length];
		BigDecimal[] extraCostHistoryAdjusted = new BigDecimal[principalHistory.length];
		double maxValue = mortgage.getTotalPayment().setScale(0, Money.ROUNDING_MODE).doubleValue();
		
		for (int i = 0; i < principalHistory.length; i++) {
			principalHistoryAdjusted[i] = principalHistory[i].add(interestHistory[i]);
			extraCostHistoryAdjusted[i] = principalHistory[i].add(interestHistory[i]).add(extraCostHistory[i]);
		}

		String[] titles = new String[] {
				frgActivity.getResources().getResourceName(R.string.principal),
				frgActivity.getResources().getResourceName(R.string.interest),
				frgActivity.getResources().getResourceName(R.string.extra_costs)};
		
		String[] colors = {"#FF5BC236", "#ff0099ff", "#ffFF0080"}; 
		
		histogram(principalHistoryAdjusted,
				interestHistory,
				extraCostHistoryAdjusted,
				maxValue,
				titles,
				colors); 
    }
}
