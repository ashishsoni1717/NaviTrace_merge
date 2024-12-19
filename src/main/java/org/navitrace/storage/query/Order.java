
package org.navitrace.storage.query;

public class Order {

    private final String column;
    private final boolean descending;
    private final int index;
    private final int size;

    public Order(String column) {
        this(column, false, 0, 0);
    }

    public Order(String column, boolean descending, int pageNo, int pageSize) {
        this.column = column;
        this.descending = descending;
        this.index = (pageNo - 1) * pageSize;
        this.size = pageSize;
    }

    public String getColumn() {
        return column;
    }

    public boolean getDescending() {
        return descending;
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return size;
    }

}
