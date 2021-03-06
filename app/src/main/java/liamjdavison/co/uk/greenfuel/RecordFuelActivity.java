package liamjdavison.co.uk.greenfuel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

import liamjdavison.co.uk.greenfuel.fragments.DatePickerFragment;
import liamjdavison.co.uk.greenfuel.model.DaoSession;
import liamjdavison.co.uk.greenfuel.model.FuelRecord;
import liamjdavison.co.uk.greenfuel.model.Vehicle;
import liamjdavison.co.uk.greenfuel.model.VehicleDao;

/**
 * Record a new {@link FuelRecord} for the current {@link Vehicle}
 */
public class RecordFuelActivity extends AppCompatActivity  implements DatePickerFragment.OnDateSelectedListener  {

	public static final String DATE_PICKER = "datePicker";
	public static final String DATE_FORMAT_UK = "dd/MM/yyyy";

	private static final String BUNDLE_FR_DATE = "BUNDLE_FR_DATE";

	public static final String BUNDLE_VEHICLE_ID = "vehicleId";

	public static final String BUNDLE_FUEL_RECORD = "fuelRecord";

	final DecimalFormat decimalFormat = new DecimalFormat("0.00");

	private VehicleDao vehicleDao;
	private Vehicle vehicle;

	private SharedPreferences preferences;

	private Calendar recordDate;

	private FuelRecord record;
	private Integer maxOdoReading = -1;

	private String maxOdoString;
	private Long vehicleId;

	private EditText editDate, editCost, editFuelVolume, editOdometer;
	private TextInputLayout tilVolume, tilCost, tilOdo;

	private TextView vehicleInfo, costPerVolume;
	private Button buttonSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_fuel);

		// restore state?
		if (null != savedInstanceState) {
			recordDate = (Calendar) savedInstanceState.getSerializable(BUNDLE_FR_DATE);
		} else {
			recordDate = null;
		}

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		buildUI();
		updateCostPerFuelVolume();
	}

	private void buildUI() {
		Bundle bundle = getIntent().getExtras();
		if (null != bundle && null != bundle.get(BUNDLE_VEHICLE_ID)) {
			vehicleId = bundle.getLong(BUNDLE_VEHICLE_ID);
		} else {
			Log.e("RecordFuelActivity", "No vehicleId was specified when trying to build UI");
			return;
		}
		// load vehicle
		DaoSession daoSession = ((GreenFuel) getApplication()).getDaoSession();
		vehicleDao = daoSession.getVehicleDao();
		vehicle = vehicleDao.load(vehicleId);
		maxOdoReading = vehicle.getMaxOdo();
		StringBuilder maxOdoSb = new StringBuilder();
		if (vehicle != null) {
			if (vehicle.getMaxOdo() != -1) {
				maxOdoSb.append(vehicle.getMaxOdo()).append(" ");
			}
			maxOdoSb.append(vehicle.getDistanceIsMetric() ? getStringForRes(R.string.lbl_UnitKilometers) : getStringForRes(R.string.lbl_UnitMiles));
		}
		maxOdoString = maxOdoSb.toString();

		editDate = (EditText) findViewById(R.id.editDate);
		editDate.requestFocus();
		editFuelVolume = (EditText) findViewById(R.id.editLitres);
//		editFuelVolume.requestFocus();

		tilVolume = (TextInputLayout) findViewById(R.id.tilVolume);
		tilCost = (TextInputLayout) findViewById(R.id.tilCost);

		editCost = (EditText) findViewById(R.id.editCost);
		tilCost.setHint(getStringForRes(R.string.hnt_totalCost) + " (" + Currency.getInstance(Locale.getDefault()).getSymbol() + ")");
		editOdometer = (EditText) findViewById(R.id.editOdometer);
		tilOdo = (TextInputLayout) findViewById(R.id.tilOdo);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		costPerVolume = (TextView) findViewById(R.id.lbl_costPerUnit);

		// vehicle summary
		vehicleInfo = (TextView) findViewById(R.id.lblVehicleInfo);

		if (null != vehicle) {
			vehicleInfo.setText(String.format("%s %s, %s", vehicle.getManufacturer(), vehicle.getModel(), vehicle.getRegistration()));
		}

		editFuelVolume.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					tilVolume.setHint(vehicle.getFuelVolumeIsMetric() ? getStringForRes(R.string.lbl_UnitLitres) : getStringForRes(R.string.lbl_UnitGallons));
				} else {
					tilVolume.setHint(getStringForRes(R.string.hnt_fuelVolume));
				}
				updateCostPerFuelVolume();
			}
		});

		editOdometer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					tilOdo.setHint(maxOdoString);
				} else {
					tilOdo.setHint(getStringForRes(R.string.lbl_Odometer));
				}
				updateCostPerFuelVolume();
			}
		});

		// date picker
		editDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editDate.setError(null);
				android.support.v4.app.DialogFragment datePickerFragment = new DatePickerFragment();

				// if date already has a value, pass it to the fragment as an argument Bundle
				if (!TextUtils.isEmpty(editDate.getText())) {
					Bundle currentlySelectedDateBundle = new Bundle();
					currentlySelectedDateBundle.putInt("day", recordDate.get(Calendar.DAY_OF_MONTH));
					currentlySelectedDateBundle.putInt("month", recordDate.get(Calendar.MONTH));
					currentlySelectedDateBundle.putInt("year", recordDate.get(Calendar.YEAR));
					datePickerFragment.setArguments(currentlySelectedDateBundle);
				}
				datePickerFragment.show(getSupportFragmentManager(), DATE_PICKER);
			}
		});

		buttonSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validateInput(v)) {
					BigDecimal cost;

					cost = new BigDecimal(editCost.getText().toString());
					Integer odoReading = null;
					if (!editOdometer.getText().toString().isEmpty()) {
						odoReading = Integer.parseInt(editOdometer.getText().toString());
					}
					record = new FuelRecord(recordDate.getTime(), cost, new BigDecimal(editFuelVolume.getText().toString()), odoReading, vehicleId);
					Intent result = new Intent();
					result.putExtra(BUNDLE_FUEL_RECORD, record);
					setResult(RESULT_OK, result);
					finish();
				}
			}
		});
	}

	private boolean validateInput(View v) {
		boolean valid = true;

		if (TextUtils.isEmpty(editDate.getText())) {
			valid = false;
			editDate.setError(getString(R.string.error_RecordFuel_DateEmpty));
		}
		if (TextUtils.isEmpty(editCost.getText())) {
			valid = false;
			editCost.setError(getString(R.string.error_RecordFuel_CostEmpty));
		}
		if (TextUtils.isEmpty(editFuelVolume.getText())) {
			valid = false;
			editFuelVolume.setError(getString(R.string.error_RecordFuel_LitresEmpty));
		}
		if (!valid) {
			Snackbar snackbar = Snackbar.make(v, R.string.error_CorrectInput, Snackbar.LENGTH_SHORT);
			snackbar.show();
		}

		return valid;
	}

	/**
	 * Override DatePickerFragment.OnDateSelectedListener.setDate
	 * This is how we communicate the date back from the DatePickerFragment
	 *
	 * @param date the chosen date
	 * @param dialogTag unique identifier
	 */
	@Override
	public void setDate(Calendar date, String dialogTag) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_UK, Locale.getDefault());
		// set the date onto our field

		switch (dialogTag) {
			case DATE_PICKER:
				recordDate = date;
				editDate.setText(sdf.format(recordDate.getTime()));
				break;
			default:
				Toast toast = Toast.makeText(this, "Unknown dialog tag: " + dialogTag, Toast.LENGTH_SHORT);
				toast.show();
		}
	}

	private String getStringForRes(@StringRes int stringResId) {
		return getApplicationContext().getString(stringResId);
	}

	private void updateCostPerFuelVolume() {
		StringBuilder sb = new StringBuilder();
		if (!editCost.getText().toString().isEmpty() && !editFuelVolume.getText().toString().isEmpty()) {
			Float costPerUnitVolume = Float.parseFloat(editCost.getText().toString()) / Float.parseFloat(editFuelVolume.getText().toString());
			sb.append(Currency.getInstance(Locale.getDefault()).getSymbol());
			sb.append(decimalFormat.format(costPerUnitVolume));
			sb.append("/");
			sb.append(vehicle.getFuelVolumeIsMetric() ? getStringForRes(R.string.lbl_UnitLitres) : getStringForRes(R.string.lbl_UnitGallons));
		}

		costPerVolume.setText(sb.toString());
	}


	/**
	 * Handle device changes, eg orientation
	 *
	 * @param outState current status bundle to save
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putSerializable(BUNDLE_FR_DATE, recordDate);
	}

	/**
	 * Restore state after a device change
	 *
	 * @param savedInstanceState status bundle
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (null != savedInstanceState) {
			recordDate = (Calendar) savedInstanceState.getSerializable(BUNDLE_FR_DATE);
		}
	}

}
