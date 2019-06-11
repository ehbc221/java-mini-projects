package jeux.enumerations;
/**Au president, on distingue quatre etats pour chacun des joueurs
 * <ol>
 * <li>Peut_jouer signifie que le joueur peut jouer ou passer son tour.</li>
 * <li>Saute signifie que si les joueurs precedent on joue des cartes de meme valeur alors son tour est saute</li>
 * <li>Passe signifie que le joueur ne peut pas jouer de carte au dessus de celle(s) proposee ou que le joueur ne voulait pas jouer</li>
 * <li>A_fini signifie que le joueur n'a plus de carte dans son jeu.</li></ol>*/
public enum Jouerie {Peut_Jouer,Saute,Passe,A_fini}