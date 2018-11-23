package zzz404.safesql;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Scope {
    select("field"), where("cond", "innerJoin"), groupBy, orderBy("asc", "desc");

    private Set<String> acceptedCommands;

    private Scope(String... commands) {
        this.acceptedCommands = new HashSet<>(Arrays.asList(commands));
    }

    public void checkCommand(String command) {
        if (!acceptedCommands.contains(command)) {
            throw new ScopeErrorException("cond", this);
        }
    }
}
