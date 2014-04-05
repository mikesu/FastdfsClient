package net.mikesu.fastdfs.client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import net.mikesu.fastdfs.command.CloseCmd;
import net.mikesu.fastdfs.command.Command;
import net.mikesu.fastdfs.command.DeleteCmd;
import net.mikesu.fastdfs.command.UploadCmd;
import net.mikesu.fastdfs.data.Result;

public class StorageClientImpl implements StorageClient{
	
	private Socket socket;
	
	public StorageClientImpl(String address) throws NumberFormatException, UnknownHostException, IOException {
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

	public Result<String> upload(File file,String fileName,byte storePathIndex) throws IOException{
		UploadCmd uploadCmd = new UploadCmd(file, fileName,storePathIndex);
		return uploadCmd.exec(socket);
	}
	
	public Result<Boolean> delete(String group,String fileName) throws IOException{
		DeleteCmd deleteCmd = new DeleteCmd(group,fileName);
		return deleteCmd.exec(socket);
	}

}
