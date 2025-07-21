package br.com.niltonjhons.cm.visao;
import br.com.niltonjhons.cm.excecao.EncerrarException;
import br.com.niltonjhons.cm.excecao.ExplosaoException;
import br.com.niltonjhons.cm.modelo.Tabuleiro;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class TabuleiroConsole {
    private Tabuleiro tabuleiro; // Instância do tabuleiro (modelo do jogo)
    private Scanner input = new Scanner(System.in); // Scanner para capturar a entrada do usuário via terminal

    // Construtor que recebe o tabuleiro e inicia o jogo imediatamente
    public TabuleiroConsole(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        executarJogo(); // Inicia o loop principal do jogo
    }

    // Método que controla o ciclo completo da aplicação: jogada + nova partida
    private void executarJogo() {
        try {
            while (true) {
                cicloDoJogo(); // Executa uma partida completa

                // Após o fim da partida, pergunta se o usuário deseja jogar novamente
                System.out.print("Outra partida? [S/N]: ");
                String opcao = input.nextLine();

                if ("N".equalsIgnoreCase(opcao)) {
                    throw new EncerrarException(); // Força a finalização do jogo
                } else {
                    tabuleiro.reiniciar(); // Reinicia o tabuleiro para uma nova partida
                    System.out.println("\u001B[32mReiniciando...\u001B[0m");
                    tabuleiro.apresentacao();// Reapresenta o cabeçalho do jogo
                }
            }
        } catch (EncerrarException e) {
            System.out.println(e.getMessage()); // Mensagem de encerramento customizada
        } catch (Exception e) {
            System.out.println("\u001B[1m\u001B[31mOs valores inseridos estão incorretos!\nEncerrando...\u001B[0m");
        } finally {
            input.close(); // Fecha o Scanner ao encerrar
        }
    }

    // Executa o ciclo interno da partida: enquanto não atingir o objetivo, solicita jogadas
    private void cicloDoJogo() {
        try {
            while(!tabuleiro.objetivoAlcancado()) {
                System.out.println(tabuleiro); // Imprime o estado atual do tabuleiro

                // Captura posição da jogada
                String digitado = capturarValorDigitado("Digite a posição da linha e coluna [L, C]: ");
                Iterator<Integer> posicao = Arrays.stream(digitado.split(","))
                        .map(e -> Integer.parseInt(e.trim()) - 1).iterator(); // Ajusta para índice base 0

                // Captura tipo de ação: abrir ou marcar
                digitado = capturarValorDigitado("1 - Abrir | 2 - (Des)Marcar: ");
                if("1".equals(digitado)) {
                    tabuleiro.abrir(posicao.next(), posicao.next()); // Abre o campo informado
                } else if ("2".equals(digitado)) {
                    tabuleiro.alternarMarcacao(posicao.next(), posicao.next()); // Marca ou desmarca o campo
                }
            }

            // Vitória: todos os campos foram resolvidos corretamente
            System.out.println(tabuleiro);
            System.out.println("""
                    \u001B[32m------------------
                    | \uD83C\uDFC6 Vitória \uD83C\uDFC6 |
                    ------------------\u001B[0m
                    """);
        } catch (ExplosaoException e) {
            // Derrota: o jogador abriu um campo com mina
            System.out.println(tabuleiro); // Mostra o tabuleiro final
            System.out.println(e.getMessage()); // Mensagem de explosão
        }
    }

    // Metodo utilitário para capturar entrada do usuário com uma mensagem
    // Se o usuário digitar "sair", o jogo termina imediatamente
    private String capturarValorDigitado(String texto) {
        System.out.print(texto);
        String interacao = input.nextLine();

        if("sair".equalsIgnoreCase(interacao)) {
            throw new EncerrarException(); // Finaliza o jogo se o usuário digitar "sair"
        }
        return interacao;
    }
}