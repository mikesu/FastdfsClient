package net.mikesu.fastdfs.data;

public class UploadStorage {
	
	private String address;
	private byte pathIndex;
	public UploadStorage(String address, byte pathIndex) {
		super();
		this.address = address;
		this.pathIndex = pathIndex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public byte getPathIndex() {
		return pathIndex;
	}
	public void setPathIndex(byte pathIndex) {
		this.pathIndex = pathIndex;
	}
	
	

}
