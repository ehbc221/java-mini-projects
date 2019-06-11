package com.laguille.belote.model.player;

import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.cardset.CardHand;

public class Player
{
	protected String name;
	protected final CardHand hand;
	protected boolean isTaker;
	protected Card cardPlayed; 
	// we will use the cardPlayed instead of creating a intermediary class which would gather the cards played by all the players for this round
	// the view will be refreshed after someone plays, there will be one observer per instance of player
	
	public Player()
	{
		name = "";
		hand = new CardHand();
		cardPlayed = null;
		isTaker = false;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public CardHand getHand()
	{
		return hand;
	}
	/*
	public void setHand(CardHand hand)
	{
		this.hand = hand;
	}
	*/
	public boolean isTaker()
	{
		return isTaker;
	}

	public void setIsTaker(boolean isTaker)
	{
		this.isTaker = isTaker;
	}

	@Override
	public boolean equals(Object obj) {
		Player player = (Player)obj;
		return player == this; // we can not duplicate a player
	}

	@Override
	public String toString() {
		return getName();
	}
	
}
