package com.mobile.freshproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CustomClockBackView cvClockBack;

    private ImageView butPlay;
    private ImageView butPause;
    private ImageView butFlag;
    private ImageView butStop;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TimeRecyclerAdapter adapter;

    private ArrayList<TimeViewItem> listOfFlags;

    private boolean isStarted;
    private int orderOfFlag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_animator);

        cvClockBack = findViewById(R.id.cv_clock_back);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        listOfFlags = new ArrayList<>();

        butPlay = findViewById(R.id.play);
        butPause = findViewById(R.id.pause);
        butFlag = findViewById(R.id.flag);
        butStop = findViewById(R.id.stop);

        butPlay.setOnClickListener(this);
        butPause.setOnClickListener(this);
        butFlag.setOnClickListener(this);
        butStop.setOnClickListener(this);
        recycleList();
    }

    private void recycleList() {
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        adapter = new TimeRecyclerAdapter(listOfFlags);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play: {
                setVisibilities(View.GONE, View.VISIBLE, View.VISIBLE, View.GONE);
                if (isStarted)
                    cvClockBack.resumeAction();
                else {
                    cvClockBack.startAction();
                    isStarted = true;
                    cvClockBack.changeCircPosition();
                }
                break;
            }
            case R.id.pause: {
                setVisibilities(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);

                cvClockBack.pauseAction();
                break;
            }
            case R.id.flag: {
                recyclerView.setVisibility(View.VISIBLE);
                showList();

                break;
            }
            case R.id.stop: {
                setVisibilities(View.VISIBLE, View.GONE, View.GONE, View.GONE);

                recyclerView.setVisibility(View.GONE);
                cvClockBack.makeDefault();
                isStarted = false;
                listOfFlags.clear();
                break;
            }
        }
    }

    private void showList() {
        orderOfFlag ++;
        listOfFlags.add(0, new TimeViewItem(orderOfFlag, cvClockBack.millis, cvClockBack.lastTimeMillis));
        cvClockBack.setLastTimeMillis();
        adapter.notifyItemInserted(0);
        adapter.notifyDataSetChanged();
        layoutManager.scrollToPosition(0);
    }

    private void setVisibilities(int play, int pause, int flag, int stop) {
        butPlay.setVisibility(play);
        butPause.setVisibility(pause);
        butFlag.setVisibility(flag);
        butStop.setVisibility(stop);
    }

    private float convertDpToPx(float valueDp) {
        return valueDp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
