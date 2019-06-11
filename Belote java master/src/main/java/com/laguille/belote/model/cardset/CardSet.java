package com.laguille.belote.model.cardset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.card.CardValue;

public abstract class CardSet extends Observable
{
	protected final List<Card> cards;

	public CardSet()
	{
		this.cards = new ArrayList<Card>();
	}
	
	public Card getCard(int index)
	{
		return cards.get(index);
	}
	
	public Card getCard(CardValue cardValue, CardColor cardColor)
	{
		for (Card card : cards)
		{
			if (card.getValue().equals(cardValue) && card.getColor().equals(cardColor))
			{
				return card;
			}
		}
		return null;
	}
	
	public List<Card> getCards()
	{
		return cards;
	}
	
	public void sort(Comparator<Card> comparator)
	{
		Collections.sort(cards, comparator);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Card card : cards)
		{
			sb.append(card);
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof CardSet))
			return false;
		CardSet cardSet = (CardSet) obj;
		int index = 0;
		while (index < this.cards.size())
		{
			if (!this.cards.get(index).equals(cardSet.cards.get(index)))
					return false;
			index++;
		}
		return true;
	}
	
	public boolean addCard(Card card)
	{
		return contains(card) ? false : cards.add(card);
	}
	
	public boolean removeCard(Card card)
	{
		return cards.remove(card);
	}
	
	public void reset()
	{
		cards.clear();
	}
	
	public boolean contains(Card card)
	{
		return cards.contains(card);
	}
	
	public boolean contains(CardColor color)
	{
		for(Card card : cards)
		{
			if (card.getColor() == color)
			{
				return true;
			}
		}
		return false;
	}

	public int getSize() 
	{
		return cards.size();
	}
	
	/**
	 * Compare the input card parameter with the value of the cards in the set of the same color
	 * @param card the card to compare
	 * @param trumpColor the trump color
	 * @return 1 if the card the highest card value in the set is higher than the card
	 * 			-1 otherwise
	 */
	public int compareTo(Card card, CardColor trumpColor)
	{
		for (Card c : cards)
		{
			if (c.getColor() == card.getColor())
			{
				if (c.compareTo(card, trumpColor) > 0)
				{
					return 1;
				}
			}
		}
		return -1;
	}
	
}
