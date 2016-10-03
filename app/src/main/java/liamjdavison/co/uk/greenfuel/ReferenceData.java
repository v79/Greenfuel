package liamjdavison.co.uk.greenfuel;

import java.util.List;

import liamjdavison.co.uk.greenfuel.model.FuelType;

/**
 * Created by Liam Davison on 01/10/2016.
 */

public interface ReferenceData {

	public List<FuelType> getFuelTypes();

	public FuelType getFuelTypeWithName(String name);
}
