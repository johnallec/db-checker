package mainpackage.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SheetCreator {
	
	private Workbook wb;
	private Sheet sheet;
	private Row row;
	
	public SheetCreator(String sheetName) {
		this.wb = new XSSFWorkbook();
		this.sheet = wb.createSheet(sheetName);
		this.sheet.setColumnWidth(0, 6000);
		this.sheet.setColumnWidth(1, 4000);
	}
	
	public void createRow(int rowNumber) {
		this.row = sheet.createRow(rowNumber);
	}
	
	public void createCell(int columnNumber, String value) {
		this.row.createCell(columnNumber).setCellValue(value);
	}
	
	public void storeFile(String nameOfFile) throws IOException {
		FileOutputStream oStream = new FileOutputStream(new File("").getAbsolutePath() + File.separator +  nameOfFile);
		wb.write(oStream);
		wb.close();
		oStream.close();
	}
	
	public void storeFile(String fileLocation, String nameOfFile) throws IOException {
		FileOutputStream oStream = new FileOutputStream(new File(fileLocation).getAbsolutePath() + File.separator +  nameOfFile);
		wb.write(oStream);
		wb.close();
		oStream.close();
	}
	
}
