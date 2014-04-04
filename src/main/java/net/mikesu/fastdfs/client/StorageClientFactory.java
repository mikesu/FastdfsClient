package net.mikesu.fastdfs.client;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class StorageClientFactory implements KeyedPooledObjectFactory<String, StorageClient> {

	@Override
	public PooledObject<StorageClient> makeObject(String key) throws Exception {
		StorageClient storageClient = new StorageClient(key);
		PooledObject<StorageClient> pooledStorageClient = new DefaultPooledObject<StorageClient>(storageClient);
		return pooledStorageClient;
	}

	@Override
	public void destroyObject(String key, PooledObject<StorageClient> pooledStorageClient)
			throws Exception {
		StorageClient storageClient = pooledStorageClient.getObject();
		storageClient.close();
	}

	@Override
	public boolean validateObject(String key, PooledObject<StorageClient> p) {
		return true;
	}

	@Override
	public void activateObject(String key, PooledObject<StorageClient> p)
			throws Exception {
		
	}

	@Override
	public void passivateObject(String key, PooledObject<StorageClient> p)
			throws Exception {
		
	}



}
