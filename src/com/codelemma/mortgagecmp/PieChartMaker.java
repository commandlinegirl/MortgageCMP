package com.codelemma.mortgagecmp;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Color;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codelemma.mortgagecmp.accounting.Money;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.PieChartVisitor;

public class PieChartMaker implements PieChartVisitor {

	private SherlockFragmentActivity frgActivity;
    
	public PieChartMaker(SherlockFragmentActivity sherlockFragmentActivity) {
		this.frgActivity = sherlockFragmentActivity;
	}

	public void drawPieChart(double[] values, String[] titles, int[] colors) {	
		final GraphicalView grfv = ChartFactory.getPieChartView(frgActivity, getSeries(values, titles), getRenderer(colors));
        LinearLayout rl = (LinearLayout) frgActivity.findViewById(R.id.chart_one_piechart);
        rl.addView(grfv);
	}

	private DefaultRenderer getRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();

		for (int color : colors)
		{
			SimpleSeriesRenderer simpleRenderer = new SimpleSeriesRenderer();
			simpleRenderer.setColor(color);
			renderer.addSeriesRenderer(simpleRenderer);
			
		}
		renderer.setLabelsTextSize(Utils.px(frgActivity, 8));
		renderer.setPanEnabled(false);
    	renderer.setZoomEnabled(false);
    	renderer.setShowLegend(false);
    	//renderer.setLegendTextSize(Utils.px(frgActivity, 10));

		return renderer;
	}
 	
    private CategorySeries getSeries(double[] values, String[] titles) {
        CategorySeries series = new CategorySeries("Mortgage");
        int length = values.length;
        for (int i = 0; i < length; i++) {
            series.add(titles[i], values[i]);
        }
    	return series;
    }

	@Override
	public void piechartMortgage(Mortgage mortgage) {		
		double[] values = new double[]{
				mortgage.getLoanAmount().setScale(2, Money.ROUNDING_MODE).doubleValue(), 
				mortgage.getTotalInterestPaid().setScale(2, Money.ROUNDING_MODE).doubleValue(), 
				mortgage.getTotalAdditionalCost().setScale(2, Money.ROUNDING_MODE).doubleValue()};
		
		String[] titles = new String[] {
				"Principal",
				"Interest",
				"Extra costs"
		};
		
		/*
		String[] titles = new String[] {
				frgActivity.getResources().getResourceName(R.string.principal),
				frgActivity.getResources().getResourceName(R.string.interest),
				frgActivity.getResources().getResourceName(R.string.extra_costs)};
		*/
		
		int[] colors = {Color.parseColor("#ff0099ff"), Color.parseColor("#ffFF0080"), Color.parseColor("#FF5BC236")}; 
	
        drawPieChart(values, titles, colors);	
	}
}