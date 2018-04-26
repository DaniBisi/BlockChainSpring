package magenta.blockChainSpring.application.model;

import java.util.LinkedList;

public class Message implements Items {
	LinkedList<String> messageTitle;
	LinkedList<String> messageVal;

	public Message() {
		messageTitle = new LinkedList<String>();
		messageVal = new LinkedList<String>();
	}
	
	public void addMessage(String title,String value) {
		messageTitle.add(title);
		messageVal.add(value);
	}
	@Override
	public LinkedList<String> getValList() {
		
		return messageVal;
	}

	@Override
	public LinkedList<String> getValName() {
		// TODO Auto-generated method stub
		return messageTitle;
	}

}
