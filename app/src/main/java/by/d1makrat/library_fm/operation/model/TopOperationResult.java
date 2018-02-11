package by.d1makrat.library_fm.operation.model;

import java.util.List;

public class TopOperationResult<T> {

    private final List<T> items;
    private final String count;

    public TopOperationResult(List<T> items, String count) {
        this.items = items;
        this.count = count;
    }

    public List<T> getItems() {
        return items;
    }

    public String getCount() {
        return count;
    }
}
