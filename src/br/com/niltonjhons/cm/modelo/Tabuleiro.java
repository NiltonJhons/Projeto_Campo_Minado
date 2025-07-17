package br.com.niltonjhons.cm.modelo;
import br.com.niltonjhons.cm.excecao.ExplosaoException;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {
    private int linhas;
    private int colunas;
    private int minas;

    private final List<Campo> campos = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        apresentacao();
        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    public void apresentacao() {
        System.out.println("""
                \u001B[31m--------------------------
                |\u001B[34m   💣 Campo Minado 💣  \u001B[31m|
                --------------------------\u001B[0m""");
    }

    public void abrir(int linha, int coluna) {
        try {
            campos.parallelStream()
                    .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                    .findFirst()
                    .ifPresent(Campo::abrir);
        } catch (ExplosaoException e) {
            campos.forEach(c -> c.setAberto(true));
            throw e;
        }
    }

    public void alternarMarcacao(int linha, int coluna) {
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(Campo::alternarMarcacao);
    }

    void gerarCampos() {
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                campos.add(new Campo(linha, coluna));
            }
        }
    }

    private void associarVizinhos() {
        for (Campo c1 : campos) {
            for (Campo c2 : campos) {
                c1.adicionarVizinho(c2);
            }
        }
    }

    private void sortearMinas() {
        long minasArmadas;
        do {
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
            minasArmadas = campos.stream().filter(Campo::isMinado).count(); // c -> c.isMinado()
        } while(minasArmadas < minas);
    }

    public boolean objetivoAlcancado() {
        return campos.stream()
                .allMatch(Campo::objetivoAlcancado); // c -> c.objetivoAlcancado()
    }

    public void reiniciar() {
        campos.forEach(Campo::reiniciar);
        sortearMinas();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  ");
        for (int c = 1; c <= colunas; c++) {
            sb.append(" ");
            sb.append(c);
            sb.append(" ");
        }

        sb.append("\n");

        int i = 0;
        for (int l = 0; l < linhas; l++) {
            sb.append(l + 1);
            sb.append(" ");
            for (int c = 0; c < colunas; c++) {
                sb.append("[\u001B[35m")
                        .append(campos.get(i).toString())
                        .append("\u001B[0m]");
                i++;
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    public List<Campo> getCampos() {
        return campos;
    }
}
