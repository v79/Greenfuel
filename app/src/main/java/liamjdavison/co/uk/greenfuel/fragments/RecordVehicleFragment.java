package liamjdavison.co.uk.greenfuel.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import liamjdavison.co.uk.greenfuel.R;
import liamjdavison.co.uk.greenfuel.ReferenceData;
import liamjdavison.co.uk.greenfuel.model.FuelType;
import liamjdavison.co.uk.greenfuel.model.Vehicle;

import static liamjdavison.co.uk.greenfuel.R.id.fuelTypeSpinner;

/**
 * Fragment for capturing information about a new Vehicle
 * Created by Liam Davison on 08/08/2016.
 */
public class RecordVehicleFragment extends Fragment implements DatePickerFragment.OnDateSelectedListener {

	public static final String DATE_PICKER = "datePicker";
	public static final String DATE_FORMAT_UK = "dd/MM/yyyy";
	private static final String BUNDLE_START_DATE = "START_DATE";

	OnVehicleCreatedListener mVehicleCreatedCallback;

	private ReferenceData refData;

	private EditText manufacturer, model, registration, odometer, engineSize, editStartDate;
	private Button btnSave;
	private Vehicle vehicle;
	private Calendar startDate;
	private TextInputLayout tilStartDate;
	private Spinner fuelTypePicker;

	private ToggleButton fuelUnits, distanceUnits;

	private FuelType selectedFuelType;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View fView = inflater.inflate(R.layout.fragment_record_vehicle, container, true);

		manufacturer = (EditText) fView.findViewById(R.id.editManufacturer);
		model = (EditText) fView.findViewById(R.id.editModel);
		registration = (EditText) fView.findViewById(R.id.editRegistration);
		odometer = (EditText) fView.findViewById(R.id.editOdometer);
		engineSize = (EditText) fView.findViewById(R.id.editEngineSize);
		editStartDate = (EditText) fView.findViewById(R.id.editRegistrationDate);
		tilStartDate = (TextInputLayout) fView.findViewById(R.id.tilStartDate);
		fuelUnits = (ToggleButton) fView.findViewById(R.id.toggleFuelUnits);
		distanceUnits = (ToggleButton) fView.findViewById(R.id.toggleDistanceUnits);


		buildFuelTypeSpinner(fView);

		btnSave = (Button) fView.findViewById(R.id.btnSaveVehicle);
		btnSave.setOnClickListener(new View.OnClickListener()

	   {
		   @Override
		   public void onClick(View v) {
//				vehicleDbHelper = VehicleDbHelper.getInstance(fView.getContext());
			   vehicle = buildVehicle();
//				long pk = vehicleDbHelper.addVehicle(vehicle);
			   mVehicleCreatedCallback.onVehicleCreated(vehicle);
		   }
	   }

		);

		editStartDate.setOnClickListener(new View.OnClickListener()
		 {
			 @Override
			 public void onClick(View v) {
				 editStartDate.setError(null);
				 android.support.v4.app.DialogFragment datePickerFragment = new DatePickerFragment();

				 // if date already has a value, pass it to the fragment as an argument Bundle
				 if (!TextUtils.isEmpty(editStartDate.getText())) {
					 Bundle currentlySelectedDateBundle = new Bundle();
					 currentlySelectedDateBundle.putInt("day", startDate.get(Calendar.DAY_OF_MONTH));
					 currentlySelectedDateBundle.putInt("month", startDate.get(Calendar.MONTH));
					 currentlySelectedDateBundle.putInt("year", startDate.get(Calendar.YEAR));
					 datePickerFragment.setArguments(currentlySelectedDateBundle);
				 }
				 // in order to to fragment-to-fragment communication, we need to set this class as the target fragment
				 datePickerFragment.setTargetFragment(RecordVehicleFragment.this, 1);
				 datePickerFragment.show(getFragmentManager(), DATE_PICKER);
			 }
		 }

		);

		return fView;
	}

	private void buildFuelTypeSpinner(View fView) {
		fuelTypePicker = (Spinner) fView.findViewById(fuelTypeSpinner);
		List<FuelType> fuelTypesList = refData.getFuelTypes();
		List<String> fuelTypes = new ArrayList<>();
		for (FuelType ft : fuelTypesList) {
			fuelTypes.add(ft.getName());
		}
		ArrayAdapter<String> fuelTypeAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, fuelTypes);
		fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fuelTypePicker.setAdapter(fuelTypeAdapter);

		fuelTypePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				Log.v("fuelType", (String) parent.getItemAtPosition(position));
				selectedFuelType = refData.getFuelTypeWithName((String) parent.getItemAtPosition(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Do nothing
			}
		});

	}

	/**
	 * Attach this class to its parent activity in order to return the VehicleID
	 *
	 * @param context the Android context
	 */
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		Activity activity;
		try {
			activity = (Activity) context;
			mVehicleCreatedCallback = (OnVehicleCreatedListener) activity;
			refData = (ReferenceData) context;
		} catch (ClassCastException cce) {
			throw new ClassCastException(context.toString()
					+ " must implement OnVehicleCreatedListener");
		}
	}

	/**
	 * Handle device changes, eg orientation
	 *
	 * @param outState current status bundle to save
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putSerializable(BUNDLE_START_DATE, startDate);
	}

	/**
	 * Restore state after a device change
	 *
	 * @param savedInstanceState status bundle
	 */
	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (null != savedInstanceState) {
			startDate = (Calendar) savedInstanceState.getSerializable(BUNDLE_START_DATE);
		}
	}

	/**
	 * Construct a new Vehicle from our values
	 *
	 * @return a new Vehicle
	 */

	private Vehicle buildVehicle() {
		Vehicle v = new Vehicle();
		v.setManufacturer(manufacturer.getText().toString());
		v.setModel(model.getText().toString());
		v.setRegistration(registration.getText().toString());
		v.setStartOdo(Integer.parseInt(odometer.getText().toString()));
		v.setEngineSize(Float.parseFloat(engineSize.getText().toString()));
		v.setRegisteredDate(startDate.getTime());
		v.setFuelType(selectedFuelType);
		v.setDistanceIsMetric(distanceUnits.isChecked());
		v.setFuelVolumeIsMetric(fuelUnits.isChecked());
		return v;
	}

	/**
	 * Implements DatePickerFragment.OnDateSelectedListener
	 *
	 * @param date      date to return
	 * @param dialogTag unique identifier for the date field
	 */
	@Override
	public void setDate(Calendar date, String dialogTag) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_UK, Locale.UK);
		// set the date onto our field

		switch (dialogTag) {
			case DATE_PICKER:
				startDate = date;
				editStartDate.setText(sdf.format(startDate.getTime()));
				break;
			default:
				Toast toast = Toast.makeText(null, "Unknown dialog tag: " + dialogTag, Toast.LENGTH_SHORT);
				toast.show();
		}
	}

	/**
	 * Interface to return the Vehicle ID to the calling Activity
	 */
	public interface OnVehicleCreatedListener {
		void onVehicleCreated(Vehicle vehicle);
	}
}
