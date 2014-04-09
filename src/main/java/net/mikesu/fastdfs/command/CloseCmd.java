package net.mikesu.fastdfs.command;

import java.io.IOException;
import java.net.Socket;

import net.mikesu.fastdfs.data.Result;

public class CloseCmd extends AbstractCmd<Boolean> {
	
	public CloseCmd() {
		super();
		this.requestCmd = FDFS_PROTO_CMD_QUIT;
	}

	@Override
	public Result<Boolean> exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		return new Result<Boolean>(SUCCESS_CODE,true);
	}


}
