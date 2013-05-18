package au.edu.jcu.v4l4j;

import java.io.IOException;



/**
 * 
 * @author sarxos
 * 
 */
public class V4L4J {

	public static final void init() {
		try {
			NativeUtils.loadLibraryFromJar("/META-INF/native/linux64", new String[] { "video", "v4l4j" });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
