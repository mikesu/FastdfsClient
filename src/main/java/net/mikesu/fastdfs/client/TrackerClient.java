package net.mikesu.fastdfs.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import net.mikesu.fastdfs.protocol.CloseCmd;
import net.mikesu.fastdfs.protocol.Command;
import net.mikesu.fastdfs.protocol.GroupInfo;
import net.mikesu.fastdfs.protocol.GroupInfoCmd;
import net.mikesu.fastdfs.protocol.QueryDownloadCmd;
import net.mikesu.fastdfs.protocol.QueryUploadCmd;
import net.mikesu.fastdfs.protocol.StorageInfo;
import net.mikesu.fastdfs.protocol.StorageInfoCmd;
import net.mikesu.fastdfs.protocol.UploadStorage;

public class TrackerClient {
	
	private String address;
	private Socket socket;
	
	
	public TrackerClient(String address) throws NumberFormatException, UnknownHostException, IOException {
		super();
		this.address = address;
		String[] hostport = address.split(":");
		socket = new Socket(hostport[0],Integer.valueOf(hostport[1]));
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public void close() throws IOException{
		Command<String> command = new CloseCmd();
		command.exec(socket);
		socket.close();
		socket = null;
	}

	
	public UploadStorage getUploadStorage() throws IOException{
		Command<UploadStorage> command = new QueryUploadCmd();
		return command.exec(socket);
	}
	
	public String getDownloadStorageAddr(String fileId) throws IOException{
		Command<String> cmd = new QueryDownloadCmd(fileId);
		return cmd.exec(socket);
	}
	
	public List<GroupInfo> getGroupInfos() throws IOException{
		Command<List<GroupInfo>> cmd = new GroupInfoCmd();
		return cmd.exec(socket);
	}
	
	public List<StorageInfo> getStorageInfos(String group) throws IOException{
		Command<List<StorageInfo>> cmd = new StorageInfoCmd(group);
		return cmd.exec(socket);
	}
	
}
