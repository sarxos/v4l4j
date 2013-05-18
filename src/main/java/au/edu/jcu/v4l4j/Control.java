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
import java.util.Map;
import java.util.Vector;

import au.edu.jcu.v4l4j.exceptions.ControlException;
import au.edu.jcu.v4l4j.exceptions.StateException;
import au.edu.jcu.v4l4j.exceptions.UnsupportedMethod;

/**
 * Objects of this class represent a video source control of any nature.
 * <code>Control</code> objects are not directly instantiated. Instead a
 * list of <code>Control</code>s associated with a <code>VideoDevice</code>
 * can be obtained by calling {@link VideoDevice#getControlList() getControlList()}.
 * Once the controls are no longer used, they must be released by calling 
 * {@link VideoDevice#releaseControlList() releaseControlList()}. Any attempt 
 * to use a control after it has been released will result in a 
 * {@link StateException}.<br> A control can be either of the following types:<br>
 * <ul>
 * <li><code>V4L4JConstants.CTRL_TYPE_BUTTON</code>: Such controls take only 2 different
 * values: 0 and 1</li>
 * <li><code>V4L4JConstants.CTRL_TYPE_SLIDER</code>: Controls of this type take a range
 * of values between a minimum value (returned by {@link #getMinValue()}) and a
 * maximum value (returned by {@link #getMaxValue()}) with an increment value 
 * (returned by {@link #getStepValue()})</li>
 * <li><code>V4L4JConstants.CTRL_TYPE_SWITCH</code>: Switch controls do not take any
 * specific value, and attempting to read its current value will always return 
 * 0. Setting any value will activate the switch.</li>
 * <li><code>V4L4JConstants.CTRL_TYPE_DISCRETE</code>: These controls accept only specific
 * discrete values, which can be retrieved using {@link #getDiscreteValues()}.
 * Each discrete value may be accompanied by a description, which is returned by
 * {@link #getDiscreteValueNames()}.</li>
 * <li><code>V4L4JConstants.CTRL_TYPE_STRING</code>: These controls accept string values, 
 * which can be retrieved using {@link #getStringValue()} and set with 
 * {@link #setStringValue(String)}. Calls to any other get/set methods will result in 
 * {@link UnsupportedMethod} exceptions. {@link #getMinValue()} & {@link #getMaxValue()} 
 * return the smallest / largest string length (number of characters) this control will accept. 
 * {@link #getStepValue()} specify the step length of the string.</li>
 * <li><code>V4L4JConstants.CTRL_TYPE_LONG</code>: These controls accept long values, 
 * between {@link Long#MIN_VALUE} and {@link Long#MAX_VALUE} with a step value
 * of 1. The actual value is set / retrieved
 * with {@link Control#setLongValue(long)} and {@link Control#getLongValue()}.
 * Calls to any other get/set methods, {@link #getMinValue()}, 
 * {@link #getMaxValue()} and {@link #getStepValue()} will result in {@link UnsupportedMethod}
 * exceptions.</li>
 * <li><code>V4L4JConstants.CTRL_TYPE_BITMASK</code>: These controls accept bitmask values, 
 * between 0 and {@link Integer#MAX_VALUE} with a step value
 * of 1. The actual value is set / retrieved
 * with {@link Control#setValue(int)} and {@link Control#getValue()}.
 * Calls to any other get/set methods will result in {@link UnsupportedMethod}
 * exceptions.</li>
 * </ul>
 * @author gilles
 *
 */
public class Control {

	/**
	 * This JNI method returns the value of a control given its id.
	 * @param o a C pointer to a struct v4l4j_device
	 * @param id the id of the control
	 * @return the value
	 * @throws ControlException if the value cant be retrieved.
	 */
	private native int doGetValue(long o, int id) throws ControlException;
	
	/**
	 * This JNI method sets the value of a control given its id.
	 * @param o a C pointer to a struct v4l4j_device
	 * @param id the id of the control
	 * @param v the new value
	 * @throws ControlException if the value cant be set.
	 */
	private native int doSetValue(long o, int id, int v)  throws ControlException;
	
	/**
	 * This JNI method returns the string value of a control given its id.
	 * @param o a C pointer to a struct v4l4j_device
	 * @param id the id of the string control
	 * @return the value
	 * @throws ControlException if the value cant be retrieved.
	 */
	private native String doGetStringValue(long o, int id) throws ControlException;
	
	/**
	 * This JNI method sets the value of a control given its id.
	 * @param o a C pointer to a struct v4l4j_device
	 * @param id the id of the stirng control
	 * @param v the new value
	 * @throws ControlException if the value cant be set.
	 */
	private native void doSetStringValue(long o, int id, String v)  throws ControlException;
	
	/**
	 * This JNI method returns the value of a control given its id.
	 * @param o a C pointer to a struct v4l4j_device
	 * @param id the id of the control
	 * @return the value
	 * @throws ControlException if the value cant be retrieved.
	 */
	private native long doGetLongValue(long o, int id) throws ControlException;
	
	/**
	 * This JNI method sets the value of a control given its id.
	 * @param o a C pointer to a struct v4l4j_device
	 * @param id the id of the control
	 * @param v the new value
	 * @throws ControlException if the value cant be set.
	 */
	private native int doSetLongValue(long o, int id, long v)  throws ControlException;
	
	private int id;
	private String name;
	private int max;
	private int min;
	private int step;
	private int type;
	private Vector<String> names;
	private int[] values;
	private long v4l4jObject;
	private int defaultValue;
	private State state;
	
	/**
	 * Builds a V4L2 control.
	 * @param id the index of the control
	 * @param name the name of the control
	 * @param min the minimum value it will accept
	 * @param max the maximum value it will accept
	 * @param step the increments
	 * @param type the type of this control
	 * @param names the names of the discrete values (if any), otherwise null
	 * @param values the discrete values if any. Otherwise null
	 * @param o A C pointer to a struct v4l4j_device
	 */
	Control(int id, String name, int min, int max, int step, int type, String[] names, int[] values, long o) {
		this.id = id;
		this.name = new String(name);
		this.min=min;
		this.max=max;
		this.step=step;
		this.type= type;
		if(names!=null) {
			this.names = new Vector<String>();
			for(String s: names)
					this.names.add(s);
		}
		this.values = values;
		
		if(min<=0 && 0<=max)
			this.defaultValue = 0;
		else
			this.defaultValue = (int) Math.round((max - min) / 2.0) + min;
		v4l4jObject = o;
		state = new State();
	}
	
	/**
	 * This method retrieves the current value of this control. Some controls
	 * (for example relative values like pan or tilt) are write-only and getting
	 * their value does not make sense. Invoking this method on this kind of
	 * controls will trigger a ControlException.   
	 * @return the current value of this control (0 if it is a button)
	 * @throws ControlException if the value cannot be retrieved
	 * @throws UnsupportedMethod if this control is of type {@link V4L4JConstants#CTRL_TYPE_STRING}
	 * (and you should be using {@link #getStringValue()} instead) or 
	 * if this control is of type {@link V4L4JConstants#CTRL_TYPE_LONG}
	 * (and you should be using {@link #setLongValue(long)} instead).
	 * @throws StateException if this control has been released and must not be used anymore.
	 */
	public int getValue() throws ControlException{
		int v = 0;

		if(type==V4L4JConstants.CTRL_TYPE_STRING)
			throw new UnsupportedMethod("This control is a string control and does not accept calls to getValue()");
		if(type==V4L4JConstants.CTRL_TYPE_LONG)
			throw new UnsupportedMethod("This control is a long control and does not support calls to setValue()");
		
		state.get();
		if(type==V4L4JConstants.CTRL_TYPE_BUTTON) {
			state.put();
			return 0;
		}
		
		try {v = doGetValue(v4l4jObject, id);}
		catch (ControlException ce){
			state.put();
			throw ce;
		}
		state.put();
		return v;
	}

	/**
	 * This method sets a new value for this control. The returned value is the new value
	 * of the control, which for normal controls will be identical to the one set. However, 
	 * some controls (for example relative values like pan or tilt) are write-only and getting
	 * their value does not make sense. The return value in this case is the 
	 * control's default value (as returned by {@link #getDefaultValue()}).
	 * @param value the new value
	 * @return the new value of the control after setting it, or the control's default
	 * value
	 * @throws ControlException if the value can not be set.
	 * @throws UnsupportedMethod if this control is of type {@link V4L4JConstants#CTRL_TYPE_STRING}
	 * (and you should be using {@link #setStringValue(String)} instead) or 
	 * if this control is of type {@link V4L4JConstants#CTRL_TYPE_LONG}
	 * (and you should be using {@link #setLongValue(long)} instead).
	 * @throws StateException if this control has been released and must not be used anymore.
	 */
	public int setValue(int value) throws ControlException {
		int v = defaultValue;
		
		if(type==V4L4JConstants.CTRL_TYPE_STRING)
			throw new UnsupportedMethod("This control is a string control and does not support calls to setValue()");
		if(type==V4L4JConstants.CTRL_TYPE_LONG)
			throw new UnsupportedMethod("This control is a long control and does not support calls to setValue()");
		
		state.get();
		if(type==V4L4JConstants.CTRL_TYPE_BUTTON)
			value = 0;
		else
			value = validateValue(value);
		
		try { doSetValue(v4l4jObject,id, value);}
		catch (ControlException ce){
			state.put();
			throw ce;
		}
		try {v = getValue();} catch (ControlException ce){}
		state.put();
		return v;
	}

	/**
	 * This method retrieves the current string value of this control. 
	 * Some controls may be write-only and getting
	 * their value does not make sense. Invoking this method on this kind of
	 * controls will trigger a ControlException.   
	 * @return the current value of this control
	 * @throws ControlException if the value cannot be retrieved
	 * @throws UnsupportedMethod if this control
	 * is not of type {@link V4L4JConstants#CTRL_TYPE_STRING}.
	 * @throws StateException if this control has been released and must not be used anymore.
	 */
	public String getStringValue() throws ControlException{
		String v;
		
		if(type!=V4L4JConstants.CTRL_TYPE_STRING)
			throw new UnsupportedMethod("This control is not a string control");
		
		state.get();
		
		try {v = doGetStringValue(v4l4jObject, id);}
		finally{state.put(); }
			
		return v;
	}

	/**
	 * This method sets a new string value for this control. The returned value 
	 * is the new value of the control.
	 * @param value the new value
	 * @return the new value of the control after setting it
	 * @throws ControlException if the value can not be set, or if the new string value's length
	 * is under / over the minimum / maximum length.
	 * @throws UnsupportedMethod if this control is not of type {@link V4L4JConstants#CTRL_TYPE_STRING},
	 * @throws StateException if this control has been released and must not be used anymore.
	 */
	public String setStringValue(String value) throws ControlException {
		String v = null;
		
		if(type!=V4L4JConstants.CTRL_TYPE_STRING)
			throw new UnsupportedMethod("This control is not a string control");
		
		if (value.length() > max)
			throw new ControlException("The new string value for this control exceeds the maximum length");
		if (value.length() < min)
			throw new ControlException("The new string value for this control is below the minimum length");
		
		state.get();
		
		try { 
			doSetStringValue(v4l4jObject,id, value);
			v = doGetStringValue( v4l4jObject, id);
		} finally {
			state.put();
		}

		return v;
	}
	
	/**
	 * This method retrieves the current long value of this control. 
	 * Some controls may be write-only and getting
	 * their value does not make sense. Invoking this method on this kind of
	 * controls will trigger a ControlException.   
	 * @return the current value of this control
	 * @throws ControlException if the value cannot be retrieved
	 * @throws UnsupportedMethod if this control
	 * is not of type {@link V4L4JConstants#CTRL_TYPE_LONG}.
	 * @throws StateException if this control has been released and must not be used anymore.
	 */
	public long getLongValue() throws ControlException{
		long v;
		
		if(type!=V4L4JConstants.CTRL_TYPE_LONG)
			throw new UnsupportedMethod("This control is not a long control");
		
		state.get();
		
		try {v = doGetLongValue(v4l4jObject, id);}
		finally{state.put(); }
			
		return v;
	}

	/**
	 * This method sets a new long value for this control. The returned value 
	 * is the new value of the control.
	 * @param value the new value
	 * @return the new value of the control after setting it
	 * @throws ControlException if the value can not be set.
	 * @throws UnsupportedMethod if this control is not of type {@link V4L4JConstants#CTRL_TYPE_LONG},
	 * @throws StateException if this control has been released and must not be used anymore.
	 */
	public long setLongValue(long value) throws ControlException {
		long v = 0;
		
		if(type!=V4L4JConstants.CTRL_TYPE_LONG)
			throw new UnsupportedMethod("This control is not a long control");
		
		state.get();
		
		try {
			doSetLongValue(v4l4jObject,id, value);
			v = doGetLongValue(v4l4jObject, id);
		} finally {
			state.put();
		}

		return v;
	}

	
	/**
	 * This method increases this control's current value by its step (as returned
	 * by {@link #getStepValue()})</code>. The returned value is the new value
	 * of the control, which for normal controls will be identical to the old one plus
	 * the step value. However, some controls (for example relative values like pan
	 * or tilt) are write-only and getting their value does not make sense. In this case,
	 * this method sets the new value to the default one plus the step value.
	 * Note that, the return value in this case is still the control's default value 
	 * (as returned by {@link #getDefaultValue()}), not the default one plus the step value.
	 * @return the new value of the control after increasing its old value, or the control's default
	 * value
	 * @throws ControlException if the value cannot be increased
	 * @throws UnsupportedMethod if this control is of type {@link V4L4JConstants#CTRL_TYPE_STRING}
	 * or {@link V4L4JConstants#CTRL_TYPE_LONG}
	 * @throws StateException if this control has been released and must not be used anymore.
	 */
	public int increaseValue() throws ControlException {
		int old = defaultValue;
		
		if(type==V4L4JConstants.CTRL_TYPE_STRING)
			throw new UnsupportedMethod("This control is a string control and does not accept calls to increaseValue()");
		if(type==V4L4JConstants.CTRL_TYPE_LONG)
			throw new UnsupportedMethod("This control is a long control and does not support calls to increaseValue()");
		
		/**
		 * the following try statement is here so that write-only 
		 * controls (Relative Pan for instance) that return a ControlException
		 * when read can still have their value increase()d.
		 */
		state.get();
		try { old = doGetValue(v4l4jObject,id); } catch (ControlException e) {}
		try { old = doSetValue(v4l4jObject,id, validateValue(old+step));}
		catch (ControlException ce) {
			state.put();
			throw ce;
		}
		state.put();
		return old;
	}
	
	/**
	 * This method decreases this control's current value by its step (as returned
	 * by {@link #getStepValue()}. The returned value is the new value
	 * of the control, which for normal controls will be identical to the old one minus
	 * the step value. However, some controls (for example relative values like pan
	 * or tilt) are write-only and getting their value does not make sense. In this case,
	 * this method sets the new value to the default one minus the step value.
	 * Note that, the return value in this case is still the control's default value 
	 * (as returned by {@link #getDefaultValue()}), not the default one plus the step value.
	 * @return the new value of the control after decreasing it.
	 * @throws ControlException if the value can not be increased
	 * @throws UnsupportedMethod if this control is of type {@link V4L4JConstants#CTRL_TYPE_STRING}
	 * or {@link V4L4JConstants#CTRL_TYPE_LONG}
	 * @throws StateException if this control has been released and must not be used anymore.
	 */
	public int decreaseValue() throws ControlException {
		int old = defaultValue;
		
		if(type==V4L4JConstants.CTRL_TYPE_STRING)
			throw new UnsupportedMethod("This control is a string control and does not support calls to decreaseValue()");
		if(type==V4L4JConstants.CTRL_TYPE_LONG)
			throw new UnsupportedMethod("This control is a long control and does not support calls to decreaseValue()");
		
		/**
		 * the following try statement is here so that write-only 
		 * controls (Relative Pan for instance) that return a ControlException
		 * when read can still have their value decrease()d
		 */
		state.get();
		try { old = doGetValue(v4l4jObject,id);} catch (ControlException e) {}
		try {old = doSetValue(v4l4jObject,id, validateValue(old-step));}
		catch (ControlException ce){
			state.put();
			throw ce;
		}
		state.put();
		return old;
	}

	/**
	 * This method retrieves the maximum value this control will accept.
	 * If this control is a string control, this method returns the maximum string
	 * size that can be set on this control.
	 * 
	 * @return the maximum value, or the largest string size
	 * @throws StateException if this control has been released and must not be used anymore.
	 * @throws UnsupportedMethod if this control is of type {@link V4L4JConstants#CTRL_TYPE_LONG}
	 */
	public int getMaxValue() {
		if(type==V4L4JConstants.CTRL_TYPE_LONG)
			throw new UnsupportedMethod("This control is a long control and does not support calls to getMaxValue()");
		
		synchronized(state){
			if(state.isNotReleased())
				return max;
			else
				throw new StateException("This control has been released and must not be used");
		}
	}

	/**
	 * This method retrieves the minimum value this control will accept.
	 * If this control is a string control, this method returns the minimum string
	 * size that can be set on this control.
	 * 
	 * @return the minimum value or the smallest string size
	 * @throws StateException if this control has been released and must not be used anymore.
	 * @throws UnsupportedMethod if this control is of type {@link V4L4JConstants#CTRL_TYPE_LONG}
	 */
	public int getMinValue() {
		if(type==V4L4JConstants.CTRL_TYPE_LONG)
			throw new UnsupportedMethod("This control is a long control and does not support calls to getMinValue()");
		
		synchronized(state){
			if(state.isNotReleased())
				return min;
			else
				throw new StateException("This control has been released and must not be used");
		}
	}

	/**
	 * This method retrieves the name of this control.
	 * @return the name of this control
	 * @throws StateException if this control has been released and must not be used anymore.
	 */
	public String getName() {
		synchronized(state){
			if(state.isNotReleased())
				return name;
			else
				throw new StateException("This control has been released and must not be used");
		}
	}

	/**
	 * This method retrieves the increment to be used when setting a new value
	 * for this control. New values must be equal to <code>getMin() + K*getStep()</code>
	 * where K is an integer, and the result is less or equal to {@link #getMaxValue()}.
	 * For string control, this  method returns the step size of the string.
	 * @return the increment
	 * @throws StateException if this control has been released and must not be used anymore.
 	 * @throws UnsupportedMethod if this control is of type {@link V4L4JConstants#CTRL_TYPE_LONG}
	 */
	public int getStepValue() {
		if(type==V4L4JConstants.CTRL_TYPE_LONG)
			throw new UnsupportedMethod("This control is a long control and does not support calls to getStepValue()");
		synchronized(state){
			if(state.isNotReleased())
				return step;
			else
				throw new StateException("This control has been released and must not be used");
		}
	}

	/**
	 * This method retrieves the type of this control. Values are
	 * <code>V4L4JConstants.CTRL_TYPE_BUTTON</code>, <code>V4L4JConstants.CTRL_TYPE_SLIDER</code>,
	 * <code>V4L4JConstants.CTRL_TYPE_SWITCH</code>, <code>V4L4JConstants.CTRL_TYPE_DISCRETE</code>,
	 * <code>V4L4JConstants.CTRL_TYPE_STRING</code>, <code>V4L4JConstants.CTRL_TYPE_LONG</code>,
	 * <code>V4L4JConstants.CTRL_TYPE_BITMASK</code>.
	 * @return the type of this controls
	 * @throws StateException if this control has been released and must not be used anymore.
	 */
	public int getType() {
		synchronized(state){
			if(state.isNotReleased())
				return type;
			else
				throw new StateException("This control has been released and must not be used");
		}
	}
	
	/**
	 * This method returns the default value for this control
	 * @return the default value for this control
	 * @throws UnsupportedMethod if this control is of type {@link V4L4JConstants#CTRL_TYPE_STRING}
	 * @throws StateException if this control has been released and must not be used anymore
	 */
	public int getDefaultValue() {
		if(type==V4L4JConstants.CTRL_TYPE_STRING)
			throw new UnsupportedMethod("This control is a string control and does not support calls to getDefaultValue()");
		if(type==V4L4JConstants.CTRL_TYPE_LONG)
			throw new UnsupportedMethod("This control is a long control and does not support calls to getDefaultValue()");
		
		synchronized(state){
			if(state.isNotReleased())
				return defaultValue;
			else
				throw new StateException("This control has been released and must not be used");
		}
	}
	
	/**
	 * This method returns a list of the discrete values accepted by this control.
	 * The list is ordered in the same way as the list returned by {@link #getDiscreteValueNames()}.
	 * So a value at index 'i' in the list returned by this method, matches the name
	 * at the same index in the list returned by {@link #getDiscreteValueNames()}.
	 * @return a list of the discrete values accepted by this control
	 * @throws UnsupportedMethod if this control does not support discrete values. 
	 * Check this control's type (with {@link #getType()}) and use the appropriate method.
	 * @throws StateException if this control has been released and must not be used anymore
	 */
	public List<Integer> getDiscreteValues(){
		if(type!=V4L4JConstants.CTRL_TYPE_DISCRETE || values==null)
			throw new UnsupportedMethod("This control does not accept discrete values");
		state.get();
		Vector<Integer> v = new Vector<Integer>();
		for(int i: values)
			v.add(new Integer(i));
		state.put();
		return v;
			
	}
	
	/**
	 * This method returns the list of names for each of the supported discrete values.
	 * The list is ordered in the same way as the list returned by {@link #getDiscreteValues()}.
	 * So the name at index 'i' in the list returned by this method, matches the value
	 * at the same index in the list returned by {@link #getDiscreteValues()}.
	 * @return a list of names for each of the supported discrete values.
	 * @throws UnsupportedMethod if this control does not support discrete values. 
	 * Check this control's type (with {@link #getType()}) and use the appropriate method.
	 * @throws StateException if this control has been released and must not be used anymore
	 */
	public List<String> getDiscreteValueNames(){
		if(type!=V4L4JConstants.CTRL_TYPE_DISCRETE || names==null)
			throw new UnsupportedMethod("This control does not have discrete values");
		state.get();
		Vector<String> v = new Vector<String>(names);
		state.put();
		return v;		
	}
	
	/**
	 * This method returns a map of all discrete value names and their values.
	 * @return a map of all discrete value names and their values.
	 * @throws UnsupportedMethod if this control does not support discrete values. 
	 * Check this control's type (with {@link #getType()}) and use the appropriate method.
	 * @throws StateException if this control has been released and must not be used anymore
	 */
	public Map<String, Integer> getDiscreteValuesMap(){
		if(type!=V4L4JConstants.CTRL_TYPE_DISCRETE || names==null)
			throw new UnsupportedMethod("This control does not have discrete values");
		state.get();
		Hashtable<String,Integer> t = new Hashtable<String,Integer>();
		for(int i=0;i<names.size(); i++)
			t.put(names.get(i), values[i]);

		state.put();
		return t;		
	}
	
	/**
	 * This method releases this control. Any attempt to use the control afterwards will
	 * raise a <code>StateException</code>
	 */
	void release(){
		state.release();
	}
	/**
	 * This method validates the given value, ie it checks that it is between
	 * the allowed minimum & maximum. If it is, the given value is returned.
	 * Otherwise, it throws a ControlException  
	 * @param val the value to be checked
	 * @return the value
	 * @throws ControlException if the value is off-limit
	 */
	private int validateValue(int val) throws ControlException{
		if(val < min || val > max)
			throw new ControlException("The value '"+val+"' is outside the allowed range");
		
		return val;
	}
	
	private class State {

		private int state;
		private int temp;
		private int users;
		
		private int INIT=0;
		private int RELEASED=1;

		public State() {
			state = INIT;
			temp = INIT;
			users = 0;
		}
		
		public synchronized void get(){
			if(state==INIT  && temp!=RELEASED) 
				users++;
			else
				throw new StateException("This Control has been released and must not be used");
			// System.out.println("GET "+(users-1)+"->"+users);
		}
		
		public synchronized void put(){
			if(state==INIT) {
				if(--users==0  && temp==RELEASED)
					notify();
			} else
				throw new StateException("This Control has been released and must not be used");
			// System.out.println("PUT "+(users+1)+"->"+users);
		}
		
		/**
		 * Must be called with the lock on this object(State) held
		 * @return
		 */
		public boolean isNotReleased(){
			return state==INIT && temp!=RELEASED;
		}
		
		
		public synchronized void release(){
			if(state==INIT && temp!=RELEASED) {
				temp=RELEASED;
				// System.out.println("RELEASE "+name+" - "+users);
				while(users!=0)
					try {
						wait();
					} catch (InterruptedException e) {
						System.err.println("Interrupted while waiting for Control users to complete");
						e.printStackTrace();
						throw new StateException("There are remaining users of this Control and it can not be stopped");
					}
			} 
			//commented out so we can release() a control multiple times
//			else
//				throw new StateException("This Control has been released");
		}
		
		@SuppressWarnings("unused")
		public synchronized void commit(){
			state=temp;
		}
	}
}
