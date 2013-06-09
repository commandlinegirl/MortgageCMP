package com.codelemma.mortgagecmp;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.mortgagecmp.accounting.HistogramVisitor;
import com.codelemma.mortgagecmp.accounting.Money;
import com.codelemma.mortgagecmp.accounting.Mortgage;


public class HistogramOneSeriesMaker implements HistogramVisitor {

	private SherlockFragmentActivity frgActivity;
          
	public HistogramOneSeriesMaker(SherlockFragmentActivity sherlockFragmentActivity) {
		this.frgActivity = sherlockFragmentActivity;
	}
		
	public void histogram(double[] values, String[] titles, String[] colors) {		
		        		
        XYMultipleSeriesRenderer mRenderer = buildBarRenderer(titles, colors);
                
        //int length = mRenderer.getSeriesRendererCount();
        //for (int i = 0; i < length; i++) {
        //  SimpleSeriesRenderer seriesRenderer = mRenderer.getSeriesRendererAt(i);
        //  seriesRenderer.setDisplayChartValues(true);
        //}
 
        CategorySeries series = getSeries(values);
        
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();        
        dataset.addSeries(series.toXYSeries());        
        
        setChartMargins(mRenderer, values);
        
        final GraphicalView grfv = ChartFactory.getBarChartView(frgActivity, 
        		dataset, 
        		mRenderer, Type.DEFAULT);
   
        LinearLayout rl=(LinearLayout) frgActivity.findViewById(R.id.chart_one_histogram);
        rl.addView(grfv);

	}

    protected XYMultipleSeriesRenderer buildBarRenderer(String[] titles, String[] colors) {
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        
        /*
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        
         
        renderer.setMarginsColor(Color.parseColor("#EEEDED"));
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0,Color.BLACK);
         
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.parseColor("#FBFBFC"));
        renderer.setOrientation(Orientation.HORIZONTAL);       
        renderer.setInScroll(false);
    	renderer.setPanEnabled(false, false);
    	renderer.setZoomEnabled(false, false);    	
        */

        //mRenderer.addXTextLabel(1, ""); // dummy first null value to add space
        mRenderer.addXTextLabel(2, titles[0]);
        mRenderer.addXTextLabel(3, titles[1]);
        mRenderer.addXTextLabel(4, titles[2]);        

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
    	mRenderer.setLabelsTextSize(Utils.px(frgActivity, 10));         	
    	mRenderer.setShowLegend(false);
    	    	
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(Color.parseColor(colors[0]));
        //r.setChartValuesSpacing(1);
        //r.setDisplayChartValues(true);
        mRenderer.addSeriesRenderer(r);
        
        return mRenderer;
    }
    
    private void setChartMargins(XYMultipleSeriesRenderer mRenderer, double[] values) {
    	
    	double max = Math.max(values[0], (Math.max(values[1], values[2])));
    	
    	int rightMargin = Utils.px(frgActivity, 12);
	    int leftMargin = Utils.px(frgActivity, 7);
	    int labelMaxYLen = String.valueOf(max).length();	    	    
		leftMargin += Utils.px(frgActivity, 4) * labelMaxYLen;
	    	        	    
    	mRenderer.setMargins(new int[] {Utils.px(frgActivity, 8), 
    			leftMargin, 
    			Utils.px(frgActivity, 15), 
    			rightMargin}); // top, left, bottom, right
    }
    
    private CategorySeries getSeries(double[] values) {
        CategorySeries series = new CategorySeries("Mortgage");
        series.add(0); // dummy first null value to add space
        int length = values.length;
        for (int i = 0; i < length; i++) {
            series.add(values[i]);
        }
        series.add(0); // dummy last null value to add space
    	return series;
    }
            
	@Override
	public void histogramMortgage(Mortgage mortgage) {		
		double[] values = new double[]{
				mortgage.getLoanAmount().setScale(2, Money.ROUNDING_MODE).doubleValue(), 
				mortgage.getTotalInterestPaid().setScale(2, Money.ROUNDING_MODE).doubleValue(), 
				mortgage.getTotalAdditionalCost().setScale(2, Money.ROUNDING_MODE).doubleValue()};
		
		String[] titles = new String[] {
				"Principal",
				"Interest",
				"Extra costs"
		};
		
		String[] colors = {"#ff00ff00", "#ff0099ff", "#ffFF0080"}; 
	
        histogram(values, titles, colors); 		
	}
}
