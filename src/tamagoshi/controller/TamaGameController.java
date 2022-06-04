package tamagoshi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import tamagoshi.jeu.TamaGame;
import tamagoshi.tamagoshis.GrosJoueur;
import tamagoshi.tamagoshis.GrosMangeur;
import tamagoshi.tamagoshis.Tamagoshi;
import tamagoshi.tamagoshis.Touriste;
import tamagoshi.util.RandomNames;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

/**
 * Controller du jeu.
 */
public class TamaGameController implements Initializable {

    /**
     * Liste des Tamagoshis en vie.
     */
    private final List<TamaFrameController> listeTamaFrameEnVie = new ArrayList<>();

    /**
     * Liste de tous les Tamagoshis générés pour la partie.
     */
    private final List<TamaFrameController> listeTamaFrameTous = new ArrayList<>();

    /**
     * Random pour générer des Tamagoshis différents.
     */
    private final Random random = new Random();

    /**
     * ResourceBundle pour avoir la langue du joueur.
     */
    private ResourceBundle rBundle;

    /**
     * Le nombre de tours effectué.
     */
    private int nbTour = 0;

    /**
     * La difficulté choisie.
     */
    private int difficulty;

    /**
     * Fenêtre des meilleurs scores.
     */
    private final Stage stageBestScore = new Stage();

    /**
     * Fenêtre des règles du jeu.
     */
    private final Stage stageGameRule = new Stage();

    /**
     * GridPane contenant tous les vues des Tamagoshis.
     */
    public GridPane gridPaneTama;

    /**
     * TextFlow du dialogue entre le jeu et le joueur.
     */
    public TextFlow textFlow;

    /**
     * CheckMenu pour savoir si les noms des Tamagoshis sont aléatoires.
     */
    public CheckMenuItem checkNomAleatoire;

    /**
     * TextField pour l'utilisateur pour qu'il puisse écrire si besoin.
     */
    public TextField textFieldUser;

    /**
     * ScrollPane pour scroller dans le TextFlox du dialogue jeu/joueur.
     */
    public ScrollPane scrollPane;

    /**
     * Menu des langues du jeu.
     */
    public Menu menuLangue;

    /**
     * Check pour mettre le jeu en anglais.
     */
    public CheckMenuItem menuLangue_en_EN;

    /**
     * Check pour mettre le jeu en français.
     */
    public CheckMenuItem menuLangue_fr_FR;

    /**
     * Check pour mettre le jeu en allemand.
     */
    public CheckMenuItem menuLangue_de_DE;

    /**
     * Menu des difficultés.
     */
    public Menu menuDifficulte;

    /**
     * Check pour mettre le jeu en difficulté 3.
     */
    public CheckMenuItem menuDifficulte_3;

    /**
     * Check pour mettre le jeu en difficulté 4.
     */
    public CheckMenuItem menuDifficulte_4;

    /**
     * Check pour mettre le jeu en difficulté 5.
     */
    public CheckMenuItem menuDifficulte_5;

    /**
     * Check pour mettre le jeu en difficulté 6.
     */
    public CheckMenuItem menuDifficulte_6;

    /**
     * Check pour mettre le jeu en difficulté 7.
     */
    public CheckMenuItem menuDifficulte_7;

    /**
     * Check pour mettre le jeu en difficulté 8.
     */
    public CheckMenuItem menuDifficulte_8;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.rBundle = resourceBundle;

        String selectedLanguage = resourceBundle.getLocale().toString();

        for (MenuItem menu : menuLangue.getItems()) {
            if (menu.getId().equals("menuLangue_" + selectedLanguage)) {

                CheckMenuItem menu1 = (CheckMenuItem) menu;
                menu1.setSelected(true);
                menu1.setDisable(true);
                break;
            }
        }

        checkNomAleatoire.setSelected(Boolean.parseBoolean(TamaGame.myProps.getProperty("game.randomName", String.valueOf(true))));
        remettreUIDifficulteAJour("menuDifficulte_" + TamaGame.myProps.getProperty("game.difficulty", "3"));

        Tamagoshi.setTempsVie(10);

        TamaGame.logger.fine("Fin Initialisation.");
    }

    /**
     * Lance un partie.
     */
    public void lancerPartie() {
        TamaGame.logger.info("Lancement Partie.");
        initTama();
    }

    /**
     * Initialise les tamagoshis en fonction de si les noms aléatoires sont sélectionnés.
     */
    private void initTama() {
        addMessageSection(rBundle.getString("game.start"));

        int nbTamagotshi = difficulty;


        if (checkNomAleatoire.isSelected()) {
            List<String> list = RandomNames.getRandomNames(difficulty);

            setTamaName(list);
            addMessageDescription(rBundle.getString("game.tamaCreated"));
            addMessageDescription(rBundle.getString("game.launch"));
            lancerTour();
        } else {
            addMessageSection(rBundle.getString("game.tamaName.description"));
            List<String> list = new ArrayList<>();
            addMessageQuestion(rBundle.getString("game.tamaName.firstQuestion"));
            textFieldUser.setDisable(false);
            textFieldUser.setPromptText(rBundle.getString("dialog.userDialog.enable.tamaName"));


            textFieldUser.setOnAction(actionEvent -> {
                list.add(textFieldUser.getText());
                addMessageUser(textFieldUser.getText());
                textFieldUser.clear();
                if (list.size() == nbTamagotshi) {
                    textFieldUser.setPromptText(rBundle.getString("dialog.userDialog.disable"));
                    textFieldUser.setDisable(true);
                    setTamaName(list);
                    addMessageDescription(rBundle.getString("game.tamaCreated"));
                    addMessageDescription(rBundle.getString("game.launch"));
                    lancerTour();
                } else {
                    addMessageQuestion(rBundle.getString("game.tamaName.nextQuestion"));
                }
            });
        }


    }

    /**
     * Si numTour inférieur au temps de vie des Tamagoshis, lance un nouveau tour.
     * Sinon fin de partie.
     */
    private void lancerTour() {

        if (nbTour == Tamagoshi.getTempsVie()) {
            TamaGame.logger.info("Fin de partie.");
            finDePartie();
        } else {
            TamaGame.logger.info("Lancement Tour n°" + nbTour);
            addMessageSection(rBundle.getString("game.step.start") + " " + (nbTour + 1));
            activerNourrir();
            nbTour++;
        }
    }

    /**
     * Activer tous les boutons "Nourrir" des tamagoshis.
     * Envoie une question au joueur.
     */
    private void activerNourrir() {
        addMessageDescription(rBundle.getString("game.step.tamaFeed.question"));
        for (TamaFrameController tamaFrame : listeTamaFrameEnVie) {
            tamaFrame.tamaParle();
            tamaFrame.activerBoutonNourrir();
        }
    }

    /**
     * Activer tous les boutons "Jouer" des tamagoshis.
     * Désactive tous les boutons "Nourrir" des tamagoshis.
     * Envoie une question au joueur.
     */
    public void tamaNourri() {
        addMessageDescription(rBundle.getString("game.step.tamaFeed.ok"));
        for (TamaFrameController tamaFrame : listeTamaFrameEnVie) {
            tamaFrame.activerBoutonJouer();
            tamaFrame.desactiverBoutonNourrir();
        }
        addMessageDescription(rBundle.getString("game.step.tamaFun.question"));
    }


    /**
     * Permet de désactiver les boutons "Jouer" des Tamagoshis après en avoir pressé un.
     * Lance ensuite la fin du tour.
     */
    public void tamaAmuse() {

        addMessageDescription(rBundle.getString("game.step.tamaFun.ok"));
        for (TamaFrameController tamaFrame : listeTamaFrameEnVie) {
            tamaFrame.desactiverBoutonJouer();
        }
        finDeTour();
    }

    /**
     * Action de fin de tour.
     */
    private void finDeTour() {
        for (Iterator<TamaFrameController> iterator = listeTamaFrameEnVie.iterator(); iterator.hasNext(); ) {

            TamaFrameController tamaFrame = iterator.next();
            tamaFrame.desactiverBoutonJouer();
            if (tamaFrame.tamaConsomme()) {
                tamaFrame.tamaGrandir();
            } else {
                iterator.remove();
            }
        }

        if (listeTamaFrameEnVie.isEmpty()) {
            finDePartie();
            return;
        }

        lancerTour();
    }

    /**
     * Action de fin de partie.
     */
    private void finDePartie() {

        TamaGame.logger.info("Fin de partie.");

        addMessageSection(rBundle.getString("game.end"));
        float ageTotal = 0;
        for (TamaFrameController tamaFrame : listeTamaFrameTous) {

            String messageTamaFin = tamaFrame.getTamagoshi().getNom()
                    + rBundle.getString("game.end.tama.was")
                    + " "
                    + rBundle.getString("game.end.tama." + tamaFrame.getTamagoshi().getClass().getSimpleName())
                    + " ";

            if (listeTamaFrameEnVie.contains(tamaFrame)) {
                addMessageDescription(messageTamaFin
                        + rBundle.getString("game.end.tama.alive"));
            } else {
                addMessageError(messageTamaFin
                        + rBundle.getString("game.end.tama.dead"));
            }
            ageTotal += tamaFrame.getTamagoshi().getAge();
        }

        int score = Math.round(ageTotal / (listeTamaFrameTous.size() * Tamagoshi.getTempsVie()) * 100);

        addMessageSection(" " + rBundle.getString("game.end.score") + " " + score + "%");

        TamaGame.lauchInsertBestScore(score, difficulty);
    }

    /**
     * Ajoute un nom à tous les tamagoshis.
     * @param listeNoms La listes des noms.
     */
    private void setTamaName(List<String> listeNoms) {
        int nbTamagotshi = difficulty;

        outerloop:
        for (int i = 0; i < nbTamagotshi; i++) {

            for (int j = 0; j < 3; j++) {

                //Si tous les tamagoshis sont insérer.
                if (gridPaneTama.getChildren().size() >= nbTamagotshi) {
                    break outerloop;
                }

                String name = listeNoms.get(j + i*3);
                Tamagoshi t;

                int r = random.nextInt(100);

                if (r < 5) { // 5%
                    t = new Tamagoshi(name, rBundle.getLocale());
                } else if (r < 20) { //15%
                    t = new Touriste(name, rBundle.getLocale());
                } else if (r < 60) { //40%
                    t = new GrosJoueur(name, rBundle.getLocale());
                } else { //40%
                    t = new GrosMangeur(name, rBundle.getLocale());
                }

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("layout/tamaframe.fxml"));
                fxmlLoader.setResources(
                        ResourceBundle.getBundle("bundles.TamaFrame",
                        new Locale(rBundle.getLocale().getLanguage(), rBundle.getLocale().getCountry()))
                );

                Parent root = null;

                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    TamaGame.logger.log(Level.SEVERE, e, ()->"Impossible de charger le FXML tamaframe.fxml.");
                    TamaGame.logger.severe("Fermeture du jeu.");
                    System.exit(0);
                }

                TamaFrameController tamaFrame = fxmlLoader.getController();
                listeTamaFrameEnVie.add(tamaFrame);
                listeTamaFrameTous.add(tamaFrame);
                tamaFrame.setTamaGameControllerParent(this);
                tamaFrame.setTamagoshi(t);

                gridPaneTama.add(root, j, i);
            }
        }
    }

    /**
     * Ajoute un message de section en bleu-violet dans le textFlow de l'UI.
     * @param message Le message de section.
     */
    private void addMessageSection(String message) {
        addMessage("\n" + message, Color.BLUEVIOLET);
    }

    /**
     * Ajoute un message de description en bleu dans le textFlow de l'UI.
     * @param message La description.
     */
    private void addMessageDescription(String message) {
        addMessage(message, Color.BLUE);
    }

    /**
     * Ajoute une question en vert dans le textFlow de l'UI.
     * @param message La question.
     */
    private void addMessageQuestion(String message) {
        addMessage(message, Color.GREEN);
    }

    /**
     * Ajoute le message en noir de l'utilisateur dans le textFlow de l'UI.
     * @param message Le message de l'utilisateur.
     */
    private void addMessageUser(String message) {
        addMessage("Vous : " + message, Color.BLACK);
    }

    /**
     * Ajoute un message d'erreur en rouge dans le textFlow de l'UI.
     * @param message Le message d'erreur.
     */
    private void addMessageError(String message) {
        addMessage(message, Color.RED);
    }

    /**
     * Ajoute un message dans le textFlow de l'UI et de coloré ce message.
     * @param message Le message.
     * @param color La couleur du texte.
     */
    private void addMessage(String message, Color color) {
        Text text = new Text(message + "\n");
        text.setFill(color);
        text.setFont(Font.font("Pixel Sans Serif Regular", FontWeight.NORMAL, FontPosture.REGULAR, 11));
        textFlow.getChildren().add(text);
        scrollPane.vvalueProperty().bind(textFlow.heightProperty());
    }

    /**
     * Lance une nouvelle partie via l'UI.
     */
    public void nouvellePartie() {

        TamaGame.logger.info("Nouvelle Partie.");

        gridPaneTama.getChildren().clear();
        textFlow.getChildren().clear();
        listeTamaFrameEnVie.clear();
        listeTamaFrameTous.clear();
        this.nbTour = 0;

        lancerPartie();
    }

    /**
     * Via l'UI.
     * Permet de savoir si l'utilisateur veut des noms aléatoires.
     * Sauvegarde ce changement.
     */
    public void changerNomAleatoire() {

        TamaGame.logger.info("Check nom aléatoire : "+checkNomAleatoire.isSelected()+" .");

        TamaGame.myProps.setProperty("game.randomName", String.valueOf(checkNomAleatoire.isSelected()));
        TamaGame.sauvegarde();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, rBundle.getString("menu.option.randomName.alert"), ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Via l'UI.
     * Permet de changer la difficulté.
     * @param event UI source.
     */
    public void changerDifficulte(ActionEvent event) {

        CheckMenuItem checkMenuItem = (CheckMenuItem) event.getSource();
        remettreUIDifficulteAJour(checkMenuItem.getId());

        TamaGame.logger.info("Changement difficulté :" + checkMenuItem.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION, rBundle.getString("menu.option.difficulty.alert"), ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Permet d'uncheck l'ancienne difficulté et de check la nouvelle.
     * Sauvegarde la difficulté.
     * @param id L'id de l'UI source.
     */
    private void remettreUIDifficulteAJour(String id) {
        for (MenuItem menu : menuDifficulte.getItems()) {
            if (menu.getId().equals(id)) {
                CheckMenuItem menu1 = (CheckMenuItem) menu;
                menu1.setSelected(true);
                menu1.setDisable(true);

                String[] output = menu1.getId().split("_");
                difficulty = Integer.parseInt(output[1]);

                TamaGame.myProps.setProperty("game.difficulty", output[1]);
                TamaGame.sauvegarde();
            } else {
                CheckMenuItem menu1 = (CheckMenuItem) menu;
                menu1.setSelected(false);
                menu1.setDisable(false);
            }
        }
    }

    /**
     * Via l'UI.
     * Permet de changer la langue du jeu.
     * @param event UI source.
     */
    public void changerLangue(ActionEvent event) {
        String langue;
        String pays;

        if (event.getSource() == menuLangue_en_EN) {
            langue = "en";
            pays = "EN";
        } else if (event.getSource() == menuLangue_fr_FR) {
            langue = "fr";
            pays = "FR";
        } else if (event.getSource() == menuLangue_de_DE) {
            langue = "de";
            pays = "DE";
        } else {
            langue = "en";
            pays = "EN";
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rBundle.getString("menu.option.language.alert"), ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {

            TamaGame.logger.info("Changement langue (langue : " + langue + ", pays : " + pays + ").");


            TamaGame.myProps.setProperty("game.language", langue);
            TamaGame.myProps.setProperty("game.country", pays);

            TamaGame.sauvegarde();
            TamaGame.lauchGame();

        } else {
            CheckMenuItem checkMenuItem = (CheckMenuItem) event.getSource();
            checkMenuItem.setSelected(false);
        }
    }

    /**
     * Via l'UI.
     * Affiche les meilleurs scores.
     */
    public void afficherMeilleursScores() {

        FXMLLoader fxmlLoader = new FXMLLoader(TamaGame.class.getClassLoader().getResource("layout/best_score.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("bundles.BestScore", new Locale(rBundle.getLocale().getLanguage(), rBundle.getLocale().getCountry())));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            TamaGame.logger.log(Level.SEVERE, e, ()->"Impossible de charger le FXML best_score.fxml.");
            TamaGame.logger.severe("Fermeture du jeu.");
            System.exit(0);
        }

        BestScoreController bestScoreController = fxmlLoader.getController();
        bestScoreController.initializeActualDifficulty(difficulty);
        Scene scene = new Scene(root);
        stageBestScore.setScene(scene);
        stageBestScore.setResizable(false);
        stageBestScore.show();
        TamaGame.logger.info("Affichage des meilleurs scores.");
    }

    /**
     * Via l'UI.
     * Affiche les règles du jeu.
     */
    public void afficherRegles() {

        FXMLLoader fxmlLoader = new FXMLLoader(TamaGame.class.getClassLoader().getResource("layout/game_rules.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("bundles.GameRules", new Locale(rBundle.getLocale().getLanguage(), rBundle.getLocale().getCountry())));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            TamaGame.logger.log(Level.SEVERE, e, ()->"Impossible de charger le FXML game_rules.fxml.");
            TamaGame.logger.severe("Fermeture du jeu.");
            System.exit(0);
        }

        Scene scene = new Scene(root);
        stageGameRule.setScene(scene);
        stageGameRule.setResizable(false);
        stageGameRule.show();
        TamaGame.logger.info("Affichage des règles.");
    }

    /**
     * Affiche les information du jeu.
     */
    public void afficherInformation() {

        String info = """
                version: 1.0.0
                JDK : java 17.0.1, javaFX 17.0.1
                Author: Matthieu Giaccaglia""";


        Alert alert = new Alert(Alert.AlertType.INFORMATION, info, ButtonType.OK);
        alert.setTitle("TamaGame Information");
        alert.setHeaderText("TamaGame Information");
        alert.showAndWait();
    }
}