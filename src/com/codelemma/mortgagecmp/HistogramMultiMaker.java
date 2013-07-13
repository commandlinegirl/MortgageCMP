package com.codelemma.mortgagecmp;

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
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.mortgagecmp.accounting.Account;
import com.codelemma.mortgagecmp.accounting.HistogramVisitor;
import com.codelemma.mortgagecmp.accounting.Money;
import com.codelemma.mortgagecmp.accounting.Mortgage;

public class HistogramMultiMaker implements HistogramVisitor {

	private SherlockFragmentActivity frgActivity;

	public HistogramMultiMaker(SherlockFragmentActivity sherlockFragmentActivity) {
		this.frgActivity = sherlockFragmentActivity;
	}

	public void histogram(
			double[] total_fees,
			double[] total_interests,
			double[] total_principals,
			String[] titles,
			int[] colors,
			double maxValue) {

        // Build renderer with three series renderers
        XYMultipleSeriesRenderer mRenderer = buildBarRenderer(titles, colors, maxValue);
        SimpleSeriesRenderer rendererFees = getSeriesRenderer(colors[2]);
        SimpleSeriesRenderer rendererInterest = getSeriesRenderer(colors[1]);
        SimpleSeriesRenderer rendererPrincipal = getSeriesRenderer(colors[0]);        

        mRenderer.addSeriesRenderer(rendererFees);
        mRenderer.addSeriesRenderer(rendererInterest);
        mRenderer.addSeriesRenderer(rendererPrincipal);

        // Build dataset with three series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYSeries seriesFees = getSeries(total_fees, "Tax, fees, insurance");
        XYSeries seriesInterest = getSeries(total_interests, "Interest");
        XYSeries seriesPrincipal = getSeries(total_principals, "Principal");
        dataset.addSeries(seriesFees);
        dataset.addSeries(seriesInterest);
        dataset.addSeries(seriesPrincipal);

        final GraphicalView grfv = ChartFactory.getBarChartView(frgActivity, 
        		dataset, 
        		mRenderer, 
        		Type.STACKED);

        LinearLayout rl = (LinearLayout) frgActivity.findViewById(R.id.histogram_multi_breakdown);
        rl.removeAllViews();
        rl.addView(grfv);
        rl.setBackgroundResource(R.drawable.chart_bkg);
        setLegendColors(colors);
	}

    @SuppressWarnings("deprecation")
	protected XYMultipleSeriesRenderer buildBarRenderer(
    		String[] titles, 
    		int[] colors, 
    		double maxValue) {
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        //mRenderer.setBarSpacing();
    	mRenderer.setBarWidth(Utils.px(frgActivity, 35));
    	mRenderer.setDisplayChartValues(true);
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
    	mRenderer.setShowLegend(false);
    	mRenderer.setLegendTextSize(Utils.px(frgActivity, 8));
    	mRenderer.setInScroll(true);
    	mRenderer.setYAxisMax(maxValue+maxValue/20);
    	mRenderer.setYAxisMin(0);

		for (int i = 0; i < titles.length; i++) { //TODO: check if size == values.size()
	        mRenderer.addXTextLabel(i, titles[i]);
        }
		
	    mRenderer.setMargins(new int[] {
	        Utils.px(frgActivity, 8), 
			Utils.px(frgActivity, 7) + Utils.px(frgActivity, 4) * String.valueOf(maxValue).length(), 
			Utils.px(frgActivity, 10), 
			Utils.px(frgActivity, 12)}); // top, left, bottom, right

        return mRenderer;
    }

	private SimpleSeriesRenderer getSeriesRenderer(int color) {
		SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
	   	renderer.setColor(color);  	
		return renderer;
	}

    private XYSeries getSeries(double[] values, String title) {
    	XYSeries series = new XYSeries(title);
        int i;
        for (i = 0; i < values.length; i++) {
            series.add(i, values[i]);
        }
        i = i++;
        series.add(i, 0); // dummy last null value to add space
    	return series;
    }
            

	@Override
	public void plotLoanBreakdownComparison(Account account) {
	    int ind = account.getMortgagesToCompare().size();
		double[] total_principals = new double[ind+1];
		double[] total_interests = new double[ind+1];
		double[] total_fees = new double[ind+1];
		String[] titles = new String[ind+1];
		
		total_principals[0] = 0;
		total_interests[0]  = 0;
		total_fees[0] = 0;
		titles[0] = "";
		
		int j = 1;
		double maxValue = 100000;
		for (Mortgage mortgage : account.getMortgagesToCompare()) {
			total_principals[j] = mortgage.getLoanAmount().setScale(2, Money.ROUNDING_MODE).doubleValue();
			total_interests[j] = mortgage.getTotalInterestPaid().setScale(2, Money.ROUNDING_MODE).doubleValue();
			total_fees[j] = mortgage.getTotalTaxInsurancePMIClosingFees().setScale(2, Money.ROUNDING_MODE).doubleValue();
		    titles[j] = mortgage.getName();
		    maxValue = Math.max(maxValue, mortgage.getTotalPayment().setScale(0, Money.ROUNDING_MODE).doubleValue());
		    j++;
		}
		
		double[] total_fees_adjusted = new double[ind+1];
		double[] total_interests_adjusted = new double[ind+1];
		
		for (int i = 1; i <= ind; i++) {
			total_interests_adjusted[i] =  total_principals[i] + total_interests[i];
			total_fees_adjusted[i] = total_interests_adjusted[i] + total_fees[i];
		}
		
		int[] colors = {
				Color.parseColor("#FF06A2CB"),
				Color.parseColor("#ffBEF243"),
				Color.parseColor("#ffE95D22")}; 

        histogram(total_fees_adjusted,        		
        		total_interests_adjusted,
        		total_principals,
        		titles, 
        		colors,
        		maxValue);
	}

	@Override
	public void histogramMortgage(Mortgage mortgage) {
		// TODO Auto-generated method stub
	}
	
    private void setLegendColors(int[] colors) {
    	TextView tv = (TextView) frgActivity.findViewById(R.id.principal_breakdown_name);
    	tv.setTextColor(colors[0]);
    	tv = (TextView) frgActivity.findViewById(R.id.interest_breakdown_name);
    	tv.setTextColor(colors[1]);
    	tv = (TextView) frgActivity.findViewById(R.id.fees_breakdown_name);
    	tv.setTextColor(colors[2]);
    }
}
