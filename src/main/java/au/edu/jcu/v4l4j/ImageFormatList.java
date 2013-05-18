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

/**
 * ImageFormatList objects group together a list of native {@link ImageFormat}s
 * capture resolutions and frame intervals supported by a {@link VideoDevice}. 
 * A native (or raw)
 * format is a format supported and used by the driver. This native 
 * format list is obtained by calling {@link #getNativeFormats()}. 
 * Additionally, v4l4j can convert some native formats to 
 * RGB24, BGR24, JPEG, YUV420 and YVU420. The 
 * {@link #getRGBEncodableFormats()}, {@link #getBGREncodableFormats()}, 
 * {@link #getJPEGEncodableFormats()}, {@link #getYUVEncodableFormats()} 
 * & {@link #getYVUEncodableFormats()} methods returns the subset of native 
 * formats that can be converted to these formats. 
 * <code>ImageFormatList</code>s are not directly instantiated. Instead, you 
 * can get the list of {@link ImageFormat}s supported by a {@link VideoDevice} 
 * by calling {@link DeviceInfo#getFormatList()} on its associated 
 * {@link DeviceInfo}.
 * @author gilles
 *
 */	
public class ImageFormatList {
	static {
		try {
			System.loadLibrary("v4l4j");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Cant load v4l4j JNI library");
			throw e;
		}
	}

	/**
	 * Native method used to populate the {@link #formats}, {@link #JPEGformats}
	 * {@link #RGBformats}, {@link #BGRformats}, {@link #YUV420formats} &
	 * {@link #YVU420formats} members, given a v4l4j_device. This method must
	 * be called while the device info interface of libvideo is checked out.
	 * @param object a JNI pointer to a struct v4l4j_device with 
	 * the device info interface of libvideo checked out
	 */
	private native void listFormats(long object);

	
	/**
	 * The list of native image formats produced by a video device.
	 */
	private List<ImageFormat> formats;
	
	/**
	 * The list of native image formats that can be JPEG encoded
	 */
	private List<ImageFormat> JPEGformats;
	
	/**
	 * The list of native image formats that can be converted to RGB24
	 */
	private List<ImageFormat> RGBformats;
	
	/**
	 * The list of native image formats that can be converted to BGR24
	 */
	private List<ImageFormat> BGRformats;
	
	/**
	 * The list of native image formats that can be converted to YUV420
	 */
	private List<ImageFormat> YUV420formats;
	
	/**
	 * The list of native image formats that can be converted to YVU420
	 */
	private List<ImageFormat> YVU420formats;
	
	/**
	 * This method builds a list of {@link ImageFormat}s. This method must
	 * be called while the device info interface of libvideo is checked out.
	 * @param o the JNI C pointer to struct v4l4j_device
	 */
	ImageFormatList(long o){
		formats = new Vector<ImageFormat>();
		JPEGformats = new Vector<ImageFormat>();
		RGBformats = new Vector<ImageFormat>();
		BGRformats = new Vector<ImageFormat>();
		YUV420formats = new Vector<ImageFormat>();
		YVU420formats = new Vector<ImageFormat>();
		listFormats(o);
		sortLists();
	}
	
	/**
	 * This method sorts the {@link #JPEGformats}, {@link #RGBformats},
	 * {@link #BGRformats}, {@link #YUV420formats} & {@link #YVU420formats} 
	 * lists, so that formats more suited to conversion are first.
	 */
	private void sortLists(){
		//sort RGBformats
		//if native RGB24 is supported, put it first
		moveToFirstIfNative(RGBformats, V4L4JConstants.IMF_RGB24);
		
		//sort BGRformats
		//if native BGR24 is supported, put it first
		moveToFirstIfNative(BGRformats, V4L4JConstants.IMF_BGR24);
		
		//sort YUV420formats
		//if native YUV420 is supported, put it first
		moveToFirstIfNative(YUV420formats, V4L4JConstants.IMF_YUV420);
		
		//sort YVU420formats
		//if native YVU420 is supported, put it first
		moveToFirstIfNative(YVU420formats, V4L4JConstants.IMF_YVU420);
		
		//sort JPEGformats
		//put native formats first and libvideo converted ones next
		moveNativeFirst(JPEGformats);
		//if native RGB32 is supported, put it first
		moveToFirstIfNative(JPEGformats, V4L4JConstants.IMF_RGB32);
		//if native RGB24 is supported, put it first
		moveToFirstIfNative(JPEGformats, V4L4JConstants.IMF_RGB24);
		//if native UYVY is supported, put it first
		moveToFirstIfNative(JPEGformats, V4L4JConstants.IMF_UYVY);
		//if native YUYV is supported, put it first
		moveToFirstIfNative(JPEGformats, V4L4JConstants.IMF_YUYV);
		//if native YUV420P is supported, put it first
		moveToFirstIfNative(JPEGformats, V4L4JConstants.IMF_YUV420);
		//if native MJPEG is supported, put it first
		moveToFirstIfNative(JPEGformats, V4L4JConstants.IMF_MJPEG);
		//if native JPEG is supported, put it first
		moveToFirstIfNative(JPEGformats, V4L4JConstants.IMF_JPEG);
	}
	
	/**
	 * This method moves the given image format <code>format</code>
	 * in the first position of the vector.
	 * @param v the vector if image format
	 * @param format the index of the format to be moved in first position
	 */
	private void moveToFirstIfNative(List<ImageFormat> v, int format){
		for(ImageFormat i : v)
			if((i.getIndex()==format) && (formats.contains(i))){
				v.remove(i);
				v.add(0, i);
				break;
			}
	}
	
	/**
	 * This method moves the given image format <code>format</code>
	 * in the first position of the vector.
	 * @param v the vector if image format
	 * @param format the index of the format to be moved in first position
	 */
	private void moveToFirst(List<ImageFormat> v, int format){
		for(ImageFormat i : v)
			if(i.getIndex()==format){
				v.remove(i);
				v.add(0, i);
				break;
			}
	}
	
	/**
	 * This method moves the native formats in the given vector to the beginning
	 * of the vector.
	 * @param v the vector to be sorted
	 */
	private void moveNativeFirst(List<ImageFormat> v){
		int index=0;
		for(int i=0; i<v.size();i++)
			//if not native format
			if(!formats.contains(v.get(index))){
				//put it at the end
				v.add(v.remove(index));
			} else
				index++;
	}
	
	/**
	 * this method returns a format in a list given its name
	 * @param l the image format list
	 * @param n the name of the format
	 * @return the image format with the given name, or null
	 */
	private ImageFormat getFormat(List<ImageFormat> l, String n){
		for(ImageFormat f:l)
			if(f.getName().equals(n))
				return f;
		return null;
	} 
	
	/**
	 * this method returns a format in a list given its index
	 * @param l the image format list
	 * @param i the index of the format
	 * @return the image format with the given index, or null
	 */
	private ImageFormat getFormat(List<ImageFormat> l, int i){
		for(ImageFormat f:l)
			if(f.getIndex()==i)
				return f;
		return null;
	}
	
	/**
	 * This method returns the native {@link ImageFormat}s contained in this 
	 * list.
	 * @return the {@link ImageFormat}s contained in this list
	 */
	public List<ImageFormat>getNativeFormats(){
		return new Vector<ImageFormat>(formats);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat} in this list given 
	 * its name, and returns it, or <code>null</code> if not found.
	 * @param n the name of the <code>ImageFormat</code>.
	 * @return the <code>ImageFormat</code>, or <code>null</code> 
	 * if not found in the list.
	 */
	public ImageFormat getNativeFormat(String n){
		return getFormat(formats, n);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat} given its index,
	 * and returns it, or <code>null</code> if not found. A list of all known 
	 * format indexes can be found in {@link V4L4JConstants}.IMF_*. 
	 * @param i the index of the <code>ImageFormat</code> to look up
	 * @return the <code>ImageFormat</code> or <code>null</code> if not found.
	 */
	public ImageFormat getNativeFormat(int i){
		return getFormat(formats, i);
	}
	
	/**
	 * This method returns a list of native {@link ImageFormat}s contained
	 * in this object, that can be converted to JPEG by v4l4j. The list is 
	 * sorted: image formats better suited for the conversion are listed first.
	 * @return a list of {@link ImageFormat}s contained
	 * in this object, that can be converted to JPEG by v4l4j.
	 */
	public List<ImageFormat> getJPEGEncodableFormats(){
		return new Vector<ImageFormat>(JPEGformats);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat} that can be converted 
	 * to JPEG by v4l4j in this list given its name, and returns it, or 
	 * <code>null</code> if not found.
	 * @param n the name of the <code>ImageFormat</code>.
	 * @return the <code>ImageFormat</code>, or <code>null</code> 
	 * if not found in the list.
	 */
	public ImageFormat getJPEGEncodableFormat(String n){
		return getFormat(JPEGformats, n);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat}  that can be converted 
	 * to JPEG by v4l4j given its 
	 * index, and returns it, or <code>null</code> if not found. A list of all 
	 * known format indexes can be found in {@link V4L4JConstants}.IMF_* . 
	 * @param i the index of the <code>ImageFormat</code> to look up
	 * @return the <code>ImageFormat</code> or <code>null</code> if not found.
	 */
	public ImageFormat getJPEGEncodableFormat(int i){
		return getFormat(JPEGformats, i);
	}
	
	/**
	 * This method returns a list of native {@link ImageFormat}s contained
	 * in this object, that can be converted to RGB24 by v4l4j. The list is 
	 * sorted: image formats better suited for the conversion are listed first.
	 * @return a list of {@link ImageFormat}s contained
	 * in this object, that can be converted to RGB24 by v4l4j.
	 */
	public List<ImageFormat> getRGBEncodableFormats(){
		return new Vector<ImageFormat>(RGBformats);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat} that can be converted 
	 * to RGB24 by v4l4j in this list given its name, and returns it, or 
	 * <code>null</code> if not found.
	 * @param n the name of the <code>ImageFormat</code>.
	 * @return the <code>ImageFormat</code>, or <code>null</code> 
	 * if not found in the list.
	 */
	public ImageFormat getRGBEncodableFormat(String n){
		return getFormat(RGBformats, n);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat} that can be converted 
	 * to RGB24 by v4l4j given its index, and returns it, or <code>null</code> 
	 * if not found. A list of all known format indexes can be found in 
	 * {@link V4L4JConstants}.IMF_* . 
	 * @param i the index of the <code>ImageFormat</code> to look up
	 * @return the <code>ImageFormat</code> or <code>null</code> if not found.
	 */
	public ImageFormat getRGBEncodableFormat(int i){
		return getFormat(RGBformats, i);
	}
	
	/**
	 * This method returns a list of native {@link ImageFormat}s contained
	 * in this object, that can be converted to BGR24 by v4l4j. The list is 
	 * sorted: image formats better suited for the conversion are listed first.
	 * @return a list of {@link ImageFormat}s contained
	 * in this object, that can be converted to BGR24 by v4l4j.
	 */
	public List<ImageFormat> getBGREncodableFormats(){
		return new Vector<ImageFormat>(BGRformats);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat} that can be converted 
	 * to BGR24 by v4l4j in this list given its name, and returns it, or 
	 * <code>null</code> if not found.
	 * @param n the name of the <code>ImageFormat</code>.
	 * @return the <code>ImageFormat</code>, or <code>null</code> 
	 * if not found in the list.
	 */
	public ImageFormat getBGREncodableFormat(String n){
		return getFormat(BGRformats, n);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat}  that can be converted 
	 * to BGR24 by v4l4j given its index,
	 * and returns it, or <code>null</code> if not found. A list of all known 
	 * format indexes can be found in {@link V4L4JConstants}.IMF_* . 
	 * @param i the index of the <code>ImageFormat</code> to look up
	 * @return the <code>ImageFormat</code> or <code>null</code> if not found.
	 */
	public ImageFormat getBGREncodableFormat(int i){
		return getFormat(BGRformats, i);
	}
	
	/**
	 * This method returns a list of native {@link ImageFormat}s contained
	 * in this object, that can be converted to YUV420 by v4l4j. The list is 
	 * sorted: image formats better suited for the conversion are listed first.
	 * @return a list of {@link ImageFormat}s contained
	 * in this object, that can be converted to YUV420 by v4l4j.
	 */
	public List<ImageFormat> getYUVEncodableFormats(){
		return new Vector<ImageFormat>(YUV420formats);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat} that can be converted 
	 * to YUV420 by v4l4j in this list given 
	 * its name, and returns it, or <code>null</code> if not found.
	 * @param n the name of the <code>ImageFormat</code>.
	 * @return the <code>ImageFormat</code>, or <code>null</code> 
	 * if not found in the list.
	 */
	public ImageFormat getYUVEncodableFormat(String n){
		return getFormat(YUV420formats, n);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat}  that can be converted 
	 * to YUV420 by v4l4j given its index,
	 * and returns it, or <code>null</code> if not found. A list of all known 
	 * format indexes can be found in {@link V4L4JConstants}.IMF_* . 
	 * @param i the index of the <code>ImageFormat</code> to look up
	 * @return the <code>ImageFormat</code> or <code>null</code> if not found.
	 */
	public ImageFormat getYUVEncodableFormat(int i){
		return getFormat(YUV420formats, i);
	}
	
	/**
	 * This method returns a list of native {@link ImageFormat}s contained
	 * in this object, that can be converted to YVU420 by v4l4j. The list is 
	 * sorted: image formats better suited for the conversion are listed first.
	 * @return a list of {@link ImageFormat}s contained
	 * in this object, that can be converted to YVU420 by v4l4j.
	 */
	public List<ImageFormat> getYVUEncodableFormats(){
		return new Vector<ImageFormat>(YVU420formats);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat} that can be converted 
	 * to YVU420 by v4l4j in this list given 
	 * its name, and returns it, or <code>null</code> if not found.
	 * @param n the name of the <code>ImageFormat</code>.
	 * @return the <code>ImageFormat</code>, or <code>null</code> 
	 * if not found in the list.
	 */
	public ImageFormat getYVUEncodableFormat(String n){
		return getFormat(YVU420formats, n);
	}
	
	/**
	 * This method looks for a native {@link ImageFormat}  that can be converted 
	 * to YVU420 by v4l4j given its index,
	 * and returns it, or <code>null</code> if not found. A list of all known 
	 * format indexes can be found in {@link V4L4JConstants}V4L4JConstants.IMF_* 
	 * @param i the index of the <code>ImageFormat</code> to look up
	 * @return the <code>ImageFormat</code> or <code>null</code> if not found.
	 */
	public ImageFormat getYVUEncodableFormat(int i){
		return getFormat(YVU420formats, i);
	}
}

