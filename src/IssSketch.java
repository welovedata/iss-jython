import processing.core.*;
import processing.serial.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.python.core.PyObject;

import com.kenai.jffi.Array;

public class IssSketch extends PApplet {
	private static final long serialVersionUID = 1L;

	static PyObject method;
	
	public static void start(PyObject object) {
		method = object;
	    PApplet.main(new String[] { "--present", IssSketch.class.getName() });
	  }
	
	
Serial port;

public void setup() 
{
  size(200, 200);
  String portName = Serial.list()[0];
//  println( Serial.list());
  port = new Serial(this, portName, 9600);

  background(0);
  frameRate(1);
}

public void draw() {
  PyObject draw = method.__call__();
  int value = Integer.parseInt(draw.toString());
  println(value);
  port.write(value);
}


}
