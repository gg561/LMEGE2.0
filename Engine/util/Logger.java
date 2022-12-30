package util;

public class Logger {
	
	private String className;
	
	public Logger(String className) {
		this.className = className;
	}
	
	public void info(Object... details) {
		System.out.println("[ " + className + " ]" + " : " + parseObjects(details));
	}
	
	private String parseObjects(Object... objs) {
		if(objs.length == 1) {
			if(objs[0] instanceof String) {
				return (String) objs[0];
			}
			return objs[0].toString();
		}
		StringBuilder sb = new StringBuilder();
		for(Object obj : objs) {
			sb.append(obj);
		}
		return sb.toString();
	}

}
