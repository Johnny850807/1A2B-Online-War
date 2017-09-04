package gamecore.entity;

public interface Entity extends Comparable<BaseEntity>{

	int hashCode();

	boolean equals(Object obj);

	void setId(String id);

	String getId();

}