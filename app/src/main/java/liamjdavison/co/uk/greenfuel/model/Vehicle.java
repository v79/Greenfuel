package liamjdavison.co.uk.greenfuel.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.query.Query;

import java.util.Date;
import java.util.List;

/**
 * A car or other such vehicle. Will own zero or more {@link FuelType} records
 * Created by Liam Davison on 08/08/2016.
 */
@Entity
public class Vehicle {

	@Id(autoincrement = true)
	private Long id;

	@NotNull
	private String manufacturer;

	@NotNull
	private String model;

	@NotNull
	@Unique
	private String registration;

	@NotNull
	private Float engineSize;

	private Integer startOdo;

	private Integer currentOdo;

	@NotNull
	private Date registeredDate;

	@ToMany(referencedJoinProperty = "vehicleId")
	private List<FuelRecord> fuelRecords;

	// join property
	private Long fuelTypeId;

	@ToOne(joinProperty = "fuelTypeId")
	private FuelType fuelType;

	private Boolean distanceIsMetric;

	private Boolean fuelVolumeIsMetric;

	/**
	 * Used for active entity operations.
	 */
	@Generated(hash = 900796925)
	private transient VehicleDao myDao;

	/**
	 * Used to resolve relations
	 */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;

	@Generated(hash = 165448936)
	private transient Long fuelType__resolvedKey;

	@Generated(hash = 601653091)
	public Vehicle(Long id, @NotNull String manufacturer, @NotNull String model,
				   @NotNull String registration, @NotNull Float engineSize, Integer startOdo, Integer currentOdo,
				   @NotNull Date registeredDate, Long fuelTypeId, Boolean distanceIsMetric,
				   Boolean fuelVolumeIsMetric) {
		this.id = id;
		this.manufacturer = manufacturer;
		this.model = model;
		this.registration = registration;
		this.engineSize = engineSize;
		this.startOdo = startOdo;
		this.currentOdo = currentOdo;
		this.registeredDate = registeredDate;
		this.fuelTypeId = fuelTypeId;
		this.distanceIsMetric = distanceIsMetric;
		this.fuelVolumeIsMetric = fuelVolumeIsMetric;
	}

	@Generated(hash = 2006430483)
	public Vehicle() {
	}

	public Date getRegisteredDate() {
		return this.registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public Integer getCurrentOdo() {
		return this.currentOdo;
	}

	public void setCurrentOdo(Integer currentOdo) {
		this.currentOdo = currentOdo;
	}

	public Integer getStartOdo() {
		return this.startOdo;
	}

	public void setStartOdo(Integer startOdo) {
		this.startOdo = startOdo;
	}

	public Float getEngineSize() {
		return this.engineSize;
	}

	public void setEngineSize(Float engineSize) {
		this.engineSize = engineSize;
	}

	public String getRegistration() {
		return this.registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getManufacturer()).append(" ");
		sb.append(this.getModel()).append(" ");
		sb.append(this.getRegistration()).append(" (").append(this.getRegisteredDate()).append(") ");
		sb.append(this.getEngineSize()).append(" ");
		sb.append("(Odo ").append(this.getStartOdo()).append("/").append(this.getCurrentOdo()).append(") ");
		sb.append(this.getFuelType().getName()).append(" ");
		sb.append(this.getDistanceIsMetric() ? "km" : "miles").append(" per ");
		sb.append(this.getFuelVolumeIsMetric() ? "litre" : "gallon");

		return sb.toString();
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 1942392019)
	public void refresh() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.refresh(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 713229351)
	public void update() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.update(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 128553479)
	public void delete() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.delete(this);
	}

	/**
	 * Resets a to-many relationship, making the next get call to query for a fresh result.
	 */
	@Generated(hash = 834597005)
	public synchronized void resetFuelRecords() {
		fuelRecords = null;
	}

	/**
	 * To-many relationship, resolved on first access (and after reset).
	 * Changes to to-many relations are not persisted, make changes to the target entity.
	 */
	@Generated(hash = 690229951)
	public List<FuelRecord> getFuelRecords() {
		if (fuelRecords == null) {
			final DaoSession daoSession = this.daoSession;
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			FuelRecordDao targetDao = daoSession.getFuelRecordDao();
			List<FuelRecord> fuelRecordsNew = targetDao._queryVehicle_FuelRecords(id);
			synchronized (this) {
				if (fuelRecords == null) {
					fuelRecords = fuelRecordsNew;
				}
			}
		}
		return fuelRecords;
	}

	/**
	 * called by internal mechanisms, do not call yourself.
	 */
	@Generated(hash = 1588469812)
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getVehicleDao() : null;
	}

	/**
	 * called by internal mechanisms, do not call yourself.
	 */
	@Generated(hash = 1092170329)
	public void setFuelType(FuelType fuelType) {
		synchronized (this) {
			this.fuelType = fuelType;
			fuelTypeId = fuelType == null ? null : fuelType.getId();
			fuelType__resolvedKey = fuelTypeId;
		}
	}

	/**
	 * To-one relationship, resolved on first access.
	 */
	@Generated(hash = 561729034)
	public FuelType getFuelType() {
		Long __key = this.fuelTypeId;
		if (fuelType__resolvedKey == null || !fuelType__resolvedKey.equals(__key)) {
			final DaoSession daoSession = this.daoSession;
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			FuelTypeDao targetDao = daoSession.getFuelTypeDao();
			FuelType fuelTypeNew = targetDao.load(__key);
			synchronized (this) {
				fuelType = fuelTypeNew;
				fuelType__resolvedKey = __key;
			}
		}
		return fuelType;
	}

	public Long getFuelTypeId() {
		return this.fuelTypeId;
	}

	public void setFuelTypeId(Long fuelTypeId) {
		this.fuelTypeId = fuelTypeId;
	}

	public Boolean getDistanceIsMetric() {
		return distanceIsMetric;
	}

	public void setDistanceIsMetric(Boolean distanceIsMetric) {
		this.distanceIsMetric = distanceIsMetric;
	}

	public Boolean getFuelVolumeIsMetric() {
		return fuelVolumeIsMetric;
	}

	public void setFuelVolumeIsMetric(Boolean fuelVolumeIsMetric) {
		this.fuelVolumeIsMetric = fuelVolumeIsMetric;
	}

	/**
	 * Get the maximum Odometer reading for this vehicle
	 *
	 * @return -1 if not found
	 */
	public Integer getMaxOdo() {
		final DaoSession daoSession = this.daoSession;
		if (daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		FuelRecordDao fuelRecordDao = daoSession.getFuelRecordDao();
		Query maxOdoQuery = fuelRecordDao.queryBuilder().where(FuelRecordDao.Properties.VehicleId.eq(getId())).orderDesc(FuelRecordDao.Properties.Odometer).limit(1).build();
		List<FuelRecord> fuelRecords = maxOdoQuery.list();
		if (fuelRecords != null && fuelRecords.size() > 0) {
			return fuelRecords.get(0).getOdometer();
		}
		return -1;
	}
}


