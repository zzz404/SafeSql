package zzz404.safesql.dynamic;

public class ScopeErrorException extends RuntimeException {

    private static final long serialVersionUID = 363254132009013789L;

    private String command;
    private Scope scope;

    public ScopeErrorException(String command, Scope scope) {
        this.command = command;
        this.scope = scope;
    }

    @Override
    public String getMessage() {
        return String.format("'%s()' can not be called under the '%s' scope.", command, scope.name());
    }

}
