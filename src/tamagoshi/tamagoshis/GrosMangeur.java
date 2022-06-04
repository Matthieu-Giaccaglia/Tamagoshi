package tamagoshi.tamagoshis;

import java.util.Locale;

/**
 * GrosMangeur hérite de Tamagoshi
 * et a la particularité de manger plus que les Tamagoshis normaux.
 */
public class GrosMangeur extends Tamagoshi{

    /**
     * @param nom Nom du GrosMangeur.
     */
    public GrosMangeur(String nom) {
        super(nom);
    }

    /**
     * @param nom Nom du GrosMangeur.
     * @param locale Langue du GrosMangeur.
     */
    public GrosMangeur(String nom, Locale locale) {
        super(nom, locale);
    }

    /**
     * Consomme 2 point d'énergie si l'énergie != 0
     * @return true si Tamagoshi est toujours en vie, false sinon.
     */
    @Override
    public boolean consommeEnergie() {

        energie--;
        return super.consommeEnergie();
    }
}
