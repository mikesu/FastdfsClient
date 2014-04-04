package net.mikesu.fastdfs.protocol;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Arrays;

public class QueryDownloadCmd extends AbstractCmd<String> {

	@Override
	public String exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		byte[] body = response(socket.getInputStream());
		if (body.length != TRACKER_QUERY_STORAGE_FETCH_BODY_LEN) {
			throw new IOException("Invalid body length: "+ body.length);
		}
		String ip = new String(body,FDFS_GROUP_NAME_MAX_LEN,FDFS_IPADDR_SIZE - 1).trim();
		int port = (int) buff2long(body, FDFS_GROUP_NAME_MAX_LEN + FDFS_IPADDR_SIZE - 1);
		return ip + ":" + String.valueOf(port);
	}

	public QueryDownloadCmd(String fileId) throws UnsupportedEncodingException {
		super();
		int pos = fileId.indexOf("/");
		
		String group = fileId.substring(0, pos);
		String remoteFileName = fileId.substring(pos+1);
		int group_len;
		byte[] groupByte = group.getBytes("UTF-8");
		byte[] remoteFileNameByte = remoteFileName.getBytes("UTF-8");
		body1 = new byte[FDFS_GROUP_NAME_MAX_LEN + remoteFileNameByte.length];
		if (groupByte.length <= FDFS_GROUP_NAME_MAX_LEN) {
			group_len = groupByte.length;
		} else {
			group_len = FDFS_GROUP_NAME_MAX_LEN;
		}
		Arrays.fill(body1, (byte) 0);
		System.arraycopy(groupByte, 0, body1, 0, group_len);
		System.arraycopy(remoteFileNameByte, 0, body1, FDFS_GROUP_NAME_MAX_LEN, remoteFileNameByte.length);
		this.requestCmd = TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE;
		this.requestSize = FDFS_GROUP_NAME_MAX_LEN + remoteFileNameByte.length;
		this.responseCmd = TRACKER_PROTO_CMD_RESP;
		this.responseSize = -1;
	}
	
	

}
