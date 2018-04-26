package magenta.blockChainSpring.application.model;

import java.util.LinkedList;

public interface Items {
	public LinkedList<String> getValList();

	public LinkedList<String> getValName();

	public default boolean equals(Items i1) {
		return getValList().equals(i1.getValList()) && getValName().equals(i1.getValName());
	}
}
