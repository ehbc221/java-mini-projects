/**
 * 
 */
package com.laguille.belote.model.card;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.laguille.belote.model.cardset.CardComparator;
import com.laguille.belote.model.cardset.CardDeck;
import com.laguille.belote.model.cardset.CardHand;

/**
 * @author guillaume
 * 
 */
public class CardComparatorTest
{

	static CardDeck deck;
	static CardColor trumpColor;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		deck = CardDeck.getInstance();
		trumpColor = CardColor.HEART;
	}

	@Test
	public void sortCardsNoTrump()
	{
		deck.sort(CardComparator.instance());
		
		CardHand expected = new CardHand();
		for (CardColor color : CardColor.values())
		{
			buildCardSetNoTrumpColor(expected, color);
		}
		Assert.assertEquals(expected, deck);
	}

	@Test
	public void sortCardsWithTrump()
	{
		deck.sort(CardComparator.instance(trumpColor, trumpColor));

		CardHand expected = new CardHand();
		for (CardColor color : CardColor.values())
		{
			if (color != trumpColor)
				buildCardSetNoTrumpColor(expected, color);
		}
		buildCardSetTrumpColor(expected, trumpColor);
		Assert.assertEquals(expected, deck);
	}
	
	@Test
	public void compareCardsInHandNoTrumpColor()
	{
		Card c1 = new Card(CardColor.HEART, CardValue.NINE);
		Card c2 = new Card(CardColor.HEART, CardValue.QUEEN);
		
		CardComparator comparator = CardComparator.instance();
		Assert.assertEquals(-1, comparator.compare(c1, c2));
	}

	@Test
	public void compareCardsWithTrumpColor() 
	{
		Card c1 = new Card(CardColor.HEART, CardValue.NINE);
		Card c2 = new Card(CardColor.HEART, CardValue.QUEEN);
		Card c3 = new Card(CardColor.DIAMOND, CardValue.ACE);
		CardColor trumpColor = CardColor.HEART;
		
		CardComparator comparator = CardComparator.instance(trumpColor);
		Assert.assertEquals(1, comparator.compare(c1, c2));
		Assert.assertEquals(1, comparator.compare(c2, c3));
		Assert.assertEquals(-1, comparator.compare(c2, c1));
		Assert.assertEquals(-1, comparator.compare(c3, c2));
	}
	
	@Test
	public void compareCardsWithPlayerColor() 
	{
		Card c1 = new Card(CardColor.HEART, CardValue.SEVEN);
		Card c2 = new Card(CardColor.DIAMOND, CardValue.ACE);
		Card c3 = new Card(CardColor.SPADE, CardValue.TEN);
		CardColor trumpColor = CardColor.HEART;
		CardColor colorPlayed = CardColor.SPADE;
		
		CardComparator comparator = CardComparator.instance(trumpColor, colorPlayed);
		Assert.assertEquals(-1, comparator.compare(c2, c3));
		Assert.assertEquals(1, comparator.compare(c1, c3));
		Assert.assertEquals(1, comparator.compare(c3, c2));
		Assert.assertEquals(-1, comparator.compare(c3, c1));
	}
	
	private List<Card> buildCardSetNoTrumpColor(CardHand hand, CardColor color)
	{
		List<Card> cards = new ArrayList<Card>();
		for (CardValue value : CardValue.values())
		{
			hand.addCard(new Card(color, value));
		}
		return cards;
	}

	private List<Card> buildCardSetTrumpColor(CardHand hand, CardColor color)
	{
		List<Card> cards = new ArrayList<Card>();
		hand.addCard(new Card(color, CardValue.SEVEN));
		hand.addCard(new Card(color, CardValue.EIGHT));
		hand.addCard(new Card(color, CardValue.QUEEN));
		hand.addCard(new Card(color, CardValue.KING));
		hand.addCard(new Card(color, CardValue.TEN));
		hand.addCard(new Card(color, CardValue.ACE));
		hand.addCard(new Card(color, CardValue.NINE));
		hand.addCard(new Card(color, CardValue.JACK));
		return cards;
	}

}