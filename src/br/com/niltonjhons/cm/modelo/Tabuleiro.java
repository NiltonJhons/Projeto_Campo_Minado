package br.com.niltonjhons.cm.modelo;
import br.com.niltonjhons.cm.excecao.ExplosaoException;
import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {
    private int linhas;  // Quantidade de linhas do tabuleiro
    private int colunas; // Quantidade de colunas do tabuleiro
    private int minas;   // Quantidade total de minas no jogo

    private final List<Campo> campos = new ArrayList<>();

    // Construtor que inicializa o tabuleiro e seus componentes
    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        apresentacao();       // Exibe título do jogo
        gerarCampos();        // Cria os campos com base na dimensão
        associarVizinhos();   // Relaciona os campos entre si como vizinhos
        sortearMinas();       // Posiciona as minas aleatoriamente
    }

    // Responsável por exibir o título do jogo no terminal
    public void apresentacao() {
        System.out.println("""
                \u001B[31m--------------------------
                |\u001B[34m   💣 Campo Minado 💣  \u001B[31m|
                --------------------------\u001B[0m""");
    }

    // Tenta abrir um campo na posição especificada
    // Se houver uma explosão, abre todos os campos e relança a exceção
    public void abrir(int linha, int coluna) {
        try {
            campos.parallelStream()
                    .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                    .findFirst()
                    .ifPresent(Campo::abrir);
        } catch (ExplosaoException e) {
            campos.forEach(c -> c.setAberto(true)); // Revela todos os campos
            throw e; // Propaga a exceção para controle externo (fim do jogo)
        }
    }

    // Cria todos os campos com base na quantidade de linhas e colunas
    void gerarCampos() {
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                campos.add(new Campo(linha, coluna));
            }
        }
    }

    // Associa cada campo aos seus vizinhos
    private void associarVizinhos() {
        for (Campo c1 : campos) {
            for (Campo c2 : campos) {
                c1.adicionarVizinho(c2);
            }
        }
    }

    // Sorteia minas aleatoriamente nos campos até atingir a quantidade desejada
    private void sortearMinas() {
        long minasArmadas;
        do {
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar(); // Pode minar mais de uma vez o mesmo campo (ineficiente mas funcional)
            minasArmadas = campos.stream().filter(Campo::isMinado).count();
        } while (minasArmadas < minas); // Repete até atingir o número total de minas
    }

    // Alterna a marcação (bandeira) de um campo na posição especificada
    public void alternarMarcacao(int linha, int coluna) {
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(Campo::alternarMarcacao);
    }

    // Verifica se todos os campos atingiram o objetivo do jogo
    // Isto é, os campos minados foram marcados, e os demais foram abertos
    public boolean objetivoAlcancado() {
        return campos.stream()
                .allMatch(Campo::objetivoAlcancado); // c -> c.objetivoAlcancado()
    }

    // Reinicia o tabuleiro para uma nova partida
    public void reiniciar() {
        campos.forEach(Campo::reiniciar); // Limpa os estados dos campos
        sortearMinas(); // Rearma as minas aleatoriamente
    }

    // Retorna uma representação em texto do tabuleiro para exibição no terminal
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  "); // Espaço inicial para cabeçalho de colunas
        for (int c = 1; c <= colunas; c++) {
            sb.append(" ");
            sb.append(c);
            sb.append(" ");
        }

        sb.append("\n");

        int i = 0; // Índice para acessar os campos sequencialmente
        for (int l = 0; l < linhas; l++) {
            sb.append(l + 1); // Número da linha
            sb.append(" ");
            for (int c = 0; c < colunas; c++) {
                sb.append("[\u001B[35m") // Cor magenta para o conteúdo do campo
                        .append(campos.get(i).toString())
                        .append("\u001B[0m]");
                i++;
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Getter da lista de campos (caso outras classes precisem acessar diretamente)
    public List<Campo> getCampos() {
        return campos;
    }
}
