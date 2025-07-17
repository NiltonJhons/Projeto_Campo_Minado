package br.com.niltonjhons.cm;
import br.com.niltonjhons.cm.modelo.Tabuleiro;
import br.com.niltonjhons.cm.visao.TabuleiroConsole;

public class Aplicacao {
    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro(6, 6, 0);
        new TabuleiroConsole(tabuleiro);
    }
}
