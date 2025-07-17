package br.com.niltonjhons.cm.modelo;
import br.com.niltonjhons.cm.excecao.ExplosaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeralTeste {
    private Campo campo;
    private Tabuleiro tabuleiro;

    @BeforeEach
    void iniciarJogo() {
        campo = new Campo(3, 3);
    }

    @Test
    void testeVizinhoDistancia1Esquerda() {
        Campo vizinho = new Campo(3, 2);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void testeVizinhoDistancia1Direita() {
        Campo vizinho = new Campo(3, 4);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }
    @Test
    void testeVizinhoDistancia1Cima() {
        Campo vizinho = new Campo(2, 3);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }
    @Test
    void testeVizinhoDistancia1Baixo() {
        Campo vizinho = new Campo(4, 3);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void testeVizinhoDistancia2() {
        Campo vizinho = new Campo(2, 2);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void testeNaoVizinho() {
        Campo vizinho = new Campo(1, 1);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertFalse(resultado);
    }

    @Test
    void testeValorPadraoAtributoMarcado() {
        assertFalse(campo.isMarcado());
    }

    @Test
    void testeAlternarMarcacaoDuasChamadas() {
        campo.alternarMarcacao();
        campo.alternarMarcacao();
        assertFalse(campo.isMarcado());
    }

    @Test
    void testeAbrirCampoNaoMinadoNaoMarcado() { // Campo não minado e não marcado
        assertTrue(campo.abrir());
    }

    @Test
    void testeAbrirCampoNaoMinadoSimMarcado() { // Campo não minado, mas marcado
        campo.alternarMarcacao();
        assertFalse(campo.abrir());
    }

    @Test
    void testeAbrirCampoSimMinadoSimMarcado() { // Campo minado e marcado
        campo.alternarMarcacao();
        campo.minar();
        assertFalse(campo.abrir());
    }

    @Test
    void testeAbrirCampoSimMinadoNaoMarcado() { // Campo minado, mas não marcado
        campo.minar();
        assertThrows(ExplosaoException.class, () -> campo.abrir()); // Garante que o metodo gerou uma exceção
    }

    @Test
    void testeAbrirComVizinho1() {
        Campo campo11 = new Campo(1, 1);
        Campo campo22 = new Campo(2, 2);

        campo22.adicionarVizinho(campo11);
        campo.adicionarVizinho(campo22);
        campo.abrir();

        assertTrue(campo11.isAberto() && campo22.isAberto());
    }

    @Test
    void testeAbrirComVizinho2() {
        Campo campo11 = new Campo(1, 1);
        Campo campo12 = new Campo(1, 2);
        Campo campo22 = new Campo(2, 2);

        campo12.minar();

        campo22.adicionarVizinho(campo11);
        campo22.adicionarVizinho(campo12);
        campo.adicionarVizinho(campo22);
        campo.abrir();

        assertTrue(campo11.isFechado() && campo22.isAberto());
    }

    @Test
    void reinicio() {
        campo.alternarMarcacao();
        campo.minar();
        campo.reiniciar();
        assertFalse(campo.abrir() && campo.isMarcado() && campo.isMinado());
    }

    @Test
    void testeGerarCampo() {
        Tabuleiro tabuleiro = new Tabuleiro(10, 10, 0);
        int totalDeCamposEsperado = 100;
        assertEquals(totalDeCamposEsperado, tabuleiro.getCampos().size());
    }

    @Test
    void reinicioComSorteioDeMinas() {
        Tabuleiro tabuleiro = new Tabuleiro(10, 10, 10);

        long totalMinas = tabuleiro.getCampos().stream()
                .filter(Campo::isMinado)
                .count();

        assertEquals(11, totalMinas); // Gera sempre uma mina a mais devido o do-while
    }
}
