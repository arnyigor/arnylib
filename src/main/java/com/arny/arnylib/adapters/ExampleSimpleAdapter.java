package com.arny.arnylib.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
public class ExampleSimpleAdapter extends BindableViewHolder<Object> implements View.OnClickListener {

    private int position;
    private SimpleActionListener simpleActionListener;

    public ExampleSimpleAdapter(View itemView) {
        super(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void bindView(Context context, int position, Object item, ActionListener actionListener) {
        super.bindView(context, position, item, actionListener);
        this.position = position;
        simpleActionListener = (SimpleActionListener) actionListener;
        initUI();
        updateUI(item);
    }

    private void initUI() {
    }

    @SuppressLint("DefaultLocale")
    private void updateUI(Object item) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public interface SimpleActionListener extends ActionListener {
    }
}