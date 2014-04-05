package net.mikesu.fastdfs.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import net.mikesu.fastdfs.command.CloseCmd;
import net.mikesu.fastdfs.command.Command;
import net.mikesu.fastdfs.command.GroupInfoCmd;
import net.mikesu.fastdfs.command.QueryDownloadCmd;
import net.mikesu.fastdfs.command.QueryUpdateCmd;
import net.mikesu.fastdfs.command.QueryUploadCmd;
import net.mikesu.fastdfs.command.StorageInfoCmd;
import net.mikesu.fastdfs.data.GroupInfo;
import net.mikesu.fastdfs.data.Result;
import net.mikesu.fastdfs.data.StorageInfo;
import net.mikesu.fastdfs.data.UploadStorage;

public class TrackerClientImpl implements TrackerClient{
	
	private Socket socket;
	
	
	public TrackerClientImpl(String address) throws NumberFormatException, UnknownHostException, IOException {
		super();
		String[] hostport = address.split(":");
		socket = new Socket(hostport[0],Integer.valueOf(hostport[1]));
	}

	public void close() throws IOException{
		Command<Boolean> command = new CloseCmd();
		command.exec(socket);
		socket.close();
		socket = null;
	}

	
	public Result<UploadStorage> getUploadStorage() throws IOException{
		Command<UploadStorage> command = new QueryUploadCmd();
		return command.exec(socket);
	}
	
	public Result<String> getUpdateStorageAddr(String group,String fileName) throws IOException{
		Command<String> cmd = new QueryUpdateCmd(group,fileName);
		return cmd.exec(socket);
	}
	
	public Result<String> getDownloadStorageAddr(String group,String fileName) throws IOException{
		Command<String> cmd = new QueryDownloadCmd(group,fileName);
		return cmd.exec(socket);
	}
	
	public Result<List<GroupInfo>> getGroupInfos() throws IOException{
		Command<List<GroupInfo>> cmd = new GroupInfoCmd();
		return cmd.exec(socket);
	}
	
	public Result<List<StorageInfo>> getStorageInfos(String group) throws IOException{
		Command<List<StorageInfo>> cmd = new StorageInfoCmd(group);
		return cmd.exec(socket);
	}
	
}
