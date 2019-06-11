package com.laguille.belote.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.laguille.belote.model.GameModel;
import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.cardset.CardDeck;
import com.laguille.belote.model.player.Player;
import com.laguille.belote.model.player.Team;
import com.laguille.belote.referee.RefereeEngine;
import com.laguille.belote.referee.RoundResult;

public class GameController
{

	protected GameModel model;
	protected Map<Player, InterfaceController> pcMap; // map a player to a controller 
	protected RefereeEngine referee;
	
	public GameController(GameModel model)
	{
		this.model = model;
		this.referee = new RefereeEngine(model);
		
		pcMap = new HashMap<Player, InterfaceController>();

		// creating the user controller and adding it to the map
		Player user = model.getFirstTeam().getFirstPlayer();
		pcMap.put(user, new ConsoleController(user, model));

		// creating the 3 ai players and adding them to the map
		Player ai = model.getNextPlayer(user);
		pcMap.put(ai, new AIController(ai, model));

		ai = model.getNextPlayer(ai);
		pcMap.put(ai, new AIController(ai, model));

		ai = model.getNextPlayer(ai);
		pcMap.put(ai, new AIController(ai, model));
	}

	public void initGame()
	{
		// init names
		for(Player player : pcMap.keySet())
		{
			String playerName = pcMap.get(player).getUserName();
			player.setName(playerName);
		}
		
		// shuffle the deck
		model.getDeck().shuffle();
	}

	public void startGame()
	{

		while (true)
		{
			// distribute the cards
			distributeCardsFirstRound();
			
			setBidCard();

			int i = 0;
			boolean bidEnd = false;
			while ( i < model.getPlayers().length && !bidEnd)
			{
				bidEnd = pcMap.get(model.getCurrentPlayer()).getBidFirstRound();
				if (bidEnd)
				{
					model.getCurrentPlayer().setIsTaker(true);
					model.setTrumpColor(model.getBidCard().getColor());
				}
				else
				{
					model.nextPlayer();
				}
				i++;
			}
			
			if(!bidEnd) // if no one took, then it's second bid round
			{
				i = 0;
				while ( i < model.getPlayers().length && !bidEnd)
				{
					CardColor trumpColor = pcMap.get(model.getCurrentPlayer()).getBidSecondRound();
					if (trumpColor != null)
					{
						model.getCurrentPlayer().setIsTaker(true);
						model.setTrumpColor(trumpColor);
						bidEnd = true;
					}
					else
					{
						model.nextPlayer();
					}
					i++;
				}
			}
				
			if (!bidEnd) // end of the round
			{
				putCardsBackIntoDeck(model.getDeck(), model.getPlayers(), model.getBidCard());
				model.prepareNextRound();
				continue;
			}
			
			// if bid, distribute
			distributeCardsSecondRound();
			
			Team lastRoundWinner = null;
			int nbTricks = model.getCurrentPlayer().getHand().getSize();
			for (int n = 0 ; n < nbTricks ; n++)
			{
				for (int m = 0 ; m < model.getPlayers().length ; m++)
				{
					Player player = model.getCurrentPlayer();
					boolean valid = false;
					Card cardPlayed = null;
					while (!valid)
					{
						// ask for choice
						cardPlayed = pcMap.get(player).getCardToPlay();
						// ref engine checks choice is valid
						// TODO: add ref engine check
						valid = true;
					}
					// - remove card from his hand (view should refresh view observer)
					player.getHand().removeCard(cardPlayed);
					// - set it as the card played (view refreshed via observer)
					model.getTable().add(player, cardPlayed);
					// set next player
					model.nextPlayer();
				}
		
				// ref engine decides which player wins the round
				lastRoundWinner = referee.getRoundWinner();
				// cards are transferred to the team card stack
				List<Card> cardTable = model.getTable().removeAll();
				lastRoundWinner.getCardStack().addAll(cardTable);
				// and so on for 8 rounds
			}		
			// ref engine decides the score (capot, dedans, ...)
			RoundResult result = referee.getResultAndUpdateScore(lastRoundWinner);

			// 	cards are moved back into the deck:	putCardBackIntoDeck(model.getDeck(), model.getPlayers());
			putCardsBackIntoDeck(model.getDeck(), new Team[]{model.getFirstTeam(), model.getSecondTeam()});
			
			model.prepareNextRound();
		}

	}

	/**
	 * This method is used to put the players cards back into the deck
	 * This happens when no player took this round
	 * @param deck
	 * @param players
	 * @param bidCard
	 */
	protected void putCardsBackIntoDeck(CardDeck deck, Player[] players, Card bidCard) {
		// no one took this round so take the cards from the players hand
		for (Player player : players)
		{
			deck.addCards(player.getHand());
			player.getHand().reset();
		}	
		deck.addCard(bidCard);
	}
	
	/**
	 * This method is used when a round is finished to put the players cards back into the deck
	 * @param deck
	 * @param teams
	 */
	protected void putCardsBackIntoDeck(CardDeck deck, Team[] teams) {
		// the round is over so take the cards from the team stack
		for (Team team : teams)
		{
			deck.addCards(team.getCardStack());
			team.getCardStack().reset();
		}
	}

	protected void setBidCard()
	{
		// reveal the card on the top of the deck
		Card bidCard = model.getDeck().removeCardOnTop();
		model.setBidCard(bidCard);
	}
	
	protected void distributeCardsFirstRound()
	{
		Card card = null;
		Player player = model.getCurrentDistributer();
		for (int i = 0 ; i < model.getPlayers().length ; i++)
		{
			player = model.getNextPlayer(player);
			for (int cardNum = 0; cardNum < 3; cardNum++)
			{
				card = model.getDeck().removeCardOnTop();
				player.getHand().addCard(card);
			}
		}
		for (int i = 0 ; i < model.getPlayers().length ; i++)
		{
			player = model.getNextPlayer(player);
			for (int cardNum = 0; cardNum < 2; cardNum++)
			{
				card = model.getDeck().removeCardOnTop();
				player.getHand().addCard(card);
			}
			player.getHand().sort();
		}
	}

	protected void distributeCardsSecondRound()
	{
		Card card = null;
		Player player = model.getCurrentDistributer();
		for (int i = 0 ; i < model.getPlayers().length ; i++)
		{
			player = model.getNextPlayer(player);
			if (player.equals(model.getTaker()))
			{
				player.getHand().addCard(model.getBidCard());
				model.setBidCard(null);
				for (int cardNum = 0; cardNum < 2; cardNum++)
				{
					card = model.getDeck().removeCardOnTop();
					player.getHand().addCard(card);
				}
			}
			else
			{
				for (int cardNum = 0; cardNum < 3; cardNum++)
				{
					card = model.getDeck().removeCardOnTop();
					player.getHand().addCard(card);
				}
			}
			player.getHand().sort(model.getTrumpColor());
		}
	}

	// this method is not relevant at the moment
//	private void invokeInterfaceControllerMethods(String methodName, Object...parameters)
//	{
//		Class<?>[] parameterTypes = new Class<?>[parameters.length];
//		for (int i = 0 ; i < parameters.length ; i++)
//		{
//			parameterTypes[i] = parameters[i].getClass();
//		}
//		for (InterfaceController controller : controllers)
//		{
//			Method method = null;
//			try
//			{
//				method = controller.getClass().getMethod(methodName, parameterTypes);
//			}
//			catch (SecurityException e)
//			{
//				throw new RuntimeException(e);
//			}
//			catch (NoSuchMethodException e)
//			{
//				throw new RuntimeException(e);
//			}
//			
//			try
//			{
//				method.invoke(controller, parameters);
//			}
//			catch (IllegalArgumentException e)
//			{
//				throw new RuntimeException(e);			
//			}
//			catch (IllegalAccessException e)
//			{
//				throw new RuntimeException(e);			
//			}
//			catch (InvocationTargetException e)
//			{
//				throw new RuntimeException(e);			
//			}
//		}
//	}
}
