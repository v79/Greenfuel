package liamjdavison.co.uk.greenfuel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import liamjdavison.co.uk.greenfuel.model.DaoSession;
import liamjdavison.co.uk.greenfuel.model.Vehicle;
import liamjdavison.co.uk.greenfuel.model.VehicleDao;
import liamjdavison.co.uk.greenfuel.fragments.RecordVehicleFragment;

/**
 * Created by Liam Davison on 08/08/2016.
 */
public class RegisterVehicleActivity extends AppCompatActivity implements RecordVehicleFragment.OnVehicleCreatedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(getApplicationContext().getString(R.string.register_vehicle_title));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_vehicle);
	}

	@Override
	public void onVehicleCreated(Vehicle vehicle) {

		DaoSession daoSession = ((GreenFuel) getApplication()).getDaoSession();
		VehicleDao vehicleDao = daoSession.getVehicleDao();

		vehicleDao.save(vehicle);

		Intent result = new Intent();
		result.putExtra("newVehicle", vehicle.getId());
		setResult(RESULT_OK, result);
		finish();
	}
}
