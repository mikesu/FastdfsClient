package net.mikesu.fastdfs.client;

import java.io.IOException;
import java.util.List;

import net.mikesu.fastdfs.data.GroupInfo;
import net.mikesu.fastdfs.data.Result;
import net.mikesu.fastdfs.data.StorageInfo;
import net.mikesu.fastdfs.data.UploadStorage;

public interface TrackerClient {

	public Result<UploadStorage> getUploadStorage() throws IOException;
	public Result<String> getUpdateStorageAddr(String group,String fileName) throws IOException;
	public Result<String> getDownloadStorageAddr(String group,String fileName) throws IOException;
	public Result<List<GroupInfo>> getGroupInfos() throws IOException;
	public Result<List<StorageInfo>> getStorageInfos(String group) throws IOException;
	public void close() throws IOException;
	
}
