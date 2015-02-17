package com.github.sarxos.v4l4j;

import java.io.File;
import java.io.IOException;


/**
 * 
 * @author sarxos
 * 
 */
public class V4L4J {

	private static String getArch() {

		String arch = System.getProperty("os.arch");
		String dataModel = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
		String bits;

		if (isArm()) {
			return "arm" + getFloat();
		} else {

			if ("32".equals(dataModel)) {
				bits = "32";
			} else if ("64".equals(dataModel)) {
				bits = "64";
			} else {
				bits = (arch.contains("64") || arch.equalsIgnoreCase("sparcv9")) ? "64" : "32";
			}

			return bits;
		}
	}

	private static boolean isArm() {
		return System.getProperty("os.arch").equals("arm");
	}

	private static String getFloat() {
		if (isArm()) {
			return new File("/lib/arm-linux-gnueabihf").isDirectory() ? "hf" : "el";
		}
		return "";
	}

	public static final void init() {
		String arch = getArch();
		try {
			NativeUtils.loadLibraryFromJar("/META-INF/native/linux-" + arch, new String[] { "video", "v4l4j" });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
