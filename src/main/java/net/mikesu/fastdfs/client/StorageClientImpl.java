package net.mikesu.fastdfs.client;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import net.mikesu.fastdfs.FastdfsClientConfig;
import net.mikesu.fastdfs.command.CloseCmd;
import net.mikesu.fastdfs.command.Command;
import net.mikesu.fastdfs.command.DeleteCmd;
import net.mikesu.fastdfs.command.UploadCmd;
import net.mikesu.fastdfs.data.Result;

public class StorageClientImpl implements StorageClient{
	
	private Socket socket;
	private String host;
	private Integer port;
	private Integer connectTimeout = FastdfsClientConfig.DEFAULT_CONNECT_TIMEOUT * 1000;
	private Integer networkTimeout = FastdfsClientConfig.DEFAULT_NETWORK_TIMEOUT * 1000;
	
	private Socket getSocket() throws IOException{
		if(socket==null){
			socket = new Socket();
			socket.setSoTimeout(networkTimeout);
			socket.connect(new InetSocketAddress(host, port),connectTimeout);
		}
		return socket;
	}
	
	public StorageClientImpl(String address){
		super();
		String[] hostport = address.split(":");
		this.host = hostport[0];
		this.port = Integer.valueOf(hostport[1]);
	}
	
	public StorageClientImpl(String address,Integer connectTimeout, Integer networkTimeout){
		this(address);
		this.connectTimeout = connectTimeout;
		this.networkTimeout = networkTimeout;
	}

	public void close() throws IOException{
		Socket socket = getSocket();
		Command<Boolean> command = new CloseCmd();
		command.exec(socket);
		socket.close();
		socket = null;
	}

	public Result<String> upload(File file,String fileName,byte storePathIndex) throws IOException{
		Socket socket = getSocket();
		UploadCmd uploadCmd = new UploadCmd(file, fileName,storePathIndex);
		return uploadCmd.exec(socket);
	}
	
	public Result<Boolean> delete(String group,String fileName) throws IOException{
		Socket socket = getSocket();
		DeleteCmd deleteCmd = new DeleteCmd(group,fileName);
		return deleteCmd.exec(socket);
	}

}
