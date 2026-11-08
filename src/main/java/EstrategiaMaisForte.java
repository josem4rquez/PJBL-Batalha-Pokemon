public class EstrategiaMaisForte implements EstrategiaDeAtaque {

    // testa todos os ataques e fica com o que causa mais dano no alvo
    @Override
    public Ataque escolher(Pokemon meu, Pokemon alvo) {
        Ataque melhor = null;
        int maiorDano = -1;

        for (Ataque ataque : meu.getAtaques()) {
            int dano = ataque.calcularDano(meu, alvo);
            if (dano > maiorDano) {
                maiorDano = dano;
                melhor = ataque;
            }
        }
        return melhor;
    }
}