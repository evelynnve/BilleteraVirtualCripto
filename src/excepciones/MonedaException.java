package excepciones;
import java.util.*;
import javax.swing.*;

public class MonedaException extends Exception{	
	
	public MonedaException (String message, Throwable cause) {
		 super(message, cause);
	}
	
	public MonedaException () {
		 super();
	}

	public MonedaException(String string) {
		super(string);
	}
}
