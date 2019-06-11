package com.laguille.belote.model;

import java.util.Arrays;
import java.util.Observable;

import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.cardset.CardDeck;
import com.laguille.belote.model.cardset.CardTable;
import com.laguille.belote.model.player.Player;
import com.laguille.belote.model.player.Team;

public class GameModel extends Observable
{
	final protected Team firstTeam, secondTeam;
	final protected CardDeck deck;
	final protected Player[] players;
	final protected CardTable table; // between 0...4 cards being played on the table
	
	protected Player currentPlayer;
	protected Player currentDistributer;
	protected CardColor trumpColor;
	protected Card bidCard;
	protected int drawPoints; // 'litige' points in case of a draw
	
	public GameModel()
	{
		firstTeam = new Team("Team 1");
		secondTeam = new Team("Team 2");
		drawPoints = 0;
		
		deck = CardDeck.getInstance();
		table = new CardTable();
		players = new Player[4];                                //    2
		players[0] = firstTeam.getFirstPlayer();				// 1     3
		players[1] = secondTeam.getFirstPlayer();				//    0 <-- human player
		players[2] = firstTeam.getSecondPlayer();
		players[3] = secondTeam.getSecondPlayer();
		currentPlayer = players[0];
		currentDistributer = players[3];
	}

	public Team getFirstTeam()
	{
		return firstTeam;
	}

	public Team getSecondTeam()
	{
		return secondTeam;
	}

	public CardDeck getDeck()
	{
		return deck;
	}

	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer)
	{
		this.currentPlayer = currentPlayer;
	}
	
	
	public Player getCurrentDistributer()
	{
		return currentDistributer;
	}

	public void setCurrentDistributer(Player currentDistributer)
	{
		this.currentDistributer = currentDistributer;
	}

	public Player[] getPlayers()
	{
		return players;
	}

	public Player getNextPlayer(Player player)
	{
		return players[(Arrays.asList(players).indexOf(player) + 1) % 4];
	}
	
	public void nextPlayer() // increment the current player
	{
		setCurrentPlayer(getNextPlayer(getCurrentPlayer()));
	}
	
	public void nextDistributer() // increment the current distributer
	{
		setCurrentDistributer(getNextPlayer(getCurrentDistributer()));
	}
	
	public CardColor getTrumpColor()
	{
		return trumpColor;
	}
	
	public void setTrumpColor(CardColor trumpColor)
	{
		this.trumpColor = trumpColor;
	}

	public Card getBidCard()
	{
		return bidCard;
	}

	public void setBidCard(Card bidCard)
	{
		this.bidCard = bidCard;
		setChanged();
		notifyObservers(bidCard);
	}
	
	public Player getTaker()
	{
		for (Player player : players)
		{
			if (player.isTaker())
				return player;
		}
		return null;
	}
	
	public Team getTeam(Player player)
	{
		Team team = null;
		if (firstTeam.getFirstPlayer().equals(player) || firstTeam.getSecondPlayer().equals(player))
		{
			team = firstTeam;
		}
		else if (secondTeam.getFirstPlayer().equals(player) || secondTeam.getSecondPlayer().equals(player))
		{
			team = secondTeam;
		}
		return team;
	}
	
	public Team getOppositeTeam(Team team)
	{
		Team oppositeTeam = null;
		if (team.equals(firstTeam))
		{
			oppositeTeam = secondTeam;
		}
		else if (team.equals(secondTeam))
		{
			oppositeTeam = firstTeam;
		}
		return oppositeTeam;
	}
	
	
	/**
	 * @return the cards played on the table
	 */
	public CardTable getTable()
	{
		return table;
	}
	
	public int getDrawPoints() {
		return drawPoints;
	}

	public void setDrawPoints(int drawPoints) {
		this.drawPoints = drawPoints;
	}

	public void prepareNextRound() // prepare the next round by resetting the taker, the trump color, and incrementing the distributer.
	{
		for (Player player : players)
		{
			player.setIsTaker(false);
		}
		
		setTrumpColor(null);
		setBidCard(null);
		nextDistributer();
		setCurrentPlayer(getNextPlayer(getCurrentDistributer()));
	}
}
