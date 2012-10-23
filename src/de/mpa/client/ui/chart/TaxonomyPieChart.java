package de.mpa.client.ui.chart;

import java.text.AttributedString;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleInsets;

public class TaxonomyPieChart extends Chart {
	private PieDataset pieDataset;
	
	public enum TaxonomyChartType implements ChartType {
		KINGDOM("Kingdom"),
		PHYLUM("Phylum"),
		CLASS("Class"),
		SPECIES("Species");

		private String title;
		
		private TaxonomyChartType(String title) {
			this.title = title;
		}
		@Override
		public String getTitle() {
			return title + " Taxonomy";
		}
	}
		
	/**
     * Constructs an OntologyPieChart.
     *
     * @param data Input data.
     * @param chartType Chart type.
     */
    public TaxonomyPieChart(ChartData data, ChartType chartType) {
        super(data, chartType);
    }

	@Override
	protected void process(ChartData data) {
		if (data instanceof TaxonomyData) {
			TaxonomyData taxonomyData = (TaxonomyData) data;
			taxonomyData.setChartType(chartType);
			pieDataset = taxonomyData.getDataset();
		}
	}

	@Override
	protected void setChart() {
		TaxonomyChartType pieChartType = (TaxonomyChartType) chartType;
		chartTitle = pieChartType.getTitle();
		
		PiePlot3D plot = new PiePlot3DExt(pieDataset, 0.2);
        plot.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));
        plot.setStartAngle(324);
        plot.setCircular(true);
        plot.setForegroundAlpha(0.75f);
        plot.setBackgroundPaint(null);
        plot.setOutlineVisible(false);
        
		plot.setLabelGenerator(new PieSectionLabelGenerator() {
			@Override
			public String generateSectionLabel(PieDataset dataset, Comparable key) {
				Integer value = (Integer) dataset.getValue(key);
				double total = 0.0;
				for (int i = 0; i < dataset.getItemCount(); i++) {
					total += ((Integer) dataset.getValue(i)).doubleValue();
				}
				double relVal = value.doubleValue() / total;
				return key.toString() + "\n" + value + " (" + (Math.round(relVal * 1000.0) / 10.0) + "%)";
			}
			@Override
			public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
				return null;	// unused
			}
		});
        
        chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT,
                plot, false);
        ChartFactory.getChartTheme().apply(chart);
		
		chart.setBackgroundPaint(null);
	}
}
