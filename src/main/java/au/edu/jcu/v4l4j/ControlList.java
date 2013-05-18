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

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import au.edu.jcu.v4l4j.exceptions.StateException;

/**
 * Objects of this class encapsulate a list of video source {@link Control}. This class
 * can not be directly instantiated. Instead, to retrieve a list of controls 
 * from a {@link VideoDevice}, use its 
 * {@link VideoDevice#getControlList() getControlList()} method. Once
 * the control list and associated controls are no longer needed, they must be 
 * released by calling {@link VideoDevice#releaseControlList() releaseControlList()}.
 * After that, neither the list nor the controls themselves must be used. If 
 * any attempt to use them is made, a {@link StateException} will be raised.
 * @author gilles
 *
 */
public class ControlList {
	private Hashtable<String,Control> controls;
	private boolean released;
	
	/**
	 * This constructor builds a control list from the given list. (no copy is made)
	 * @param c the control list used to initialise this object.
	 */
	ControlList(Hashtable<String,Control> c){
		controls = c;
		released = false;
	}
	
	/**
	 * This constructor builds a control list from the given list. (no copy is made)
	 * @param c the control list used to initialise this object.
	 */
	ControlList(Control[] c){
		controls = new Hashtable<String, Control>();
		for(Control ctrl: c)
			controls.put(ctrl.getName(), ctrl);

		released = false;
	}
	
	/**
	 * This method returns a map of control names and controls
	 * @return a map of control names and controls
	 * @throws StateException if this control list has been released and must not be used anymore
	 */
	public synchronized Hashtable<String,Control> getTable(){
		checkReleased();
		return new Hashtable<String,Control>(controls);
	}
	
	/**
	 * This method returns a list of {@link Control}s
	 * @return a list of {@link Control}s
	 * @throws StateException if this control list has been released and must not be used anymore
	 */
	public synchronized List<Control> getList(){
		checkReleased();
		return new Vector<Control>(controls.values());
	}
	
	/**
	 * This method returns a control given its name.
	 * @return the control matching the given name, null otherwise
	 * @throws StateException if this control list has been released and must not be used anymore
	 */
	public synchronized Control getControl(String n){
		checkReleased();
		return controls.get(n);
	}
	
	/**
	 * This method released the control list, and all controls in it.
	 */
	synchronized void release(){
		released = true;
		for(Control c: controls.values())
			c.release();
	}

	private void checkReleased(){
		if(released)
			throw new StateException("The control list has been released and must not be used");
	}
}
