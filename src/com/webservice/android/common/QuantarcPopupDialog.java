package com.webservice.android.common;

import android.app.AlertDialog;
import android.view.WindowManager;

import com.webservice.android.activities.DialogActivity;
import com.webservice.android.activities.DialogTabActivity;
import com.webservice.android.activities.R;

public class QuantarcPopupDialog {
	private AlertDialog alertDialog;
	private DialogActivity someActivity;
	private DialogTabActivity someTabActivity;

	public QuantarcPopupDialog(DialogActivity someActivity) {
		this.someActivity = someActivity;
	}

	public QuantarcPopupDialog(DialogTabActivity someTabActivity) {
		this.someTabActivity = someTabActivity;
	}

	public void showPopupDialog(String displayText, String positiveButtonText,
			String negativeButtonText) {
		alertDialog = new AlertDialog.Builder(someActivity)
				.setTitle(
						someActivity.getResources().getString(
								R.string.login_app)).setMessage(displayText)
				// .setIcon(R.drawable.alert_icon)
				.setPositiveButton(positiveButtonText, someActivity)
				.setNegativeButton(negativeButtonText, someActivity).show();

		WindowManager.LayoutParams layoutParams = alertDialog.getWindow()
				.getAttributes();
		layoutParams.dimAmount = 0.9f;
		alertDialog.getWindow().setAttributes(layoutParams);
		alertDialog.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	}

	public void showPopupTabDialog(String displayText,
			String positiveButtonText, String negativeButtonText) {
		alertDialog = new AlertDialog.Builder(someTabActivity)
				.setTitle(
						someTabActivity.getResources().getString(
								R.string.login_app)).setMessage(displayText)
				// .setIcon(R.drawable.alert_icon)
				.setPositiveButton(positiveButtonText, someTabActivity)
				.setNegativeButton(negativeButtonText, someTabActivity).show();

		WindowManager.LayoutParams layoutParams = alertDialog.getWindow()
				.getAttributes();
		layoutParams.dimAmount = 0.9f;
		alertDialog.getWindow().setAttributes(layoutParams);
		alertDialog.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	}
	
	public void showPopupTabDialog(String displayText, String buttonText) {
		alertDialog = new AlertDialog.Builder(someTabActivity)
				.setTitle(
						someTabActivity.getResources().getString(
								R.string.login_app)).setMessage(displayText)
				// .setIcon(R.drawable.alert_icon)
				.setPositiveButton(buttonText, someTabActivity).show();

		WindowManager.LayoutParams layoutParams = alertDialog.getWindow()
				.getAttributes();
		layoutParams.dimAmount = 0.9f;
		alertDialog.getWindow().setAttributes(layoutParams);
		alertDialog.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	}

	public void showPopupDialog(String displayText, String positiveButtonText) {
		alertDialog = new AlertDialog.Builder(someActivity)
				.setTitle(
						someActivity.getResources().getString(
								R.string.login_app)).setMessage(displayText)
				// .setIcon(R.drawable.alert_icon)
				.setPositiveButton(positiveButtonText, someActivity).show();

		WindowManager.LayoutParams layoutParams = alertDialog.getWindow()
				.getAttributes();
		layoutParams.dimAmount = 0.9f;
		alertDialog.getWindow().setAttributes(layoutParams);
		alertDialog.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	}

}
