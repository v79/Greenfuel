package liamjdavison.co.uk.greenfuel;

import android.app.Application;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import liamjdavison.co.uk.greenfuel.model.DaoMaster;
import liamjdavison.co.uk.greenfuel.model.DaoSession;
import liamjdavison.co.uk.greenfuel.model.FuelType;
import liamjdavison.co.uk.greenfuel.model.FuelTypeDao;

/**
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
		List<FuelType> fuelTypes = fuelTypeDao.loadAll();
		if(fuelTypes == null || fuelTypes.size() == 0) {
			Log.e("ReferenceData","No fuel types found. Creating reference data");
			FuelType petrol = new FuelType(0L,"Petrol");
			FuelType diesel = new FuelType(1L,"Diesel");
			FuelType electric = new FuelType(2L,"Electric");
			fuelTypeDao.save(petrol);
			fuelTypeDao.save(diesel);
			fuelTypeDao.save(electric);
		}
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}
}
