package net.mikesu.fastdfs.protocol;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Arrays;


public class QueryUploadCmd extends AbstractCmd<UploadStorage>  {
	

	@Override
	public UploadStorage exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		byte[] body = response(socket.getInputStream());
		String ip_addr = new String(body,FDFS_GROUP_NAME_MAX_LEN,FDFS_IPADDR_SIZE - 1).trim();
		int port = (int) buff2long(body,FDFS_GROUP_NAME_MAX_LEN	+ FDFS_IPADDR_SIZE - 1);
		byte storePath = body[TRACKER_QUERY_STORAGE_STORE_BODY_LEN - 1];
		UploadStorage uploadStorage = new UploadStorage(ip_addr+":"+String.valueOf(port), storePath);
		return uploadStorage;
	}


	public QueryUploadCmd() {
		super();
		this.requestCmd = TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
		this.requestSize = 0;
		this.responseCmd = TRACKER_PROTO_CMD_RESP;
		this.responseSize = TRACKER_QUERY_STORAGE_STORE_BODY_LEN;
	}
	
	public QueryUploadCmd(String group) throws UnsupportedEncodingException {
		super();
		this.requestCmd = TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE;
		this.requestSize = FDFS_GROUP_NAME_MAX_LEN;
		this.responseCmd = TRACKER_PROTO_CMD_RESP;
		this.responseSize = TRACKER_QUERY_STORAGE_STORE_BODY_LEN;
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
	}
	
	
}
