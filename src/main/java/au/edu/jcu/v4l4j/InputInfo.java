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

import java.util.HashSet;
import java.util.Set;

import au.edu.jcu.v4l4j.exceptions.NoTunerException;

/**
 * Objects of this class encapsulate information about a video input.<br>
 * <ul>
 * <li>The name of this input</li>
 * <li>The supported standards ({@link V4L4JConstants#STANDARD_PAL}, {@link V4L4JConstants#STANDARD_SECAM},
 * {@link V4L4JConstants#STANDARD_NTSC} or {@link V4L4JConstants#STANDARD_WEBCAM})</li>
 * <li>The input type ({@link V4L4JConstants#INPUT_TYPE_TUNER} or
 * {@link V4L4JConstants#INPUT_TYPE_CAMERA})</li>
 * <li>The {@link TunerInfo} object associated with this input if it is
 * connected to a tuner.</li>
 * </ul>
 * You do not directly instantiate <code>InputInfo</code> objects. Instead, they
 * are created by the corresponding {@link VideoDevice} object through its 
 * {@link VideoDevice#getDeviceInfo()} method.
 * @author gilles
 *
 */
public class InputInfo {
	/**
	 * The name of this input
	 */
	private String name;
	
	/**
	 * The type of this input
	 */
	private short type;
	
	/**
	 * A set of the supported standards ({@link V4L4JConstants#STANDARD_PAL}, {@link V4L4JConstants#STANDARD_SECAM},
 * {@link V4L4JConstants#STANDARD_NTSC} or {@link V4L4JConstants#STANDARD_WEBCAM})
	 */
	private Set<Integer> supportedStandards;
	
	/**
	 * The tuner associated with this input, if any
	 */
	private TunerInfo tuner;
	
	/**
	 * The V4L id associated with this input
	 */
	private int index;
	
	/**
	 * This constructor builds an Input object representing a physical input on the V4L 
	 * video device.
	 * @param n the name of the input
	 * @param stds the supported standards
	 * @param tun the tuner associated with this input if any
	 */
	private InputInfo(String n, int[] stds, short t, TunerInfo tun, int id){
		tuner = tun;
		supportedStandards = new HashSet<Integer>();
		for(int s: stds)
			supportedStandards.add(new Integer(s));
		type = t;
		name = n;
		index = id;
	}
	
	/**
	 * This constructor builds an Input object representing a TUNER input on the V4L 
	 * video device.
	 * @param n the name of the input
	 * @param stds the supported standards
	 * @param tun the tuner associated with this input
	 */
	InputInfo(String n, int[] stds, TunerInfo tun, int id){
		this(n, stds, V4L4JConstants.INPUT_TYPE_TUNER, tun, id);
	}
	
	/**
	 * This constructor builds an Input object representing a CAMERA input on the V4L 
	 * video device.
	 * @param n the name of the input
	 * @param stds the supported standards
	 */
	InputInfo(String n, int[] stds, int id){
		this(n,stds,V4L4JConstants.INPUT_TYPE_CAMERA ,null, id);
	}

	/**
	 * This method returns the name of this input
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method returns the type of this input 
	 * ({@link V4L4JConstants#INPUT_TYPE_TUNER} or {@link V4L4JConstants#INPUT_TYPE_CAMERA})
	 * @return the type of this input
	 */
	public short getType() {
		return type;
	}

	/**
	 * This method returns the standards supported by this input
	 * ({@link V4L4JConstants#STANDARD_PAL}, {@link V4L4JConstants#STANDARD_SECAM},
	 *  {@link V4L4JConstants#STANDARD_NTSC} or {@link V4L4JConstants#STANDARD_WEBCAM})
	 * @return the supportedStandards
	 */
	public Set<Integer> getSupportedStandards() {
		return supportedStandards;
	}

	/**
	 * This method returns the {@link TunerInfo} associated with this input, if any.
	 * @return the tuner
	 * @throws NoTunerException if this input is of type 
	 * {@link V4L4JConstants#INPUT_TYPE_CAMERA}, and is not a tuner.
	 */
	public TunerInfo getTunerInfo() throws NoTunerException{
		if(tuner==null)
			throw new NoTunerException("No tuner connected to this input");
		
		return tuner;
	}
	
	/**
	 * This method returns a boolean depending on whether this input
	 * has a tuner or not.
	 * @return true if this input has a tuner, false otherwise
	 */
	public boolean hasTuner() {
		return tuner == null ? false : true;
	}
	
	/**
	 * This method returns the (V4L) index of this input
	 * @return the (V4L) index of this input
	 */
	public int getIndex(){
		return index;
	}
}
