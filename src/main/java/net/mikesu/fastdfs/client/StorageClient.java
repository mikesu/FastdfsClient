package net.mikesu.fastdfs.client;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.mikesu.fastdfs.data.Result;

public interface StorageClient {
	
	public Result<String> upload(File file,String fileName,byte storePathIndex) throws IOException;
	public Result<Boolean> delete(String group,String fileName) throws IOException;
	public Result<Boolean> setMeta(String group,String fileName,Map<String,String> meta) throws IOException;
	public Result<Map<String,String>> getMeta(String group,String fileName) throws IOException;
	public void close() throws IOException;
	
}
