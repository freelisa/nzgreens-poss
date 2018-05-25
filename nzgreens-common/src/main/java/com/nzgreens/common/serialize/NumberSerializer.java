package com.nzgreens.common.serialize;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author hcf
 * @version V1.0
 * @date 2016/9/14 17:15
 */
public class NumberSerializer implements ObjectSerializer {

	private DecimalFormat format = new DecimalFormat("###0.00");

	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
					  int features) throws IOException {
		if (object instanceof BigDecimal) {
			BigDecimal value = (BigDecimal) object;
			String text = format.format(value);
			serializer.write(text);
		} else {
			serializer.write(object);
		}
	}
}
