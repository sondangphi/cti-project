
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.table.AbstractTableModel;

public class CustomTableModel extends AbstractTableModel {   
       
    public final static boolean GENDER_MALE = true;
    public final static boolean GENDER_FEMALE = true;
    String[]columnNames = null;
    public Object [][]values = null;
    public CustomTableModel(){
        values=new Object[][]{
        {"Clay", "Ashworth",new GregorianCalendar(1962, Calendar.FEBRUARY, 20).getTime(),new Float(12345.67), GENDER_MALE},
        {"Jacob", "Ashworth",new GregorianCalendar(1987, Calendar.JANUARY, 6).getTime(),new Float(23456.78), GENDER_MALE},
        {"Jordan", "Ashworth",new GregorianCalendar(1989, Calendar.AUGUST, 31).getTime(),new Float(34567.89), GENDER_FEMALE},
        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE},
        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE},
        {"Clay", "Ashworth",new GregorianCalendar(1962, Calendar.FEBRUARY, 20).getTime(),new Float(12345.67), GENDER_MALE},
        {"Jacob", "Ashworth",new GregorianCalendar(1987, Calendar.JANUARY, 6).getTime(),new Float(23456.78), GENDER_MALE},
        {"Jordan", "Ashworth",new GregorianCalendar(1989, Calendar.AUGUST, 31).getTime(),new Float(34567.89), GENDER_FEMALE},
        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE},
        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE},
        {"Clay", "Ashworth",new GregorianCalendar(1962, Calendar.FEBRUARY, 20).getTime(),new Float(12345.67), GENDER_MALE},
        {"Jacob", "Ashworth",new GregorianCalendar(1987, Calendar.JANUARY, 6).getTime(),new Float(23456.78), GENDER_MALE},
        {"Jordan", "Ashworth",new GregorianCalendar(1989, Calendar.AUGUST, 31).getTime(),new Float(34567.89), GENDER_FEMALE},
        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE},
        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE}
        };        
        columnNames = new String[]{"First Name", "Last Name", "Date of Birth", "Account Balance", "Gender"};
    }
    public CustomTableModel(String []colname,Object [][]v){
        columnNames = colname;
        values = v;
    }
    public CustomTableModel(Object [][]v){
        values = v;
    }    
//    public final static String[]columnNames = {"First Name", "Last Name", "Date of Birth", "Account Balance", "Gender"};
//    public Object [][]values={
//        {"Clay", "Ashworth",new GregorianCalendar(1962, Calendar.FEBRUARY, 20).getTime(),new Float(12345.67), GENDER_MALE},
//        {"Jacob", "Ashworth",new GregorianCalendar(1987, Calendar.JANUARY, 6).getTime(),new Float(23456.78), GENDER_MALE},
//        {"Jordan", "Ashworth",new GregorianCalendar(1989, Calendar.AUGUST, 31).getTime(),new Float(34567.89), GENDER_FEMALE},
//        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE},
//        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE},
//        {"Clay", "Ashworth",new GregorianCalendar(1962, Calendar.FEBRUARY, 20).getTime(),new Float(12345.67), GENDER_MALE},
//        {"Jacob", "Ashworth",new GregorianCalendar(1987, Calendar.JANUARY, 6).getTime(),new Float(23456.78), GENDER_MALE},
//        {"Jordan", "Ashworth",new GregorianCalendar(1989, Calendar.AUGUST, 31).getTime(),new Float(34567.89), GENDER_FEMALE},
//        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE},
//        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE},
//        {"Clay", "Ashworth",new GregorianCalendar(1962, Calendar.FEBRUARY, 20).getTime(),new Float(12345.67), GENDER_MALE},
//        {"Jacob", "Ashworth",new GregorianCalendar(1987, Calendar.JANUARY, 6).getTime(),new Float(23456.78), GENDER_MALE},
//        {"Jordan", "Ashworth",new GregorianCalendar(1989, Calendar.AUGUST, 31).getTime(),new Float(34567.89), GENDER_FEMALE},
//        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE},
//        {"Evelyn", "Kirk",new GregorianCalendar(1945, Calendar.JANUARY, 16).getTime(),new Float(-456.70), GENDER_FEMALE},
//        };     

    @Override
    public int getRowCount() {        
        return values.length;
    }

    @Override
    public int getColumnCount() {
        return values[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return values[rowIndex][columnIndex];
    }
    
    @Override
    public String getColumnName(int column){
        return columnNames[column];
    }
}
