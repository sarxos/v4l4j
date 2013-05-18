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

import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

/**
 * We need our own implementation of a WritableRaster because we cannot create one
 * with {@link Raster#createInterleavedRaster(DataBuffer, int, int, int, int, int[], Point)} as
 * we cannot pass a {@link V4L4JDataBuffer} to it, it will throw an exception because
 * it expects a {@link DataBufferByte} object.
 * @author gilles
 *
 */
public class V4L4JRaster extends WritableRaster {
	
	public V4L4JRaster(SampleModel sampleModel, DataBuffer dataBuffer,
			Point origin) {
		super(sampleModel, dataBuffer, origin);
		// TODO Auto-generated constructor stub
	}
}
