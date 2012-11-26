
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileWriter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author leehoa
 */
public class csvfile {
     public static void main(String args[]) {
         write();
         read();
     }
     
     static public void read(){
                  try{
            CsvReader products = new CsvReader("products.csv");
            products.readHeaders();
            while (products.readRecord()){
                String productID = products.get("ProductID");
                String productName = products.get("ProductName");
                String supplierID = products.get("SupplierID");
                String categoryID = products.get("CategoryID");
                String quantityPerUnit = products.get("QuantityPerUnit");
                String unitPrice = products.get("UnitPrice");
                String unitsInStock = products.get("UnitsInStock");
                String unitsOnOrder = products.get("UnitsOnOrder");
                String reorderLevel = products.get("ReorderLevel");
                String discontinued = products.get("Discontinued");

                // perform program logic here
                System.out.println(productID + ":" + productName);
            }
            products.close();
         }catch(Exception e){
         }
     }
     
     static public void write(){
                String outputFile = "users.csv";		
		// before we open the file check to see if it already exists
		boolean alreadyExists = new File(outputFile).exists();
			
		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
			
			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists)
			{
				csvOutput.write("id");
				csvOutput.write("name");
				csvOutput.endRecord();
			}
			// else assume that the file already has the correct header line
			
			// write out a few records
			csvOutput.write("1");
			csvOutput.write("Bruce");
			csvOutput.endRecord();
			
			csvOutput.write("2");
			csvOutput.write("John");
			csvOutput.endRecord();
			
			csvOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}         
     }
}
