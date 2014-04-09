package net.mikesu.fastdfs;

import java.io.File;

public interface FastdfsClient {
	
	public String upload(File file) throws Exception;
	public String upload(File file,String fileName) throws Exception;
	public String getUrl(String fileId) throws Exception;
	public Boolean delete(String fileId) throws Exception;
	public void close();

}
