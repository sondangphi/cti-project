
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class tableModel extends  AbstractTableModel {
    private  Vector colName;
    private  Vector rowValue;
    
    public tableModel(){
    }
    
    public tableModel(Vector col){
        colName = col;
    }    
    public tableModel(Vector col, Vector row){
        colName = col;
        rowValue = row;
    }    

    public int getColumnCount() {
        return colName.size();
    }

    public int getRowCount() {
        return rowValue.size();
    }

    public Object getValueAt(int row, int column) {
        Vector rowData = (Vector) (rowValue.elementAt(row));
        return rowData.elementAt(column);
    }

    @Override
    public String getColumnName(int column) {
        return (String) (colName.elementAt(column));
    }
}