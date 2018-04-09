package magenta.BlockChainSpring;

import com.google.common.collect.ObjectArrays;

public class commandFactory {

	private String query;
	private String args;

	public commandFactory(String query, String args) {
		this.query = query;
		this.args = args;

	}

	public String[] getFormattedQuery() {

		String[] formattedQ = { query };
		if (!args.isEmpty()) {
			String[] arguments = args.split(" ");
			formattedQ = ObjectArrays.concat(formattedQ, arguments, String.class);
		}
		return formattedQ;
	}

	public command getCommand() {
		
		switch (query) {
		case "queryAllCars":
			return new parserAll();
		case "queryCar":
			return new parserCar();
		default:
			return null;
		}
	}

}
