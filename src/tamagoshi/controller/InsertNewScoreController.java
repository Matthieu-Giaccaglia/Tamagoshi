package tamagoshi.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tamagoshi.jeu.TamaGame;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller pour l'affichage de l'insertion d'un nouveau meilleur score.
 */
public class InsertNewScoreController implements Initializable {

    /**
     * Score à sauvegarder.
     */
    private int scoreToSave;
    /**
     * Difficulté du niveau du score obtenu.
     */
    private int difficulte;

    /**
     * Label contenant message pour le joueur.
     */
    public Label labelDesc;
    /**
     * Textfield 1er lettre du nom
     */
    public TextField textFieldName1;
    /**
     * Textfield 2ème lettre du nom
     */
    public TextField textFieldName2;
    /**
     * Textfield 3ème lettre du nom
     */
    public TextField textFieldName3;
    /**
     * Button de sauvegarde.
     */
    public Button buttonSave;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addTextLimiter(textFieldName1, 1);
        addTextLimiter(textFieldName2, 1);
        addTextLimiter(textFieldName3, 1);
    }

    /**
     * Indique le score à sauvegarder et la difficulté du niveau.
     * @param scoreToSave Le score.
     * @param difficulte La difficulte.
     */
    public void setScoreToSaveAndDifficulty(int scoreToSave, int difficulte) {
        this.scoreToSave = scoreToSave;
        this.difficulte = difficulte;

        for (int i = 0; i<TamaGame.listesDesScores.get(difficulte).length; i++) {

            if (scoreToSave > TamaGame.listesDesScores.get(difficulte)[i].getScore()) {
                labelDesc.setText(labelDesc.getText() + (i+1));
                break;
            }
        }

    }


    /**
     * Ajoute une limite de caractère à un TextField et vérifie s'il s'agit d'une lettre.
     * Active le bouton "Sauvegarder" si les 3 TextField sont remplis.
     * Source : https://stackoverflow.com/a/21978453
     * @param tf TextField
     * @param maxLength int
     */
    public void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {

            String s = newValue;

            if (tf.getText().length() > maxLength) {
                s = s.substring(0, maxLength);
            }

            //Ajout perso.
            if (!s.equals("") && !Character.isLetter(s.charAt(0))) {
                s = "";
            }

            tf.setText(s.toUpperCase());

            //Ajout perso.
            this.buttonSave.setDisable(this.textFieldName1.getText().equals("")
                    || this.textFieldName2.getText().equals("")
                    || this.textFieldName3.getText().equals(""));
        });
    }

    /**
     * Via l'UI
     * Permet de sauvegarder le score.
     */
    public void saveScore() {

        String name = textFieldName1.getText() + textFieldName2.getText() + textFieldName3.getText();
        TamaGame.addScore(scoreToSave, name, difficulte);
    }

}
