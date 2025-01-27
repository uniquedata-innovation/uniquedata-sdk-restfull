package br.com.uniquedata.restfull.sdk.pojo;

public class ExtractMethod {
	
	private String methodName;
	
	private Object methodValue;

	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(final String methodName) {
		this.methodName = methodName;
	}
	
	public Object getMethodValue() {
		return methodValue;
	}
	
	public void setMethodValue(final Object methodValue) {
		this.methodValue = methodValue;
	}

	public ExtractMethod() {}
	
	public ExtractMethod(final String methodName, final Object methodValue) {
		this.methodName = methodName;
		this.methodValue = methodValue;
	}
	
}
