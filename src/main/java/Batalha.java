import java.util.ArrayList;

public class Batalha {
    private Treinador jogador;
    private Treinador cpu;
    private EstrategiaDeAtaque estrategiaCpu;
    private ArrayList<String> log = new ArrayList<>();

    public Batalha(Treinador jogador, Treinador cpu, EstrategiaDeAtaque estrategiaCpu) {
        this.jogador = jogador;
        this.cpu = cpu;
        this.estrategiaCpu = estrategiaCpu;
    }

    // devolve só as linhas desse turno, pra tela mostrar
    public ArrayList<String> executarTurno(Ataque escolhaDoJogador) {
        ArrayList<String> linhas = new ArrayList<>();

        if (terminou()) {
            return linhas;
        }

        Pokemon pokemonJogador = jogador.getTime().getAtivo();
        Pokemon pokemonCpu = cpu.getTime().getAtivo();
        Ataque escolhaDaCpu = estrategiaCpu.escolher(pokemonCpu, pokemonJogador);

        // o mais rápido ataca primeiro, no empate vai o jogador
        if (pokemonJogador.getStatus().getVelocidade() >= pokemonCpu.getStatus().getVelocidade()) {
            atacar(pokemonJogador, escolhaDoJogador, pokemonCpu, cpu, linhas);
            if (!pokemonCpu.estaDesmaiado()) {
                atacar(pokemonCpu, escolhaDaCpu, pokemonJogador, jogador, linhas);
            }
        } else {
            atacar(pokemonCpu, escolhaDaCpu, pokemonJogador, jogador, linhas);
            if (!pokemonJogador.estaDesmaiado()) {
                atacar(pokemonJogador, escolhaDoJogador, pokemonCpu, cpu, linhas);
            }
        }

        for (String linha : linhas) {
            log.add(linha);
        }
        return linhas;
    }

    private void atacar(Pokemon atacante, Ataque ataque, Pokemon alvo, Treinador donoDoAlvo, ArrayList<String> linhas) {
        int dano = ataque.calcularDano(atacante, alvo);
        alvo.receberDano(dano);
        linhas.add(atacante.getNome() + " usou " + ataque.getNome() + " e causou " + dano + " de dano.");

        if (alvo.estaDesmaiado()) {
            linhas.add(alvo.getNome() + " desmaiou!");
            Pokemon proximo = donoDoAlvo.getTime().getAtivo();
            if (proximo != null) {
                linhas.add(donoDoAlvo.getNome() + " enviou " + proximo.getNome() + "!");
            }
        }
    }

    public boolean terminou() {
        return jogador.getTime().todosDesmaiados() || cpu.getTime().todosDesmaiados();
    }

    public Treinador vencedor() {
        if (!terminou()) {
            return null;
        }
        if (cpu.getTime().todosDesmaiados()) {
            return jogador;
        }
        return cpu;
    }

    public Treinador getJogador() {
        return jogador;
    }

    public Treinador getCpu() {
        return cpu;
    }

    public ArrayList<String> getLog() {
        return new ArrayList<>(log);
    }
}