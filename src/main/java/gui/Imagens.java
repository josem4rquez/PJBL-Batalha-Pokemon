package gui;

import java.awt.Image;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

// Baixa o sprite (imagem) do Pokémon pela URL e guarda num Map,
// para não baixar a mesma imagem de novo a cada atualização da tela.
public class Imagens {

    private static final Map<String, ImageIcon> cache = new HashMap<>();

    private Imagens() {
    }

    // devolve null se a URL for vazia, inválida ou a imagem não carregar
    public static ImageIcon sprite(String url, int tamanho) {
        String chave = url + "@" + tamanho;
        if (cache.containsKey(chave)) {
            return cache.get(chave);
        }
        try {
            ImageIcon original = new ImageIcon(URI.create(url).toURL());
            if (original.getIconWidth() <= 0) {
                return null;                        // não carregou (sem internet, por exemplo)
            }
            Image ampliada = original.getImage().getScaledInstance(tamanho, tamanho, Image.SCALE_FAST);
            ImageIcon icone = new ImageIcon(ampliada);
            cache.put(chave, icone);
            return icone;
        } catch (Exception e) {
            return null;
        }
    }
}
