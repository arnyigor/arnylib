package com.arny.arnylib.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.arny.arnylib.R;
import com.arny.arnylib.adapters.ADBuilder;
import com.arny.arnylib.utils.Calculator;
public class CalculatorDialog extends ADBuilder {
	private final CalculatorResultListener resultListener;
	private Calculator mCalculator;
	private boolean mFreshState = true;
	private TextView mTvDisplay;
	private StringBuffer mDisplay;
	private static final int MAX_BUTTONS = 21; // total of 20 buttons according to the layout.
	private Button[] mButtons; // auto fill in buttons
	private String value;
	private enum InputButton {
		EXP(1, "e"),
		BRACKETL(2, "("),
		BRACKETR(3, ")"),
		NUM7(4, "7"),
		NUM8(5, "8"),
		NUM9(6, "9"),
		DIV(7, "/"),
		NUM4(8, "4"),
		NUM5(9, "5"),
		NUM6(10, "6"),
		MULT(11, "*"),
		NUM1(12, "1"),
		NUM2(13, "2"),
		NUM3(14, "3"),
		MINUS(15, "-"),
		NUM0(16, "0"),
		DOT(17, "."),
		PLUS(18, "+");
		private int idx;
		private String symbol;
		InputButton(int i, String s) {
			idx = i;
			symbol = s;
		}
		public int getIdx() {
			return idx;
		}
		public String getChar() {
			return symbol;
		}
	}
	private enum ActionKey {
		CLEAR(0),
		EQUALS(19),
		OK(20);
		private int idx;
		ActionKey(int i) {
			idx = i;
		}
		public int getIdx() {
			return idx;
		}
	}

	public interface CalculatorResultListener {
		void onResult(String result);
	}

	public CalculatorDialog(Context context, String value, CalculatorResultListener resultListener) {
		super(context);
		mButtons = new Button[MAX_BUTTONS];
		this.value = value;
		this.resultListener = resultListener;
	}

	public CalculatorDialog(Context context, CalculatorResultListener resultListener) {
		this(context, "", resultListener);
	}

	@Override
	protected void initUI(View view) {
		mTvDisplay = view.findViewById(R.id.main_display);
	}

	@Override
	protected String getTitle() {
		return "Калькулятор";
	}

	@Override
	protected View getView() {
		return LayoutInflater.from(getContext()).inflate(R.layout.calc_dialog_layout, null);
	}

	public String getData() {
		return value;
	}

	@Override
	protected void updateDialogView() {
		mDisplay = new StringBuffer();
		if (value != null) {
			setDisplayString(value);
		} else {
			setDisplayString("0");
		}
		mCalculator = new Calculator();
		setButtonIds();
		addInputButtonListeners();
		addActionKeyListeners();
		updateMainDisplay();
	}

	private void setButtonIds() {
		for (int i = 0; i < MAX_BUTTONS; i++) {
			mButtons[i] = getDialogView().findViewById(R.id.buttonClear + i);
		}
	}

	private void setDisplayString(String s) {
		mDisplay.replace(0, mDisplay.length(), s);
	}

	private void updateMainDisplay() {
		mTvDisplay.setText(mDisplay);
	}

	// Add button listeners to buttons.
	private void addInputButtonListeners() {
		//Numbers.
		for (InputButton b : InputButton.values()) {
			mButtons[b.getIdx()].setOnClickListener(v -> {
				if (mFreshState) {
					mDisplay.replace(0, mDisplay.length(), b.getChar());
					mFreshState = false;
				} else {
					mDisplay.append(b.getChar());
				}
				updateMainDisplay();
			});
		}
	}

	private void addActionKeyListeners() {
		for (ActionKey k : ActionKey.values()) {
			final ActionKey ak = k;
			mButtons[k.getIdx()].setOnClickListener(v -> {
				processActionKey(ak);
				updateMainDisplay();
			});
		}
	}

	private void processActionKey(ActionKey k) {
		switch (k) {
			case CLEAR:
				setDisplayString("0");
				mCalculator.handleKeyPress(Calculator.Key.CLEAR);
				mFreshState = true;
				break;
			case EQUALS:
				mCalculator.setInput(mDisplay.toString());
				mCalculator.handleKeyPress(Calculator.Key.EQUALS);
				setDisplayString(mCalculator.getOutput());
				if (mCalculator.getFailState()) {
					mCalculator.handleKeyPress(Calculator.Key.CLEAR);
					mFreshState = true;
				}
				break;
			case OK:
				if (resultListener != null) {
					resultListener.onResult(mTvDisplay.getText().toString());
				}
				return;
			default:
				break;
		}
	}

}
