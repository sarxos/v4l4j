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

import java.util.List;
import java.util.Vector;

import au.edu.jcu.v4l4j.exceptions.StateException;

/**
 * Objects of this class encapsulate a list of available {@link Tuner}s. 
 * This class can not be directly instantiated. Instead, to retrieve a list of 
 * tuners from a {@link VideoDevice}, use its
 * {@link VideoDevice#getTunerList() getTunerList()} method.  
 * @author gilles
 *
 */
public class TunerList {
	private Vector<Tuner> tuners;
	private boolean released;
	
	/**
	 * This constructor builds a control list from the given list. (a copy of 
	 * the list object is made).
	 * @param c the tuner list used to initialise this object.
	 */
	TunerList(List<Tuner> t){
		tuners= new Vector<Tuner>(t);
		released = false;
	}
	
	/**
	 * This method returns a copy of the tuner list.
	 * @return a copy of the tuner list.
	 * @throws StateException if this tuner list has been released and must 
	 * not be used anymore
	 */
	public synchronized List<Tuner> getList(){
		checkReleased();
		return new Vector<Tuner>(tuners);
	}
	
	/**
	 * This method returns a tuner given its index.
	 * @return the tuner matching the given index, null otherwise
	 * @throws StateException if this tuner list has been released and must not 
	 * be used anymore
	 * @throws ArrayIndexOutOfBoundsException if the given index is out of bounds
	 */
	public synchronized Tuner getTuner(int i){
		checkReleased();
		for(Tuner t: tuners)
			if(t.getIndex()==i)
				return t;
		
		throw new ArrayIndexOutOfBoundsException("No tuner with such index");
	}
	
	/**
	 * This method released the tuner list, and all tuners in it.
	 */
	synchronized void release(){
		released = true;
		for(Tuner t: tuners)
			t.release();
	}

	private void checkReleased(){
		if(released)
			throw new StateException("The tuner list has been released and " +
					"must not be used");
	}
}

