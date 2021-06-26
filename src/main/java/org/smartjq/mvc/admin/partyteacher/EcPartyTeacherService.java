package org.smartjq.mvc.admin.partyteacher;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.smartjq.mvc.common.model.EcPartyTeacher;
import org.smartjq.mvc.common.utils.UuidUtil;

import java.io.*;

/**
 * @author CYZ
 */
public class EcPartyTeacherService{
	public static final EcPartyTeacherService me = new EcPartyTeacherService();
	public static final String TABLE_NAME = EcPartyTeacher.tableName;

	public String importExcel(File file) throws FileNotFoundException {
		Object[] insertvalue = new Object[48];
		InputStream fis = new FileInputStream(file);
		int lastRowNum = 0;
		int physicalRowNum = 0;
		int lastCellNum = 0;
		String ghflag = "ok";
		XSSFSheet st = null;
		XSSFWorkbook wb = null;
		POIFSFileSystem fs = null;
		try {
			wb = new XSSFWorkbook(fis);
			st = wb.getSheetAt(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		lastRowNum = st.getLastRowNum();
		physicalRowNum = st.getPhysicalNumberOfRows();
		System.out.println("lastRowNum=" + lastRowNum + " physicalRowNum=" + physicalRowNum);
		for (int r = 1; r <= st.getLastRowNum(); r++) {
			XSSFRow row = st.getRow(r);
			lastCellNum = row.getLastCellNum();
			if (row != null) {
				String adj = notPriceNeed(insertvalue, row, lastCellNum, r - 1);
				if (adj == "no") {
					ghflag = "no";
					break;
				}
			}
		}
		return ghflag;
	}


	/**
	 * @param insertvalue
	 * @param row
	 * @param order
	 * @return
	 */
	private String notPriceNeed(Object[] insertvalue, XSSFRow row, int lastCellNum, int order) {
		XSSFCell cell0 = row.getCell(0);
		for (int i = 0; i < 24; i++) {
			XSSFCell cell1 = row.getCell(i);
			if (cell1 != null) {
				cell1.setCellType(CellType.STRING);
			}
			insertvalue[i] = cell1 == null ? null : (cell1 != null && cell1.getStringCellValue() != null && !cell1.getStringCellValue().equals("")) ? cell1.getStringCellValue() : "";
		}
		EcPartyTeacher teacher = new EcPartyTeacher();
		teacher.setId(UuidUtil.getUUID());
		teacher.setSchool((insertvalue[1] + "").trim().equals("null") ? null : ((insertvalue[1] + "").trim()));
		teacher.setZhibu((insertvalue[2] + "").trim().equals("null") ? null : ((insertvalue[2] + "").trim()));
		teacher.setSex((insertvalue[3] + "").trim().equals("null") ? null : ((insertvalue[3] + "").trim()));
		teacher.setMingzu((insertvalue[4] + "").trim().equals("null") ? null : ((insertvalue[4] + "").trim()));
		teacher.setBrith((insertvalue[5] + "").trim().equals("null") ? null : ((insertvalue[5] + "").trim()));
		teacher.setGuanji((insertvalue[6] + "").trim().equals("null") ? null : ((insertvalue[6] + "").trim()));
		teacher.setXueli((insertvalue[7] + "").trim().equals("null") ? null : ((insertvalue[7] + "").trim()));
		teacher.setRudangTime((insertvalue[8] + "").trim().equals("null") ? null : ((insertvalue[8] + "").trim()));
		teacher.setZsoryb((insertvalue[9] + "").trim().equals("null") ? null : ((insertvalue[9] + "").trim()));
		teacher.save();
		return "ok";
	}

}