package net.mikesu.fastdfs.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public abstract class AbstractCmd<T> implements Command<T> {
	
	
	public static final byte FDFS_PROTO_CMD_QUIT = 82;
	public static final byte TRACKER_PROTO_CMD_SERVER_LIST_GROUP = 91;
	public static final byte TRACKER_PROTO_CMD_SERVER_LIST_STORAGE = 92;
	public static final byte TRACKER_PROTO_CMD_SERVER_DELETE_STORAGE = 93;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE = 101;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE = 102;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE = 103;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE = 104;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL = 105;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ALL = 106;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ALL = 107;
	public static final byte TRACKER_PROTO_CMD_RESP = 100;
	public static final byte FDFS_PROTO_CMD_ACTIVE_TEST = 111;
	public static final byte STORAGE_PROTO_CMD_UPLOAD_FILE = 11;
	public static final byte STORAGE_PROTO_CMD_DELETE_FILE = 12;
	public static final byte STORAGE_PROTO_CMD_SET_METADATA = 13;
	public static final byte STORAGE_PROTO_CMD_DOWNLOAD_FILE = 14;
	public static final byte STORAGE_PROTO_CMD_GET_METADATA = 15;
	public static final byte STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE = 21;
	public static final byte STORAGE_PROTO_CMD_QUERY_FILE_INFO = 22;
	public static final byte STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE = 23; // create appender file
	public static final byte STORAGE_PROTO_CMD_APPEND_FILE = 24; // append file
	public static final byte STORAGE_PROTO_CMD_MODIFY_FILE = 34; // modify appender file
	public static final byte STORAGE_PROTO_CMD_TRUNCATE_FILE = 36; // truncate appender file
	public static final byte STORAGE_PROTO_CMD_RESP = TRACKER_PROTO_CMD_RESP;
	
	public static final byte FDFS_STORAGE_STATUS_INIT = 0;
	public static final byte FDFS_STORAGE_STATUS_WAIT_SYNC = 1;
	public static final byte FDFS_STORAGE_STATUS_SYNCING = 2;
	public static final byte FDFS_STORAGE_STATUS_IP_CHANGED = 3;
	public static final byte FDFS_STORAGE_STATUS_DELETED = 4;
	public static final byte FDFS_STORAGE_STATUS_OFFLINE = 5;
	public static final byte FDFS_STORAGE_STATUS_ONLINE = 6;
	public static final byte FDFS_STORAGE_STATUS_ACTIVE = 7;
	public static final byte FDFS_STORAGE_STATUS_NONE = 99;

	/**
	 * for overwrite all old metadata
	 */
	public static final byte STORAGE_SET_METADATA_FLAG_OVERWRITE = 'O';

	/**
	 * for replace, insert when the meta item not exist, otherwise update it
	 */
	public static final byte STORAGE_SET_METADATA_FLAG_MERGE = 'M';

	public static final int FDFS_PROTO_PKG_LEN_SIZE = 8;
	public static final int FDFS_PROTO_CMD_SIZE = 1;
	public static final int FDFS_GROUP_NAME_MAX_LEN = 16;
	public static final int FDFS_IPADDR_SIZE = 16;
	public static final int FDFS_DOMAIN_NAME_MAX_SIZE = 128;
	public static final int FDFS_VERSION_SIZE = 6;
	public static final int FDFS_STORAGE_ID_MAX_SIZE = 16;

	public static final String FDFS_RECORD_SEPERATOR = "\u0001";
	public static final String FDFS_FIELD_SEPERATOR = "\u0002";

	public static final int TRACKER_QUERY_STORAGE_FETCH_BODY_LEN = FDFS_GROUP_NAME_MAX_LEN + FDFS_IPADDR_SIZE - 1 + FDFS_PROTO_PKG_LEN_SIZE;
	public static final int TRACKER_QUERY_STORAGE_STORE_BODY_LEN = FDFS_GROUP_NAME_MAX_LEN + FDFS_IPADDR_SIZE + FDFS_PROTO_PKG_LEN_SIZE;

	public static final int PROTO_HEADER_CMD_INDEX = FDFS_PROTO_PKG_LEN_SIZE;
	public static final int PROTO_HEADER_STATUS_INDEX = FDFS_PROTO_PKG_LEN_SIZE + 1;

	public static final byte FDFS_FILE_EXT_NAME_MAX_LEN = 6;
	public static final byte FDFS_FILE_PREFIX_MAX_LEN = 16;
	public static final byte FDFS_FILE_PATH_LEN = 10;
	public static final byte FDFS_FILENAME_BASE64_LENGTH = 27;
	public static final byte FDFS_TRUNK_FILE_INFO_LEN = 16;

	public static final byte ERR_NO_ENOENT = 2;
	public static final byte ERR_NO_EIO = 5;
	public static final byte ERR_NO_EBUSY = 16;
	public static final byte ERR_NO_EINVAL = 22;
	public static final byte ERR_NO_ENOSPC = 28;
	public static final byte ECONNREFUSED = 61;
	public static final byte ERR_NO_EALREADY = 114;

	public static final long INFINITE_FILE_SIZE = 256 * 1024L * 1024 * 1024	* 1024 * 1024L;
	public static final long APPENDER_FILE_SIZE = INFINITE_FILE_SIZE;
	public static final long TRUNK_FILE_MARK_SIZE = 512 * 1024L * 1024 * 1024 * 1024 * 1024L;
	public static final long NORMAL_LOGIC_FILENAME_LENGTH = FDFS_FILE_PATH_LEN	+ FDFS_FILENAME_BASE64_LENGTH + FDFS_FILE_EXT_NAME_MAX_LEN + 1;
	public static final long TRUNK_LOGIC_FILENAME_LENGTH = NORMAL_LOGIC_FILENAME_LENGTH	+ FDFS_TRUNK_FILE_INFO_LEN;
	
	protected byte requestCmd;
	protected long requestSize;
	protected byte responseCmd;
	protected long responseSize;
	protected byte[] body1;
	
	
	protected void request(OutputStream socketOut)throws IOException {
		socketOut.write(getRequestHeaderAndBody1());
		
	}
	
	protected void request(OutputStream socketOut,InputStream is)throws IOException {
		request(socketOut);
		int readBytes;
		byte[] buff = new byte[256 * 1024];
		while ((readBytes = is.read(buff)) >= 0) {
			if (readBytes == 0) {
				continue;
			}
			socketOut.write(buff, 0, readBytes);
		}
		is.close();
	}
	
	protected byte[] getRequestHeaderAndBody1() {
		if(body1==null){
			body1 = new byte[0];
		}
		byte[] header = new byte[FDFS_PROTO_PKG_LEN_SIZE + 2 + body1.length];
		Arrays.fill(header, (byte) 0);
		byte[] hex_len = long2buff(requestSize);
		System.arraycopy(hex_len, 0, header, 0, hex_len.length);
		System.arraycopy(body1, 0, header, FDFS_PROTO_PKG_LEN_SIZE + 2, body1.length);
		header[PROTO_HEADER_CMD_INDEX] = requestCmd;
		header[PROTO_HEADER_STATUS_INDEX] = (byte) 0;
		return header;
	}
	
	protected byte[] response(InputStream socketIn)throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		response(socketIn,os);
		return os.toByteArray();
		
	}
	
	protected void response(InputStream socketIn,OutputStream os) throws IOException {
		byte[] header = new byte[FDFS_PROTO_PKG_LEN_SIZE + 2];
		int bytes = socketIn.read(header);
		if (bytes != header.length) {
			throw new IOException("recv package size " + bytes + " != "	+ header.length);
		}

		if (header[PROTO_HEADER_CMD_INDEX] != responseCmd) {
			throw new IOException("recv cmd: " + header[PROTO_HEADER_CMD_INDEX]	+ " is not correct, expect cmd: " + responseCmd);
		}

		if (header[PROTO_HEADER_STATUS_INDEX] != 0) {
			throw new IOException("recv header status:"+header[PROTO_HEADER_STATUS_INDEX]);
		}

		long respSize = buff2long(header, 0);
		if (respSize < 0) {
			throw new IOException("recv body length: " + respSize + " < 0!");
		}

		if (responseSize >= 0 && respSize != responseSize) {
			throw new IOException("recv body length: " + respSize + " is not correct, expect length: " + responseSize);
		}
		

		byte[] buff = new byte[2 * 1024];
		int totalBytes = 0;
		int remainBytes = (int) respSize;

		while (totalBytes < respSize) {
			int len = remainBytes;
			if(len>buff.length){
				len = buff.length;
			}
			
			if ((bytes = socketIn.read(buff, 0, len)) < 0) {
				break;
			}
			os.write(buff, 0, bytes);
			totalBytes += bytes;
			remainBytes -= bytes;
		}

		if (totalBytes != respSize) {
			throw new IOException("recv package size " + totalBytes + " != "+ respSize);
		}
		os.close();
	}
	
	public static byte[] long2buff(long n) {
		byte[] bs;

		bs = new byte[8];
		bs[0] = (byte) ((n >> 56) & 0xFF);
		bs[1] = (byte) ((n >> 48) & 0xFF);
		bs[2] = (byte) ((n >> 40) & 0xFF);
		bs[3] = (byte) ((n >> 32) & 0xFF);
		bs[4] = (byte) ((n >> 24) & 0xFF);
		bs[5] = (byte) ((n >> 16) & 0xFF);
		bs[6] = (byte) ((n >> 8) & 0xFF);
		bs[7] = (byte) (n & 0xFF);

		return bs;
	}
	
	public static long buff2long(byte[] bs, int offset) {
		return (((long) (bs[offset] >= 0 ? bs[offset] : 256 + bs[offset])) << 56)
				| (((long) (bs[offset + 1] >= 0 ? bs[offset + 1]
						: 256 + bs[offset + 1])) << 48)
				| (((long) (bs[offset + 2] >= 0 ? bs[offset + 2]
						: 256 + bs[offset + 2])) << 40)
				| (((long) (bs[offset + 3] >= 0 ? bs[offset + 3]
						: 256 + bs[offset + 3])) << 32)
				| (((long) (bs[offset + 4] >= 0 ? bs[offset + 4]
						: 256 + bs[offset + 4])) << 24)
				| (((long) (bs[offset + 5] >= 0 ? bs[offset + 5]
						: 256 + bs[offset + 5])) << 16)
				| (((long) (bs[offset + 6] >= 0 ? bs[offset + 6]
						: 256 + bs[offset + 6])) << 8)
				| ((long) (bs[offset + 7] >= 0 ? bs[offset + 7]
						: 256 + bs[offset + 7]));
	}
	
}
