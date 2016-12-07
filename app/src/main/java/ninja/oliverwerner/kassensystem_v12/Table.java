package ninja.oliverwerner.kassensystem_v12;

public class Table {

    private int tableId, tableStatus;
    private String tableName;

    public Table(int tableId, String tableName, int tableStatus) {
        this.tableId = tableId;
        this.tableName = tableName;
        this.tableStatus = tableStatus;
    }

    public int getTableId() {
        return this.tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
