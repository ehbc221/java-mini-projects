package com.laguille.belote.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.laguille.belote.model.GameModel;
import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.player.Player;
import com.laguille.belote.view.View;
import com.laguille.belote.view.console.ConsoleInterface;

public class ConsoleController implements InterfaceController
{

	protected GameModel model;
	private BufferedReader br;
	protected View view;
	private Player player;
	
	public ConsoleController(Player player, GameModel model)
	{
		this.model = model;
		this.view = new ConsoleInterface(model);
		setPlayer(player);
		br = new BufferedReader(new InputStreamReader(System.in));
	}

	@Override 
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	@Override
	public String getUserName()
	{
/*		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String userName = null;

		try
		{
			userName = br.readLine();
			while (userName == null || userName.isEmpty())
			{
				System.out.println("Please provide a username.\n");
				userName = br.readLine();
			}
		}
		catch (IOException ioe)
		{
			System.out.println("IO error trying to read your name!");
			System.exit(1);
		}
		*/
		view.askUserName();
		String userName = "Player South";
		return userName;
	}
	
	@Override
	public boolean getBidFirstRound() 
	{ 
		view.askBidFirstRound();
		String answer = null;

		try
		{
			answer = br.readLine();
			while (answer == null || !answer.matches("[YN]"))
			{
				view.askBidFirstRound();
				answer = br.readLine();
			}
		}
		catch (IOException ioe)
		{
			throw new RuntimeException("IO error trying to read your answer!");
		}
	
		return answer.equals("Y");
	}

	@Override
	public CardColor getBidSecondRound() 
	{ 
		CardColor trumpColor = null;
		
		view.askBidSecondRound();
		
		String answer = null;
		String possibleAnswers = null;
		// we have to remove the bid color of the first round from the possible answers
		CardColor bidColor = model.getBidCard().getColor();
		if (bidColor == CardColor.CLUB)
		{
			possibleAnswers = "DHSN";
		}
		else if (bidColor == CardColor.DIAMOND)
		{
			possibleAnswers = "HCSN";
		}
		else if (bidColor == CardColor.HEART)
		{
			possibleAnswers = "DCSN";
		}
		else if (bidColor == CardColor.SPADE)
		{
			possibleAnswers = "DHCN";
		}
		try
		{
			answer = br.readLine();
			while (answer == null || !answer.matches("[" + possibleAnswers + "]"))
			{
				view.askBidSecondRound();
				answer = br.readLine();
			}
		}
		catch (IOException ioe)
		{
			throw new RuntimeException("IO error trying to read your answer!");
		}
	
		if (answer.equals("D"))
		{
			trumpColor = CardColor.DIAMOND;
		}
		else if (answer.equals("H"))
		{
			trumpColor = CardColor.HEART;
		}
		else if (answer.equals("C"))
		{
			trumpColor = CardColor.CLUB;
		}
		else if (answer.equals("S"))
		{
			trumpColor = CardColor.SPADE;
		}
		return trumpColor;
	}

	@Override
	public Card getCardToPlay()
	{
		Card card = null;
		boolean ok = false;
		try
		{
			int index = 0;
			while (!ok)
			{
				view.askCardToPlay();
				String answer = br.readLine();
				try 
				{
					index = Integer.valueOf(answer);
				}
				catch(NumberFormatException e)
				{
					view.displayWarning("Wrong answer format. Try again.\n");
				}
				ok = (index > 0 && index <= player.getHand().getSize());
				if (!ok)
				{
					view.displayWarning("Wrong answer format. Try again.\n");
				}
					
			}
			card = player.getHand().getCard(index - 1);
		}
		catch (IOException ioe)
		{
			throw new RuntimeException("IO error trying to read your answer!");
		}
		return card;
	}
	
}
