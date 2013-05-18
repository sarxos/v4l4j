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
 * Objects of this class are used to retrieve JPEG-encoded frames from a 
 * {@link VideoDevice}. A JPEG frame grabber can only be created 
 * if the associated video device can produce images in a format v4l4j
 * knows how to encode in JPEG. The {@link VideoDevice#supportJPEGConversion()} 
 * method can be used to find out whether a video device can have its images 
 * JPEG-encoded by v4l4j, ie if a JPEG frame grabber can be instantiated.
 * <code>JPEGFrameGrabber</code> objects are not 
 * instantiated directly. Instead, the 
 * {@link VideoDevice#getJPEGFrameGrabber(int, int, int, int, int)}
 * or 
 * {@link VideoDevice#getJPEGFrameGrabber(int, int, int, int, int, ImageFormat)}
 * method must be called on the associated {@link VideoDevice}. JPEG frame grabbers
 * implement the {@link FrameGrabber} interface which provides methods to handle
 * video capture. See {@link FrameGrabber its documentation} for more information.
 * 
 * @see FrameGrabber {@link FrameGrabber}
 * @author gilles
 *
 */
public class JPEGFrameGrabber extends AbstractGrabber {
	
	private int quality;
	
	/**
	 * This constructor builds a FrameGrabber object used to capture JPEG frames
	 * from a video source
	 * @param di the DeviceInfo of the VideoDevice who created this frame grabber
	 * @param w the requested frame width 
	 * @param h the requested frame height
	 * @param ch the input index, as returned by {@link InputInfo#getIndex()}
	 * @param std the video standard, as returned by 
	 * {@link InputInfo#getSupportedStandards()} (see V4L4JConstants).
	 * @param q the JPEG image quality (the higher, the better the quality), 
	 * between the range {@link V4L4JConstants#MIN_JPEG_QUALITY} and 
	 * {@link V4L4JConstants#MAX_JPEG_QUALITY}.
	 * @param imf the image format frame should be captured in 
	 * @param factory the thread factory to use when creating the push source
	 */
	JPEGFrameGrabber(DeviceInfo di,long o, int w, int h, int ch, int std, int q
			, Tuner t,ImageFormat imf, ThreadFactory factory) throws V4L4JException{
		super(di, o,w,h,ch,std, t , imf, JPEG_GRABBER, factory);	
		setJPGQuality(q);
	}
	
	/**
	 * This method initialises the capture, and apply the capture parameters.
	 * V4L may either adjust the height and width parameters to the closest 
	 * valid values or reject them altogether. If the values were adjusted, they
	 * can be retrieved after calling {@link #init()} using {@link #getWidth()}
	 * and {@link #getHeight()}
	 * @throws VideoStandardException if the chosen video standard is not 
	 * supported
	 * @throws ImageFormatException this exception is thrown if the chosen image
	 * format cannot be JPEG encoded. If no image format was chosen, the video 
	 * device does not have any image formats that can be jpeg encoded
	 * (let the author know, see README file)
	 * @throws CaptureChannelException if the given channel number value is not 
	 * valid
	 * @throws ImageDimensionException if the given image dimensions are not 
	 * supported
	 * @throws InitialisationException if the video device file can not be 
	 * initialised 
	 * @throws StateException if the frame grabber is already initialised
	 * or released
	 * @throws V4L4JException if there is an error applying capture parameters
	 */
	void init() throws V4L4JException{
		try {
			super.init();
			setQuality(object, quality);
		} catch (ImageFormatException ife){
			if(format == -1){
				String msg ="v4l4j was unable to find image format supported by"
					+ "the \nvideo device and that can be encoded in JPEG.\n"
					+ "Please let the author know about this, so that support\n"
					+ "for this video device can be improved. See \nREADME file"
					+ " on how to submit v4l4j reports.";
				System.out.println(msg);
				ife = new ImageFormatException(msg);
			}
			
			throw ife;
		}
	}
	
	/**
	 * This method sets the desired JPEG quality.
	 * @param q the quality (between 
	 * {@link V4L4JConstants#MIN_JPEG_QUALITY} and 
	 * {@link V4L4JConstants#MAX_JPEG_QUALITY} inclusive)
	 * @throws StateException if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public void setJPGQuality(int q){
		state.checkReleased();
		if(q<V4L4JConstants.MIN_JPEG_QUALITY)
			q =V4L4JConstants.MIN_JPEG_QUALITY; 
		if(q>V4L4JConstants.MAX_JPEG_QUALITY)
			q = V4L4JConstants.MAX_JPEG_QUALITY;
		
		setQuality(object, q);
		quality = q;
	}
	
	/**
	 * This method returns the current JPEG quality.
	 * @return the JPEG quality
	 * @throws StateException if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public int getJPGQuality(){
		state.checkReleased();
		return quality;
	}
	
	/**
	 * This method returns the native image format used by this 
	 * FrameGrabber. The returned format specifies the image format the capture
	 * uses, ie the one images are retrieved from the device BEFORE JPEG 
	 * conversion.
	 * @return the native image format used by this FrameGrabber.
	 * @throws StateException if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public ImageFormat getImageFormat(){
		state.checkReleased();
		return dInfo.getFormatList().getNativeFormat(format);
	}
	
	@Override
	protected void createBuffers(int bufferSize) {
		int numberOfBuffers = nbV4LBuffers;
		while(numberOfBuffers-- > 0)
			videoFrames.add(new JPEGVideoFrame(this, bufferSize));
	}
}
