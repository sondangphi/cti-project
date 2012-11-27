package org.asterisk.model;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class tbModel extends  AbstractTableModel {
    private  Vector colName;
    private  Vector rowValue;
    
    public tbModel(){
    }
    
    public tbModel(Vector col){
        colName = col;
    }    
    public tbModel(Vector col, Vector row){
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