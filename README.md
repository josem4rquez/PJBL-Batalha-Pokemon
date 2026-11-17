# Batalha Pokémon

Projeto final da disciplina de Programação Orientada a Objetos (PUCPR): um jogo de batalha por turnos entre o jogador e a CPU, em Java 17 com interface gráfica Swing e dados buscados na [PokéAPI](https://pokeapi.co).

## Problema

O jogador monta um time de 3 Pokémon buscando pelo nome, escolhe a dificuldade da CPU e batalha por turnos. Vence quem derrubar os 3 Pokémon do adversário.

## Funcionalidades

- Busca de Pokémon pelo nome na PokéAPI, com nome, tipos, status, sprite e 4 ataques
- Montagem do time do jogador, com até 3 Pokémon
- Time da CPU sorteado entre 14 Pokémon dos tipos fogo, água e planta
- Duas dificuldades: fácil (a CPU escolhe um ataque aleatório) e difícil (a CPU escolhe o ataque que causa mais dano)
- Batalha por turnos: o Pokémon mais rápido ataca primeiro, e no empate ataca o jogador
- Troca automática quando um Pokémon desmaia
- Ataques físicos e especiais, cada um com seu cálculo de dano
- Efetividade entre fogo, água e planta: dano dobrado na vantagem e pela metade na desvantagem
- Tela de batalha com sprites, barras de HP, os 4 ataques como botões e log dos turnos
- Botão de nova partida
- Mensagens na tela para Pokémon inexistente, time cheio e falta de conexão

## Como rodar

Pré-requisitos: JDK 17 ou superior, Maven, conexão com a internet e uma IDE Java (VS Code com o Extension Pack for Java, ou IntelliJ).

1. Clone o repositório:
   ```
   git clone https://github.com/josem4rquez/PJBL-Batalha-Pokemon.git
   ```
2. Abra a pasta do projeto na IDE e espere o Maven baixar as dependências (Gson).
3. Rode a classe `JanelaPrincipal`, em `src/main/java/gui`.

## Como jogar

1. Digite o nome de um Pokémon em inglês (ex.: `charizard`) e clique em buscar. Repita até ter 3.
2. Escolha a dificuldade e clique em iniciar.
3. Na batalha, clique em um dos 4 ataques. Cada clique é um turno.
4. Quando um lado ficar sem Pokémon de pé, aparece o vencedor. Use o botão de nova partida para jogar de novo.

A primeira busca de cada Pokémon pode levar alguns segundos, porque cada ataque exige uma consulta à API. Buscas repetidas são instantâneas.

## Integrantes

| Integrante | Responsabilidade |
|---|---|
| EduardoLopessz | Pacote `model`: regras do jogo e batalha |
| José Marques | Pacote `api`: integração com a PokéAPI, e README |
| GabrielMenSilva | Pacote `gui`: interface gráfica |

## Estrutura

| Pacote | Classes | Responsabilidade |
|---|---|---|
| `model` | `Pokemon`, `Status`, `Ataque`, `AtaqueFisico`, `AtaqueEspecial`, `Time`, `Treinador`, `Batalha`, `EstrategiaDeAtaque`, `EstrategiaAleatoria`, `EstrategiaMaisForte`, `TimeCheioException` | Regras do jogo |
| `api` | `FontePokemon`, `FontePokeApi`, `PokeApiClient`, `PokemonMapper`, `PokemonNaoEncontradoException`, `ApiIndisponivelException` | Buscar os dados na PokéAPI e transformar em objetos do jogo |
| `gui` | `JanelaPrincipal`, `TelaMontarTime`, `TelaBatalha`, `PainelPokemon`, `Imagens` | Interface gráfica |

## Decisões de modelagem

| Conceito | Onde | Por quê |
|---|---|---|
| Encapsulamento | `Pokemon.hpAtual`, `Time.adicionar()`, atributos `private final` | O HP só muda por `receberDano()` e nunca fica negativo; o time nunca passa de 3 |
| Herança | `AtaqueFisico` e `AtaqueEspecial` estendem `Ataque`; as telas estendem `JPanel` | Físico e especial são dois jeitos diferentes de calcular o mesmo dano |
| Classe abstrata | `Ataque` | Todo ataque tem nome, poder e tipo em comum, e a efetividade é calculada igual; só o dano muda |
| Interface | `EstrategiaDeAtaque`, `FontePokemon` | São só comportamentos, sem dados: a batalha e as telas usam sem saber qual classe está por trás |
| Polimorfismo | `calcularDano()`, `escolher()`, `buscar()` | A `Batalha` recebe a estratégia da CPU e uma lista de `Ataque` sem saber qual implementação veio |
| Coleções | `ArrayList` nos times, tipos e ataques; `HashMap` nos caches e na leitura dos status | Lista quando a ordem importa, mapa para achar por nome |
| Associação | `Batalha` → `Treinador` → `Time` → `Pokemon` → `Ataque` | Os objetos colaboram em vez de uma classe fazer tudo |
| Exceções | `TimeCheioException`, `PokemonNaoEncontradoException`, `ApiIndisponivelException` | Erros esperados viram mensagens na tela, não travamentos |

Outras decisões:

- **4 ataques obrigatórios.** Só entram ataques com poder e de classe física ou especial. Pokémon que não têm 4 ataques assim, como o Ditto, são recusados com uma mensagem, para a tela sempre ter 4 botões.
- **Cache com cópias.** A `FontePokeApi` guarda cada Pokémon já buscado para não repetir as consultas, mas devolve sempre uma cópia nova. Sem isso, o mesmo Pokémon nos dois times compartilharia o HP.
- **Tipos como texto.** Os tipos ficam como `String`, no formato da API (`fire`, `water`, `grass`), e são comparados diretamente no cálculo de efetividade.
- **Alternativa descartada:** criar `PokemonFogo`, `PokemonAgua` etc. por herança. Um Pokémon pode ter dois tipos, e o tipo é um dado, não um comportamento diferente.
