package londonmet.aladdin.hourstracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Aladdin on 26/05/2015.
 */
public class CreatePdf {
    DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public CreatePdf(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }


    public void createPdf() throws FileNotFoundException, DocumentException {

        String dir = Environment.getExternalStorageDirectory()+File.separator+"myLogs";
        File folder = new File(dir);
        folder.mkdirs();

        File file = new File(dir, "LogHistory.pdf");

        Chunk CONNECT = new Chunk(new LineSeparator(0.5f, 95, BaseColor.BLACK, Element.ALIGN_CENTER, 3.5f));
        LineSeparator UNDERLINE = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);

        Cursor c1 = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LOG, null);
        Document document = new Document();  // create the document
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        Paragraph p3 = new Paragraph();
        p3.add("Your Log History for Primark \n");
        document.add(p3);

        Paragraph p4 = new Paragraph();
        p4.add(" ");
        document.add(p4);


        PdfPTable table = new PdfPTable(4);
        table.addCell("Date");
        table.addCell("Start");
        table.addCell("End");
        table.addCell("Total");

        while (c1.moveToNext()) {
            String date = c1.getString(3);
            String start = c1.getString(1);
            String end = c1.getString(2);
            String total = c1.getString(4);

            table.addCell(date);
            table.addCell(start);
            table.addCell(end);
            table.addCell(total);
        }

        document.add(table);
        document.addCreationDate();
        document.close();
    }

}
