package zzz404.safesql.dynamic;

import zzz404.safesql.SafeSqlException;

@SuppressWarnings("serial")
public class ScopeErrorException extends SafeSqlException {

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
