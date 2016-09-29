package liamjdavison.co.uk.greenfuel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import liamjdavison.co.uk.greenfuel.model.DaoSession;
import liamjdavison.co.uk.greenfuel.model.FuelRecordDao;
import liamjdavison.co.uk.greenfuel.model.Vehicle;
import liamjdavison.co.uk.greenfuel.model.VehicleDao;

public class DeveloperTools extends AppCompatActivity {

	private static final String JSON_FILENAME = "fuelRecord.json";

	private Button btnDeleteDB, btnAddFakeRecords, btnExport, btnImport;
	private TextView numCars, numFuelRecords;

	private VehicleDao vehicleDao;
	private FuelRecordDao fuelRecordDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_developer_tools);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		DaoSession daoSession = ((GreenFuel) getApplication()).getDaoSession();
		vehicleDao = daoSession.getVehicleDao();
		fuelRecordDao = daoSession.getFuelRecordDao();
		buildLayout();

		logAllVehicles();
	}

	private void logAllVehicles() {
		List<Vehicle> vehicles = vehicleDao.loadAll();
		if(vehicles != null && vehicles.size() > 0) {
			for (Vehicle v : vehicles) {
				Log.v("Vehicle " + v.getId(), v.toString());
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			startActivity(new Intent(getApplicationContext(), FuelRecorderOverview.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void buildLayout() {
		numCars = (TextView) findViewById(R.id.debugVehicles);
		numCars.setText(String.valueOf(vehicleDao.count()));

		numFuelRecords = (TextView) findViewById(R.id.debugDBFields);
		numFuelRecords.setText(String.valueOf(fuelRecordDao.count()));

		btnDeleteDB = (Button) findViewById(R.id.btnClearDB);
		btnDeleteDB.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast toast;

				emptyDatabase();

				toast = Toast.makeText(getApplicationContext(),"DATABASE RESET", Toast.LENGTH_SHORT);
				toast.show();
				return true;
			}
		});
	}

	private void emptyDatabase() {
		vehicleDao.deleteAll();
		fuelRecordDao.deleteAll();
		numCars.setText(String.valueOf(vehicleDao.count()));
		numFuelRecords.setText(String.valueOf(fuelRecordDao.count()));
	}

}
