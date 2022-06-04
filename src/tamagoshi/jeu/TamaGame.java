package tamagoshi.jeu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tamagoshi.controller.InsertNewScoreController;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * La classe permettant de lancer le controller avec la vue du jeu.
 */
public class TamaGame extends Application {

    /**
     * Localisation de la sauvegarde. (Répertoire du jeu)
     */
    public static String propertiesFileLocation = "myProperties.properties";

    /**
     * Les props.
     */
    public static Properties myProps = new Properties();

    /**
     * La fenêtre du jeu.
     */
    private static Stage stageTamaGame;

    /**
     * La fenêtre pour insérer un nouveau score.
     */
    private static Stage stageInsertScore;

    /**
     * Le layout de la vue du jeu.
     */
    private static Parent rootTamaGameLayout;

    /**
     * Liste pour charger en mémoire les scores afin d'éviter de regarder dans les properties dès que nécessaire.
     */
    public static HashMap<Integer, Score[]> listesDesScores = new HashMap<>();

    /**
     * Le logger du jeu.
     */
    public static final Logger logger = Logger.getLogger("TamaGame");

    /**
     * Lancement du jeu avec le chargement du logger.
     * @param args arguments
     */
    public static void main(String[] args) {

        logger.setLevel(Level.ALL);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        logger.addHandler(ch);

        Handler fh;
        try {
            fh = new FileHandler("TamaGame_log.txt");
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.ALL);
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e, ()->"Initialisation TamaGame.log échoué");
        }

        logger.fine("Configuation Logger Ok !");

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stageTamaGame = stage;
        stageInsertScore = new Stage();
        chargerSauvegarde();
        initialiseListeScore();
        lauchGame();
    }

    /**
     * Lance la fenêtre principale du jeu.
     */
    public static void lauchGame() {
        FXMLLoader fxmlLoader = new FXMLLoader(TamaGame.class.getClassLoader().getResource("layout/tama_game_graphic.fxml"));

        String langue = myProps.getProperty("game.language", "en");
        String pays = myProps.getProperty("game.country", "EN");
        sauvegarde();
        fxmlLoader.setResources(ResourceBundle.getBundle("bundles.TamaGameGraphics", new Locale(langue, pays)));

        try {
            rootTamaGameLayout = fxmlLoader.load();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e, ()->"Impossible de charger le FXML tama_game_graphic.fxml.");
            logger.severe("Fermeture du jeu.");
            System.exit(0);
        }

        Scene scene = new Scene(rootTamaGameLayout);
        stageTamaGame.setScene(scene);
        stageTamaGame.setTitle("TameGame");
        stageTamaGame.setResizable(false);
        stageTamaGame.show();
        logger.fine("Lancement de la fenêtre principal.");
    }

    /**
     * Lance la fenêtre pour insérer un nouveau score.
     * @param score Le nouveau score à insérer.
     * @param difficulte La difficulté du jeu où le score a été obtenu.
     */
    public static void lauchInsertBestScore(int score, int difficulte) {

        Score[] scores = listesDesScores.get(difficulte);
        if (scores.length>0 && scores[scores.length - 1].getScore() >= score) {
            return;
        }

        rootTamaGameLayout.setDisable(true);

        FXMLLoader fxmlLoader = new FXMLLoader(TamaGame.class.getClassLoader().getResource("layout/insert_new_score.fxml"));

        String langue = myProps.getProperty("game.language", "en");
        String pays = myProps.getProperty("game.country", "EN");

        fxmlLoader.setResources(ResourceBundle.getBundle("bundles.InsertNewScore", new Locale(langue, pays)));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e, ()->"Impossible de charger le FXML insert_new_score.fxml.");
            logger.severe("Fermeture du jeu.");
            System.exit(0);
        }

        InsertNewScoreController insertNewScoreController = fxmlLoader.getController();
        insertNewScoreController.setScoreToSaveAndDifficulty(score, difficulte);

        Scene scene = new Scene(root);
        stageInsertScore.setOnCloseRequest(windowEvent -> {
            rootTamaGameLayout.setDisable(false);
        });
        stageInsertScore.setScene(scene);
        stageInsertScore.setTitle("TameGame Best Score");
        stageInsertScore.setResizable(false);
        stageInsertScore.show();
        logger.fine(() -> "Lancement de la fenêtre d'insertion d'un nouveau score.");
    }

    /**
     * Charge la sauvegarde du jeu. Sinon créer un fichier de sauvegarde.
     */
    private static void chargerSauvegarde() {
        try (InputStream in = new FileInputStream(propertiesFileLocation)) {
            myProps.load(in);
            logger.fine("Fichier Properties trouvé.");
        } catch (IOException e) {
            logger.log(Level.WARNING, e, ()->"Fichier Properties introuvable pour charger la sauvegarde. Création d'un nouveau fichier de sauvegarde.");
            myProps = new Properties();
        }
    }

    /**
     * Permet de sauvegarder toutes les données dans les props.
     */
    public static void sauvegarde() {
        sauvegarderLesScores();
        try (OutputStream out = new FileOutputStream(propertiesFileLocation)) {
            myProps.store(out, "TamaGame save files");
            logger.fine("Sauvegarde terminée.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, e, ()->"Fichier Properties introuvable pour sauvegarder.");
            logger.severe("Fermeture du jeu.");
            System.exit(1);
        }
    }

    /**
     * Permet d'ajouter des props de tous les scores en mémoire du jeu.
     */
    private static void sauvegarderLesScores() {

        String propsKeyName = "game.bestScore.name.";
        String propsKeyScore = "game.bestScore.score.";

        for (int difficulte = 3; difficulte <= 8; difficulte++ ){

            Score[] scores = listesDesScores.get(difficulte);

            for (int rang = 0; rang < scores.length; rang++) {

                if (rang > 2) break;

                myProps.setProperty(propsKeyName + difficulte + "." + (rang+1), scores[rang].getName());
                myProps.setProperty(propsKeyScore + difficulte + "." + (rang+1), String.valueOf(scores[rang].getScore()));
            }
        }
        logger.fine("Sauvegarde des scores dans les props terminée.");
    }

    /**
     * Récupère tous les scores sauvegardés pour les garder en mémoire.
     */
    private static void initialiseListeScore() {
        for (int difficulte = 3; difficulte <= 8; difficulte++) {
            listesDesScores.put(difficulte, verificationScore(difficulte));
        }
    }

    /**
     * Vérifie si les scores sont corrects (non modifié par l'utilisateur)
     * @param difficulte Difficulté des scores à vérifier.
     * @return Tableau de scores de la difficulté mis en paramètre.
     */
    private static Score[] verificationScore(int difficulte) {

        String propsKeyName = "game.bestScore.name.";
        String propsKeyScore = "game.bestScore.score.";

        List<Score> tmpList = new ArrayList<>();

        for (int rang = 1; rang <= 3; rang++) {


            try {
                String scoreString = myProps.getProperty(propsKeyScore + difficulte + "." + rang);
                String name = myProps.getProperty(propsKeyName + difficulte + "." + rang);

                if (scoreString == null && name == null) {
                    continue;
                } else if (scoreString == null)  {
                    logger.warning("Suppression du score (difficulte : " + difficulte + ", rang : " + rang + ") car pas de score trouvé.");
                    myProps.remove(propsKeyName + difficulte + "." + rang);
                    continue;
                } else if (name == null) {
                    logger.warning("Suppression du score (difficulte : " + difficulte + ", rang : " + rang + ") car pas de nom trouvé.");
                    myProps.remove(propsKeyName + difficulte + "." + rang);
                    continue;
                }

                int score = Integer.parseInt(scoreString);

                if (score < 0 || name.equals("")) {
                    logger.warning("Suppression du score (difficulte : " + difficulte + ", rang : " + rang + ") car score<0 ou pas de nom associé.");
                    myProps.remove(propsKeyScore + difficulte + "." + rang);
                    myProps.remove(propsKeyName + difficulte + "." + rang);
                    continue;
                }

                tmpList.add(new Score(score, name));

            } catch (NumberFormatException e) {
                int finalRang = rang;
                logger.log(Level.WARNING, e, ()->"Suppression du score (difficulte : " + difficulte + ", rang : " + finalRang + ") car format incorrect du score.");
                myProps.remove(propsKeyScore + difficulte + "." + rang);
                myProps.remove(propsKeyName + difficulte + "." + rang);
            }
        }

        tmpList.sort(new TrierScore());
        return tmpList.toArray(Score[]::new);
    }

    /**
     * Permet d'ajouter un nouveau score dans le bon classement.
     * @param newScore Le nouveau score.
     * @param name Le nom du joueur.
     * @param difficulte La difficulté du niveau.
     */
    public static void addScore(int newScore, String name, int difficulte) {

        stageInsertScore.close();
        rootTamaGameLayout.setDisable(false);

        List<Score> scoreList = new ArrayList<>(List.of(listesDesScores.get(difficulte)));
        scoreList.add(new Score(newScore, name));
        scoreList.sort(new TrierScore());

        listesDesScores.put(difficulte, scoreList.toArray(Score[]::new));
        sauvegarde();
        logger.fine("Nouveau score ajouté.");
    }


    /**
     * Permet uniquement de trier les scores par ordre décroissant.
     */
    private static class TrierScore implements Comparator<Score> {

        @Override
        public int compare(Score o1, Score o2) {
            return o2.getScore() - o1.getScore();
        }
    }
}

