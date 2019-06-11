package com.laguille.belote.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.laguille.belote.model.GameModel;
import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.cardset.CardComparator;
import com.laguille.belote.model.cardset.CardDeck;
import com.laguille.belote.model.player.Player;

public class GameControllerTest
{

	static GameController controller;
	static List<Card> initialDeck;

	@BeforeClass
	public static void setUp() throws Exception
	{
		GameModel model = new GameModel();
		controller = new GameController(model);
		controller.initGame();

		CardDeck deck = controller.model.getDeck();
		initialDeck = new ArrayList<Card>();
		for (Card card : deck.getCards())
		{
			initialDeck.add(new Card(card.getColor(), card.getValue()));
			System.out.println(card);
		}
	}

	@Test
	public void testDistributeFirstRound()
	{
		controller.distributeCardsFirstRound();

		int[] player1CardIdx = new int[] {0, 1, 2, 12, 13};
		int[] player2CardIdx = new int[] {3, 4, 5, 14, 15};
		int[] player3CardIdx = new int[] {6, 7, 8, 16, 17};
		int[] player4CardIdx = new int[] {9, 10, 11, 18, 19};
		
		// check that what remains in the deck is correct
		CardDeck shuffledDeck = controller.model.getDeck();
		Assert.assertEquals(initialDeck.subList(20, 32), shuffledDeck.getCards());

		CardComparator comparator = CardComparator.instance();
		// check the cards for the first receiver (player next to the
		// distributer)
		Player player = controller.model.getNextPlayer(controller.model.getCurrentDistributer());
		List<Card> cardsExpected = new ArrayList<Card>();
		for (int index : player1CardIdx)
		{
			cardsExpected.add(initialDeck.get(index));
		}
		Collections.sort(cardsExpected, comparator);
		List<Card> cardsHeld = player.getHand().getCards();
		Assert.assertEquals(cardsExpected, cardsHeld);

		// check the cards for the second receiver
		player = controller.model.getNextPlayer(player);
		cardsExpected = new ArrayList<Card>();
		for (int index : player2CardIdx)
		{
			cardsExpected.add(initialDeck.get(index));
		}		
		Collections.sort(cardsExpected, comparator);
		cardsHeld = player.getHand().getCards();
		Assert.assertEquals(cardsExpected, cardsHeld);

		// check the cards for the third receiver
		player = controller.model.getNextPlayer(player);
		cardsExpected = new ArrayList<Card>();
		for (int index : player3CardIdx)
		{
			cardsExpected.add(initialDeck.get(index));
		}		
		Collections.sort(cardsExpected, comparator);
		cardsHeld = player.getHand().getCards();
		Assert.assertEquals(cardsExpected, cardsHeld);

		// check the cards for the last receiver
		player = controller.model.getNextPlayer(player);
		cardsExpected = new ArrayList<Card>();
		for (int index : player4CardIdx)
		{
			cardsExpected.add(initialDeck.get(index));
		}		
		Collections.sort(cardsExpected, comparator);
		cardsHeld = player.getHand().getCards();
		Assert.assertEquals(cardsExpected, cardsHeld);
	}

	@Test
	public void testBidCard()
	{
		controller.setBidCard();
		Assert.assertEquals(initialDeck.get(20), controller.model.getBidCard());
	}

	@Test
	public void testDistributeSecondRound()
	{
	
		int[] player1CardIdx = new int[] {0, 1, 2, 12, 13, 21, 22, 23};
		int[] player2CardIdx = new int[] {3, 4, 5, 14, 15, 24, 25, 26};
		int[] player3CardIdx = new int[] {6, 7, 8, 16, 17, 27, 28, 29};
		int[] player4CardIdx = new int[] {9, 10, 11, 18, 19, 20, 30, 31}; //taker
		
		controller.model.getCurrentDistributer().setIsTaker(true);
		controller.model.setTrumpColor(CardColor.HEART);
		controller.distributeCardsSecondRound();

		CardComparator comparator = CardComparator.instance(controller.model.getTrumpColor());
		
		// check that what remains in the deck is correct
		CardDeck shuffledDeck = controller.model.getDeck();
		Assert.assertTrue(shuffledDeck.getCards().isEmpty());

		// check the cards for the first receiver (player next to the
		// distributer)
		Player player = controller.model.getNextPlayer(controller.model.getCurrentDistributer());
		List<Card> cardsExpected = new ArrayList<Card>();
		for (int index : player1CardIdx)
		{
			cardsExpected.add(initialDeck.get(index));
		}
		Collections.sort(cardsExpected, comparator);
		List<Card> cardsHeld = player.getHand().getCards();
		Assert.assertEquals(cardsExpected, cardsHeld);

		// check the cards for the second receiver
		player = controller.model.getNextPlayer(player);
		cardsExpected = new ArrayList<Card>();
		for (int index : player2CardIdx)
		{
			cardsExpected.add(initialDeck.get(index));
		}
		Collections.sort(cardsExpected, comparator);
		cardsHeld = player.getHand().getCards();
		Assert.assertEquals(cardsExpected, cardsHeld);

		// check the cards for the third receiver
		player = controller.model.getNextPlayer(player);
		cardsExpected = new ArrayList<Card>();
		for (int index : player3CardIdx)
		{
			cardsExpected.add(initialDeck.get(index));
		}
		Collections.sort(cardsExpected, comparator);
		cardsHeld = player.getHand().getCards();
		Assert.assertEquals(cardsExpected, cardsHeld);

		// check the cards for the last receiver who is also the taker
		player = controller.model.getNextPlayer(player);
		cardsExpected = new ArrayList<Card>();
		for (int index : player4CardIdx)
		{
			cardsExpected.add(initialDeck.get(index));
		}
		Collections.sort(cardsExpected, comparator);
		cardsHeld = player.getHand().getCards();
		Assert.assertEquals(cardsExpected, cardsHeld);
	}

	@Test
	public void testPutCardsBackIntoDeck()
	{
		controller.putCardsBackIntoDeck(controller.model.getDeck(), controller.model.getPlayers(), controller.model.getBidCard());
		Assert.assertEquals(32, controller.model.getDeck().getSize());
		Assert.assertEquals(0, controller.model.getPlayers()[0].getHand().getCards().size());
		Assert.assertEquals(0, controller.model.getPlayers()[1].getHand().getCards().size());
		Assert.assertEquals(0, controller.model.getPlayers()[2].getHand().getCards().size());
		Assert.assertEquals(0, controller.model.getPlayers()[3].getHand().getCards().size());
	}
}
