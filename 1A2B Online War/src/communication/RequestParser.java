package communication;

import gamecore.entity.Entity;

public interface RequestParser {
	Message<? extends Entity> parseRequest(String json);
}
