public class AtaqueEspecial extends Ataque {

    public AtaqueEspecial(String nome, int poder, String tipo) {
        super(nome, poder, tipo);
    }

    @Override
    public int calcularDano(Pokemon atacante, Pokemon alvo) {
        double razao = (double) atacante.getStatus().getAtaqueEspecial() / alvo.getStatus().getDefesaEspecial();
        double dano = (getPoder() * razao / 5 + 2) * efetividadeContra(alvo);
        return (int) dano;
    }

    @Override
    public String descricao() {
        return super.descricao() + " | especial";
    }
}