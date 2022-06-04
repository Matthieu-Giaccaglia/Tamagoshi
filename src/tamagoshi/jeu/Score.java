package tamagoshi.jeu;

/**
 * Pour stocker les scores plus facilement.
 */
public class Score {

    /**
     * Score obtenu.
     */
    private final int score;

    /**
     * Nom du joueur.
     */
    private final String name;

    /**
     * @param score Le score obtenu.
     * @param name Le nom du joueur.
     */
    public Score(int score, String name) {
        this.score = score;
        this.name = name;
    }

    /**
     * @return Nom du joueur.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Score obtenu.
     */
    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Score{" +
                "score=" + score +
                ", name='" + name + '\'' +
                '}';
    }
}
