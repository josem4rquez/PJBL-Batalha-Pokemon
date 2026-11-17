package model;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// vitórias e derrotas do jogador, salvas no placar.dat
public class Placar implements Serializable {
    private int vitorias;
    private int derrotas;

    public void registrar(boolean venceu) {
        if (venceu) {
            vitorias++;
        } else {
            derrotas++;
        }
    }

    public void salvar() {
        try {
            ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream("placar.dat"));
            saida.writeObject(this);
            saida.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar o placar.");
        }
    }

    // na primeira partida o arquivo ainda não existe, então começa zerado
    public static Placar carregar() {
        try {
            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream("placar.dat"));
            Placar placar = (Placar) entrada.readObject();
            entrada.close();
            return placar;
        } catch (IOException | ClassNotFoundException e) {
            return new Placar();
        }
    }

    public int getVitorias() {
        return vitorias;
    }

    public int getDerrotas() {
        return derrotas;
    }
}