package net.mikesu.fastdfs.protocol;

import java.io.IOException;
import java.net.Socket;

public interface Command<T> {

	public T exec(Socket socket) throws IOException;
	
}
