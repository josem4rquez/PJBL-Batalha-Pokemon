package model;
public class AtaqueFisico extends Ataque {

    public AtaqueFisico(String nome, int poder, String tipo) {
        super(nome, poder, tipo);
    }

    @Override
    public int calcularDano(Pokemon atacante, Pokemon alvo) {
        double razao = (double) atacante.getStatus().getAtaque() / alvo.getStatus().getDefesa();
        double dano = (getPoder() * razao / 5 + 2) * efetividadeContra(alvo);
        return (int) dano;
    }
}