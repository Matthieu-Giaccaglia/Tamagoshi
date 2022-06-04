package tamagoshi.tamagoshis;

import java.util.Locale;

/**
 * GrosMangeur hérite de Tamagoshi
 * et a la particularité de manger plus que les Tamagoshis normaux.
 */
public class GrosJoueur extends Tamagoshi{

    /**
     * @param nom Nom du GrosMangeur.
     */
    public GrosJoueur(String nom) {
        super(nom);
    }

    /**
     * @param nom Nom du GrosJoueur.
     * @param locale Langue du GrosJoueur.
     */
    public GrosJoueur(String nom, Locale locale) {
        super(nom, locale);
    }

    /**
     * Consomme 2 point de fun si le fun != 0
     * @return true si Tamagoshi est toujours en vie, false sinon.
     */
    @Override
    public boolean consommeFun() {
        
        fun--;

        return super.consommeFun();
    }
}
