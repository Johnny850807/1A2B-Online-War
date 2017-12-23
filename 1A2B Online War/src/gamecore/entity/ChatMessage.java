package gamecore.entity;


import java.util.Date;

/**
 * @author AndroidWork
 * ChatMessages in the chat.
 */
public class ChatMessage extends Entity {
	private GameRoom gameRoom;
    private Player poster;
    private String content;
    private Date postDate = new Date();

    public ChatMessage(GameRoom gameRoom, Player poster, String content) {
    	this.gameRoom = gameRoom;
        this.poster = poster;
        this.content = content;
    }
    
    public void setGameRoom(GameRoom gameRoom) {
		this.gameRoom = gameRoom;
	}
    
    public String getGameRoomId(){
    	return gameRoom.getId();
    }
    
    public GameRoom getGameRoom() {
		return gameRoom;
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
