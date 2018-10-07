package com.example.lo_wh.saving;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    PieChart pieChart;
    float savingsBalance;
    long creditBalance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("accountBalance", Context.MODE_PRIVATE);
        if(sharedPref!=null){
            savingsBalance = sharedPref.getFloat("savingsBalance", 1080.0f);
            creditBalance = sharedPref.getLong("creditBalance", 0l);
        }

        pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setOnChartValueSelectedListener(this);
        setData((int)savingsBalance);


        //pieChart.animateXY(1400, 1400);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
        // get je_input_dialog.xml view


        Button addTarget = findViewById(R.id.addTargetBtn);
        addTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(StatsActivity.this);
                View promptView = layoutInflater.inflate(R.layout.input_target, null);
                final TextView target2 = findViewById(R.id.target2);
                final ProgressBar progress = findViewById(R.id.progressBar6);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StatsActivity.this);
                alertDialogBuilder.setView(promptView);
                final EditText editText = promptView.findViewById(R.id.je_input_dialog_input);
                // setup a dialog window

                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //get text and set new view
                                String customTarget = editText.getText().toString();
                                String toFill = "RM 0 / RM " + customTarget + " saved";
                                target2.setText(toFill);
                                target2.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        });
    }

    private void setData(int savingBalance) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        entries.add(new PieEntry(100, "January"));
        entries.add(new PieEntry(130, "February"));
        entries.add(new PieEntry(80, "March"));
        entries.add(new PieEntry(150, "April"));
        entries.add(new PieEntry(50, "May"));
        entries.add(new PieEntry(100, "June"));
        entries.add(new PieEntry(160, "July"));
        entries.add(new PieEntry(200, "August"));
        entries.add(new PieEntry(110, "September"));
        entries.add(new PieEntry(savingBalance - 1080, "October"));

        PieDataSet dataSet = new PieDataSet(entries, "Months");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        Log.d("TEST", "test");

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Savings of 2018\nCurrent Savings RM" + savingsBalance);
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 15, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 15, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 6, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 6, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());

        //dialog box to let user know savings of the month
        AlertDialog alertDialog = new AlertDialog.Builder(StatsActivity.this).create();
        alertDialog.setTitle("Savings amount");
        alertDialog.setMessage("You have saved RM " + e.getY() + "! \nKeep it up!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Got it!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}
