/*
* Copyright (C) 2011 Gilles Gigan (gilles.gigan@gmail.com)
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

import java.awt.image.DataBuffer;

/**
 * This class represents a {@link DataBuffer} containing an image
 * obtained though v4l4j.
 * @author gilles
 *
 */
class V4L4JDataBuffer extends DataBuffer {
	private byte byteArray[];
	
	V4L4JDataBuffer(byte array[]) {
		super(TYPE_BYTE, array.length);
		byteArray = array;
	}
	
	public void setNewFrameSize(int s){
		size = s;
	}

	@Override
	public int getElem(int bank, int i) {
		if (bank != 0)
			throw new IndexOutOfBoundsException("Only one bank in this data buffer");
		
		return (int)(byteArray[i]) & 0xff;
	}

	@Override
	public void setElem(int bank, int i, int val) {
		// Do nothing
	}

}
