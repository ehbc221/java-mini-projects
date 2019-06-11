package com.laguille.belote.controller;

import com.laguille.belote.model.GameModel;
import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.player.Player;

public class AIController implements InterfaceController
{

	protected GameModel model;
	private Player player;
	
	public AIController(Player player, GameModel model)
	{
		this.model = model;
		setPlayer(player);
	}

	@Override 
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	@Override
	public String getUserName()
	{
		if (model.getFirstTeam().getFirstPlayer().equals(player))
		{
			return "Player South";
		}
		else if (model.getSecondTeam().getFirstPlayer().equals(player))
		{
			return "Player West";
		}
		else if (model.getFirstTeam().getSecondPlayer().equals(player))
		{
			return "Player North";
		}
		else
		{
			return "Player East";
		}
	}
	
	@Override
	public boolean getBidFirstRound() 
	{ 
		return false;
	}

	@Override
	public CardColor getBidSecondRound() 
	{
		return null;
	}

	@Override
	public Card getCardToPlay()
	{
		return player.getHand().getCard(0);
	}
}