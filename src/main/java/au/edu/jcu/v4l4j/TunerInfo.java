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

/**
 * Object of this class encapsulate information about a tuner:
 * <ul>
 * <li>its type: {@link V4L4JConstants#TUNER_TYPE_RADIO} or {@link V4L4JConstants#TUNER_TYPE_TV},</li>
 * <li>the frequency unit: {@link V4L4JConstants#FREQ_KHZ} or {@link V4L4JConstants#FREQ_MHZ},</li>
 * <li>the name of the tuner,</li>
 * <li>the index of the tuner,</li>
 * <li>and the lowest and highest tunable frequencies.</li>  
 * </ul>
 * @author gilles
 *
 */
public class TunerInfo {
	/**
	 * The type of this tuner (RADIO or TV)
	 */
	private int type;
	
	/**
	 * Frequency unit (MHZ or KHZ)
	 */
	private int unit;
	
	/**
	 * The lowest & highest tunable frequencies
	 */
	private long rangeLow, rangeHigh;
	
	/**
	 * The name of this tuner
	 */
	private String name;
	
	/**
	 * The V4L index of this tuner 
	 */
	private int index;
	
	
	TunerInfo(String n, int id, int u, int t, long low, long high){
		type = t;
		unit = u;
		rangeLow = low & 0xffffffff;
		rangeHigh = high & 0xffffffff;
		name = n;
		index = id;
		
	}

	/**
	 * This method returns the type of this tuner ({@link V4L4JConstants#TUNER_TYPE_RADIO}
	 *  or {@link V4L4JConstants#TUNER_TYPE_TV}).
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * This method returns the unit used by this tuner ({@link V4L4JConstants#FREQ_MHZ}
	 *  or {@link V4L4JConstants#FREQ_KHZ}).
	 * @return the unit ({@link V4L4JConstants#FREQ_MHZ}
	 *  or {@link V4L4JConstants#FREQ_KHZ}).
	 */
	public int getUnit() {
		return unit;
	}


	/**
	 * This method returns the lowest tunable frequency supported by this tuner.
	 * @return the lowest tunable frequency supported by this tuner.
	 */
	public double getRangeLow() {
		return rangeLow*0.0625;
	}


	/**
	 * This method returns the highest tunable frequency supported by this tuner.
	 * @return the highest tunable frequency supported by this tuner.
	 */
	public double getRangeHigh() {
		return rangeHigh*0.0625;
	}


	/**
	 * This method returns the name of this tuner.
	 * @return the name of this tuner.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * This method returns the V4L index of this tuner.
	 * @return the V4L index of this tuner.
	 */
	public int getIndex(){
		return index;
	}
}
