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

import au.edu.jcu.v4l4j.VideoDevice;

/**
 * This class of exception is thrown when trying to release a 
 * {@link VideoDevice} using its {@link VideoDevice#release(boolean)} method
 * with a <code>true</code> argument, when the video device is still in use  
 * @author gilles
 *
 */
public class ReleaseException extends RuntimeException {

	private static final long serialVersionUID = -7410432021368200123L;

	public ReleaseException(String message) {
		super(message);
	}

	public ReleaseException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ReleaseException(Throwable throwable) {
		super(throwable);
	}
}
