package com.laguille.belote.model.cardset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.player.Player;


/*
 * This will contain the cards played by the player for a single trick
 * The cards are always added 1 by 1, size varies between 0...4
 */
public class CardTable extends Observable
{
	protected Map<Player, Card> table; // map the card played by a player
	private CardColor askedColor;
	
	public CardTable()
	{
		table = new HashMap<Player, Card>(4);
	}
	
	public List<Card> removeAll()
	{
		List<Card> cards = new ArrayList<Card>(table.values());
		table.clear();
		setChanged();
		notifyObservers();
		return cards;
	}
	
	public void add(Player player, Card card)
	{
		if (table.isEmpty())
		{
			askedColor = card.getColor();
		}
		table.put(player, card);
		setChanged();
		notifyObservers();
	}

	public Card getCard(Player player)
	{
		return table.get(player);
	}
	
	/**
	 * Compare the input card parameter with the value of the cards on the table of the same color
	 * @param card the card to compare
	 * @param trumpColor the trump color
	 * @return 1 if the card the highest card value on the table is higher than the card
	 * 			-1 otherwise
	 */
	public int compareTo(Card card, CardColor trumpColor)
	{
		for (Card c : table.values())
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
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Player player : table.keySet())
		{
			sb.append(player.getName()).append(":");
			sb.append(table.get(player)).append("\n");
		}
		return sb.toString();
	}

	public boolean isEmpty()
	{
		return table.isEmpty();
	}
	
	public Player getWinner(CardColor trumpColor)
	{
		Player winner = table.keySet().toArray(new Player[0])[0];
		for (Player player : table.keySet())
		{
			CardComparator comparator = CardComparator.instance(trumpColor, askedColor);
			if (comparator.compare(table.get(player), table.get(winner)) > 0) 
			{
				winner = player;
			}
		}
		return winner;
	}
}