package zzz404.safesql;

public class ScopeErrorException extends RuntimeException {

    private static final long serialVersionUID = 363254132009013789L;

    private String command;
    private Scope errorScope;

    public ScopeErrorException(String command, Scope errorScope) {
        this.command = command;
        this.errorScope = errorScope;
    }

    @Override
    public String getMessage() {
        return String.format("'%s()' can not be called under the '%s' scope.", command, errorScope.name());
    }

}
