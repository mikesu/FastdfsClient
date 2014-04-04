package net.mikesu.fastdfs.protocol;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GroupInfoCmd extends AbstractCmd<List<GroupInfo>> {

	@Override
	public List<GroupInfo> exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		byte[] data = response(socket.getInputStream());
		List<GroupInfo> groupInfos = new ArrayList<GroupInfo>();
		int dataLength = data.length;
		if(dataLength%GroupInfo.BYTE_SIZE!=0){
			throw new IOException("recv body length: " + data.length + " is not correct");
		}
		int offset = 0;
		while(offset<dataLength){
			GroupInfo groupInfo = new GroupInfo(data,offset);
			groupInfos.add(groupInfo);
			offset += GroupInfo.BYTE_SIZE;
		}
		return groupInfos;
	}

	public GroupInfoCmd() {
		super();
		this.requestCmd = TRACKER_PROTO_CMD_SERVER_LIST_GROUP;
		this.requestSize = 0;
		this.responseCmd = TRACKER_PROTO_CMD_RESP;
		this.responseSize = -1;
	}

}
