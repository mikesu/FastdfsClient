package net.mikesu.fastdfs.client;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class TrackerClientFactory implements KeyedPooledObjectFactory<String,TrackerClient> {

	@Override
	public PooledObject<TrackerClient> makeObject(String key) throws Exception {
		TrackerClient trackerClient = new TrackerClient(key);
		PooledObject<TrackerClient> pooledTrackerClient = new DefaultPooledObject<TrackerClient>(trackerClient);
		return pooledTrackerClient;
	}

	@Override
	public void destroyObject(String key, PooledObject<TrackerClient> pooledTrackerClient)
			throws Exception {
		TrackerClient trackerClient = pooledTrackerClient.getObject();
		trackerClient.close();
	}

	@Override
	public boolean validateObject(String key, PooledObject<TrackerClient> p) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void activateObject(String key, PooledObject<TrackerClient> p)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void passivateObject(String key, PooledObject<TrackerClient> p)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	

}
