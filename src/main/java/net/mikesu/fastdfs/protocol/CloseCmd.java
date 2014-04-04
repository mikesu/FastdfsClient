package net.mikesu.fastdfs.protocol;

import java.io.IOException;
import java.net.Socket;

public class CloseCmd extends AbstractCmd<String> {
	
	public CloseCmd() {
		super();
		this.requestCmd = FDFS_PROTO_CMD_QUIT;
		this.requestSize = 0;
	}

	@Override
	public String exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		return "OK";
	}


}
