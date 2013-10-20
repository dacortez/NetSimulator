package dacortez.netSimulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class Parser {
	private File file;
	
	public Parser(File file) {
		this.file = file;
	}
	
	public void parse() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
