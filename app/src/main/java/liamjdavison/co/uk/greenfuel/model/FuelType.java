package liamjdavison.co.uk.greenfuel.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Liam Davison on 29/09/2016.
 */
public class FuelType {

	private Boolean metric;
	private String name;

	public FuelType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getMetric() {
		return this.metric;
	}

	public void setMetric(Boolean isMetric) {
		this.metric = isMetric;
	}

	@Override
	public String toString() {
		return this.getName() + "(Metric: " + this.getMetric() + ")";
	}
}
