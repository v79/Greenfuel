package liamjdavison.co.uk.greenfuel.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Liam Davison on 08/08/2016.
 */
@Entity
public class FuelRecord implements Parcelable {

	@Id(autoincrement = true)
	private Long id;

	@NotNull
	private Date date;

	@Convert(columnType = Long.class, converter = BigDecimalConverter.class)
	private BigDecimal cost;

	@Convert(columnType = Long.class, converter = BigDecimalConverter.class)
	private BigDecimal fuelVolume;

	private Integer odometer;

	// this is the join parameter; I don't need to specify both sides of the relationship
	private Long vehicleId;

	public FuelRecord(Date date, BigDecimal cost, BigDecimal fuelVolume, @Nullable Integer odometer, Long vehicleId) {
		this.setDate(date);
		this.setCost(cost);
		this.setVehicleId(vehicleId);
		this.odometer = odometer;
		this.fuelVolume = fuelVolume;
	}

	public Integer getOdometer() {
		return this.odometer;
	}

	public void setOdometer(Integer odometer) {
		this.odometer = odometer;
	}

	public BigDecimal getFuelVolume() {
		return this.fuelVolume;
	}

	public void setFuelVolume(BigDecimal fuelVolume) {
		this.fuelVolume = fuelVolume;
	}

	public BigDecimal getCost() {
		return this.cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*
		Implementation of Parcelable, to transfer objects through Intents
	 */

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(date.getTime());
		dest.writeSerializable(cost);
		dest.writeSerializable(fuelVolume);
		if (odometer != null) {
			dest.writeInt(odometer);
		} else {
			dest.writeInt(-1);
		}
		dest.writeLong(vehicleId);
	}

	public static final Parcelable.Creator<FuelRecord> CREATOR = new Parcelable.Creator<FuelRecord>() {
		public FuelRecord createFromParcel(Parcel in) {
			return new FuelRecord(in);
		}

		@Override
		public FuelRecord[] newArray(int size) {
			return new FuelRecord[size];
		}
	};

	private FuelRecord(Parcel in) {
		long tmpDate = in.readLong();
		this.date = tmpDate == -1 ? null : new Date(tmpDate);
		this.cost = (BigDecimal) in.readSerializable();
		this.fuelVolume = (BigDecimal) in.readSerializable();
		this.odometer = in.readInt();
		this.vehicleId = in.readLong();
	}

	public Long getVehicleId() {
		return this.vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	@Generated(hash = 835232797)
	public FuelRecord(Long id, @NotNull Date date, BigDecimal cost,
			BigDecimal fuelVolume, Integer odometer, Long vehicleId) {
		this.id = id;
		this.date = date;
		this.cost = cost;
		this.fuelVolume = fuelVolume;
		this.odometer = odometer;
		this.vehicleId = vehicleId;
	}

	@Generated(hash = 20486409)
	public FuelRecord() {
	}

	/*
	Sorting
	 */
	public static class DateDescOrder implements java.util.Comparator<FuelRecord> {
		@Override
		public int compare(FuelRecord lhs, FuelRecord rhs) {
			return rhs.getDate().compareTo(lhs.getDate());
		}
	}
}
