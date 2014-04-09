package net.mikesu.fastdfs;

import static org.junit.Assert.*;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

public class FastdfsClientConfigTest {

	@Test
	public void testFastdfsClientConfigString() throws ConfigurationException {
		FastdfsClientConfig fastdfsClientConfig = new FastdfsClientConfig("FastdfsClient.properties");
		assertEquals(5*1000, fastdfsClientConfig.getConnectTimeout());
		assertEquals(30*1000,fastdfsClientConfig.getNetworkTimeout());
	}

}
