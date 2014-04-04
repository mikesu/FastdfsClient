package net.mikesu.fastdfs.protocol;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StorageInfoCmd extends AbstractCmd<List<StorageInfo>> {

	@Override
	public List<StorageInfo> exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		byte[] data = response(socket.getInputStream());
		List<StorageInfo> storageInfos = new ArrayList<StorageInfo>();
		int dataLength = data.length;
		if(dataLength%StorageInfo.BYTE_SIZE!=0){
			throw new IOException("recv body length: " + data.length + " is not correct");
		}
		int offset = 0;
		while(offset<dataLength){
			StorageInfo storageInfo = new StorageInfo(data,offset);
			storageInfos.add(storageInfo);
			offset += StorageInfo.BYTE_SIZE;
		}
		return storageInfos;
	}

	public StorageInfoCmd(String group) throws UnsupportedEncodingException {
		super();
		int group_len;
		byte[] bs = group.getBytes("UTF-8");
		body1 = new byte[FDFS_GROUP_NAME_MAX_LEN];
		if (bs.length <= FDFS_GROUP_NAME_MAX_LEN) {
			group_len = bs.length;
		} else {
			group_len = FDFS_GROUP_NAME_MAX_LEN;
		}
		Arrays.fill(body1, (byte) 0);
		System.arraycopy(bs, 0, body1, 0, group_len);
		this.requestCmd = TRACKER_PROTO_CMD_SERVER_LIST_STORAGE;
		this.requestSize = body1.length;
		this.responseCmd = TRACKER_PROTO_CMD_RESP;
		this.responseSize = -1;
	}

	public StorageInfoCmd(String group,String ip) throws UnsupportedEncodingException {
		super();
		int group_len;
		byte[] groupByte = group.getBytes("UTF-8");
		byte[] ipByte = ip.getBytes("UTF-8");
		body1 = new byte[FDFS_GROUP_NAME_MAX_LEN + ipByte.length];
		if (groupByte.length <= FDFS_GROUP_NAME_MAX_LEN) {
			group_len = groupByte.length;
		} else {
			group_len = FDFS_GROUP_NAME_MAX_LEN;
		}
		Arrays.fill(body1, (byte) 0);
		System.arraycopy(groupByte, 0, body1, 0, group_len);
		System.arraycopy(ipByte, 0, body1, FDFS_GROUP_NAME_MAX_LEN, ipByte.length);
		this.requestCmd = TRACKER_PROTO_CMD_SERVER_LIST_STORAGE;
		this.requestSize = body1.length;
		this.responseCmd = TRACKER_PROTO_CMD_RESP;
		this.responseSize = -1;
	}
	
	

}
