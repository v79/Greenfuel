package liamjdavison.co.uk.greenfuel;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import liamjdavison.co.uk.greenfuel.model.DaoMaster;
import liamjdavison.co.uk.greenfuel.model.DaoSession;

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
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}
}
