package com.laguille.belote.model.cardset;

import java.util.Collections;

import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.card.CardValue;

public class CardDeck extends CardSet
{
	protected static final CardDeck cardDeck = new CardDeck();
	
	private CardDeck()
	{
		super();
		init();
	}
	
	public static CardDeck getInstance() 
	{
		return cardDeck;
	}
	
	protected void init()
	{
		for (CardColor color : CardColor.values())
		{
			for (CardValue value : CardValue.values())
			{
				cards.add(new Card(color, value));
			}
		}
	}
	
	public Card removeCardOnTop()
	{
		if (!cards.isEmpty())
			return cards.remove(0);
		return null;
	}
	
	public void shuffle() 
	{
		Collections.shuffle(cards);
	}
	
	public void addCards(CardSet cardSet)
	{
		for (Card card : cardSet.cards)
		{
			addCard(card);
		}
	}

	@Override
	public int compareTo(Card card, CardColor trumpColor)
	{
		throw new UnsupportedOperationException();
	}
}
