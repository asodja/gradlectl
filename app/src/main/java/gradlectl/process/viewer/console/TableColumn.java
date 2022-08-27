package gradlectl.process.viewer.console;

import java.util.ArrayList;
import java.util.List;

class TableColumn {

    private final List<String> values;
    private int maxWidth;

    public TableColumn(TableHeader header) {
        this.values = new ArrayList<>();
        add(header.toString());
    }

    public void add(String value) {
        values.add(value);
        maxWidth = Math.max(value.length(), maxWidth);
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public String getValue(int index) {
        return values.get(index);
    }
}
