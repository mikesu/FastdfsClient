package net.mikesu.fastdfs;

import java.io.File;
import java.util.Map;

public interface FastdfsClient {
	
	public String upload(File file) throws Exception;
	public String upload(File file,String fileName) throws Exception;
	public String getUrl(String fileId) throws Exception;
	public Boolean setMeta(String fileId,Map<String,String> meta) throws Exception;
	public Map<String,String> getMeta(String fileId) throws Exception;
	public Boolean delete(String fileId) throws Exception;
	public void close();

}
