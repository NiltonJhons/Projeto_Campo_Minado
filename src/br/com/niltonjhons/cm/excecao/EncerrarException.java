package br.com.niltonjhons.cm.excecao;

import java.io.Serial;

public class EncerrarException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    @Override
    public String getMessage() {
        return "\u001B[34mEncerrando... Até mais!\u001B[0m";
    }
}
