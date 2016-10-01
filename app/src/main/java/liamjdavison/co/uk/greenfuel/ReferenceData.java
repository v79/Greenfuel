package liamjdavison.co.uk.greenfuel;

import java.util.List;

import liamjdavison.co.uk.greenfuel.model.FuelType;
import liamjdavison.co.uk.greenfuel.model.FuelTypeDao;

/**
 * Created by Liam Davison on 01/10/2016.
 */

public interface ReferenceData {

	/**
	 * Get a FuelTypeDAO object
	 *
	 * @return FuelTypeDAO
	 */
//	public FuelTypeDao getFuelTypeDao();
	public List<FuelType> getFuelTypes();

	public FuelType getFuelTypeWithName(String name);
}
