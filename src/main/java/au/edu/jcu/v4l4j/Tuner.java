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

import au.edu.jcu.v4l4j.exceptions.StateException;

/**
 * Instances of this class are used to represent a tuner attached to a 
 * video input. Methods in this class control the frequency of the tuner 
 * and report the received signal strength and the automatic frequency 
 * control (when its value is negative, the frequency is too low and when 
 * positive, it is too high). The AFC may not be available on all hardware. 
 * If not, {@link #getAFC()} returns a value of 0.<code>Tuner</code>s are not 
 * directly instantiated. Instead, to retrieve the <code>Tuner</code> 
 * associated with a frame grabber, call {@link FrameGrabber#getTuner()} 
 * on the associated {@link FrameGrabber} object. Alternatively, you can also
 * obtain a list of {@link Tuner} objects by calling 
 * {@link VideoDevice#getTunerList()}. 
 * Methods in this class must not be called once the associated video device has
 * been released. Otherwise, a {@link StateException} will be thrown.
 * 
 * @author gilles
 *
 */
public class Tuner {
	private long v4l4j_object;
	private TunerInfo info;
	private int index;

	private native long getFreq(long o, int i);
	private native void setFreq(long o, int i, long f);
	private native int getAfc(long o, int i);
	private native int getRssi(long o, int i);
	
	private boolean released;
	
	Tuner(long o, TunerInfo ti){
		info = ti;
		index = info.getIndex();
		v4l4j_object = o;
		released = false;
	}
	
	/**
	 * This method returns the current frequency of this tuner.
	 * @return the current frequency 
	 * @throws StateException if the associated frame grabber has been released
	 * and this tuner must not be used anymore.
	 */
	public synchronized double getFrequency(){
		checkRelease();
		return getFreq(v4l4j_object, index)*0.0625;
	}
	
	/**
	 * This method sets the frequency of this tuner.
	 * @param f the new frequency
	 * @throws StateException if the associated frame grabber has been released
	 * and this tuner must not be used anymore.
	 */
	public synchronized void setFrequency(double f){
		checkRelease();
		long l = (long) (f/0.0625);
		setFreq(v4l4j_object, index, l);
	}
	
	/**
	 * This method returns the current AFC (Automatic Frequency Control) value. 
	 * When its value is negative, the frequency is too low and when 
	 * positive, it is too high.
	 * @return the current AFC value
	 * @throws StateException if the associated frame grabber has been released
	 * and this tuner must not be used anymore.
	 */
	public synchronized int getAFC() {
		checkRelease();
		return getAfc(v4l4j_object, index);
	}
	
	/**
	 * This method returns the current received signal strength.
	 * @return the current received signal strength
	 * @throws StateException if the associated frame grabber has been released
	 * and this tuner must not be used anymore.
	 */
	public synchronized int getRSSI(){
		checkRelease();
		return getRssi(v4l4j_object, index);
	}
	
	/**
	 * This method returns the index of this tuner.
	 * @return the index of this tuner
	 * @throws StateException if the associated frame grabber has been released
	 * and this tuner must not be used anymore.
	 */
	public synchronized int getIndex(){
		checkRelease();
		return index;
	}
	
	/**
	 * This method returns the {@link TunerInfo} object associated with this 
	 * tuner.
	 * @return the {@link TunerInfo} object associated with this tuner.
	 * @throws StateException if the associated frame grabber has been released
	 * and this tuner must not be used anymore.
	 */
	public synchronized TunerInfo getInfo(){
		checkRelease();
		return info;
	}
	
	/**
	 * This method releases this tuner.
	 */
	synchronized void release(){
		released = true;
	}
	
	private void checkRelease(){
		if(released)
			throw new StateException("The frame grabber associated with this " +
					"tuner has been already released and the tuner must not be "
					+"used anymore");
	}
}
