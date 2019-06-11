package com.laguille.belote.view.console;

import com.laguille.belote.model.GameModel;
import com.laguille.belote.model.card.Card;
import com.laguille.belote.view.View;

public class ConsoleInterface extends View
{
	public ConsoleInterface(GameModel model)
	{
		super(model);
	}

	@Override
	public void askUserName()
	{
		System.out.println("Enter your name: ");
	}
	
	@Override
	public void askBidFirstRound() 
	{ 
		System.out.println(model.getCurrentDistributer().getName() + " distributes.\n");
		System.out.println(model.getCurrentPlayer().getName() + ": The bid card is: " + model.getBidCard() + "\n");
		System.out.println("Do you want to take? (Y/N)\n");
	}

	@Override
	public void askBidSecondRound() 
	{ 
		System.out.println(model.getCurrentPlayer().getName() + ": Second Round: Indicate the color you want to take (D/H/C/S or N): \n");
	}
	
	@Override
	public void askCardToPlay()
	{
		System.out.println(model.getCurrentPlayer().getName() + ": Which card do you want to play?\n");
		for (int ind = 0 ; ind < model.getCurrentPlayer().getHand().getCards().size() ; ind++)
		{
			Card card = model.getCurrentPlayer().getHand().getCard(ind);
			System.out.println(String.valueOf(ind + 1) + ":" + card);
		}
	}
	
	@Override
	public void displayWarning(String message)
	{
		System.out.println("Warning: " + message);
	}
}