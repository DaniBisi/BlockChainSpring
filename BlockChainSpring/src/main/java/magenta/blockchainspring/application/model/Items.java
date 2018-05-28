package magenta.blockchainspring.application.model;

import java.util.List;

public interface Items {
	public List<String> getValList();

	public List<String> getValName();

	public default boolean isEqualsToItem(Items i1) {
		return getValList().equals(i1.getValList()) && getValName().equals(i1.getValName());
	}
}
