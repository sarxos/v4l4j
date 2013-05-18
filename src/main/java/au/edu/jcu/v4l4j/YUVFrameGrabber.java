/*
* Copyright (C) 2007-2008 Gilles Gigan (gilles.gigan@gmail.com)
* eResearch Centre, James Cook University (eresearch.jcu.edu.au)
*
* This program was developed as part of the ARCHER project
* (Australian Research Enabling Environment) funded by a   
* Systemic Infrastructure Initiative (SII) grant and supported by the Australian
* Department of Innovation, Industry, Science and Research
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

import java.util.concurrent.ThreadFactory;

import au.edu.jcu.v4l4j.exceptions.CaptureChannelException;
import au.edu.jcu.v4l4j.exceptions.ImageFormatException;
import au.edu.jcu.v4l4j.exceptions.InitialisationException;
import au.edu.jcu.v4l4j.exceptions.StateException;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;
import au.edu.jcu.v4l4j.exceptions.VideoStandardException;

/**
 * Objects of this class are used to retrieve YUV420-encoded frames from a 
 * {@link VideoDevice}. A YUV420 frame grabber can only be 
 * created if the associated video device can produce images in a format v4l4j
 * knows how to encode in YUV420. The {@link VideoDevice#supportYUVConversion()} 
 * method can be used to find out whether a video device can have its images 
 * YUV420-encoded by v4l4j, ie if a YUV420 frame grabber can be instantiated.
 * <code>YUVFrameGrabber</code> objects are not instantiated directly. 
 * Instead, the 
 * {@link VideoDevice#getYUVFrameGrabber(int, int, int, int)}
 * or 
 * {@link VideoDevice#getYUVFrameGrabber(int, int, int, int, ImageFormat)}
 * method must be called on the associated {@link VideoDevice}. YUV frame grabbers
 * implement the {@link FrameGrabber} interface which provides methods to handle
 * video capture. See {@link FrameGrabber its documentation} for more information.
 * 
 * @see FrameGrabber {@link FrameGrabber}
 * @author gilles
 *
 */
public class YUVFrameGrabber extends AbstractGrabber {
	/**
	 * This constructor builds a FrameGrabber object used to capture YUV420
	 * frames from a video source
	 * @param di the DeviceInfo of the VideoDevice who created this frame grabber
	 * @param o a JNI pointer to a v4l4j_device structure
	 * @param w the requested frame width 
	 * @param h the requested frame height
	 * @param ch the input index, as returned by 
	 * {@link InputInfo#getIndex()}
	 * @param std the video standard, as returned by 
	 * {@link InputInfo#getSupportedStandards()} (see V4L4JConstants)
	 * @param imf the image format frame should be captured in 
	 * @param factory the thread factory to use when creating the push source
	 */
	YUVFrameGrabber(DeviceInfo di, long o, int w, int h, int ch, int std, Tuner t,
			ImageFormat imf, ThreadFactory factory) throws ImageFormatException {
		super(di, o, w, h, ch, std, t, imf, YUV_GRABBER, factory);
	}

	/**
	 * This method initialises the capture, and apply the capture parameters.
	 * V4L may either adjust the height and width parameters to the closest 
	 * valid values or reject them altogether. If the values were adjusted, 
	 * they can be retrieved after calling {@link #init()} using 
	 * {@link #getWidth()} and {@link #getHeight()}.
	 * @throws VideoStandardException if the chosen video standard is not 
	 * supported
	 * @throws ImageFormatException this exception is thrown if the chosen image
	 * format cannot be YUV420 encoded. If no image format was chosen, the video 
	 * device does not have any image formats that can be YUV420 encoded
	 * (let the author know, see README file)
	 * @throws CaptureChannelException if the given channel number value is not 
	 * valid
	 * @throws ImageDimensionException if the given image dimensions are not 
	 * supported
	 * @throws InitialisationException if the video device file can not be 
	 * initialised 
	 * @throws StateException if the frame grabber is already initialised or
	 * released
	 * @throws V4L4JException if there is an error applying capture parameters
	 */
	void init() throws V4L4JException{
		try {
			super.init();
		} catch (ImageFormatException ife){
			if(format == -1){
				String msg = 
					"v4l4j was unable to find image format supported by the"
					+ " \nvideo device and that can be converted to YUV420.\n"
					+ "Please let the author know about this, so that support\n"
					+ "for this video device can be improved. See \nREADME file"
					+ " on how to submit v4l4j reports.";
				System.err.println(msg);
				ife = new ImageFormatException(msg);
			}
			
			throw ife;
		}
	}
	
	/**
	 * This method returns the native image format used by this 
	 * FrameGrabber. The returned format specifies the image format the capture
	 * uses, ie the one images are retrieved from the device BEFORE YUV 
	 * conversion.
	 * @return the native image format used by this FrameGrabber.
	 * @throws StateException if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public ImageFormat getImageFormat(){
		state.checkReleased();
		return dInfo.getFormatList().getYUVEncodableFormat(format);
	}
	
	@Override
	protected void createBuffers(int bufferSize) {
		int numberOfBuffers = nbV4LBuffers;
		
		while(numberOfBuffers-- > 0)
			// TODO: fix me, find a way to create a writable raster 
			// and BufferedImage for planar YUV420 image format
			videoFrames.add( new UncompressedVideoFrame(this, bufferSize, null, null) );
	}
}
