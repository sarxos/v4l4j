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

package au.edu.jcu.v4l4j.exceptions;

/**
 * This class of exception is thrown when a unexpected method is invoked 
 * on a v4l4j object the framegrabber when its current state forbids the 
 * execution of that method. For instance, calling 
 * <code>getVideoFrame()</code> before calling <code>init()</code>, 
 * <code>startCapture()</code> is illegal and <code>getVideoFrame()</code> 
 * will throw a StateException. 
 * @author gilles
 */
public class StateException extends RuntimeException {

	private static final long serialVersionUID = 1351714754008657462L;

	public StateException(String message) {
		super(message);
	}

	public StateException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public StateException(Throwable throwable) {
		super(throwable);
	}
}
