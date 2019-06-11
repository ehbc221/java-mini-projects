package com.laguille.belote.controller;

import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.player.Player;

public interface InterfaceController
{
	public void setPlayer(Player player); // attach a player to this controller
	
	public String getUserName();
	
	public boolean getBidFirstRound();
	
	// returns the color of the bid if the player took
	// returns false otherwise
	public CardColor getBidSecondRound();
	
	public Card getCardToPlay();
	
}
