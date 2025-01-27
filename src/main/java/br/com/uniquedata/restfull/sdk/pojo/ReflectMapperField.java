package br.com.uniquedata.restfull.sdk.pojo;

import java.lang.reflect.Field;

public class ReflectMapperField {
	
	private Field fieldIn;
	
	private Field fieldOut;

	public ReflectMapperField() {}
	
	public ReflectMapperField(final Field fieldIn, final Field fieldOut) {
		this.fieldIn = fieldIn;
		this.fieldOut = fieldOut;
	}
	
	public boolean isMatch() {
		return fieldIn != null && fieldOut != null;
	}
	
	public Field getFieldIn() {
		return fieldIn;
	}

	public void setFieldIn(final Field fieldIn) {
		this.fieldIn = fieldIn;
	}

	public Field getFieldOut() {
		return fieldOut;
	}

	public void setFieldOut(final Field fieldOut) {
		this.fieldOut = fieldOut;
	}

}
