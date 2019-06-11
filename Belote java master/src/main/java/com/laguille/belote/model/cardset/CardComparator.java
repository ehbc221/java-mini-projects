package com.laguille.belote.model.cardset;

import java.util.Comparator;

import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;

public class CardComparator implements Comparator<Card>
{

	protected static CardColor trumpColor;
	// the played color is used when comparing different card values during a
	// play
	// this attribute IS NOT used when sorting out a player hand
	protected static CardColor playedColor;
	protected static CardComparator instance = new CardComparator();

	private CardComparator()
	{
	}

	public static CardComparator instance()
	{
		CardComparator.trumpColor = null;
		CardComparator.playedColor = null;
		return instance;
	}

	public static CardComparator instance(CardColor trumpColor)
	{
		CardComparator.trumpColor = trumpColor;
		CardComparator.playedColor = null;
		return instance;
	}

	public static CardComparator instance(CardColor trumpColor,
			CardColor playedColor)
	{
		CardComparator.trumpColor = trumpColor;
		CardComparator.playedColor = playedColor;
		return instance;
	}

	@Override
	public int compare(Card c1, Card c2)
	{
		if (CardComparator.trumpColor == null)
		{
			// no card has been yet played and no trump color
			// this happens when sorting out a player hand after the first distribution round
			// the sorting will depend on the ordinal value of the card color
			// and the card value if the color are similar
			if (c1.getColor() == c2.getColor())
			{
				return new Integer(c1.getValue().ordinal()).compareTo(new Integer(
						c2.getValue().ordinal()));
			}
			else 
			{
				return new Integer(c1.getColor().ordinal()).compareTo(new Integer(
					c2.getColor().ordinal()));
			}
		}
		else
		{
			// comparison between two cards after the trump color is set
			// this could happen when sorting out a player hand after the second distribution round
			// or when comparing the value of two cards
			if (c1.getColor() == c2.getColor())
			{
				return c1.compareTo(c2, trumpColor);
			}
			else if (c1.getColor() == trumpColor)
			{
				return 1;
			}
			else if (c2.getColor() == trumpColor)
			{
				return -1;
			}
			else if (CardComparator.playedColor != null
					&& c1.getColor() == CardComparator.playedColor)
			{ // c2 had to discard
				return 1;
			}
			else if (CardComparator.playedColor != null
				&& c2.getColor() == CardComparator.playedColor)
			{ // c1 had to discard
				return -1;
			}
			else 
			{
				// both c1 and c2 had to discard
				return new Integer(c1.getColor().ordinal()).compareTo(new Integer(
						c2.getColor().ordinal()));
			}
		}
	}

}
