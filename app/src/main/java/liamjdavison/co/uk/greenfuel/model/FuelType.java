package liamjdavison.co.uk.greenfuel.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * A simple table representing a fuel type, e.g. Petrol or Diesel
 * Created by Liam Davison on 29/09/2016.
 */
@Entity
public class FuelType {

	@Id(autoincrement = true)
	private Long id;

	private String name;

	public FuelType(String name) {
		this.name = name;
	}

	@Generated(hash = 234358082)
	public FuelType(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Generated(hash = 1777130301)
	public FuelType() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
