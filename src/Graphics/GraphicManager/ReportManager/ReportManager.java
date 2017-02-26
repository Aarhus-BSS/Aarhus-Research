/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.GraphicManager.ReportManager;

import Common.MsgBoxer.MBox;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author d3vil401
 */
public class ReportManager 
{
    private static final String _DEFAULT_EXTENSION = ".xls";
    
    public enum _ALIGNMENT
    {
        LEFT, RIGHT, CENTER;
    }
    
    private HSSFWorkbook _workbook = new HSSFWorkbook();
    private HSSFSheet _sheet = null;
    private Row _rows = null;
    private Cell _cells = null;
    private String _sheetName = "";
    private CellStyle _cellStyle = null;
    
    public ReportManager(String _sheetName)
    {
        this._sheetName = _sheetName;
        this._sheet = this._workbook.createSheet(_sheetName);
    }
    
    public ReportManager()
    {
        this._sheet = this._workbook.createSheet();
    }
    
    // TODO: Add more stuff.
    public void buildStyle(_ALIGNMENT _align)
    {
        switch (_align)
        {
            case LEFT:
                this._cellStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
                break;
                
            case CENTER:
                this._cellStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
                break;
                
            case RIGHT:
                this._cellStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);
                break;
        }
    }
    
    public void assignCellStyle(CellStyle _style)
    {
        this._cellStyle.cloneStyleFrom(_style);
    }
    
    // http://viralpatel.net/blogs/java-read-write-excel-file-apache-poi/
    public void applyData(ReportContentManager _content)
    {
        // TODO (BUG): Sort the map! We're having some unordered report indexing.
        Set<String> _set = _content.getMap().keySet();
	long _rowIndex = 0;
        long _cellIndex = 0;
        
	for (String key : _set) 
        {
            _rowIndex++;
            
            if (_rowIndex > 65534)
            {
                MBox.showBox("Buffer Overflow in report writing...re-run the simulation.", "I still have to fix this", MBox._MSGBOX_TYPE.TYPE_ERROR);
                return;
            }
            
            Row row = this._sheet.createRow((int) _rowIndex);
            
            Object[] _data = _content.getMap().get(key);
            
            _cellIndex = 0;
            
            for (Object obj : _data) 
            {
                Cell cell = row.createCell((int) _cellIndex++);
                
		if(obj instanceof Date) 
                    cell.setCellValue((Date)obj);
		else if(obj instanceof Boolean)
                    cell.setCellValue((Boolean)obj);
                else if(obj instanceof String)
                    cell.setCellValue((String)obj);
		else if(obj instanceof Double)
                    cell.setCellValue((Double)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
                else if(obj instanceof Float)
                    cell.setCellValue(String.valueOf(obj));
                
                if (this._cellStyle != null)
                    cell.setCellStyle(this._cellStyle);
            }
	}

    }
    
    public void write(String _path)
    {
        try 
        {
            FileOutputStream out = new FileOutputStream(new File(_path + File.separator + this._sheetName + _DEFAULT_EXTENSION));
            this._workbook.write(out);
            out.flush();
	} catch (FileNotFoundException e) {
            MBox.showBox("Unable to write the file: " + e.getMessage(), "Critical Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
	} catch (IOException e) {
            MBox.showBox("Unable to write the file: " + e.getMessage(), "Critical Error", MBox._MSGBOX_TYPE.TYPE_ERROR);
	}
    }
}
