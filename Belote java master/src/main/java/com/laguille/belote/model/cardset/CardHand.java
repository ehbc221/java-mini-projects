package com.laguille.belote.model.cardset;

import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;

public class CardHand extends CardSet
{
	CardComparator comparator;
	
	@Override
	public boolean removeCard(Card card)
	{
		boolean remove = super.removeCard(card);
		if (remove)
		{
			setChanged();
			notifyObservers(card);
		}
		return remove;
	}

	@Override
	public boolean addCard(Card card)
	{
		boolean add = super.addCard(card);
//		if (add)
//		{
//			setChanged();
//			notifyObservers(card);
//		}
		// why is the code above commented out? The only time when a card is added to a hand is during the 1st and 2nd round
		// of distribution. We only notify the views of the change once all the card are distributed.
		// Hence the notifyObservers is located in the CardHand sort method.
		return add;
	}

	@Override
	public void reset()
	{
		super.reset();
		setChanged();
		notifyObservers();
	}

	public void sort()
	{
		comparator = CardComparator.instance();
		sort(comparator);
		setChanged();
		notifyObservers(this);
	}
	
	public void sort(CardColor trumpColor)
	{
		comparator = CardComparator.instance(trumpColor);
		sort(comparator);
		setChanged();
		notifyObservers(this);
	}

}
