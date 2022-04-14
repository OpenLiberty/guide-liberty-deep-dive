package it.io.openliberty.deepdive.rest;

public class SystemData {

	private int id;
	private String hostname;
	private String osName;
	private String javaVersion;
	private Long heapSize;
	
    public SystemData() {
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public Long getHeapSize() {
		return heapSize;
	}

	public void setHeapSize(Long heapSize) {
		this.heapSize = heapSize;
	}

}
