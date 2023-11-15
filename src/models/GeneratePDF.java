package models;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GeneratePDF {

    private static final Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN,30,Font.BOLD);
    private static final Font categoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font headerTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);

    public static void createPDF(String filePath, ArrayList<MyProcess> readyProcess, ArrayList<MyProcess> dispatchedProcess,
                                 ArrayList<MyProcess> executingProcess, ArrayList<MyProcess> toLockedProcess,
                                 ArrayList<MyProcess> lockedProcess, ArrayList<MyProcess> wakeUpProcess,
                                 ArrayList<MyProcess> expiredProcess, ArrayList<MyProcess> terminatedProcess)
            throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath + ".pdf"));
        document.open();
        addFirstPage(document);
        addInfo(readyProcess, document, "Listado de procesos Listos", 1);
        addInfo(dispatchedProcess, document, "listado de procesos Despachados", 2);
        addInfo(executingProcess, document, "Listado de procesos que estuvieron en Ejecucion", 3);
        addInfo(toLockedProcess, document, "Listado de procesos que pasaron a Bloqueo", 4);
        addInfo(lockedProcess, document, "Listado de procesos Bloqueados", 5);
        addInfo(wakeUpProcess, document, "Listado de procesos Despertados", 6);
        addInfo(expiredProcess, document, "Listado de procesos que Expiraron Tiempo", 7);
        addInfo(terminatedProcess, document, "Listado de procesos Terminados", 8);
        document.close();
    }

    private static void addFirstPage(Document document) throws DocumentException {
        Chunk title = new Chunk("REPORTES", titleFont);
        Paragraph paragraph = new Paragraph(title);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        Chapter chapter = new Chapter(paragraph,1);
        chapter.setNumberDepth(0);
        document.add(chapter);
    }

    private static void addInfo(ArrayList<MyProcess> processInfo, Document document, String txt, int tableNumber) throws DocumentException {
        Anchor anchor = new Anchor(txt, categoryFont);
        Chapter chapter = new Chapter(new Paragraph(anchor), tableNumber);
        chapter.add(new Paragraph("    "));
        chapter.add(addProcessInfo(processInfo));
        document.add(chapter);
    }

    private static void addHeaders(PdfPTable table){
        PdfPCell nameHeader = new PdfPCell(new Phrase("Nombre", headerTitleFont));
        nameHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(nameHeader);
        PdfPCell timeHeader = new PdfPCell(new Phrase("Unidades de tiempo", headerTitleFont));
        timeHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(timeHeader);
        PdfPCell isLockedHeader = new PdfPCell(new Phrase("Bloqueo", headerTitleFont));
        isLockedHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(isLockedHeader);
    }

    private static PdfPTable addProcessInfo(ArrayList<MyProcess> processInfo){
        PdfPTable table = new PdfPTable(3);
        addHeaders(table);
        for(MyProcess process : processInfo){
            table.addCell(process.getName());
            table.addCell(String.valueOf(process.getTime()));
            String isLockedTxt = process.isLocked() ? "Si" : "No";
            table.addCell(isLockedTxt);
        }
        return table;
    }
}
