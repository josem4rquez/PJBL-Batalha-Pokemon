import java.util.ArrayList;
import java.util.Random;

public class EstrategiaAleatoria implements EstrategiaDeAtaque {
    private Random random = new Random();

    @Override
    public Ataque escolher(Pokemon meu, Pokemon alvo) {
        ArrayList<Ataque> ataques = meu.getAtaques();
        int indice = random.nextInt(ataques.size());
        return ataques.get(indice);
    }
}