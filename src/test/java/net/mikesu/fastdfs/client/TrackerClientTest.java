package net.mikesu.fastdfs.client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

public class TrackerClientTest {

//	@Test
	public void testGetUploadStorageAddr() throws NumberFormatException, UnknownHostException, IOException {
		TrackerClient trackerClient = new TrackerClient("10.125.176.138:22122");
		System.out.println("Upload "+trackerClient.getUploadStorage());
		trackerClient.close();
	}
	
//	@Test
	public void testGetDownloadStorageAddr() throws IOException {
		
		TrackerClient trackerClient = new TrackerClient("10.125.176.138:22122");
		System.out.println("Download " + trackerClient.getDownloadStorageAddr("group1/M00/00/00/Cn2wilM00puAa0xSAANVQ4eIxAM143.jpg"));
		trackerClient.close();
	}
	
	@Test
	public void testGetGroupInfos() throws NumberFormatException, UnknownHostException, IOException{
		TrackerClient trackerClient = new TrackerClient("10.125.176.138:22122");
//		trackerClient.getGroupInfos();
		trackerClient.getStorageInfos("group1");
		
		System.out.println();
		trackerClient.close();
	}
}
