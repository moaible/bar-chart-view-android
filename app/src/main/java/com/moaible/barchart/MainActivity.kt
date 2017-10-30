package com.moaible.barchart

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        renderChartThread()
    }


    /**
     * Render bar chart data using thread
     */
    fun renderChartThread() {
        val thread = Thread(Runnable {
            runOnUiThread { renderBarChart() }
        })
        thread.start()
    }


    /**
     * Render dataset for bar chart
     */
    fun renderBarChart() {
        val csvDataset = Dataset(context = applicationContext)
        csvDataset.parseCSV()
        val entries = csvDataset.models.mapIndexedTo(ArrayList<BarEntry>()) { i, model -> BarEntry(model.price, i) }
        val count = entries.count()
        val dataset = BarDataSet(entries, "")
        dataset.color = Color.parseColor("#EFEFFF")
        dataset.setDrawValues(false)
        val labels = csvDataset.models.map { it.time }
        val data = BarData(labels, dataset)
        barchart.setDescription("Price Chart")
        barchart.data = data
        barchart.xAxis.setDrawGridLines(false)
        barchart.axisLeft.setDrawGridLines(false)
        barchart.xAxis.setLabelsToSkip(count / 4 - 1)
        barchart.xAxis.setAxisMaxValue(count.toFloat())
        barchart.setDrawBorders(false)
        barchart.setDrawGridBackground(false)
        barchart.setDescriptionColor(Color.WHITE)
        barchart.isAutoScaleMinMaxEnabled = false
        barchart.axisLeft.isEnabled = false
        barchart.axisRight.isEnabled = false
        barchart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barchart.isDoubleTapToZoomEnabled = false
        barchart.isHighlightPerTapEnabled = false
        barchart.isHighlightPerDragEnabled = false

        //Custom marker
        barchart.setDrawMarkerViews(true)
        val markerView = CustomMarkerView(applicationContext, R.layout.marker_view, csvDataset.models)
        barchart.markerView = markerView
        barchart.setTouchEnabled(true)
        barchart.setDescription("")

        // hide legend
        val legend = barchart.legend
        legend.isEnabled = false
        //barchart.setViewPortOffsets(0f, 0f, 0f, 0f) //remove padding
        barchart.invalidate()
        barchart.animateXY(300, 300)
    }
}
