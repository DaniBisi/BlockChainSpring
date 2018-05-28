package magenta.blockchainspring.application.model;

import java.util.LinkedList;
import java.util.List;

public class Message implements Items {
	LinkedList<String> messageTitle;
	LinkedList<String> messageVal;

	public Message() {
		messageTitle = new LinkedList<>();
		messageVal = new LinkedList<>();
	}
	
	public void addMessage(String title,String value) {
		messageTitle.add(title);
		messageVal.add(value);
	}
	@Override
	public List<String> getValList() {
		
		return messageVal;
	}

	@Override
	public List<String> getValName() {
		return messageTitle;
	}

}
