package liamjdavison.co.uk.greenfuel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import liamjdavison.co.uk.greenfuel.model.DaoSession;
import liamjdavison.co.uk.greenfuel.model.FuelRecord;
import liamjdavison.co.uk.greenfuel.model.FuelRecordDao;
import liamjdavison.co.uk.greenfuel.model.Vehicle;
import liamjdavison.co.uk.greenfuel.model.VehicleDao;

/**
 * Main Activity for the FuelRecorder app. Displays a list of {@link FuelRecord} and prompts the user to create another, or to register a {@link Vehicle} if none exists.
 */
public class FuelRecorderOverview extends AppCompatActivity {

	private static final int REGISTER_NEW_VEHICLE = 1;
	private static final int REQUEST_FUEL_RECORD = 2;

	private RecyclerView mFuelRecordsRecyclerView;
	private RecyclerView.LayoutManager mLayoutManager;
	private FuelRecordRecyclerAdapter mFRAdapter;

	private Vehicle vehicle;
	private Long numVehicles = 0l;
	private List<FuelRecord> records;

	private VehicleDao vehicleDao;
	private FuelRecordDao fuelRecordDao;

	private FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fuel__recorder__overview);

		// set up recyclerview
		mFuelRecordsRecyclerView = (RecyclerView) findViewById(R.id.fuelRecordsRecycler);
		mLayoutManager = new LinearLayoutManager(this);
		mFuelRecordsRecyclerView.setLayoutManager(mLayoutManager);

		DaoSession daoSession = ((GreenFuel) getApplication()).getDaoSession();
		vehicleDao = daoSession.getVehicleDao();
		fuelRecordDao = daoSession.getFuelRecordDao();
		numVehicles = vehicleDao.count();

		buildVehicleSummary();

		Toast toast;
		if(vehicle != null) {
			// load fuel records
			records = vehicle.getFuelRecords();
			if (records != null) {
				Collections.sort(records, new FuelRecord.DateDescOrder());
				toast = Toast.makeText(this, "Loaded " + records.size() + " fuel records", Toast.LENGTH_LONG);
			} else {
				toast = Toast.makeText(this,"Click the fuel icon to create a new record", Toast.LENGTH_LONG);
			}
		} else {
			toast = Toast.makeText(this,"Click the car icon to register your first vehicle", Toast.LENGTH_LONG);
		}

		mFRAdapter = new FuelRecordRecyclerAdapter(this,records);
		mFuelRecordsRecyclerView.setAdapter(mFRAdapter);

		if(toast != null) {
			toast.show();
		}
	}

	private void buildVehicleSummary() {
		fab = (FloatingActionButton) findViewById(R.id.fab);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if( numVehicles == 0) {
					// Register a new vehicle
					Intent registerNewVehicleIntent = new Intent(getApplicationContext(), RegisterVehicleActivity.class);
					startActivityForResult(registerNewVehicleIntent, REGISTER_NEW_VEHICLE);
				} else {
					// create a fuel record
					Intent recordFuelPurchaseIntent = new Intent(getApplicationContext(), RecordFuelActivity.class );
					recordFuelPurchaseIntent.putExtra("vehicleId", vehicle.getId());
					startActivityForResult(recordFuelPurchaseIntent, REQUEST_FUEL_RECORD);
				}
			}
		});
		TextView vehicleManufacturer = (TextView) findViewById(R.id.lblVehicleManufacturer);
		TextView vehicleModel = (TextView) findViewById(R.id.lblVehicleModel);
		TextView vehicleEngineSize = (TextView) findViewById(R.id.lblVehicleEngineSize);
		RelativeLayout summaryLayout = (RelativeLayout) findViewById(R.id.layoutSummary);
		if (numVehicles > 0) {
			vehicleModel.setVisibility(View.VISIBLE);
			vehicleEngineSize.setVisibility(View.VISIBLE);
			summaryLayout.setVisibility(View.VISIBLE);

			//TODO get vehicle 0 for now
			vehicle = vehicleDao.loadAll().get(0);

			vehicleManufacturer.setText(vehicle.getManufacturer());
			vehicleModel.setText(vehicle.getModel());
			String engineSize = vehicle.getEngineSize() + " " + getString(R.string.lbl_UnitLitres).toLowerCase();
			vehicleEngineSize.setText(engineSize);
			fab.setImageDrawable(getDrawable(R.drawable.gas_filler));

		} else {
			vehicleManufacturer.setText("Click the 'car' icon to register a new vehicle");
			vehicleModel.setVisibility(View.INVISIBLE);
			vehicleEngineSize.setVisibility(View.INVISIBLE);
			summaryLayout.setVisibility(View.INVISIBLE);
			fab.setImageDrawable(getDrawable(R.drawable.car));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.overview,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("onOptionsItemSelected", String.valueOf(item.getItemId()));
		switch (item.getItemId()) {
			case R.id.menu_developer:
				// show developer activity
				Intent developerIntent = new Intent(this, DeveloperTools.class);
				startActivity(developerIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast toast;

		switch(requestCode) {
			case REGISTER_NEW_VEHICLE:
				long vehicleId;
				if(resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					vehicleId = bundle.getLong("newVehicle");
					vehicle = vehicleDao.load(vehicleId);
					numVehicles = vehicleDao.count();
					buildVehicleSummary();
					toast = Toast.makeText(this, "New vehicle saved: " + vehicle.getId() + " (" + vehicle.getManufacturer() + ")", Toast.LENGTH_LONG);
					toast.show();
					break;
				}
			case REQUEST_FUEL_RECORD:
				if(resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					FuelRecord result = bundle.getParcelable("fuelRecord");
					addFuelRecord(result);

					Collections.sort(records,new FuelRecord.DateDescOrder());
					mFRAdapter.refresh(records);

					toast = Toast.makeText(this,"Fuel record saved: " + result.getId(), Toast.LENGTH_LONG);
					toast.show();
					break;
				}

		}
	}

	private void addFuelRecord(FuelRecord recordToAdd) {
		if(records == null) {
			records = new ArrayList<>();
		}
		fuelRecordDao.save(recordToAdd);
		records.add(recordToAdd);
	}
}
