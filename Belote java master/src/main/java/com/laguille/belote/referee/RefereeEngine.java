package com.laguille.belote.referee;

import com.laguille.belote.model.GameModel;
import com.laguille.belote.model.card.Card;
import com.laguille.belote.model.card.CardColor;
import com.laguille.belote.model.player.Player;
import com.laguille.belote.model.player.Team;

public class RefereeEngine {

	private GameModel model;
	private int drawScore;
	private final int FINAL_SCORE = 1000;
	
	public RefereeEngine(GameModel model)
	{
		this.model = model;
	}
	
	public Team getRoundWinner()
	{
		Player winner = model.getTable().getWinner(model.getTrumpColor());
		return model.getTeam(winner);
	}
	
	public RoundResult getResultAndUpdateScore(Team lastRoundWinner)
	{
		Team taker = model.getTeam(model.getTaker());
		int takerScore = calculateRoundPoints(taker, model.getTrumpColor(), taker.equals(lastRoundWinner));

		Team opponent = null;
		if (taker.equals(model.getFirstTeam()))
		{
			opponent = model.getSecondTeam();
		}
		else
		{
			opponent = model.getFirstTeam(); 
		}
		int opponentScore = calculateRoundPoints(opponent, model.getTrumpColor(), opponent.equals(lastRoundWinner));
		
		RoundResult result = null;
		if (opponentScore == 0)
		{
			result = RoundResult.OPPONENT_NULL;
			taker.addScore(takerScore + 90); // +100 for capot - 10 de der
		}
		else if (takerScore == 0)
		{
			result = RoundResult.TAKER_NULL;
			opponent.addScore(opponentScore + 90); // +100 for capot - 10 de der
		}
		else if (takerScore < opponentScore)
		{
			result = RoundResult.FAILED_CONTRACT;
			opponent.addScore(takerScore + opponentScore);
		}
		else if (takerScore == opponentScore)
		{
			result = RoundResult.DRAWN;
		}
		else
		{
			result = RoundResult.WON_CONTRACT;
			taker.addScore(takerScore);
			opponent.addScore(opponentScore);
		}
		
		if (drawScore > 0) // if previous round was a draw
		{
			if (result == RoundResult.DRAWN) // if it's a draw again
			{
				drawScore += takerScore;
			}
			else
			{
				if (result == RoundResult.WON_CONTRACT || result == RoundResult.OPPONENT_NULL)
				{
					taker.addScore(drawScore);
				}
				else
				{
					opponent.addScore(drawScore);
				}
				drawScore = 0;
			}
		}
		return result;
	}
	
	private int calculateRoundPoints(Team team, CardColor trumpColor, boolean isLastRoundWinner)
	{
		int score = 0;
		for (Card card : team.getCardStack().getCards())
		{
			score += card.getPoints(trumpColor);
		}
		// TODO: annonces
		if (isLastRoundWinner)
		{
			score += 10;
		}
		return score;
	}
	
	public boolean isGameOver()
	{
		return (model.getFirstTeam().getScore() >= 1000 
				|| model.getSecondTeam().getScore() >= 1000)
				&& model.getFirstTeam().getScore() != model.getSecondTeam().getScore();
	}
	
	public Team getGameWinner()
	{
		if (!isGameOver())
		{
			return null;
		}
		else if (model.getFirstTeam().getScore() > model.getSecondTeam().getScore())
		{
			return model.getFirstTeam();
		}
		else
		{
			return model.getSecondTeam();
		}
	}
}
