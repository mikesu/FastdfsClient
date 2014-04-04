package net.mikesu.fastdfs.client;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.mikesu.fastdfs.FastdfsClient;
import net.mikesu.fastdfs.protocol.GroupInfo;
import net.mikesu.fastdfs.protocol.StorageInfo;
import net.mikesu.fastdfs.protocol.UploadStorage;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;

public class DefaultFastdfsClient implements FastdfsClient{
	
	private GenericKeyedObjectPool<String, TrackerClient> trackerClientPool;
	private GenericKeyedObjectPool<String, StorageClient> storageClientPool;
	private Map<String,String> storageIpMap = new ConcurrentHashMap<String, String>();
	
	
	public DefaultFastdfsClient() throws Exception {
		super();
		trackerClientPool = new GenericKeyedObjectPool<>(new TrackerClientFactory());
		storageClientPool = new GenericKeyedObjectPool<>(new StorageClientFactory());
		updateStorageIpMap();
	}
	
	private void updateStorageIpMap() throws Exception{
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = trackerClientPool.borrowObject(trackerAddr);
		List<GroupInfo> groupInfos = trackerClient.getGroupInfos();
		for(GroupInfo groupInfo:groupInfos){
			List<StorageInfo>  storageInfos= trackerClient.getStorageInfos(groupInfo.getGroupName());
			for(StorageInfo storageInfo:storageInfos){
				storageIpMap.put(storageInfo.getIpAddr()+":"+storageInfo.getStoragePort(), storageInfo.getDomainName()+":"+storageInfo.getStorageHttpPort());
			}
		}
		trackerClientPool.returnObject(trackerAddr, trackerClient);
	}
	
	private String getDownloadHostPort(String storageAddr) throws Exception{
		String downloadHostPort = storageIpMap.get(storageAddr);
		if(downloadHostPort==null){
			updateStorageIpMap();
			downloadHostPort = storageIpMap.get(storageAddr);
		}
		return downloadHostPort;
	}
	
	public String getUrl(String fileId) throws Exception{
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = trackerClientPool.borrowObject(trackerAddr);
		String storageAddr = trackerClient.getDownloadStorageAddr(fileId);
		String hostPort = getDownloadHostPort(storageAddr);
		trackerClientPool.returnObject(trackerAddr, trackerClient);
		return "http://"+hostPort+fileId.substring(fileId.indexOf("/"));
	}
	
	public String upload(File file) throws Exception{
		String fileName = file.getName();
		return upload(file, fileName);
	}

	public String upload(File file,String fileName) throws Exception{
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = trackerClientPool.borrowObject(trackerAddr);
		UploadStorage uploadStorage = trackerClient.getUploadStorage();
		StorageClient storageClient = storageClientPool.borrowObject(uploadStorage.getAddress());
		String fileId = storageClient.upload(file,fileName,uploadStorage.getPathIndex());
		storageClientPool.returnObject(uploadStorage.getAddress(), storageClient);
		trackerClientPool.returnObject(trackerAddr, trackerClient);
		return fileId;
	}

	
	public String getTrackerAddr(){
		return "10.125.176.138:22122";
	}
	

	

}
