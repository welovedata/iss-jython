import org.python.core.PyObject;

import processing.core.PApplet;
import processing.serial.Serial;

public class IssSketch extends PApplet {
	private static final long serialVersionUID = 1L;

	static PyObject callback;

	/**
	 * Main entry point that stores reference to the Python callback
	 * and then hands control to the Processing libraries.
	 */
	public static void start(PyObject method) {
		callback = method;
		PApplet.main(new String[] { "--present", IssSketch.class.getName() });
	}

	Serial port;

	/**
	 * Standard Processing setup method, setting up the Serial port
	 */
	public void setup() {
		size(200, 200);
		String portName = Serial.list()[0];
		// println( Serial.list());
		port = new Serial(this, portName, 9600);

		background(0);
		frameRate(1);
	}

	/**
	 * Standard Processing draw method, called at frameRate (once per sec)
	 * Calls python callback to get number between 0 and 255
	 */
	public void draw() {
		PyObject draw = callback.__call__();
		int value = Integer.parseInt(draw.toString());
		println(value);
		port.write(value);
	}

}
