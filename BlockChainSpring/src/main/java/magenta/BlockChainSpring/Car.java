package magenta.BlockChainSpring;


public class Car {
	private String color;
	private String make;
	private String model;
	private String owner;
	private String key;

	public Car(String key, String color, String make, String model, String owner) {
		super();
		this.key = key;
		this.color = color;
		this.make = make;
		this.model = model;
		this.owner = owner;
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
}
