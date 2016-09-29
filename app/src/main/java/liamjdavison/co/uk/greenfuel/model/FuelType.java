package liamjdavison.co.uk.greenfuel.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Liam Davison on 29/09/2016.
 */
@Entity
public class FuelType {

	@Id(autoincrement = true)
	private Long id;

	@NotNull
	private String name;

	public FuelType(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Generated(hash = 1603975810)
	public FuelType(Long id, @NotNull String name) {
		this.id = id;
		this.name = name;
	}

	@Generated(hash = 1777130301)
	public FuelType() {
	}

	@Override
	public String toString() {
		return this.id + " " + this.getName();
	}
}
