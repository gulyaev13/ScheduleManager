package sample.model;

import java.io.FileOutputStream;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class PDFCreator {
    private String filePath;
    private String championshipName;
    private Championship championship;
    private String normalFont = "/sample/resources/fonts/times.ttf";
    private String boldFont = "/sample/resources/fonts/timesbd.ttf";
    private String windowsTimes = "C:/Windows/Fonts/times.ttf";
    private String windowsTimesBold = "C:/Windows/Fonts/timesbd.ttf";
    private static int FONT_SIZE_SMALL = 10;
    private static int FONT_SIZE_MEDIUM = 12;
    private static int FONT_SIZE_BIG = 16;
    private boolean russianLanguage;
    private Font titleFont;
    private Font listFont;
    private Font listTitleFont;
    private List<Team> allTeams;
    public PDFCreator(){
        try{
            BaseFont titleFontBase = BaseFont.createFont(windowsTimesBold, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            titleFont = new Font(titleFontBase, FONT_SIZE_BIG);
            BaseFont listFontBase = BaseFont.createFont(windowsTimes, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            listFont = new Font(listFontBase, FONT_SIZE_SMALL);
            BaseFont listTitleFontBase = BaseFont.createFont(windowsTimesBold, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            listTitleFont = new Font(listTitleFontBase, FONT_SIZE_MEDIUM);
            russianLanguage = true;
        } catch (Exception e){
            try {
                BaseFont titleFontBase = BaseFont.createFont(boldFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                titleFont = new Font(titleFontBase, FONT_SIZE_BIG);
                BaseFont listFontBase = BaseFont.createFont(normalFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                listFont = new Font(listFontBase, FONT_SIZE_SMALL);
                BaseFont listTitleFontBase = BaseFont.createFont(boldFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                listTitleFont = new Font(listTitleFontBase, FONT_SIZE_MEDIUM);
                russianLanguage = true;
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        /*BaseFont titleFontBase = BaseFont.createFont(windowsTimesBold, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        titleFont = new Font(titleFontBase, FONT_SIZE_BIG);
        titleFont = FontFactory.getFont(windowsTimesBold, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        titleFont.setSize(FONT_SIZE_BIG);
        listFont = FontFactory.getFont(windowsTimes, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        listFont.setSize(FONT_SIZE_SMALL);
        listTitleFont = FontFactory.getFont(windowsTimesBold, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        listTitleFont.setSize(FONT_SIZE_MEDIUM);*/
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setChampionshipName(String championshipName) {
        this.championshipName = championshipName;
    }

    public void setChampionship(Championship championship) {
        this.championship = championship;
        this.championship.setRussianLanguage(russianLanguage);
    }

    public void setAllTeams(List<Team> allTeams) {
        this.allTeams = allTeams;
    }

    public void createPDF(List<GameTour> gameTourList) throws Exception{
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        Paragraph title = new Paragraph(championshipName, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(FONT_SIZE_BIG);
        document.add(title);

        Paragraph listTitle = new Paragraph(russianLanguage ? "Список команд:" : "Command list:", listTitleFont);
        listTitle.setAlignment(Element.ALIGN_LEFT);
        document.add(listTitle);
        int i = 1;
        String teamsString = "";
        for(Team team : allTeams){
            teamsString += "    " + i++ + ". " + team + "\n";
        }
        Paragraph paragraph = new Paragraph(teamsString,listTitleFont);
        document.add(paragraph);
        i = 1;
        for (GameTour gameTour : gameTourList){
            Paragraph tourTitle = new Paragraph(championship.getTourString(i++), listTitleFont);//"Тур " + i++
            document.add(tourTitle);
            Paragraph tourText = new Paragraph("" + gameTour, listFont);
            document.add(tourText);
        }
        document.close();
    }

    private String getUnicode(String str){
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c > 31 && c < 127)
                result += String.valueOf(c);
            else
                result += String.format("\\u%04x", (int)c);
        }
        System.out.println(result);
        return result;
    }
}
