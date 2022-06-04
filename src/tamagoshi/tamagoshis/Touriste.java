package tamagoshi.tamagoshis;

import java.util.*;

/**
 * Touriste hérite de Tamagoshi
 * et a la particularité de ne pas parler le même langage que l'utilisateur.
 */
public class Touriste extends Tamagoshi {

    /**
     * @param nom Nom du Touriste.
     * @param locale Langue du joueur qui va être changé par une autre.
     */
    public Touriste(String nom, Locale locale) {
        super(nom, changerNationnalite(locale));
    }

    /**
     * Random pour choisir une langue aléatoire pour le Tamagoshi qui ne soit pas la langue du joueur.
     */
    private static final Random random = new Random();

    /**
     * Permet de changer la langue du Tamagoshi.
     * (Penser à mettre à jour la liste des langues dès qu'une nouvelle trad est ajouté au jeu).
     * En static pour pouvoir être appelé dans le constructeur avant l'appel du constructeur parent.
     * @param locale Langue de l'utilisateur.
     * @return Une nouvelle langue.
     */
    private static Locale changerNationnalite(Locale locale) {

        List<Locale> toutesLesLangues = new ArrayList<>(
                Arrays.asList(new Locale("fr", "FR"),
                        new Locale("en", "EN"),
                        new Locale("de", "DE"))
        );

        if (toutesLesLangues.size() == 0)
            return locale;

        toutesLesLangues.remove(locale);

        return toutesLesLangues.get(random.nextInt(toutesLesLangues.size()));
    }


}
