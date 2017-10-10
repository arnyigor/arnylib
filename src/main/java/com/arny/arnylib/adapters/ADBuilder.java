package com.arny.arnylib.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
public abstract class ADBuilder extends AlertDialog.Builder {
	private AlertDialog mAlertDialog;
    private View view;

	public ADBuilder(Context context, int themeResId) {
		super(context, themeResId);
		view = LayoutInflater.from(getContext()).inflate(themeResId, null);
	}

    protected AlertDialog getDialog() {
        return mAlertDialog;
    }

    public ADBuilder(Context context) {
		super(context);
	}

	@Override
	public AlertDialog show() {
		if (view == null) {
			view = getView();
		}
		this.setView(view);
		this.setTitle(getTitle());
		initUI(view);
		mAlertDialog = super.show();
        updateDialogView();
        return mAlertDialog;
	}

	protected abstract void initUI(View view);
	protected abstract String getTitle();
	protected abstract View getView();
	protected abstract void updateDialogView();
}
