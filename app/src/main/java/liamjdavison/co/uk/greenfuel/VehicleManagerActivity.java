package liamjdavison.co.uk.greenfuel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import liamjdavison.co.uk.greenfuel.model.DaoSession;
import liamjdavison.co.uk.greenfuel.model.VehicleDao;

/**
 * Created by Liam Davison on 28/01/2017.
 */

public class VehicleManagerActivity extends AppCompatActivity {

	private TextView numVehicles;

	private Long vehicleCount = 0l;

	private VehicleDao vehicleDao;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle_manager);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		DaoSession daoSession = ((GreenFuel) getApplication()).getDaoSession();
		vehicleDao = daoSession.getVehicleDao();
		vehicleCount = vehicleDao.count();

		buildLayout();
	}

	private void buildLayout() {
		numVehicles = (TextView) findViewById(R.id.numVehicles);
		numVehicles.setText(Long.toString(vehicleCount));
	}
}
