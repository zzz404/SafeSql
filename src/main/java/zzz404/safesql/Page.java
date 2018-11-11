package zzz404.safesql;

import java.util.List;

public class Page<T> {
    private int totalCount;
    private List<T> result;

    public Page(int totalCount, List<T> result) {
        this.totalCount = totalCount;
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<T> getResult() {
        return result;
    }

}
