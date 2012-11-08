
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class rsTableModel extends  AbstractTableModel {
    //-- Khai báo 2 Vector Object để sử dụng cho JTable
    private  Vector colName;        //-- Chứa thông tin là tên của các Field dùng làm ColumnHeader
    private  Vector rowValue;              //-- Phần chứa dữ liệu của JTable
    
    public rsTableModel(){
    }
    public rsTableModel(Vector col, Vector row){
        colName = col;
        rowValue = row;
    }    
    public rsTableModel(ResultSet rsData) throws SQLException {
        ResultSetMetaData rsMeta = rsData.getMetaData();    //-- Đọc MetaData của ResultSet
        int count = rsMeta.getColumnCount();              //-- Xác định số Field trong ResultSet

        colName = new Vector(count);
        rowValue = new Vector();
                  //--- Lặp tương ứng với số phần tử của columnHeaders để lấy tên của từng cột trong bảng
        for (int i = 1; i <= count; i++) {
            colName.addElement(rsMeta.getColumnName(i));
        }
           //--- Lặp từ Record đầu tiên đến cuối ResultSet để lấy dữ liệu và Add vào Table
        while (rsData.next()) {
                      //--- Khai báo 1 Object Vector có tên là rowData để chứa dữ liệu tương ứng với mỗi dòng đọc từ Table
            Vector dataRow = new Vector(count);
                      //-- Lặp dựa theo số cột của bảng để lấy thông tin của từng đối tượng
            for (int i = 1; i <= count; i++) {
                dataRow.addElement(rsData.getObject(i));
            }
            rowValue.addElement(dataRow);
        }
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