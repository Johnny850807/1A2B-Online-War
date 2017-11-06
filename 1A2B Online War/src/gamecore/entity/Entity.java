package gamecore.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.naming.directory.InitialDirContext;

/**
 * @author AndroidWork
 * A Serializable Entity must have a 'id' property. You should invoke 'initId()' to make sure that 
 * a new UUID assigned to the entity.
 */
public class Entity implements Serializable{
	protected String id;
	
	public Entity initId(){
		this.id = UUID.randomUUID().toString();
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public int compareTo(Entity o) {
		return id.compareTo(o.getId());
	}
	
	
}

