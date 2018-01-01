package gamecore.entity;

import utils.ForServer;

@ForServer
public interface LeisureTimeChallengeable {
	/**
	 * @return if the time is expired
	 */
	@ForServer
	public boolean isLeisureTimeExpired();
	
	/**
	 * update the latest leisure time
	 */
	@ForServer
	public void pushLeisureTime();
}
