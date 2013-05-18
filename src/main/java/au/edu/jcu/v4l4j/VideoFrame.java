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
import java.awt.image.DataBuffer;
import java.awt.image.Raster;

import au.edu.jcu.v4l4j.exceptions.StateException;
import au.edu.jcu.v4l4j.exceptions.UnsupportedMethod;

/**
 * Objects implementing this interface represent a single video frame captured by v4l4j.
 * They provide access to image data through various objects, including byte arrays
 * and {@link DataBuffer}s. During capture, v4l4j creates <code>VideoFrame</code>s 
 * and passes them the registered {@link CaptureCallback} object. Check the 
 * {@link FrameGrabber} class for more information on how to perform a capture.<br>
 * When accessing the image data as a byte array, as returned by {@link #getBytes()},
 * note that the size of returned byte array can be longer than the actual image size.
 * What this means is that you should use {@link #getFrameLength()} to figure out how
 * many elements in the byte array are actually used by the image instead of relying
 * on <code>byte[].length</code>.<br> 
 * From the moment a <code>VideoFrame</code> is returned by a frame grabber, the 
 * <code>VideoFrame</code> object itself as well as any of the objects obtained through 
 * its methods can be used. <b>When the <code>VideoFrame</code> and any of the objects
 * obtained through it) are dealt with and no longer used, the <code>VideoFrame</code>
 * must be recycled, which marks it as being available to v4l4j to store an upcoming 
 * image.</b> From the moment a <code>VideoFrame</code> is recycled, 
 * neither the <code>VideoFrame</code> nor objects obtained through it 
 * can be used. Bad things will happen if you violate this rule. It is recommended
 * that you explicitly set all references to this object, as well as references to objects
 * obtained through this frame, to null.<br>    
 * Note that not all video frames are capable of returning a raster or a buffered image.
 * For example, rasters cannot be created for compressed image formats, 
 * which means <code>VideoFrame</code>s returned by {@link JPEGFrameGrabber}s 
 * cannot return a buffered image, and calling {@link #getBufferedImage()} will 
 * throw a {@link UnsupportedMethod} exception. In the same way, video frames 
 * returned by raw frame grabbers cannot create rasters and an {@link UnsupportedMethod}
 * exception will be thrown if {@link #getRaster()} is called.
 * @author gilles
 *
 */
public interface VideoFrame {
	/**
	 * This method returns the frame grabber object which captured this frame
	 * @return the frame grabber object which captured this frame.
	 */
	public FrameGrabber	getFrameGrabber();
	
	/**
	 * This method returns the length of this video frame in bytes.
	 * @return the length of this video frame in bytes.
	 * @throws StateException if this video frame has been recycled already.
	 */
	public int		getFrameLength();
	
	/**
	 * This method returns this video frame's sequence number (a monotonically
	 * increasing number for each captured frame). This number can be used
	 * to find out when a frame was dropped: if <code>currentSequenceNumber != 
	 * (previousSequenceNumber + 1)</code> then <code>(currentSequenceNumber -
	 * previousSequenceNumber(</code> frames were dropped.
	 * @return this video frame's sequence number
	 * @throws StateException if this video frame has been recycled already. 
	 */
	public long 	getSequenceNumber();

	/**
	 * This method returns the OS time (number of microseconds elapsed since
	 * startup) at which this video frame was captured. 
	 * @return the OS time (number of microseconds elapsed since
	 * startup) at which this video frame was captured.
	 * @throws StateException if this video frame has been recycled already.
	 */
	public long 	getCaptureTime();
	
	/**
	 * This method returns the image data as a byte array.<b>Please note that
	 * the size of the returned byte array can be greater than the actual frame
	 * size. You should not use the byte array length (as returned by 
	 * <code>byte[].length</code>) as the length of the image. Instead, use the
	 * value returned from {@link #getFrameLength()}.</b>
	 * @return the image data as a byte array. The array may be longer than
	 * the actual frame length.
	 * @throws StateException if this video frame has been recycled already.
	 */
	public byte[]	getBytes();
	
	/**
	 * This method returns the image data encapsulated in a {@link DataBuffer}
	 * object. The data is stored as bytes in the data buffer.
	 * @return the image data encapsulated in a {@link DataBuffer}
	 * @throws StateException if this video frame has been recycled already. 
	 */
	public DataBuffer		getDataBuffer();
	
	/**
	 * This method returns the image data encapsulated in a {@link Raster} object.
	 * Rasters cannot be created for some image formats (including compressed formats
	 * like JPEG). In this case this method will throw a {@link UnsupportedMethod} 
	 * exception.
	 * @return the image data encapsulated in a {@link Raster} object
	 * @throws {@link UnsupportedMethod} exception if the image format this
	 * video frame uses cannot be encapsulated in a {@link Raster}.
	 * @throws StateException if this video frame has been recycled already.
	 */
	public Raster			getRaster();
	
	/**
	 * This method returns the image data encapsulated in a {@link BufferedImage} object.
	 * Buffered images cannot be created for some image formats (including raw image 
	 * formats). In this case this method will throw a {@link UnsupportedMethod} 
	 * exception.
	 * @return the image data encapsulated in a {@link BufferedImage} object
	 * @throws {@link UnsupportedMethod} exception if the image format this
	 * video frame uses cannot be encapsulated in a {@link BufferedImage}.
	 * @throws StateException if this video frame has been recycled already.
	 */
	public BufferedImage	getBufferedImage();
	
	/**
	 * This method marks this video frame as being no longer used, and ready
	 * to be reused by v4l4j. After calling this method, do not use either 
	 * this object or any of the objects obtained through it 
	 * (byte array, data buffer, raster, buffered image, ...) <b> or bad things
	 * WILL happen</b>. 
	 */
	public void 			recycle();
}
