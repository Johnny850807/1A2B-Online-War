package gamecore.entity;


import java.util.Date;

/**
 * @author Waterball
 * ChatMessages in the chat.
 */
public class ChatMessage extends Entity {
	private String gameRoomId;
    private Player poster;
    private String content;
    private Date postDate = new Date();
    
    public ChatMessage(GameRoom gameRoom, Player poster, String content) {
    	this(gameRoom.getId(), poster, content);
    }
    
    public ChatMessage(String gameRoomId, Player poster, String content) {
    	this.gameRoomId = gameRoomId;
        this.poster = poster;
        this.content = content;
    }
    
    public String getGameRoomId(){
    	return gameRoomId;
    }
 
    public Player getPoster() {
        return poster;
    }

    public void setPoster(Player poster) {
        this.poster = poster;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    @Override
    public String toString() {
        return poster.getName() + ": " + content + "   " + postDate.toString();
    }
}
