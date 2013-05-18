/**
 * <h1>Video4Linux4java Main Package</h1>
 * <h2>Introduction</h2>
 * The Video4Linux (V4L) API provides data structures & methods to access and control
 * video input & output hardware, as well as radio tuners. The API is divided into
 * several interfaces, including video capture, video overlay, video output & 
 * teletext interfaces. v4l4j provides access to the video capture 
 * interface of V4L only. v4l4j allows you to retrieve information about a video
 * device, access its video controls & tuners and grab images from it.<br> 
 * 
 * <h2>Examples</h2>
 * v4l4j comes with many simple, easy-to-follow example applications in the
 * au.edu.jcu.v4l4j.examples package. It is strongly suggested you take a look
 * at them as you read these JavaDoc pages as they help illustrate concepts 
 * crucial to a correct use of this API.
 * <h2>Usage</h2>
 * <h3>Video device object</h3>
 * To use v4l4j, the first step is to create a {@link au.edu.jcu.v4l4j.VideoDevice}
 * object, which represents a physical video input hardware device. This includes webcams 
 * and capture / tuner cards. The only pre-requisite is that your hardware must be 
 * supported and its driver loaded. Creating a {@link au.edu.jcu.v4l4j.VideoDevice}
 * is simple. You only need the path to a valid device file to create this object:<br>
 * <code>
 * VideoDevice vd = new VideoDevice("/dev/video0");
 * </code><br>
 * As it is the case most of the time when interacting with hardware, you must
 * release resources and data structures when finished. All v4l4j classes follow 
 * this rule, including {@link au.edu.jcu.v4l4j.VideoDevice}s. The rare exceptions
 * are clearly identified in their java documentation.<br>
 * <code>
 * vd.release();</code><br>
 * Once an object has been <code>release()</code>d, it must not be reused, and 
 * all references to it must be discarded, by explicitly setting them to 
 * <code>null</code> for example. Calling any <code>release()</code> method more 
 * than once will throw a
 *  {@link au.edu.jcu.v4l4j.exceptions.StateException StateException}.
 * <br>
 * {@link au.edu.jcu.v4l4j.VideoDevice} objects give you access to 4 broad 
 * categories:
 * <ul>
 * <li>information about the underlying hardware through 
 * {@link au.edu.jcu.v4l4j.DeviceInfo} objects,</li>
 * <li>a variety of controls through {@link au.edu.jcu.v4l4j.ControlList}
 * objects, and</li>
 * <li>a frame capture facility using {@link au.edu.jcu.v4l4j.FrameGrabber}
 * objects.</li>
 * <li>an interface to control any tuners, using {@link au.edu.jcu.v4l4j.TunerList}
 * objects.
 * </ul>
 * Each of these can be obtained independently from each other, by calling 
 * the appropriate method on a {@link au.edu.jcu.v4l4j.VideoDevice} instance.
 * This flexibility allows an application to create a GUI which provides access
 * to video controls, while another application is streaming video from the 
 * device. For this to work, the device driver must allows multiple applications
 * to open the device file simultaneously. As an example, the UVC driver works
 * this way.
 * <h3>Video hardware information</h3>
 * With a valid {@link au.edu.jcu.v4l4j.VideoDevice} object, you 
 * can retrieve information about 
 * the underlying hardware and find out, for example how many video inputs the 
 * device has, how many tuners are available, the set of supported video 
 * standards and capture resolutions. This kind of information is contained in a 
 * {@link au.edu.jcu.v4l4j.DeviceInfo} object. You can get a reference to a 
 * {@link au.edu.jcu.v4l4j.DeviceInfo} object for a given video device by calling
 * {@link au.edu.jcu.v4l4j.VideoDevice#getDeviceInfo()} on the 
 * {@link au.edu.jcu.v4l4j.VideoDevice} object. See the documentation of the
 * {@link au.edu.jcu.v4l4j.VideoDevice} and {@link au.edu.jcu.v4l4j.DeviceInfo}
 * classes for more information.
 * 
 * <h3>Video controls</h3>
 * Video capture devices usually have a set of controls which influence 
 * various parameters related to the video stream. Examples are brightness, 
 * contrast, gamma & white balance. A single control is represented by
 * {@link au.edu.jcu.v4l4j.Control} object. The set of supported controls for 
 * a given video device hardware-dependent and is embedded into a 
 * {@link au.edu.jcu.v4l4j.ControlList} object, which can be obtained by calling 
 * {@link au.edu.jcu.v4l4j.VideoDevice#getControlList()}. <strong>The 
 * {@link au.edu.jcu.v4l4j.ControlList} object must be released when controls 
 * are no longer needed.</strong> This is done by calling 
 * {@link au.edu.jcu.v4l4j.VideoDevice#releaseControlList()}. Again, once released,
 * neither the {@link au.edu.jcu.v4l4j.ControlList} nor individual 
 * {@link au.edu.jcu.v4l4j.Control}s must be used, and any reference to them 
 * must be discarded, by explicitly setting them to <code>null</code> for example.
 * For more information, check the documentation of the 
 * {@link au.edu.jcu.v4l4j.Control} and {@link au.edu.jcu.v4l4j.ControlList} classes.
 * 
 * <h3>Video capture</h3>
 * Capture in v4l4j is extremely simple. v4l4j hands out captured frames in a
 * {@link au.edu.jcu.v4l4j.VideoFrame} object, either in a native image format (
 * also referred to as raw format in v4l4j's documentation) or one of the 5 convenience
 * image formats (JPEG, RGB24, BGR24, YUV420 or YVU420). Each of these 6 image
 * formats is output by a dedicated frame grabber object, which will grab frames and 
 * convert them to that format if required. 
 *    
 * The set of native {@link au.edu.jcu.v4l4j.ImageFormat}s supported by a video 
 * device can be obtained using the {@link au.edu.jcu.v4l4j.DeviceInfo} object
 * (see paragraph "Video hardware information" above). 
 * When capturing in a native format, v4l4j simply hands out the 
 * captured frame without further processing. When capturing in one of the 
 * convenience formats, v4l4j will transparently convert the image.<br>
 * Frame grabbers operate in push mode: the frame grabber
 * delivers captured frames to a callback object you provide.  
 *  
 * Frame capture in v4l4j is done using objects implementing the
 * {@link au.edu.jcu.v4l4j.FrameGrabber} interface. Check the documentation 
 * of the {@link au.edu.jcu.v4l4j.FrameGrabber} class for more information.
 * 
 * <h3>Tuners</h3>
 * v4l4j provides access to tuners if present in the video device. A 
 * {@link au.edu.jcu.v4l4j.TunerList} object can be obtained by calling 
 * {@link au.edu.jcu.v4l4j.VideoDevice#getTunerList()}. Note that the tuner list
 * need not be released when finished.
 */
package au.edu.jcu.v4l4j;
