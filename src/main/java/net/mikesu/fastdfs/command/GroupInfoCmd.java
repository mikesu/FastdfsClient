package net.mikesu.fastdfs.command;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.mikesu.fastdfs.data.GroupInfo;
import net.mikesu.fastdfs.data.Result;

public class GroupInfoCmd extends AbstractCmd<List<GroupInfo>> {

	@Override
	public Result<List<GroupInfo>> exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		Response response = response(socket.getInputStream());
		if(response.isSuccess()){
			byte[] data = response.getData();
			int dataLength = data.length;
			if(dataLength%GroupInfo.BYTE_SIZE!=0){
				throw new IOException("recv body length: " + data.length + " is not correct");
			}
			List<GroupInfo> groupInfos = new ArrayList<GroupInfo>();
			int offset = 0;
			while(offset<dataLength){
				GroupInfo groupInfo = new GroupInfo(data,offset);
				groupInfos.add(groupInfo);
				offset += GroupInfo.BYTE_SIZE;
			}
			return new Result<List<GroupInfo>>(response.getCode(),groupInfos);
		}else{
			return new Result<List<GroupInfo>>(response.getCode(),"Error");
		}
	}

	public GroupInfoCmd() {
		super();
		this.requestCmd = TRACKER_PROTO_CMD_SERVER_LIST_GROUP;
		this.responseCmd = TRACKER_PROTO_CMD_RESP;
		this.responseSize = -1;
	}

}
