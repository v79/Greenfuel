package liamjdavison.co.uk.greenfuel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import liamjdavison.co.uk.greenfuel.model.DaoSession;
import liamjdavison.co.uk.greenfuel.model.FuelType;
import liamjdavison.co.uk.greenfuel.model.FuelTypeDao;
import liamjdavison.co.uk.greenfuel.model.Vehicle;
import liamjdavison.co.uk.greenfuel.model.VehicleDao;
import liamjdavison.co.uk.greenfuel.fragments.RecordVehicleFragment;

/**
 * Activity to create a new {@link Vehicle} and save it
 * Created by Liam Davison on 08/08/2016.
 */
public class RegisterVehicleActivity extends AppCompatActivity implements RecordVehicleFragment.OnVehicleCreatedListener, ReferenceData {

	private DaoSession daoSession;

	private VehicleDao vehicleDao;

	private FuelTypeDao fuelTypeDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(getApplicationContext().getString(R.string.register_vehicle_title));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_vehicle);

		daoSession = ((GreenFuel) getApplication()).getDaoSession();
	}

	@Override
	public void onVehicleCreated(Vehicle vehicle) {

		vehicleDao = daoSession.getVehicleDao();
		vehicleDao.save(vehicle);

		Intent result = new Intent();
		result.putExtra("newVehicle", vehicle.getId());
		setResult(RESULT_OK, result);
		finish();
	}

	@Override
	public List<FuelType> getFuelTypes() {
		fuelTypeDao = getFuelTypeDao();
		return fuelTypeDao.loadAll();
	}

	@Override
	public FuelType getFuelTypeWithName(String name) {
		if (getFuelTypes() != null) {
			for (FuelType ft : getFuelTypes()) {
				if (ft.getName().equals(name)) {
					return ft;
				}
			}
		}
		return null;
	}

	private FuelTypeDao getFuelTypeDao() {
		if (daoSession == null) {
			daoSession = ((GreenFuel) getApplication()).getDaoSession();
		}
		return daoSession.getFuelTypeDao();
	}
}
