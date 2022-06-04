package tamagoshi.tamagoshis;

import javafx.scene.image.Image;

import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Classe Tamagoshi
 */
public class Tamagoshi {

    /**
     * Age du Tamagoshi.
     */
    private int age;

    /**
     * Energie maximum que peut avoir le Tamagoshi.
     */
    private final int maxEnergie;

    /**
     * Energie du Tamagoshi.
     */
    protected int energie;

    /**
     * Fun maximum que peut avoir le Tamagoshi.
     */
    private final int maxFun;

    /**
     * Fun du Tamagoshi.
     */
    protected int fun;

    /**
     * Nom du Tamagoshi.
     */
    private final String nom;

    /**
     * L'espérance de vie des Tamagoshis.
     */
    private static int tempsVie;

    /**
     * Random pour générer des points d'énergie ou de fun de façon aléatoire.
     */
    private final Random random = new Random();

    /**
     * Le status du Tamagoshi.
     */
    private Status status;

    /**
     * Raison de la mort du Tamagoshi.
     */
    private String raisonMort;

    /**
     * Enumération des status possibles pour un Tamagoshi.
     */
    public enum Status {
        /**
         * Status affamé, le Tamagoshi a besoin de manger.
         */
        AFFAME,
        /**
         * Status ennuyé, le Tamagoshi a besoin de jouer.
         */
        ENNUYE,
        /**
         * Status affamé et ennuyé, le Tamagoshi a besoin de manger et de jouer.
         */
        AFFAME_ET_ENNUYE,
        /**
         * Status heureux, le Tamagoshi n'a besoin de rien.
         */
        HEUREUX,
        /**
         * Status mort, le Tamagoshi est mort et ne peut plus rien faire.
         */
        MORT
    }

    /**
     * Langue du Tamagoshi.
     */
    protected ResourceBundle rBungle;

    /**
     * @param nom Nom du Tamagoshi.
     */
    public Tamagoshi(String nom){
        this.nom = nom;
        this.age = 0;

        this.maxEnergie = random.nextInt(9-5 +1) + 5;
        this.energie = random.nextInt(7-3 +1) + 3;
        this.maxFun = random.nextInt(9-5 +1) + 5;
        this.fun = random.nextInt(7-3 +1) +3;

        this.rBungle = ResourceBundle.getBundle("bundles.Tamagoshi", new Locale("en", "EN"));
        this.raisonMort = rBungle.getString("death.reason.not");
    }

    /**
     *
     * @param nom Nom du Tamagoshi.
     * @param locale Langue du Tamagoshi.
     */
    public Tamagoshi(String nom, Locale locale){
        this(nom);
        this.rBungle = ResourceBundle.getBundle("bundles.Tamagoshi", locale);
        this.raisonMort = rBungle.getString("death.reason.not");
    }

    /**
     * @return La langue du Tamagoshi.
     */
    public ResourceBundle getrBungle() {
        return rBungle;
    }

    /**
     * @return Le temps de vie du Tamagoshi.
     */
    public static int getTempsVie(){
        return tempsVie;
    }

    /**
     * Permet d'initialiser de manière générale le temps de vie de tous les Tamagoshis
     * @param tempsVie Le temps de vie des Tamagoshis.
     */
    public static void setTempsVie(int tempsVie){
        Tamagoshi.tempsVie = tempsVie;
    }

    /**
     * Indique le status du Tamagoshi, s'il s'ennuit, s'il a faim ou s'il est tranquille.
     * @return true si le Tamagoshi est tranquille, false sinon.
     */
    public String parle(){

        mettreAJourStatut();

        String value;

        switch (this.status) {
            case AFFAME -> value = rBungle.getString("status.hungry");
            case ENNUYE -> value = rBungle.getString("status.boring");
            case AFFAME_ET_ENNUYE -> value = rBungle.getString("status.hungry.boring");
            case HEUREUX -> value = rBungle.getString("status.happy");
            case MORT -> value = rBungle.getString("status.death");
            default -> value = rBungle.getString("status.default");
        }

        return value;
    }

    /**
     * @return Le status Tamagoshi.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Met à jour le statut du Tamagoshi.
     */
    private void mettreAJourStatut(){

        if (this.energie < 4 && this.fun <4) {
            this.status = Status.AFFAME_ET_ENNUYE;
        } else if (this.energie < 4) {
            this.status = Status.AFFAME;
        } else if (this.fun < 4) {
            this.status = Status.ENNUYE;
        } else {
            this.status = Status.HEUREUX;
        }
    }


    /**
     * Permet de restaurer l'énergie du tamagoshi entre 1 et 3 points s'il n'est pas full énergie.
     * @return true s'il a mangé, false sinon.
     */
    public String mange() {

        int min = 2;
        int max = 3;

        if(this.energie < this.maxEnergie){
            int r = random.nextInt(max-min) + min;

            this.energie += r;
            if(this.energie > this.maxEnergie)
                this.energie = this.maxEnergie;
            return rBungle.getString("eat.good");
        }

        return rBungle.getString("eat.bad");
    }

    /**
     * Permet de restaurer le fun du tamagoshi entre 1 et 3 points s'il n'est pas full fun.
     * @return true s'il a joué, false sinon.
     */
    public String joue() {

        int min = 2;
        int max = 3;

        if(this.fun < this.maxFun){

            int r = random.nextInt(max-min) + min;

            this.fun += r;
            if(this.fun > this.maxFun)
                this.fun = this.maxFun;
            return rBungle.getString("play.good");
        }
        return rBungle.getString("play.bad");
    }

    /**
     * Consomme 1 point d'énergie si l'énergie != 0
     * @return true si Tamagoshi est toujours en vie, false sinon.
     */
    public boolean consommeEnergie() {

        this.energie = consomme(energie, rBungle.getString("death.reason.hungry"));
        return energie  > 0;
    }

    /**
     * Consomme 1 point de fun si le fun != 0
     * @return true si Tamagoshi est toujours en vie, false sinon.
     */
    public boolean consommeFun() {
        this.fun = consomme(fun, rBungle.getString("death.reason.boring"));
        return fun > 0;
    }

    /**
     * Consomme 1 point d'une statistique donnée et affiche un message de mort si la statistique est inférieure à 0.
     * @param statistique La statistique.
     * @param messageMort Le message de mort.
     * @return true si en vie, false sinon.
     */
    private int consomme(int statistique, String messageMort){

        if(statistique > 0)
            statistique--;

        if(statistique <= 0) {
            this.raisonMort = messageMort;
        }

        return statistique;
    }

    /**
     * @return La raison de la mort.
     */
    public String getRaisonMort() {
        return raisonMort;
    }

    /**
     *
     * @return Le nom du tamagoshi.
     */
    public String getNom(){ return nom; }

    /**
     *
     * @return L'âge du Tamagoshi.
     */
    public int getAge(){ return age; }

    /**
     * Fait grandir le tamagoshi.
     * @param age Valeur a ajouté à l'âge.
     */
    public void grandir(int age) { this.age += age; }

    @Override
    public String toString() {
        return "Tamagoshi{" +
                "age=" + age +
                ", maxEnergie=" + maxEnergie +
                ", energie=" + energie +
                ", maxFun=" + maxFun +
                ", fun=" + fun +
                ", nom='" + nom + '\'' +
                '}';
    }
}
