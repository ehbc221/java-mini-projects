package com.laguille.belote.main;

import com.laguille.belote.controller.GameController;
import com.laguille.belote.model.GameModel;

public class Main
{
	public static void main(String[] args)
	{
		GameModel model = new GameModel();
		GameController controller = new GameController(model);
		controller.initGame();
		controller.startGame();
		
//		model.getFirstTeam().getFirstPlayer().getHand().addCard(model.getDeck().getCard(25));
//		model.getFirstTeam().getFirstPlayer().getHand().addCard(model.getDeck().getCard(24));
//		model.getFirstTeam().getFirstPlayer().getHand().addCard(model.getDeck().getCard(11));
	}
}
