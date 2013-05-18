/*
* Copyright (C) 2011 Gilles Gigan (gilles.gigan@gmail.com)
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public  License as published by the
* Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
* or FITNESS FOR A PARTICULAR PURPOSE.  
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package au.edu.jcu.v4l4j;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import au.edu.jcu.v4l4j.exceptions.UnsupportedMethod;

/**
 * Instances of this class encapsulate image data for a JPEG compressed
 * image. They will not generate a {@link Raster} as rasters only support
 * uncompressed format. They do support however creation of {@link BufferedImage}s. 
 * @author gilles
 *
 */
class JPEGVideoFrame extends BaseVideoFrame {

	JPEGVideoFrame(AbstractGrabber grabber, int bufferSize) {
		super(grabber, bufferSize);
	}

	@Override
	protected WritableRaster refreshRaster() {
		throw new UnsupportedMethod("A raster cannot be generated for a JPEG frame");
	}
	
	@Override
	protected BufferedImage refreshBufferedImage() {
		try {
			return ImageIO.read(new ByteArrayInputStream(frameBuffer, 0, frameLength));
		} catch (IOException e) {
			System.err.println("It seems your JVM is unable to decode this image.");
			
			// print supported image types and mime types
			System.err.println("Supported image types:");
			String supportedTypes[] = ImageIO.getReaderFormatNames();
			for (String name : supportedTypes)
				System.err.println(name);
			System.err.println("Supported MIME types:");
			String supportedMimeTypes[] = ImageIO.getReaderMIMETypes();
			for (String name : supportedMimeTypes)
				System.err.println(name);
			
			e.printStackTrace();
			throw new UnsupportedMethod("Unable to decode the image", e);
		}
	}

}
