package br.com.niltonjhons.cm.modelo;
import static org.junit.jupiter.api.Assertions.*;
import br.com.niltonjhons.cm.excecao.ExplosaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CampoTeste {

    private Campo campo;

    @BeforeEach
    void iniciar() {
        campo = new Campo(3, 3);
    }

    @Test
    void vizinhoDistancia1Esquerda() {
        Campo vizinho = new Campo(3, 2);
        assertTrue(campo.adicionarVizinho(vizinho));
    }

    @Test
    void vizinhoDistancia1Direita() {
        Campo vizinho = new Campo(3, 4);
        assertTrue(campo.adicionarVizinho(vizinho));
    }

    @Test
    void vizinhoDistancia1Cima() {
        Campo vizinho = new Campo(2, 3);
        assertTrue(campo.adicionarVizinho(vizinho));
    }

    @Test
    void vizinhoDistancia1Baixo() {
        Campo vizinho = new Campo(4, 3);
        assertTrue(campo.adicionarVizinho(vizinho));
    }

    @Test
    void vizinhoDiagonalDistancia2() {
        Campo vizinho = new Campo(2, 2);
        assertTrue(campo.adicionarVizinho(vizinho));
    }

    @Test
    void naoVizinho() {
        Campo vizinho = new Campo(1, 1);
        assertFalse(campo.adicionarVizinho(vizinho));
    }

    @Test
    void contarMinasNaVizinhanca() {
        Campo viz1 = new Campo(2, 2);
        Campo viz2 = new Campo(2, 3);
        Campo viz3 = new Campo(2, 4);

        viz1.minar();
        viz3.minar();

        campo.adicionarVizinho(viz1);
        campo.adicionarVizinho(viz2);
        campo.adicionarVizinho(viz3);

        assertEquals(2, campo.minasNaVizinhanca());
    }

    @Test
    void valorPadraoMarcado() {
        assertFalse(campo.isMarcado());
    }

    @Test
    void alternarMarcacaoDuasVezes() {
        campo.alternarMarcacao();
        campo.alternarMarcacao();
        assertFalse(campo.isMarcado());
    }

    @Test
    void abrirCampoNaoMinadoNaoMarcado() {
        assertTrue(campo.abrir());
    }

    @Test
    void abrirCampoNaoMinadoMarcado() {
        campo.alternarMarcacao();
        assertFalse(campo.abrir());
    }

    @Test
    void abrirCampoMinadoMarcado() {
        campo.alternarMarcacao();
        campo.minar();
        assertFalse(campo.abrir());
    }

    @Test
    void abrirCampoMinadoNaoMarcado() {
        campo.minar();
        assertThrows(ExplosaoException.class, () -> campo.abrir());
    }

    @Test
    void abrirComVizinhoSeguro() {
        Campo viz1 = new Campo(2, 2);
        Campo viz2 = new Campo(1, 1);

        viz1.adicionarVizinho(viz2);
        campo.adicionarVizinho(viz1);

        campo.abrir();

        assertTrue(viz1.isAberto());
        assertTrue(viz2.isAberto());
    }

    @Test
    void abrirComVizinhoMinado() {
        Campo viz1 = new Campo(2, 2);
        Campo viz2 = new Campo(2, 3);
        viz2.minar();

        viz1.adicionarVizinho(viz2);
        campo.adicionarVizinho(viz1);

        campo.abrir();

        assertTrue(viz1.isAberto());
        assertFalse(viz2.isAberto());
    }

    @Test
    void reiniciarCampo() {
        campo.minar();
        campo.alternarMarcacao();
        campo.abrir();
        campo.reiniciar();

        assertFalse(campo.isMinado());
        assertFalse(campo.isMarcado());
        assertFalse(campo.isAberto());
    }

    // Teste extra: campo objetivo alcançado
    @Test
    void objetivoAlcancadoDesvendado() {
        campo.abrir();
        assertTrue(campo.objetivoAlcancado());
    }

    @Test
    void objetivoAlcancadoProtegido() {
        campo.minar();
        campo.alternarMarcacao();
        assertTrue(campo.objetivoAlcancado());
    }

    @Test
    void toStringCampoMarcado() {
        campo.alternarMarcacao();
        assertEquals("\u001B[1m\u001B[34mX\u001B[0m", campo.toString());
    }

    @Test
    void toStringCampoAbertoMinado() {
        campo.minar();
        try {
            campo.abrir();
        } catch (ExplosaoException ignored) {}
        assertEquals("\u001B[1m\u001B[31m*\u001B[0m", campo.toString());
    }

    @Test
    void toStringCampoAbertoComMinasVizinhanca() {
        Campo viz = new Campo(2, 2);
        viz.minar();
        campo.adicionarVizinho(viz);
        campo.abrir();
        assertEquals("1", campo.toString());
    }

    @Test
    void toStringCampoAbertoSeguro() {
        campo.abrir();
        assertEquals(" ", campo.toString());
    }

    @Test
    void toStringCampoFechado() {
        assertEquals("\u001B[33m?\u001B[0m", campo.toString());
    }
}

