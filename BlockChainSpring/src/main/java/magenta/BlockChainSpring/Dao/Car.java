package magenta.BlockChainSpring.Dao;

import java.util.LinkedList;

public class Car implements Items{
	private String color;
	private String make;
	private String model;
	private String owner;
	private String key;
	private LinkedList<String> valList;
	private LinkedList<String> ValName;

	public Car(String key, String color, String make, String model, String owner) {
		super();
		this.key = key;
		this.color = color;
		this.make = make;
		this.model = model;
		this.owner = owner;
		this.ValName = new LinkedList<String>();
		this.valList = new LinkedList<String>();
	}

	public String getKey() {
		return key;
	}

	public String getColor() {
		return color;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public String getOwner() {
		return owner;
	}

	@Override
	public LinkedList<String> getValList() {
		valList.add(this.key);
		valList.add(this.color);
		valList.add(this.make);
		valList.add(this.model);
		valList.add(this.owner);
		return valList;
	}

	@Override
	public LinkedList<String> getValName() {
		ValName.add("key");
		ValName.add("color");
		ValName.add("make");
		ValName.add("model");
		ValName.add("owner");
		return ValName;
	}
}
