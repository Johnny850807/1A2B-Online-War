package communication;

import gamecore.entity.Entity;

public interface RequestParser {
	<T extends Entity> Message<T> parseRequest(String json);
}
