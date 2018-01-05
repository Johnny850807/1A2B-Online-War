package gamecore.entity;

import utils.ForServer;

/**
 * @author Waterball
 * This interface denotes that an implementation object can be challenged by the challenger
 * which is created for making sure all the implementation objects won't stay 
 * in the leisure state for a long time.
 */
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
