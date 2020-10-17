package com.rolekbartlomiej.weather_android.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.rolekbartlomiej.weather_android.R
import com.rolekbartlomiej.weather_android.databinding.WidgetHourlyWeatherChartBinding
import com.rolekbartlomiej.weather_android.domain.service.data.all.Hourly
import java.text.SimpleDateFormat
import java.util.*

class HourlyWeatherChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding =
        WidgetHourlyWeatherChartBinding.inflate(LayoutInflater.from(context), this, true)
    private val colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)
    private val colorBlack = ContextCompat.getColor(context, R.color.colorBlack)

    init {
        initChart()
    }

    fun setData(data: List<Hourly>) {
        val entries = mutableListOf<Entry>()
        data.forEach {
            entries.add(Entry(it.dt / HOUR_IN_SECONDS.toFloat(), it.temp.toInt().toFloat()))
        }
        val dataSet = LineDataSet(entries, "").apply {
            color = colorPrimary
            valueTextColor = colorBlack
            fillColor = colorPrimary
            valueTextSize = DATASET_TEXT_SIZE
            setDrawFilled(true)
            setDrawValues(true)
            lineWidth = LINE_PATH_WIDTH
            isHighlightEnabled = true
            setDrawHighlightIndicators(false)
            setDrawCircles(false)
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            valueFormatter = lineValueFormatter
        }
        with(binding.chart) {
            this.data = LineData().apply { addDataSet(dataSet) }
            invalidate()
        }
    }

    private fun initChart() {
        with(binding.chart) {
            setTouchEnabled(true)
            setPinchZoom(false)
            setScaleEnabled(false)
            with(viewPortHandler) {
                setMinimumScaleX(HORIZONTAL_SCALE)
                setMaximumScaleX(HORIZONTAL_SCALE)
                setMaximumScaleY(VERTICAL_SCALE)
            }
            description = Description().apply { this.text = "" }
            legend.isEnabled = false

            with(xAxis) {
                valueFormatter = XAxisValueFormatter()
                setDrawGridLines(false)
                setDrawAxisLine(false)
            }
            axisRight.disableLegend()
            axisLeft.disableLegend()
        }
    }

    private val lineValueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toInt().toString() + "°"
        }
    }

    private inner class XAxisValueFormatter : ValueFormatter() {
        private val hourSdf: SimpleDateFormat = SimpleDateFormat("HH", Locale.getDefault())
        private val dayMonthHourSdf: SimpleDateFormat =
            SimpleDateFormat("dd MMM HH", Locale.getDefault())

        override fun getFormattedValue(value: Float): String {
            val millis = value.toLong() * MILLISECONDS * HOUR_IN_SECONDS
            val cal = Calendar.getInstance().apply { time = Date(millis) }
            return if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
                dayMonthHourSdf.format(Date(millis))
            } else {
                hourSdf.format(Date(millis))
            }
        }
    }

    private fun YAxis.disableLegend() {
        this.setDrawAxisLine(false)
        this.setDrawGridLines(false)
        this.setDrawLabels(false)
    }

    companion object {
        private const val HOUR_IN_SECONDS = 3600L
        private const val MILLISECONDS = 1000L
        private const val VERTICAL_SCALE = 1f
        private const val HORIZONTAL_SCALE = 8f
        private const val LINE_PATH_WIDTH = 1.5f
        private const val DATASET_TEXT_SIZE = 16f
    }
}