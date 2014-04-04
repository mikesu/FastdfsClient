package net.mikesu.fastdfs.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Arrays;

public class UploadCmd extends AbstractCmd<String> {
	
	private File file;

	@Override
	public String exec(Socket socket) throws IOException {
		InputStream is = new FileInputStream(file);
		request(socket.getOutputStream(), is);
		byte[] body = response(socket.getInputStream());
		String group = new String(body, 0,	FDFS_GROUP_NAME_MAX_LEN).trim();
		String remoteFileName = new String(body,FDFS_GROUP_NAME_MAX_LEN, body.length - FDFS_GROUP_NAME_MAX_LEN);
		return group + "/" + remoteFileName;
	}

	public UploadCmd(File file,String fileName,byte storePathIndex) throws UnsupportedEncodingException {
		super();
		this.file = file;
		this.requestCmd = STORAGE_PROTO_CMD_UPLOAD_FILE;
		this.requestSize = 15 + file.length();
		this.responseCmd = STORAGE_PROTO_CMD_RESP;
		this.responseSize = -1;
		this.body1 = new byte[15];
		Arrays.fill(body1, (byte) 0);
		this.body1[0] = storePathIndex;
		byte[] fileSizeByte = long2buff(file.length());
		byte[] fileExtNameByte = getFileExtNameByte(fileName);
		int fileExtNameByteLen = fileExtNameByte.length;
		if(fileExtNameByteLen>FDFS_FILE_EXT_NAME_MAX_LEN){
			fileExtNameByteLen = FDFS_FILE_EXT_NAME_MAX_LEN;
		}
		System.arraycopy(fileSizeByte, 0, body1, 1, fileSizeByte.length);
		System.arraycopy(fileExtNameByte, 0, body1, fileSizeByte.length + 1, fileExtNameByteLen);
	}

	private byte[] getFileExtNameByte(String fileName) throws UnsupportedEncodingException {
		String fileExtName = null;
		int nPos = fileName.lastIndexOf('.');
		if (nPos > 0 && fileName.length() - nPos <= FDFS_FILE_EXT_NAME_MAX_LEN + 1) {
			fileExtName = fileName.substring(nPos + 1);
		}
		if (fileExtName != null && fileExtName.length() > 0) {
			return fileExtName.getBytes("UTF-8");
		}else{
			return new byte[0];
		}
	}
}
