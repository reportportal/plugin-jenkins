/*
 * Copyright 2016 EPAM Systems
 *
 *
 * This file is part of EPAM Report Portal.
 * https://github.com/reportportal/plugin-jenkins
 *
 * Report Portal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Report Portal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Report Portal.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epam.reportportal

import hudson.model.AbstractProject
import hudson.model.Result
import hudson.model.RootAction
import hudson.model.Run
import hudson.util.*
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.CategoryAxis
import org.jfree.chart.axis.CategoryLabelPositions
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.category.StackedAreaRenderer
import org.jfree.chart.title.LegendTitle
import org.jfree.data.category.CategoryDataset
import org.jfree.ui.RectangleEdge
import org.jfree.ui.RectangleInsets
import org.kohsuke.stapler.StaplerRequest
import org.kohsuke.stapler.StaplerResponse

import java.awt.*

class RPProjectAction implements RootAction {

    final AbstractProject<?, ?> project

    RPProjectAction(AbstractProject project) {
        this.project = project
    }

    @Override
    String getIconFileName() {
        '/plugin/rp4jenkins/img/RP_ic_100.svg'
    }

    @Override
    String getDisplayName() {
        'Report Portal'
    }

    @Override
    String getUrlName() {
        'rp'
    }

    Graph doGraph(final StaplerRequest req, StaplerResponse rsp) {
        final DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> dataSetBuilder = new DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel>()
        for (Run<?, ?> build = getProject().lastBuild; build != null; build = build.previousCompletedBuild) {
            ChartUtil.NumberOnlyBuildLabel label = new ChartUtil.NumberOnlyBuildLabel(build)
            RPBuildAction action = build.getAction(RPBuildAction.class)
            if (null == build.result || build.result.isWorseThan(Result.FAILURE) || action == null) {
                continue
            }
            if (!build.result.isCompleteBuild() && build.result == Result.FAILURE) {
                continue
            }
            dataSetBuilder.add(action.launch.passed, "Passed", label)
            dataSetBuilder.add(action.launch.failed, "Failed", label)
            dataSetBuilder.add(action.launch.skipped, "Skipped", label)
        }
        new GraphImpl(dataSetBuilder.build()) {
        }.doPng(req, rsp)
    }

    @groovy.transform.CompileStatic
    abstract class GraphImpl extends Graph {
        CategoryDataset dataSet

        protected GraphImpl(CategoryDataset dataSet) {
            super(-1, 700, 300)
            this.dataSet = dataSet
        }

        protected JFreeChart createGraph() {
            createChart(dataSet)
        }
    }


    static JFreeChart createChart(CategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createStackedAreaChart(null, // chart title
                null, // unused
                "Tests Count", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // urls
        )
        final LegendTitle legend = chart.getLegend()
        legend.setPosition(RectangleEdge.RIGHT)

        chart.setBackgroundPaint(Color.white)

        final CategoryPlot plot = chart.getCategoryPlot()
        plot.setBackgroundPaint(Color.WHITE)
        plot.setOutlinePaint(null)
        plot.setForegroundAlpha(0.8f)
        plot.setDomainGridlinesVisible(true)
        plot.setDomainGridlinePaint(Color.white)
        plot.setRangeGridlinesVisible(true)
        plot.setRangeGridlinePaint(Color.black)

        CategoryAxis domainAxis = new ShiftedCategoryAxis(null)
        plot.setDomainAxis(domainAxis)
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90)
        domainAxis.setLowerMargin(0.0)
        domainAxis.setUpperMargin(0.0)
        domainAxis.setCategoryMargin(0.0)

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis()
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits())

        StackedAreaRenderer ar = new StackedAreaRenderer2() {
            @Override
            String generateToolTip(CategoryDataset dataSet1, int row, int column) {
                switch (row) {
                    case 0:
                        return 5 + " Failure(s)"
                    case 1:
                        return 5 + " Pass"
                    case 2:
                        return 5 + " Skip(s)"
                    default:
                        return ""
                }
            }
        }
        plot.setRenderer(ar)
        ar.setSeriesPaint(0, ColorPalette.RED)
        ar.setSeriesPaint(1, ColorPalette.BLUE)
        ar.setSeriesPaint(2, ColorPalette.YELLOW)
        plot.setInsets(new RectangleInsets(0, 0, 0, 5.0))
        chart
    }
}
