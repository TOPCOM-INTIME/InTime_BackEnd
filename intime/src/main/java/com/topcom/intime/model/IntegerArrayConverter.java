package com.topcom.intime.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class IntegerArrayConverter implements AttributeConverter<List<Integer>, String> {

	private static final String SPLIT_CHAR = ",";

	  @Override
	  public String convertToDatabaseColumn(List<Integer> attribute) {
		  System.out.println("TAG : " + attribute);
		  if(attribute == null) {
			  return null;
		  }
	    return attribute.stream().map(String::valueOf).collect(Collectors.joining(SPLIT_CHAR));
	  }

	  @Override
	  public List<Integer> convertToEntityAttribute(String dbData) {
		  
		  if(dbData == null) {
			  System.out.println("TAG - dbDATA : " + dbData);
			  return null;
		  }
		  return Arrays.stream(dbData.split(SPLIT_CHAR))
				  .map(Integer::parseInt)
				  .collect(Collectors.toList());

	  }
	  
}
