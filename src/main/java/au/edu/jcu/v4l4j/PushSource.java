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

import java.util.concurrent.ThreadFactory;

import au.edu.jcu.v4l4j.exceptions.StateException;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;


/**
 * PushSource instances create their own thread which polls
 * a frame grabber and notify the 
 * {@link CaptureCallback} object given in the constructor
 * each time a new frame is available.
 * @author gilles
 *
 */
class PushSource implements Runnable {
	private CaptureCallback 		callback;
	private AbstractGrabber			frameGrabber;
	private Thread					thread;
	private ThreadFactory			threadFactory;
	
	private int						state;
	private static final int		STATE_STOPPED = 0;
	private static final int		STATE_RUNNING = 1;
	private static final int		STATE_ABOUT_TO_STOP = 2;
	
	/**
	 * This method builds a new <code>PushSource</code> instance
	 * which will obtain frames from the given frame grabber and
	 * pass them to the given callback object.
	 * @param grabber the {@link FrameGrabber} instance on which
	 * this push source will repeatedly call {@link FrameGrabber#getVideoFrame()}.
	 * @param callback an object implementing the {@link CaptureCallback}
	 * interface to which the frames will be delivered through the 
	 * {@link CaptureCallback#nextFrame(VideoFrame)}.
	 */
	public PushSource(AbstractGrabber grabber, CaptureCallback callback, ThreadFactory factory) {
		if ((grabber == null) || (callback == null))
			throw new NullPointerException("the frame grabber and callback cannot be null");
		
		this.callback = callback;
		frameGrabber = grabber;
		threadFactory = factory;
		state = STATE_STOPPED;
	}
	
	/**
	 * This method instructs this source to start the capture and to push
	 * to the captured frame to the  
	 * {@link CaptureCallback}. 
	 * @throws StateException if the capture has already been started
	 */
	public synchronized final long startCapture() throws V4L4JException{
		if (state != STATE_STOPPED)
			throw new StateException("The capture has already been started");
		
		// Update our state and start the thread
		state = STATE_RUNNING;
		thread = threadFactory.newThread(this);
		thread.setName(thread.getName() + " - v4l4j push source");
		thread.start();

		return thread.getId();
	}
	
	/**
	 * This method instructs this source to stop frame delivery
	 * to the {@link CaptureCallback} object.
	 * @throws StateException if the capture has already been stopped
	 */
	public final void stopCapture() {
		synchronized (this) {
			// make sure we are running
			if ((state == STATE_STOPPED) || (state == STATE_ABOUT_TO_STOP))
				//throw new StateException("The capture is about to stop");
				return;

			// update our state
			state = STATE_ABOUT_TO_STOP;
		}
		
		if (thread.isAlive()) {
			thread.interrupt();
			
			// wait for thread to exit if the push thread is not the one
			// trying to join
			if (! Thread.currentThread().equals(thread)) {
				while (state == STATE_ABOUT_TO_STOP) {
					try {
						// wait for thread to exit
						thread.join();
						//thread = null;
					} catch (InterruptedException e) {
						// interrupted while waiting for the thread to join
						// keep waiting
						// System.err.println("interrupted while waiting for frame pusher thread to complete");
						// e.printStackTrace();
						// throw new StateException("interrupted while waiting for frame pusher thread to complete", e);
					}
				}
			}
			
		}
	}

	@Override
	public final void run() {
		VideoFrame frame = null;
		
		while (! Thread.interrupted()){
			try {
				// Get the next frame 
				frame = frameGrabber.getNextVideoFrame();
				
				// and deliver it to the callback object
				try { callback.nextFrame(frame); }
				catch (Throwable t) {} // ignore any exception thrown by the callback
			} catch (Throwable t) {
				// Received an exception. If we are in the middle of a capture (ie. it does not
				// happen as the result of the capture having been stopped or the frame 
				// grabber released), then pass it on to the callback object.
				//e.printStackTrace();
				try {
					if(frameGrabber.isStarted())
						callback.exceptionReceived(new V4L4JException("Exception received while grabbing next frame", t));
				} catch (Throwable t2) {
					// either the frame grabber has been released or the callback raised
					// an exception. do nothing, just exit.
				}
				// and make this thread exit
				Thread.currentThread().interrupt();
			}
		}

		// update state
		state = STATE_STOPPED;
	}
}
