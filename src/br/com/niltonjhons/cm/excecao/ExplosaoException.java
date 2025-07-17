package br.com.niltonjhons.cm.excecao;
import java.io.Serial;

public class ExplosaoException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return """
                ------------------
                | \u001B[1m\u001B[31m  BUUUMMM! \uD83D\uDCA3\u001B[0m |
                | ☠\uFE0FGAME OVER☠\uFE0F |
                ------------------
                """;
    }
}
