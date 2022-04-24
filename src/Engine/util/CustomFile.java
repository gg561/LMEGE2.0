package Engine.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import org.lwjglx.BufferUtils;

public class CustomFile {
	
	private static final String PATH_SEPARATOR = "/";
	
	private String name;
	private String path;
	
	public CustomFile(String path) {
		this.path = PATH_SEPARATOR + path;
		String[] paths = path.split(PATH_SEPARATOR);
		name = paths[paths.length - 1];
	}
	
	public CustomFile(String... path) {
		this.path = "";
		for(String dir : path) {
			this.path += (PATH_SEPARATOR + dir);
		}
		name = path[path.length - 1];
	}
	
	public CustomFile(CustomFile file, String subFile) {
		this.path = file.path + PATH_SEPARATOR + subFile;
		this.name = subFile;
	}
	
	public CustomFile(CustomFile file, String... subFiles) {
		this.path = file.path;
		for(String sub : subFiles) {
			path += (PATH_SEPARATOR + sub);
		}
		this.name = subFiles[subFiles.length - 1];
	}
	
	public String getPath() {
		return path;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getPath();
	}
	
	public InputStream getStream() {
		return Class.class.getResourceAsStream(path);
	}
	
	public BufferedReader getReader() {
		try {
			InputStreamReader isr = new InputStreamReader(getStream());
			BufferedReader br = new BufferedReader(isr);
			return br;
		}catch(Exception e) {
			System.out.println("Couldn't get reader for " + path);
			e.printStackTrace();
			throw e;
		}
	}
	
	public ByteBuffer getByteBuffer(int size) {
		InputStream is = getStream();
		ByteBuffer buffer = BufferUtils.createByteBuffer(size);
		int data = -1;
		try {
			while((data = is.read()) != -1) {
				buffer.put((byte) data);
				if(buffer.remaining() == 0) {
					ByteBuffer newBuffer = BufferUtils.createByteBuffer(buffer.capacity() + size);
					buffer.flip();
					newBuffer.put(buffer);
					buffer = newBuffer;
				}
			}
			is.close();
			buffer.flip();
			return buffer;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		} finally {
			if(is != null) {
				System.out.println("notnull");
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("notnull");
					e.printStackTrace();
				}
			}
		}
	}

}
