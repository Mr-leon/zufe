package org.smartjq.mvc.admin.partystudent;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.smartjq.mvc.common.model.EcPartyStudent;
import org.smartjq.mvc.common.utils.UuidUtil;

import java.io.*;

/**
 * @author CYZ
 */
public class EcPartyStudentService {
    public static final EcPartyStudentService me = new EcPartyStudentService();
    public static final String TABLE_NAME = EcPartyStudent.tableName;

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
        EcPartyStudent student = new EcPartyStudent();
        student.setId(UuidUtil.getUUID());
        student.setSchool((insertvalue[1] + "").trim().equals("null") ? null : ((insertvalue[1] + "").trim()));
        student.setZhibu((insertvalue[2] + "").trim().equals("null") ? null : ((insertvalue[2] + "").trim()));
        student.setGrade((insertvalue[3] + "").trim().equals("null") ? null : ((insertvalue[3] + "").trim()));
        student.setClasses((insertvalue[4] + "").trim().equals("null") ? null : ((insertvalue[4] + "").trim()));
        student.setSex((insertvalue[5] + "").trim().equals("null") ? null : ((insertvalue[5] + "").trim()));
        student.setMingzu((insertvalue[6] + "").trim().equals("null") ? null : ((insertvalue[6] + "").trim()));
        student.setBrith((insertvalue[7] + "").trim().equals("null") ? null : ((insertvalue[7] + "").trim()));
        student.setGuanji((insertvalue[8] + "").trim().equals("null") ? null : ((insertvalue[8] + "").trim()));
        student.setXueli((insertvalue[9] + "").trim().equals("null") ? null : ((insertvalue[9] + "").trim()));
        student.setRudangTime((insertvalue[10] + "").trim().equals("null") ? null : ((insertvalue[10] + "").trim()));
        student.setZhuanzhenTime((insertvalue[11] + "").trim().equals("null") ? null : ((insertvalue[11] + "").trim()));
        student.setZsoryb((insertvalue[12] + "").trim().equals("null") ? null : ((insertvalue[12] + "").trim()));
        student.setShenfen((insertvalue[13] + "").trim().equals("null") ? null : ((insertvalue[13] + "").trim()));
        student.save();
        return "ok";
    }


}