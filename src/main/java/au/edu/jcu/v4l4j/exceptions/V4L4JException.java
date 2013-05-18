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
 * A V4L4JExcption is the common superclass of all exceptions thrown by v4l4j. 
 * @author gilles
 */
public class V4L4JException extends Exception {

	private static final long serialVersionUID = -8247407640809375121L;
	public V4L4JException(String message) {
		    super(message);
		  }
	public V4L4JException(String message, Throwable throwable) {
		super(message, throwable);
	}
	public V4L4JException(Throwable throwable) {
		super(throwable);
	}
}
