package com.laguille.belote.model.player;

import java.util.Observable;

import com.laguille.belote.model.cardset.CardStack;


public class Team extends Observable
{
	protected final Player firstPlayer, secondPlayer;
	protected Integer score;
	protected final CardStack cardStack;
	protected String name;
	
	public Team()
	{
		firstPlayer = new Player();
		secondPlayer = new Player();
		cardStack = new CardStack();
		score = 0;
	}
	
	public Team(String name)
	{
		this();
		setName(name);
	}
	
	public Player getFirstPlayer()
	{
		return firstPlayer;
	}

	public Player getSecondPlayer()
	{
		return secondPlayer;
	}

	public Integer getScore()
	{
		return score;
	}

	public void addScore(Integer score)
	{
		this.score += score;
		setChanged();
		notifyObservers();
	}

	public void resetScore()
	{
		this.score = 0;
	}

	public CardStack getCardStack()
	{
		return cardStack;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isTaker()
	{
		return (firstPlayer.isTaker() || secondPlayer.isTaker());
	}
	
	@Override
	public boolean equals(Object obj) {
		Team team = (Team)obj;
		return team == this; // we can not duplicate a team
	}
	
	@Override
	public String toString() {
		return name;
	}
}
