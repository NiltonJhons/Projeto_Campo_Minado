package br.com.niltonjhons.cm.modelo;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TabuleiroTeste {

    @Test
    void gerarCampos() {
        Tabuleiro tabuleiro = new Tabuleiro(5, 5, 0);
        assertEquals(25, tabuleiro.getCampos().size());
    }

    @Test
    void sortearMinas() {
        Tabuleiro tabuleiro = new Tabuleiro(10, 10, 10);
        long minas = tabuleiro.getCampos().stream().filter(Campo::isMinado).count();
        assertEquals(10, minas);
    }

    @Test
    void objetivoNaoAlcancadoAoIniciar() {
        Tabuleiro tabuleiro = new Tabuleiro(5, 5, 3);
        assertFalse(tabuleiro.objetivoAlcancado());
    }

    @Test
    void reiniciarTabuleiro() {
        Tabuleiro tabuleiro = new Tabuleiro(6, 6, 5);
        tabuleiro.reiniciar();

        long minas = tabuleiro.getCampos().stream()
                .filter(Campo::isMinado)
                .count();

        assertEquals(5, minas);
    }

    @Test
    void abrirCampoNoTabuleiro() {
        Tabuleiro tabuleiro = new Tabuleiro(3, 3, 0);
        tabuleiro.abrir(1, 1);

        long abertos = tabuleiro.getCampos().stream()
                .filter(Campo::isAberto)
                .count();

        assertTrue(abertos > 0);
    }

    @Test
    void marcarCampoNoTabuleiro() {
        Tabuleiro tabuleiro = new Tabuleiro(3, 3, 0);
        tabuleiro.alternarMarcacao(0, 0);

        boolean marcado = tabuleiro.getCampos().stream()
                .anyMatch(Campo::isMarcado);

        assertTrue(marcado);
    }

    @Test
    void toStringNaoEhVazio() {
        Tabuleiro tabuleiro = new Tabuleiro(3, 3, 0);
        String representacao = tabuleiro.toString();

        assertTrue(representacao.contains("["));
        assertTrue(representacao.contains("?")); // Campo fechado
        assertTrue(representacao.contains("1 ") || representacao.contains("2 ") || representacao.contains("3 "));
    }
}

