package br.com.niltonjhons.cm.modelo;
import br.com.niltonjhons.cm.excecao.ExplosaoException;
import java.util.ArrayList;
import java.util.List;

public class Campo {
    private final int linha;
    private final int coluna;

    private boolean minado;  // Se há uma mina
    private boolean aberto;  // Se já foi aberto
    private boolean marcado; // Se há bandeira

    private List<Campo> vizinhos = new ArrayList<>();

    // Construtor que define a posição do campo (linha e coluna) no tabuleiro
    Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    // Adiciona um campo como vizinho, se estiver em posição válida ao redor (horizontal, vertical ou diagonal)
    // Retorna true se o vizinho foi adicionado com sucesso
    boolean adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = this.linha != vizinho.linha;
        boolean colunaDiferente = this.coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(this.linha - vizinho.linha);
        int deltaColuna = Math.abs(this.coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;


        if (deltaGeral == 1 && !diagonal) { // Vizinhos diretos (acima, abaixo, esquerda, direita)
            vizinhos.add(vizinho);
            return true;
        } else if (deltaGeral == 2 && diagonal) { // Vizinhos diagonais (cantos)
            vizinhos.add(vizinho);
            return true;
        } else { // Não é vizinho válido
            return false;
        }
    }

    /* Tenta abrir o campo atual
    Se estiver marcado ou já aberto, não faz nada
    Se estiver minado, lança exceção (ExplosaoException)
    Se a vizinhança for segura, abre recursivamente os vizinhos */
    boolean abrir() {
        if (!aberto && !marcado) {
            aberto = true;
            if (minado) {
                throw new ExplosaoException(); // Estoura o campo (fim de jogo)
            }
            if (vizinhancaSegura()) { // Abre todos os vizinhos automaticamente se não há minas ao redor
                vizinhos.forEach(v -> v.abrir());
            }
            return true;
        } else {
            return false;
        }
    }

    // Verifica se todos os vizinhos são seguros (nenhum minado)
    boolean vizinhancaSegura() {
        return vizinhos.stream()
                .noneMatch(v -> v.minado);
    }

    // Mina o campo atual (coloca uma bomba)
    void minar() {
        minado = true;
    }

    // Alterna a marcação do campo (como se o jogador marcasse ou desmarcasse uma bandeira)
    // Só é permitido se o campo ainda não estiver aberto
    void alternarMarcacao() {
        if (!aberto) {
            marcado = !marcado;
        }
    }

    // Retorna a quantidade de minas nos campos vizinhos
    long minasNaVizinhanca() {
        return vizinhos.stream()
                .filter(v -> v.minado)
                .count();
    }

    /* Verifica se o campo atingiu seu objetivo no jogo
    Isso ocorre se:
        o campo não tem mina e foi aberto
        ou se o campo tem mina e foi corretamente marcado */
    boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    // Reinicia o estado do campo (usado ao reiniciar o jogo)
    void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;
    }

    // Retorna uma representação visual do campo para ser exibida no terminal
    @Override
    public String toString() {
        if (marcado) {
            return "\u001B[1m\u001B[34mX\u001B[0m";
        } else if (aberto && minado) {
            return "\u001B[1m\u001B[31m*\u001B[0m";
        } else if (aberto && minasNaVizinhanca() > 0) {
            return Long.toString(minasNaVizinhanca());
        } else if (aberto) {
            return " ";
        } else {
            return "\u001B[33m?\u001B[0m";
        }
    }

    public boolean isMinado() {
        return minado;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public boolean isAberto() {
        return aberto;
    }
    public boolean isFechado() {
        return !isAberto();
    }

    public int getLinha() {
        return linha;
    }
    public int getColuna() {
        return coluna;
    }

    void setAberto(boolean aberto) {
        this.aberto = aberto;
    }
}
