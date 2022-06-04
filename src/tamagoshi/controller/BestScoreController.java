package tamagoshi.controller;

import javafx.fxml.Initializable;
import javafx.scene.text.*;
import tamagoshi.jeu.Score;
import tamagoshi.jeu.TamaGame;

import java.net.URL;
import java.util.*;

/**
 * Controller de l'affichage des meilleurs scores.
 */
public class BestScoreController implements Initializable {

    /**
     * ResourcesBundle pour avoir la langue de l'utilisateur.
     */
    private ResourceBundle rBundle;

    /**
     * Layout pour afficher les meilleurs scores de la difficulté choisie.
     */
    public TextFlow textFlowActualDifficulty;

    /**
     * Layout pour afficher les meilleurs scores dans toutes les difficultés.
     */
    public TextFlow textFlowAllDifficulty;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rBundle = resourceBundle;
        afficherTousLesScores();
    }

    /**
     * Permet d'indiquer la difficulté choisie par le joueur.
     * @param difficulte La difficulte.
     */
    public void initializeActualDifficulty(int difficulte) {
        afficherScores(TamaGame.listesDesScores.get(difficulte), textFlowActualDifficulty, 40);
    }

    /**
     * Permet d'afficher les scores.
     * @param scores Tableau de scores à afficher.
     * @param textFlow Le TextFlow de l'affichage.
     * @param fontSize La taille du texte.
     */
    private void afficherScores(Score[] scores, TextFlow textFlow, int fontSize) {
        if (scores.length == 0) {
            Text textScore = new Text(rBundle.getString("score.not.found") + "\n");
            textScore.setFont(Font.font("Pixel Sans Serif Regular", FontWeight.NORMAL, FontPosture.REGULAR, fontSize));
            textFlow.getChildren().add(textScore);
        }

        for (int i = 0; i < scores.length; i++) {

            if (i > 3 - 1) break;

            Text textScore = new Text(scores[i].getName() + " : " + scores[i].getScore() + "% \n");
            textScore.setFont(Font.font("Pixel Sans Serif Regular", FontWeight.NORMAL, FontPosture.REGULAR, fontSize));
            textFlow.getChildren().add(textScore);
        }
    }

    /**
     * Affiche tous les scores.
     */
    public void afficherTousLesScores() {

        for (int i = 3; i <= 8; i++) {

            Text textTitle = new Text(rBundle.getString("difficulty") + " " + i + "\n");
            textTitle.setFont(Font.font("Pixel Sans Serif Regular", FontWeight.NORMAL, FontPosture.REGULAR, 24));
            textFlowAllDifficulty.getChildren().add(textTitle);

            afficherScores(TamaGame.listesDesScores.get(i), textFlowAllDifficulty, 18);

            textFlowAllDifficulty.getChildren().add(new Text("\n\n"));
        }
    }
}
