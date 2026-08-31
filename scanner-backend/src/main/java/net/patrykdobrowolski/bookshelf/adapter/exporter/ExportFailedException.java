package net.patrykdobrowolski.bookshelf.adapter.exporter;

public class ExportFailedException extends Exception {

    public ExportFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
