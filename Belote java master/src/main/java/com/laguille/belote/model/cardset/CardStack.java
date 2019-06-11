package com.laguille.belote.model.cardset;

import java.util.List;

import com.laguille.belote.model.card.Card;

/*
 * This will contain the cards "won" by a team
 * The cards are always added 4 by 4
 */
public class CardStack extends CardSet
{
	public void addAll(List<Card> cards)
	{
		this.cards.addAll(cards);
	}
	
}
