package model;
public class Treinador {
    private String nome;
    private Time time;

    public Treinador(String nome) {
        this.nome = nome;
        this.time = new Time();
    }

    public String getNome() {
        return nome;
    }

    public Time getTime() {
        return time;
    }
}