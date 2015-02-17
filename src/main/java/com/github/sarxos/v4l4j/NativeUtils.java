package com.github.sarxos.v4l4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;


/**
 * Simple library class for working with JNI (Java Native Interface)
 * 
 * @see http://frommyplayground.com/how-to-load-native-jni-library-from-jar
 * @author Adam Heirnich <adam@adamh.cz>, http://www.adamh.cz
 */
public class NativeUtils {

	/**
	 * Private constructor - this class will never be instanced
	 */
	private NativeUtils() {
	}

	/**
	 * Loads library from current JAR archive
	 * 
	 * The file from JAR is copied into system temporary directory and then
	 * loaded. The temporary file is deleted after exiting. Method uses String
	 * as filename because the pathname is "abstract", not system-dependent.
	 * 
	 * @param filename The filename inside JAR as absolute path (beginning with
	 *            '/'), e.g. /package/File.ext
	 * @throws IOException If temporary file creation or read/write operation
	 *             fails
	 * @throws IllegalArgumentException If source file (param path) does not
	 *             exist
	 * @throws IllegalArgumentException If the path is not absolute or if the
	 *             filename is shorter than three characters (restriction of
	 *             {@see File#createTempFile(java.lang.String,
	 *             java.lang.String)}).
	 */
	public static void loadLibraryFromJar(String jarpath, String[] libs) throws IOException {

		File libspath = File.createTempFile("libs", "");
		if (!libspath.delete()) {
			throw new IOException("Cannot clean " + libspath);
		}
		if (!libspath.exists()) {
			if (!libspath.mkdirs()) {
				throw new IOException("Cannot create directory " + libspath);
			}
		}

		libspath.deleteOnExit();

		try {
			addLibraryPath(libspath.getAbsolutePath());
		} catch (Exception e) {
			throw new IOException(e);
		}

		for (String lib : libs) {

			String libfile = "lib" + lib + ".so";
			String path = jarpath + "/" + libfile;

			if (!path.startsWith("/")) {
				throw new IllegalArgumentException("The path to be absolute (start with '/').");
			}

			File file = new File(libspath, libfile);
			file.createNewFile();
			file.deleteOnExit();

			byte[] buffer = new byte[1024];
			int readBytes;

			InputStream is = NativeUtils.class.getResourceAsStream(path);
			if (is == null) {
				throw new FileNotFoundException("File " + path + " was not found inside JAR.");
			}

			OutputStream os = new FileOutputStream(file);
			try {
				while ((readBytes = is.read(buffer)) != -1) {
					os.write(buffer, 0, readBytes);
				}
			} finally {
				os.close();
				is.close();
			}
		}

		for (String lib : libs) {
			System.loadLibrary(lib);
		}
	}

	/**
	 * Adds the specified path to the java library path
	 * 
	 * @param pathToAdd the path to add
	 * @throws Exception
	 */
	public static void addLibraryPath(String pathToAdd) throws Exception {
		Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
		usrPathsField.setAccessible(true);

		// get array of paths
		final String[] paths = (String[]) usrPathsField.get(null);

		// check if the path to add is already present
		for (String path : paths) {
			if (path.equals(pathToAdd)) {
				return;
			}
		}

		// add the new path
		final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
		newPaths[newPaths.length - 1] = pathToAdd;
		usrPathsField.set(null, newPaths);
	}
}