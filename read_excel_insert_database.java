package excel_read;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.sql.*;
/**
 *
 * @author furkantekke
 */
public class Excel_read {

    /**
     * @param args the command line arguments
     */
    // JDBC driver and Database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/deneme";

// database user name and password
    static final String USER = "root";
    static final String PASS = "pass";
    
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        
        Connection conn = null;
        PreparedStatement stmt =null;
        try{
        
            Class.forName("com.mysql.jdbc.Driver");
// Baglantı acılır.
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//bağlantı başarılıysa devam eder
           
            
String query = " insert into database_table_name (soyad,adres,enlembilgisi,boylambilgisi,postakod,ilcesemt,sehir,ulke)"
+ " values (?,?,?,?,?,?,?,?)";
           stmt = conn.prepareStatement(query);
            
            String okunacak = "/Users/furkantekke/Desktop/Mission 3/okunacak_excel.xlsx";
      FileInputStream fil = new FileInputStream(new File(okunacak));
     Workbook wb = WorkbookFactory.create(fil);
		Sheet oku = wb.getSheetAt(0);
          
                int a=1; //for use column names in prepared statement query
                
                //for reading datas from excel and insert them into database
                
               // 98 data are in my excel sheet
               for(int j=1;j<=97;j++){ 
                for(int i=4;i<=11;i++){
                          Cell cell = oku.getRow(j).getCell(i); //choosing data to readable
                          String x = getCellValueAsString(cell); //choosed data is converting string with getCellValueAsString 
                          
                          if(i==9)//if readed data is in column 9 than insert database 1 
                              stmt.setInt(a, 1);
                          
                          else if(i==11){//if readed data is in 11 column than convert it to integer value than insert in database
                              int result = Integer.parseInt(x);
                              stmt.setInt(a,result);}
                          else//all other situations are inserting database
                              stmt.setString (a, x);
                         a++;//selecting next column in query
                }
               a=1;//refresh query columns 
              stmt.execute(); //rows are inserted database and saved
                }
              
      
       
        conn.close();//close the database connection

        }
         catch (Exception e)
    {
      System.err.println("Got an exception!");
      System.err.println(e.getMessage());
        System.out.println(e);
    }
      
    }
    
    
    
	public static String getCellValueAsString(Cell cell) {
		String strCellValue = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			case STRING:
				strCellValue = cell.toString();
				break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"dd/MM/yyyy");
					strCellValue = dateFormat.format(cell.getDateCellValue());
				} else {
					Double value = cell.getNumericCellValue();
					Long longValue = value.longValue();
					strCellValue = new String(longValue.toString());
				}
				break;
			
			case BLANK:
				strCellValue = "";
				break;
			}
		}
		return strCellValue;
	}
}

