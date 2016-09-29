package liamjdavison.co.uk.greenfuel.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

import liamjdavison.co.uk.greenfuel.R;

/**
 * Created by Liam Davison on 08/08/2016.
 */
public class DatePickerFragment  extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private final Calendar c = Calendar.getInstance();
	private int year = c.get(Calendar.YEAR);
	private int month = c.get(Calendar.MONTH);
	private int day = c.get(Calendar.DAY_OF_MONTH);

	/**
	 * Define an interface that the calling Activity must implement. This is how we communicate back to the Activity
	 */
	public interface OnDateSelectedListener {
		/**
		 * @param date      date to return
		 * @param dialogTag unique identifier for the date field
		 */
		void setDate(Calendar date, String dialogTag);
	}

	private OnDateSelectedListener onDateSelectedListener;

	/**
	 * Override the standard onCreateDialog by setting the date (from arguments bundle if exists), and call the standard constructor
	 *
	 * @param savedInstanceState bundle, may contain an existing date
	 * @return a popup date picker dialog
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// check to see if the arguments contain a date
		Log.d("thisDatePickerTag: ", this.getTag());
		Bundle args = getArguments();
		if (args != null && args.containsKey("day") && args.containsKey("month") && args.containsKey("year")) {
			day = args.getInt("day");
			month = args.getInt("month");
			year = args.getInt("year");
		} else {
			// Use the current date as the default date in the picker
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		}
		// In order to return a date from this dialogfragment to its parent fragment, we need to set up the onDateSelectedListener
		if (getTargetFragment() != null) {
			if (onDateSelectedListener == null) {
				// fetch the listener by casting from the parent fragment
				onDateSelectedListener = (OnDateSelectedListener) getFragmentManager().findFragmentById(R.id.recordVehicleFragment);
			}
			return new DatePickerDialog(getTargetFragment().getContext(), this, year, month, day);
		} else {
			// getTargetFragment is null, so we're attaching to an Activity
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
	}

	/**
	 * Standard method which is called when the user selects a date
	 * Here we format the date and return it as a string, using our interface defined above
	 *
	 * @param view
	 * @param year  calendar year
	 * @param month calendar month
	 * @param day   calendar day
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		onDateSelectedListener.setDate(c, this.getTag());
	}

	/**
	 * This method attaches the fragment to its parent activity, and connects the listener interface
	 *
	 * @param activity
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onDateSelectedListener = (OnDateSelectedListener) activity;
		} catch (Exception e) {
			Log.e("DatePickerFragment", e.getMessage());
		}
	}

}
