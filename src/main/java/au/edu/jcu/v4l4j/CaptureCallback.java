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

import au.edu.jcu.v4l4j.exceptions.V4L4JException;

/**
 * Objects implementing this interface receive notifications from frame grabbers
 * when new captured frames become available, or an exception
 * occurs during a capture. In order to capture from a video device,
 * you must instantiate an object which implements this interface
 * and pass it to a frame grabber 
 * (via @link {@link FrameGrabber#setCaptureCallback(CaptureCallback)}).
 * When you start the capture, v4l4j will call {@link #nextFrame(VideoFrame)}
 * to deliver new frames to your application as soon as they arrive.
 * Check the {@link FrameGrabber} page for more information.
 * @author gilles
 *
 */
public interface CaptureCallback {
	
	/**
	 * During a capture, this method is called by v4l4j to provide
	 * the latest video frame. It is important that you minimise
	 * the amount of code and processing done in this method in 
	 * order to maintain the appropriate frame rate.
	 * <br>Make sure the frame is recycled when no longer used. It does not
	 * have to be done in this method, but it must be done at some point in
	 * the near future.
	 * @param frame the latest captured frame
	 */
	public void nextFrame(VideoFrame frame);
	
	/**
	 * This method is called if an error occurs during capture.
	 * It is safe to assume that if this method is called,
	 * no more calls to {@link #nextFrame(VideoFrame)} will follow.
	 * @param e the exception that was raised during the capture.
	 */
	public void exceptionReceived(V4L4JException e);
}
