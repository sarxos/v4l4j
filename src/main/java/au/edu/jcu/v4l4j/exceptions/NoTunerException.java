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
 * This class of exception is thrown when the <code>Input.getTuner()</code> method is invoked on
 * an input with no tuner (Input.getType() returns Input.CAMERA).
 * @author gilles
 */
public class NoTunerException extends V4L4JException {

	private static final long serialVersionUID = -4596596557974047977L;

	public NoTunerException(String message) {
		super(message);
	}

	public NoTunerException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public NoTunerException(Throwable throwable) {
		super(throwable);
	}

}
