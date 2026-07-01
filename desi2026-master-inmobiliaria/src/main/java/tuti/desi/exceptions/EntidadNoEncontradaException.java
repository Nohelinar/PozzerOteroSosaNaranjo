package tuti.desi.exceptions;

public class EntidadNoEncontradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntidadNoEncontradaException(String entidad, Object id) {
        super("No existe " + entidad + " con id=" + id);
    }
}