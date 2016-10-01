package liamjdavison.co.uk.greenfuel;

import android.app.Application;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

import liamjdavison.co.uk.greenfuel.model.DaoMaster;
import liamjdavison.co.uk.greenfuel.model.DaoSession;
import liamjdavison.co.uk.greenfuel.model.FuelType;
import liamjdavison.co.uk.greenfuel.model.FuelTypeDao;

/**
 * Root application for GreenFuel. Responsible for initialising the app and reference data
 * Created by Liam Davison on 08/08/2016.
 */
public class GreenFuel extends Application {

	public static final boolean ENCRYPTED = false;

	private DaoSession daoSession;

	@Override
	public void onCreate() {
		super.onCreate();

		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"fuelrecord-model");
		Database db = helper.getWritableDb();
		daoSession = new DaoMaster(db).newSession();

		buildReferenceData();
	}

	private void buildReferenceData() {
		FuelTypeDao fuelTypeDao = daoSession.getFuelTypeDao();
		List<FuelType> fuelTypeList = fuelTypeDao.loadAll();
		if (fuelTypeList == null || fuelTypeList.size() == 0) {
			Log.d("BuildRefData", "No fuel type records exist, creating");
			fuelTypeList = new ArrayList<FuelType>();
			fuelTypeList.add(new FuelType("Petrol"));
			fuelTypeList.add(new FuelType("Diesel"));
			for (FuelType ft : fuelTypeList) {
				fuelTypeDao.save(ft);
			}
		}
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}
}
