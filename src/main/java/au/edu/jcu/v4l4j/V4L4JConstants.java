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

public class V4L4JConstants {

	/**
	 * Input from a tuner
	 */
	public static final short INPUT_TYPE_TUNER = 1;
	/**
	 * Camera-type input
	 */
	public static final short INPUT_TYPE_CAMERA = 2;
	/**
	 * Video standard value for webcams
	 */
	public static final int STANDARD_WEBCAM=0;
	/**
	 * Video standard value for PAL sources
	 */
	public static final int STANDARD_PAL=1;
	/**
	 * Video standard value for SECAM sources
	 */
	public static final int STANDARD_SECAM=2;
	/**
	 * Video standard value for NTSC sources
	 */
	public static final int STANDARD_NTSC=3;
	/**
	 * Setting the capture width to this value will set the actual width to the
	 * maximum width supported by the hardware  
	 */
	public static final int MAX_WIDTH = 0;
	/**
	 * Setting the capture height to this value will set the actual height to 
	 * the maximum height supported by the hardware  
	 */
	public static final int MAX_HEIGHT = 0;
	/**
	 * This value represents the maximum value of the JPEG quality setting
	 */
	public static final int MAX_JPEG_QUALITY = 100;
	/**
	 * This value represents the minimum value of the JPEG quality setting
	 */
	public static final int MIN_JPEG_QUALITY = 0;
	/**
	 * If a control has a type equal to CTRL_TYPE_BUTTON, its value is always 0,
	 * and pressing it is done by setting any value using 
	 * {@link Control#setValue(int)}.
	 */
	public final static int CTRL_TYPE_BUTTON=0;
	/**
	 * If a control has a type equal to CTRL_TYPE_SLIDER, it accepts a range of 
	 * values between a minimum (as returned by {@link Control#getMinValue()})
	 * and a maximum (as returned by {@link Control#getMaxValue()}) in 
	 * increments (as returned by {@link Control#getStepValue()})
	 */
	public final static int CTRL_TYPE_SLIDER=1;
	/**
	 * If a control has a type equal to CTRL_TYPE_SWITCH, it accepts two 
	 * different values: 
	 * 0 (as returned by {@link Control#getMinValue()}) and
	 * 1 (as returned by {@link Control#getMaxValue()}.
	 */
	public final static int CTRL_TYPE_SWITCH=2;	
	/**
	 * If a control has a type equal to CTRL_TYPE_DISCRETE, it only accepts 
	 * a set of values (discrete values). This set of acceptable values is  
	 * returned by {@link Control#getDiscreteValues()}.
	 * These discrete values may have string descriptions (returned by 
	 * {@link Control#getDiscreteValueNames()}) and are in the range 
	 * {@link Control#getMinValue()} and {@link Control#getMaxValue()}. The 
	 * step value as returned by {@link Control#getStepValue()} is not 
	 * applicable.
	 */
	public final static int CTRL_TYPE_DISCRETE=3;
	/**
	 * If a control has a type equal to CTRL_TYPE_STRING, it only accepts / 
	 * returns strings.
	 * {@link Control#getMinValue()} and {@link Control#getMaxValue()} return 
	 * the minimum and maximum string length. The actual value is set / retrieved
	 * with {@link Control#setStringValue(String)} and {@link Control#getStringValue()}.  
	 */
	public final static int CTRL_TYPE_STRING=4;
	/**
	 * If a control has a type equal to CTRL_TYPE_LONG, it only accepts long values,
	 * between {@link Long#MIN_VALUE} and {@link Long#MAX_VALUE} with a step value
	 * of 1. The actual value is set / retrieved
	 * with {@link Control#setLongValue(long)} and {@link Control#getLongValue()}.  
	 */
	public final static int CTRL_TYPE_LONG=5;
	/**
	 * If a control has a type equal to CTRL_TYPE_BITMASK, it only accepts integer values,
	 * between 0 and {@link Integer#MAX_VALUE} with a step value
	 * of 1. The actual value is set / retrieved
	 * with {@link Control#setValue(int)} and {@link Control#getValue()}.  
	 */
	public final static int CTRL_TYPE_BITMASK=6;
	
	
	/**
	 * A tuner with a type (as returned by {@link TunerInfo#getType()}) equal to
	 * TUNER_TYPE_RADIO is a radio tuner
	 */
	public static final int TUNER_TYPE_RADIO = 1;
	/**
	 * A tuner with a type (as returned by {@link TunerInfo#getType()}) equal to
	 * TUNER_TV_TYPE is a  TV tuner
	 */
	public static final int TUNER_TYPE_TV = 2;
	/**
	 * Frequencies of a tuner (as returned by 
	 * {@link TunerInfo#getUnit()}) are expressed in KHz
	 */
	public static final int FREQ_KHZ = 1;
	/**
	 * Frequencies of a tuner (as returned by 
	 * {@link TunerInfo#getUnit()}) are expressed in MHz
	 */
	public static final int FREQ_MHZ = 2;
	
	// RGB image formats
	/**
	 * RGB332 image format
	 */
	public static final int IMF_RGB332=0;
	/**
	 * RGB444 image format
	 */
	public static final int IMF_RGB444=1;
	/**
	 * RGB555 image format
	 */
	public static final int IMF_RGB555=2;
	/**
	 * RGB565 image format
	 */
	public static final int IMF_RGB565=3;
	/**
	 * RGB555X image format
	 */
	public static final int IMF_RGB555X=4;
	/**
	 * RGB565X image format
	 */
	public static final int IMF_RGB565X=5;
	/**
	 * BGR24 image format
	 */
	public static final int IMF_BGR24=6;
	/**
	 * RGB24 image format
	 */
	public static final int IMF_RGB24=7;
	/**
	 * BGR32 image format
	 */
	public static final int IMF_BGR32=8;
	/**
	 * RGB32 image format
	 */
	public static final int IMF_RGB32=9;
	
	// Grey image formats
	/**
	 * GREY image format
	 */
	public static final int IMF_GREY=10;
	/**
	 * Y4 image format
	 */
	public static final int IMF_Y4=11;	
	/**
	 * Y6 image format
	 */
	public static final int IMF_Y6=12;
	/**
	 * Y10 image format
	 */
	public static final int IMF_Y10=13;	
	/**
	 * Y16 image format
	 */
	public static final int IMF_Y16=14;
	
	// PAL 8 format
	/**
	 * PAL8 image format
	 */
	public static final int IMF_PAL8=15;
	
	// YUV formats
	/**
	 * YVU410 image format
	 */
	public static final int IMF_YVU410=16;
	/**
	 * YVU420 image format
	 */
	public static final int IMF_YVU420=17;	
	/**
	 * YUYV image format
	 */
	public static final int IMF_YUYV=18;
	/**
	 * YYUV image format
	 */
	public static final int IMF_YYUV=19;
	/**
	 * YVYU image format
	 */
	public static final int IMF_YVYU=20;
	/**
	 * UYVY image format
	 */
	public static final int IMF_UYVY=21;
	/**
	 * VYUY image format
	 */
	public static final int IMF_VYUY=22;
	/**
	 * YUV422 planar image format
	 */
	public static final int IMF_YUV422P=23;
	/**
	 * YUV411 planar image format
	 */
	public static final int IMF_YUV411P=24;
	/**
	 * Y41P image format
	 */
	public static final int IMF_Y41P=25;
	/**
	 * YUV444 image format
	 */
	public static final int IMF_YUV444=26;
	/**
	 * YUV555 image format
	 */
	public static final int IMF_YUV555=27;
	/**
	 * YUV565 image format
	 */
	public static final int IMF_YUV565=28;
	/**
	 * YUV32 image format
	 */
	public static final int IMF_YUV32=29;
	/**
	 * YUV410 planar image format
	 */
	public static final int IMF_YUV410=30;
	/**
	 * YUV420 planar image format index
	 */
	public static final int IMF_YUV420=31;
	/**
	 * HI240 image format
	 */
	public static final int IMF_HI240=32;
	/**
	 * HM12 image format
	 */
	public static final int IMF_HM12=33;
	
	// two planes - Y and Cb/Cr interleaved
	/**
	 * NV12 image format
	 */
	public static final int IMF_NV12=34;
	/**
	 * NV21 image format
	 */
	public static final int IMF_NV21=35;
	/**
	 * NV16 image format
	 */
	public static final int IMF_NV16=36;
	/**
	 * NV61 image format
	 */
	public static final int IMF_NV61=37;
	
	// Bayer formats
	/**
	 * SBGGR8 bayer image format
	 */
	public static final int IMF_SBGGR8=38;
	/**
	 * SGBRG8 image format
	 */
	public static final int IMF_SGBRG8=39;
	/**
	 * SGRBG8 bayer image format
	 */
	public static final int IMF_SGRBG8=40;
	/**
	 * SRGGB8 bayer image format
	 */
	public static final int IMF_SRGGB8=41;
	/**
	 * SBGGR10 bayer image format
	 */
	public static final int IMF_SBGGR10=42;
	/**
	 * SGBRG10 bayer image format
	 */
	public static final int IMF_SGBRG10=43;
	/**
	 * SGRBG10 bayer image format
	 */
	public static final int IMF_SGRBG10=44;
	/**
	 * SRGGB10 bayer image format
	 */
	public static final int IMF_SRGGB10=45;
	/**
	 * SGRBG10_DPCM8 bayer image format
	 */
	public static final int IMF_SGRBG10DPCM8=46;
	/**
	 * SBGGR16 bayer image format
	 */
	public static final int IMF_SBGGR16=47;

	// compressed formats
	/**
	 * MJPEG image format
	 */
	public static final int IMF_MJPEG=48;
	/**
	 * JPEG image format
	 */
	public static final int IMF_JPEG=49;
	/**
	 * DV image format
	 */
	public static final int IMF_DV=50;
	/**
	 * MPEG image format
	 */
	public static final int IMF_MPEG=51;
	
	// vendor specific
	/**
	 * CPIA1 image format
	 */
	public static final int IMF_CPIA1=52;
	/**
	 * WNVA image format
	 */
	public static final int IMF_WNVA=53;	
	/**
	 * SN9C10X image format
	 */
	public static final int IMF_SN9C10X=54;
	/**
	 * SN9C20X_I420 image format
	 */
	public static final int IMF_SN9C20X_I420=55;
	/**
	 * PWC1 image format
	 */
	public static final int IMF_PWC1=56;
	/**
	 * PWC2 image format
	 */
	public static final int IMF_PWC2=57;
	/**
	 * ET61X251 image format
	 */
	public static final int IMF_ET61X251=58;
	/**
	 * SPCA501 image format
	 */
	public static final int IMF_SPCA501=59;
	/**
	 * SPCA505 image format
	 */
	public static final int IMF_SPCA505=60;
	/**
	 * SPCA508 image format
	 */
	public static final int IMF_SPCA508=61;
	/**
	 * SPCA561 image format
	 */
	public static final int IMF_SPCA561=62;
	/**
	 * PAC207 image format
	 */
	public static final int IMF_PAC207=63;
	/**
	 * MR97310A image format
	 */
	public static final int IMF_MR97310A=64;
	/**
	 * SN9C2028 image format
	 */
	public static final int IMF_SN9C2028=65;
	/**
	 * SQ905C image format
	 */
	public static final int IMF_SQ905C=66;
	/**
	 * PJPG image format
	 */
	public static final int IMF_PJPG=67;
	/**
	 * OV511 image format
	 */
	public static final int IMF_OV511=68;
	/**
	 * OV518 image format
	 */
	public static final int IMF_OV518=69;
	/**
	 * STV0680 image format
	 */
	public static final int IMF_STV0680=70;
	/**
	 * TM6000 image format
	 */
	public static final int IMF_TM6000=71;
	/**
	 * CIT_YYVYUY image format
	 */
	public static final int IMF_CIT_YYVYUY=72;
	/**
	 * KONICA420 image format
	 */
	public static final int IMF_KONICA420=73;
}
