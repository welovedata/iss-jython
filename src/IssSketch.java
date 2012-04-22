import org.python.core.PyObject;

import processing.core.PApplet;
import cc.arduino.*;

public class IssSketch extends PApplet {
	private static final long serialVersionUID = 1L;

	static PyObject callback;

	/**
	 * Main entry point that stores reference to the Python callback and then
	 * hands control to the Processing libraries.
	 */
	public static void start(PyObject method) {
		callback = method;
		PApplet.main(new String[] { "--present", IssSketch.class.getName() });
	}

	Arduino arduino;
	int servoPin1 = 10; 

	/**
	 * Standard Processing setup method, setting up the Arduino port
	 */
	public void setup() {
		size(200, 200);

		arduino = new Arduino(this, Arduino.list()[1]);
		arduino.pinMode(servoPin1, Arduino.OUTPUT);

		background(0);
		frameRate(1);
	}

	/**
	 * Standard Processing draw method, called at frameRate (once per sec) Calls
	 * python callback to get number between 0 and 255 and then decides whether
	 * to make waves.
	 */
	public void draw() {
		PyObject draw = callback.__call__();
		int value = Integer.parseInt(draw.toString());
		println(value);
		if (value > 127) {
			makeWave();
		}
	}

	void makeWave() {
		arduino.analogWrite(servoPin1, 90);
		delay(200);

		arduino.analogWrite(servoPin1, 180);
		delay(200);
	}
}
