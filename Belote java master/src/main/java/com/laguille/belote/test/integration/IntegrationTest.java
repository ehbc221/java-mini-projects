package com.laguille.belote.test.integration;

import java.util.List;

import com.laguille.belote.controller.GameController;
import com.laguille.belote.model.GameModel;
import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.cardset.CardTable;
import com.laguille.belote.model.player.Player;

import fit.ColumnFixture;

public class IntegrationTest extends ColumnFixture {

	public Card cardSouth;
	public Card cardWest;
	public Card cardNorth;
	public Card cardEast;
	public CardColor trumpColor;
	
	public Player winner()
	{
		table.add(model.getFirstTeam().getFirstPlayer(), cardSouth);
		table.add(model.getSecondTeam().getFirstPlayer(), cardWest);
		table.add(model.getFirstTeam().getSecondPlayer(), cardNorth);
		table.add(model.getSecondTeam().getSecondPlayer(), cardEast);
		return table.getWinner(trumpColor);
	}
	
	public int score()
	{
		List<Card> cards = table.removeAll();
		int score = 0;
		for (Card c : cards)
		{
			score += c.getPoints(trumpColor);
		}
		return score;
	}
	
	public Object parse(String s, Class type) throws Exception {
	    if (type.equals(Card.class))
	    {
	    	return ParserUtil.parseCard(s);
    	}
	    if (type.equals(CardColor.class))
	    {
	    	return CardColor.valueOf(s);
	    }
	    if (type.equals(Player.class))
	    {
	    	return ParserUtil.parsePlayer(s);
	    }
	    return super.parse(s, type);
	  }
	
	protected static final CardTable table;
	protected static final GameModel model;
	protected static final GameController controller;
	
	static
	{
		model = new GameModel();
		controller = new GameController(model);
		controller.initGame();
		table = model.getTable();
	}
	
}
