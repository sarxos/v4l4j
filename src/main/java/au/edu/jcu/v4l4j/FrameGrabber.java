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

import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.FrameInterval.DiscreteInterval;
import au.edu.jcu.v4l4j.exceptions.CaptureChannelException;
import au.edu.jcu.v4l4j.exceptions.VideoStandardException;
import au.edu.jcu.v4l4j.exceptions.InvalidValue;
import au.edu.jcu.v4l4j.exceptions.NoTunerException;
import au.edu.jcu.v4l4j.exceptions.StateException;
import au.edu.jcu.v4l4j.exceptions.UnsupportedMethod;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

/**
 * Objects implementing this interface provide methods to capture frames from 
 * a {@link VideoDevice}. <code>FrameGrabber</code>s are not instantiated 
 * directly. Instead, one of the <code>getXXXFrameGrabber()</code> methods
 * must be called on a {@link VideoDevice} to obtain one. The requested height
 * and width may be adjusted to the closest supported values. The adjusted width 
 * and height can be retrieved by calling {@link #getWidth()} and 
 * {@link #getHeight()}.<br>
 * Frame grabbers operate in push mode: you must give v4l4j
 * an object implementing the {@link CaptureCallback} interface by calling
 * {@link #setCaptureCallback(CaptureCallback)}. During capture,
 * v4l4j will create a thread and deliver new frames to the capture callback 
 * object. By default, new threads are created by the thread factory returned by 
 * <code>Executors.defaultThreadFactory()</code>, but you can
 * provide your own thread factory by calling 
 * {@link VideoDevice#setThreadFactory(java.util.concurrent.ThreadFactory factory)}
 * <b>prior to creating a <code>FrameGrabber</code> object</b>.<br>
 * A typical <code>FrameGrabber</code> use case is as follows:
 * <br>
 * Create the video device and frame grabber:
 * <code><br><br>
 * //create a new video device<br>
 * VideoDevice vd = new VideoDevice("/dev/video0");<br>
 * // vd.setThreadFactory(myThreadFactory); // set your own thread factory if required.
 * <br>//Create an instance of FrameGrabber
 * <br>FrameGrabber f = vd.getJPEGFrameGrabber(320, 240, 0, 0, 80);
 * <br>//If supported, this frame grabber will capture frames and convert them
 * <br>//to JPEG before delivering them to your application. 
 * <br>
 * <br>//Instantiate an object that implements the {@link CaptureCallback}
 * <br>//interface which will receive captured frame
 * <br>myCallbackObject = new MyCallbackObjectclass();
 * <br>
 * <br>//pass it to the frame grabber.
 * <br>f.setCaptureCallback(myCallbackObject);
 * <br>
 * <br>//Now start the capture
 * <br>f.startCapture();
 * <br>
 * <br>//At this stage, myCallbackObject.nextFrame() will be called every time
 * <br>//a new frame is available (see next paragraph).
 * <br>
 * <br>//When appropriate, stop the capture
 * <br>f.stopCapture();
 * <br>//Release the frame grabber
 * <br>vd.releaseFrameGrabber();
 * <br>//Release the video device
 * <br>vd.release();
 * </code><br><br>
 * In myCallbackObject, when the capture is started, the following method is
 * called every time a new frame is available :<br>
 * <code>
 * <br>public void nextFrame(VideoFrame frame) {
 * <br>&nbsp;&nbsp; //do something useful with frame. But make sure you recycle
 * <br>&nbsp;&nbsp; //it when dealt with, so v4l4j can re-use it later on
 * <br>&nbsp;&nbsp; frame.recycle();
 * <br>}<br>
 * </code>

 * For a concrete example of push mode capture which displays the video 
 * stream, see the au.edu.jcu.v4l4j.examples.SimpleViewer example application.
 * <br>Again, you must recycle video frames when they are no
 * longer used.<br></br>
 * 
 * Only one frame grabber can be used at any one time on a given video device.
 * Once a frame grabber is released with 
 * {@link VideoDevice#releaseFrameGrabber()}, another one can be obtained. 
 * However, when the capture is stopped with {@link #stopCapture()}, it can be 
 * started again with {@link #stopCapture()} without having to create a new 
 * frame grabber.
 * 
 * @see RawFrameGrabber
 * @see JPEGFrameGrabber
 * @see RGBFrameGrabber
 * @see BGRFrameGrabber
 * @see YUVFrameGrabber
 * @see YVUFrameGrabber
 * @author gilles
 *
 */
public interface FrameGrabber {

	/**
	 * This method returns the native image format used by this 
	 * FrameGrabber. The returned format specifies the image format in which
	 * frames are obtained from the device.
	 * @return the native image format used by this FrameGrabber.
	 * @throws StateException if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public ImageFormat getImageFormat();
	
	/**
	 * This method returns the number of buffers v4l4j has negotiated with
	 * the driver. The driver stores captured frames in these buffers and returns
	 * them to v4l4j. A {@link VideoFrame} represents (and encapsulates) one of 
	 * these buffers. Hence, this number is an indication of how many video
	 * frames can be delivered by v4l4j to your application before the capture 
	 * blocks until a buffer is returned to the driver. Practically speaking, 
	 * this number specifies how many times v4l4j can call 
	 * {@link CaptureCallback#nextFrame(VideoFrame frame)} and block waiting for
	 * one of the previous frames to be recycled through {@link VideoFrame#recycle()}.<br>
	 * You can specify how many buffers the driver should use by
	 * setting the <code>v4l4j.num_driver_buffers</code> property to an integer value 
	 * <b>before creating a frame grabber object</b>. The number you specify is only an
	 * indication, and the driver can decide to allocate a different number of buffers.
	 * Before setting this number, make sure you fully understand the implications of doing this.
	 * @return the number of frame buffers used to retrieve frames from 
	 * the driver
	 */
	public int getNumberOfVideoFrames();
	
	/**
	 * This method returns the number of recycled video frames, ie. the number 
	 * of video frames are currently available to v4l4j (and the driver) to 
	 * store new incoming frames.
	 * This number indicates how many more frames can be captured by v4l4j 
	 * before the capture stops until you recycle one of the previous frame that was
	 * delivered to your application by calling {@link VideoFrame#recycle()}.
	 * @return the number of video frames currently recycled.
	 * @see #getNumberOfVideoFrames()
	 */
	public int getNumberOfRecycledVideoFrames();
	
	/**
	 * This method sets the frame interval used for capture. The frame interval
	 * defined the lapse of time (in second) between two captured frames and is 
	 * the inverse of the frame rate. It may or may not be supported by the 
	 * underlying hardware/driver. If not supported, calling this method 
	 * throws a {@link UnsupportedMethod} exception. This method will throw an
	 * {@link InvalidValue} exception if the given frame interval value is not
	 * supported.
	 * As a guide, you can check the {@link ResolutionInfo} objects associated
	 * with the video device to find out whether frame intervals are at all 
	 * supported, and if they are, what values (or range of values) are accepted.
	 * {@link ResolutionInfo} objects can be obtained for each 
	 * {@link ImageFormat}. See the {@link ImageFormat} and 
	 * {@link ResolutionInfo} documentation for more information.
	 * @param num the numerator of the frame interval to be set
	 * @param denom the denominator of the frame interval to be set
	 * @throws UnsupportedMethod if setting the frame interval is not supported.
	 * @throws InvalidValue if the given frame interval value is invalid.
	 * @throws StateException if capture is ongoing (the frame interval must be
	 * set when not capturing), or if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public void setFrameInterval(int num, int denom) throws InvalidValue;
	
	/**
	 * This method returns the current frame interval used for capture. 
	 * It may or may not be supported by the 
	 * underlying hardware/driver. If not supported, calling this method 
	 * throws a {@link UnsupportedMethod} exception.
	 * @throws UnsupportedMethod if setting the frame interval is not supported.
	 * @throws StateException if capture is ongoing (the frame interval must be
	 * queried when not capturing), or if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public DiscreteInterval getFrameInterval();

	/**
	 * This method adjusts the current video input number and video standard.
	 * @throws VideoStandardException if the chosen video standard is not 
         * supported
         * @throws CaptureChannelException if the given channel number value is not 
         * valid
	 */
	public void setVideoInputNStandard(int inputNumber, int standard) throws VideoStandardException, CaptureChannelException;

	/**
	 * This method retrieves the current video input channel number.
	 * @return the current input number
	 */
	public int getVideoInput();

	/**
	 * This method retrieves the current video standard used by the current video input.
	 * @return the current standard
	 */
	public int getVideoStandard();

	/**
	 * This method returns the {@link Tuner} associated with the input of this 
	 * <code>FrameGrabber</code>, or throws a {@link NoTunerException} if there
	 * is none.   
	 * @return the {@link Tuner} object associated with the chosen input.
	 * @throws NoTunerException if the selected input does not have a tuner
	 * @throws StateException if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public Tuner getTuner() throws NoTunerException;
	
	/**
	 * <code>setCaptureCallback</code> sets the {@link CaptureCallback} object
	 * which will be notified by this grabber as soon as new frames become
	 * available.<br>
	 * This method cannot be called while the capture is active, ie. in between
	 * a call to {@link #startCapture()} and {@link #stopCapture()}.
	 * @param callback an object implementing the {@link CaptureCallback}
	 * interface which will receive new frames and capture exceptions.
	 * @throws StateException if this method is invoked while capture is active,
	 * ie. after a call to {@link #startCapture()} and prior a call 
	 * to {@link #stopCapture()}.  
	 */
	public void setCaptureCallback(CaptureCallback callback);

	/**
	 * This method starts the capture. Frames will
	 * be delivered to the provided {@link CaptureCallback} object.
	 * @throws V4L4JException if no valid {@link CaptureCallback} object was 
	 * provided (call {@link #setCaptureCallback(CaptureCallback callback)}
	 * OR if the capture cannot be started
	 * @throws StateException if this <code>FrameGrabber</code> has been already
	 * released, and therefore must not be used anymore
	 */
	public void startCapture() throws V4L4JException;

	/**
	 * This method stops the capture, and recycles all @link {@link VideoFrame}s.
	 * @throws StateException if the capture has not been started, is already 
	 * stopped, or if this <code>FrameGrabber</code> has been already released, 
	 * and therefore must not be used anymore.
	 */
	public void stopCapture();

	/**
	 * This method returns the actual height of captured frames.
	 * @return the height
	 * @throws StateException if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public int getHeight();

	/**
	 * This method returns the actual width of captured frames.
	 * @return the width
	 * @throws StateException if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public int getWidth();

	/**
	 * This method returns the video channel used to capture frames.
	 * @return the channel
	 * @throws StateException if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public int getChannel();

	/**
	 * This method returns the actual video standard: 
	 * {@link V4L4JConstants#STANDARD_NTSC}, {@link V4L4JConstants#STANDARD_PAL}
	 * , {@link V4L4JConstants#STANDARD_SECAM} or 
	 * {@link V4L4JConstants#STANDARD_WEBCAM}
	 * @return the video standard
	 * @throws StateException if this 
	 * <code>FrameGrabber</code> has been already released, and therefore must
	 * not be used anymore.
	 */
	public int getStandard();

}
