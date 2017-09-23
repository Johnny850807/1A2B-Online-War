package gamecore.entity;

public interface Entity extends Comparable<Entity>{

	int hashCode();

	boolean equals(Object obj);

	void setId(String id);

	String getId();

}