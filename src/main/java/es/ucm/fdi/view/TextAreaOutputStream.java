package es.ucm.fdi.view;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {

	private JTextArea textArea;
	private boolean start = true;
	
	public TextAreaOutputStream(JTextArea txt) {
		textArea = txt;
	}
	
	public void write(byte []b) throws IOException {
		if(start) {
			textArea.setText("");
			start = false;
		}
		String str = new String(b);
		textArea.append(str);
	}
	
	public void setStart(boolean option) {
		start = option;
	}

	@Override
	public void write(int b) throws IOException {}

}
