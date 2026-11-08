public abstract class Ataque {
    private String nome;
    private int poder;
    private String tipo;

    public Ataque(String nome, int poder, String tipo) {
        this.nome = nome;
        this.poder = poder;
        this.tipo = tipo;
    }

    // cada tipo de ataque calcula o dano do seu jeito
    public abstract int calcularDano(Pokemon atacante, Pokemon alvo);

    // se o alvo tiver dois tipos, os multiplicadores se acumulam
    protected double efetividadeContra(Pokemon alvo) {
        double efetividade = 1.0;
        for (String tipoAlvo : alvo.getTipos()) {
            efetividade = efetividade * multiplicador(tipoAlvo);
        }
        return efetividade;
    }

    // fire > grass, grass > water, water > fire
    private double multiplicador(String tipoAlvo) {
        if ((tipo.equals("fire") && tipoAlvo.equals("grass"))
                || (tipo.equals("grass") && tipoAlvo.equals("water"))
                || (tipo.equals("water") && tipoAlvo.equals("fire"))) {
            return 2.0;
        } else if ((tipo.equals("grass") && tipoAlvo.equals("fire"))
                || (tipo.equals("water") && tipoAlvo.equals("grass"))
                || (tipo.equals("fire") && tipoAlvo.equals("water"))) {
            return 0.5;
        }
        return 1.0;
    }

    public String descricao() {
        return nome + " | " + tipo + " | poder " + poder;
    }

    public String getNome() {
        return nome;
    }

    public int getPoder() {
        return poder;
    }

    public String getTipo() {
        return tipo;
    }
}