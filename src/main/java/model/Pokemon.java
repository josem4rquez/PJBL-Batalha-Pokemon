package model;
import java.util.ArrayList;

public class Pokemon {
    private String nome;
    private ArrayList<String> tipos;
    private Status status;
    private ArrayList<Ataque> ataques;
    private String spriteUrl;
    private int hpAtual;

    public Pokemon(String nome, ArrayList<String> tipos, Status status, ArrayList<Ataque> ataques, String spriteUrl) {
        this.nome = nome;
        this.tipos = tipos;
        this.status = status;
        this.ataques = ataques;
        this.spriteUrl = spriteUrl;
        this.hpAtual = status.getHp();
    }

    // o HP só muda por aqui e nunca fica negativo
    public void receberDano(int dano) {
        if (dano <= 0) {
            return;
        }

        hpAtual -= dano;

        if (hpAtual < 0) {
            hpAtual = 0;
        }
    }

    public boolean estaDesmaiado() {
        return hpAtual == 0;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<String> getTipos() {
        return tipos;
    }

    public Status getStatus() {
        return status;
    }

    public ArrayList<Ataque> getAtaques() {
        return ataques;
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }

    public int getHpAtual() {
        return hpAtual;
    }
}