package liamjdavison.co.uk.greenfuel.model;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.math.BigDecimal;

/**
 * Created by Liam Davison on 08/08/2016.
 */
public class BigDecimalConverter implements PropertyConverter<BigDecimal, Long> {
	@Override
	public BigDecimal convertToEntityProperty(Long databaseValue) {
		return new BigDecimal(databaseValue);
	}

	@Override
	public Long convertToDatabaseValue(BigDecimal entityProperty) {
		return entityProperty.longValue();
	}
}
