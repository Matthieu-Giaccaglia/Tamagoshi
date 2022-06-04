package tamagoshi.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tamagoshi.jeu.TamaGame;
import tamagoshi.tamagoshis.GrosJoueur;
import tamagoshi.tamagoshis.GrosMangeur;
import tamagoshi.tamagoshis.Tamagoshi;
import tamagoshi.tamagoshis.Touriste;

import java.io.InputStream;

/**
 * Controller pour l'affichage d'un Tamagoshi.
 */
public class TamaFrameController {


    /**
     * Label du nom du Tamagoshi.
     */
    public Label labelNom;

    /**
     * ImageView du Tamagoshi.
     */
    public ImageView imageTama;

    /**
     * Bouton pour jouer avec le Tamagoshi.
     */
    public Button boutonJouer;

    /**
     * Bouton pour nourrir avec le Tamagoshi.
     */
    public Button boutonNourrir;

    /**
     * Label indiquant le status du Tamagoshi.
     */
    public Label labelStatus;

    /**
     * Controller parent permettant d'envoyer des données comme par exemple si un Tamagoshi a mangé.
     */
    private TamaGameController tamaGameControllerParent;

    /**
     * Image du Tamagoshi.
     */
    Image image;

    /**
     * Le Tamagoshi.
     */
    Tamagoshi tamagoshi;

    /**
     * Le répertoire de l'image.
     */
    String repertoireImage;

    /**
     * Récupère l'image du Tamagoshi.
     * @return L'image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Permet de récupérer le controller parent.
     * @param tamaGameControllerParent Le parent.
     */
    public void setTamaGameControllerParent(TamaGameController tamaGameControllerParent) {
        this.tamaGameControllerParent = tamaGameControllerParent;
    }

    /**
     * Met une image au Tamagoshi.
     * @param imgName L'image.
     */
    public void setImage(String imgName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("raw/tamagoshi/"+ repertoireImage +"/" + imgName);

        if (inputStream == null) {
            TamaGame.logger.warning("Image " + "raw/tamagoshi/"+ repertoireImage +"/" + imgName + " introuvable.");
        } else {
            image = new Image(inputStream,100,100,false,false);
            imageTama.setImage(image);
        }
    }

    /**
     * Associe un Tamagoshi au controller.
     * @param tamagoshi Le Tamagoshi.
     */
    public void setTamagoshi(Tamagoshi tamagoshi) {
        this.tamagoshi = tamagoshi;

        if (tamagoshi instanceof GrosJoueur)
            repertoireImage = "joueur";
        else if (tamagoshi instanceof GrosMangeur)
            repertoireImage = "mangeur";
        else if (tamagoshi instanceof Touriste)
            repertoireImage = "touriste/" + tamagoshi.getrBungle().getLocale().toString();
        else
            repertoireImage = "normal";

        labelNom.setText(tamagoshi.getNom());
    }

    /**
     * Permet de faire parler le Tamagoshi et de changer son image en fonction de son statut.
     */
    public void tamaParle(){
        labelStatus.setText(tamagoshi.parle());
        switch (tamagoshi.getStatus()) {
            case AFFAME -> setImage("faim.png");
            case ENNUYE -> setImage("triste.png");
            case AFFAME_ET_ENNUYE -> setImage("triste_faim.png");
            case HEUREUX -> setImage("normal.png");
        }
    }

    /**
     * Active le bouton "Nourrir".
     */
    public void activerBoutonNourrir(){
        this.boutonNourrir.setDisable(false);
    }

    /**
     * Désactive le bouton "Nourrir".
     */
    public void desactiverBoutonNourrir(){
        this.boutonNourrir.setDisable(true);
    }

    /**
     * Active le bouton "Jouer".
     */
    public void activerBoutonJouer(){
        this.boutonJouer.setDisable(false);
    }

    /**
     * Désactive le bouton "Jouer".
     */
    public void desactiverBoutonJouer(){
        this.boutonJouer.setDisable(true);
    }

    /**
     * Via l'UI.
     * Préviens le controller Parent que le Tamagoshi a été nourri.
     */
    public void tamaNourrir() {
        labelStatus.setText(this.tamagoshi.mange());
        this.tamaGameControllerParent.tamaNourri();
    }

    /**
     * Via l'UI.
     * Préviens le controller Parent que le Tamagoshi a joué.
     */
    public void tamaJouer() {
        labelStatus.setText(this.tamagoshi.joue());
        this.tamaGameControllerParent.tamaAmuse();
    }

    /**
     * Fais consommer l'énergie et le fun du Tamagoshi.
     * @return Le Tamagoshi en vie.
     */
    public boolean tamaConsomme(){

        boolean estEnVie = this.tamagoshi.consommeEnergie() && this.tamagoshi.consommeFun();

        if (!estEnVie) {
            this.repertoireImage = "mort";
            setImage("tombe.png");
            labelStatus.setText(tamagoshi.getRaisonMort());
            cacherBouton();
        }
        return estEnVie;
    }

    /**
     * Fais grandir le Tamagoshi.
     */
    public void tamaGrandir(){
        this.tamagoshi.grandir(1);
    }

    /**
     * @return Le Tamagoshi.
     */
    public Tamagoshi getTamagoshi() {
        return tamagoshi;
    }

    /**
     * Permet de cacher les boutons "Nourrir" et "Jouer".
     */
    public void cacherBouton() {
        boutonJouer.setVisible(false);
        boutonNourrir.setVisible(false);
    }

}
