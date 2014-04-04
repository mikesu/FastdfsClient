package net.mikesu.fastdfs.client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import net.mikesu.fastdfs.protocol.CloseCmd;
import net.mikesu.fastdfs.protocol.Command;
import net.mikesu.fastdfs.protocol.UploadCmd;

public class StorageClient {
	
	private String address;
	private Socket socket;
	
	public StorageClient(String address) throws NumberFormatException, UnknownHostException, IOException {
		super();
		this.address = address;
		String[] hostport = address.split(":");
		socket = new Socket(hostport[0],Integer.valueOf(hostport[1]));
	}
	
	public void close() throws IOException{
		Command<String> command = new CloseCmd();
		command.exec(socket);
		socket.close();
		socket = null;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String upload(File file,String fileName,byte storePathIndex) throws IOException{
		UploadCmd uploadCmd = new UploadCmd(file, fileName,storePathIndex);
		return uploadCmd.exec(socket);
	}
	

}
