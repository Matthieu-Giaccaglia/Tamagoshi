package tamagoshi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * La classe RandomNames peut être utilisé afin de récupérer un nom aléatoire pré-définie.
 */
public class RandomNames {

    /**
     * Pour éviter de créer des instances d'une classe utils.
     */
    private RandomNames() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Random pour prendre des noms aléatoires dans la liste namesList.
     */
    private static final Random random = new Random();

    /**
     * Liste de noms aléatoires.
     */
    private static final String[] namesList = {
            "Robert",
            "Philippe",
            "Justin",
            "Paul",
            "Matthieu",
            "Julie",
            "Astérix",
            "Obélix",
            "Tintin",
            "Ilyes",
            "Mateusz",
            "Florian",
            "Walter",
            "Alexandre",
            "Hamza",
            "Leila",
            "Luke",
            "Yoda",
            "Altaïr",
            "Glados",
            "Buzz",
            "Woody",
            "Vi",
            "Jinx"
    };

    /**
     * Retourne un nom aléatoire parmis la liste pré-définie.
     * @param nombreDeNoms Nombre de noms aléatoires à renvoyer.
     * @return un nom aléatoire.
     */
    public static List<String> getRandomNames(int nombreDeNoms){


        List<String> copieListeNoms = new ArrayList<>(List.of(namesList));
        List<String> retourListeNoms = new ArrayList<>();


        for (int i = 0; i < nombreDeNoms; i++) {

            int index = random.nextInt(copieListeNoms.size());
            retourListeNoms.add(copieListeNoms.get(index));
            copieListeNoms.remove(index);
        }

        return retourListeNoms;
    }
}
