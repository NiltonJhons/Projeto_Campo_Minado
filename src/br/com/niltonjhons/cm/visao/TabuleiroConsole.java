package br.com.niltonjhons.cm.visao;
import br.com.niltonjhons.cm.excecao.EncerrarException;
import br.com.niltonjhons.cm.excecao.ExplosaoException;
import br.com.niltonjhons.cm.modelo.Tabuleiro;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class TabuleiroConsole {
    private Tabuleiro tabuleiro;
    private Scanner input = new Scanner(System.in);

    public TabuleiroConsole(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;

        executarJogo();
    }

    private void executarJogo() {
        try {
            while (true) {
                cicloDoJogo();
                System.out.print("Outra partida? [S/N]: ");
                String opcao = input.nextLine();
                if ("N".equalsIgnoreCase(opcao)) {
                    throw new EncerrarException();
                } else {
                    tabuleiro.reiniciar();
                    System.out.println("\u001B[32mReiniciando...\u001B[0m");
                    tabuleiro.apresentacao();
                }
            }
        } catch (EncerrarException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("\u001B[1m\u001B[31mOs valores inseridos estão incorretos!\nEncerrando...\u001B[0m");
        } finally {
            input.close();
        }
    }

    private void cicloDoJogo() {
        try {
            while(!tabuleiro.objetivoAlcancado()) {
                System.out.println(tabuleiro);
                String digitado = capturarValorDigitado("Digite a posição da linha e coluna [L, C]: ");
                Iterator<Integer> posicao = Arrays.stream(digitado.split(","))
                        .map(e -> Integer.parseInt(e.trim()) - 1).iterator();

                digitado = capturarValorDigitado("1 - Abrir | 2 - (Des)Marcar: ");
                if("1".equals(digitado)) {
                    tabuleiro.abrir(posicao.next(), posicao.next());
                } else if ("2".equals(digitado)) {
                    tabuleiro.alternarMarcacao(posicao.next(), posicao.next());
                }
            }
            System.out.println(tabuleiro);
            System.out.println("""
                    \u001B[32m------------------
                    | \uD83C\uDFC6 Vitória \uD83C\uDFC6 |
                    ------------------\u001B[0m
                    """);
        } catch (ExplosaoException e) {
            System.out.println(tabuleiro);
            System.out.println(e.getMessage());
        }
    }

    private String capturarValorDigitado(String texto) {
        System.out.print(texto);
        String interacao = input.nextLine();

        if("sair".equalsIgnoreCase(interacao)) {
            throw new EncerrarException();
        }
        return interacao;
    }
}