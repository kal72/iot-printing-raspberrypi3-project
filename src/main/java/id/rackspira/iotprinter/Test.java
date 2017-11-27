package id.rackspira.iotprinter;

import com.lowagie.text.DocumentException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Test {

    public static void main(String[] args){
//        URL oracle = null;
//        try {
//            oracle = new URL("http://mygis.freecluster.eu/lorem.pdf");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(oracle.openStream()));
//            String inputLine;
//            while ((inputLine = in.readLine()) != null)
//                System.out.println(inputLine);
//            generatePDF(inputLine, "test.pdf");
//
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        try {
            saveFileFromUrlWithJavaIO("percobaan.pdf",
                    "http://localhost:8080/repositories/86e3a9bc-1600-43f3-b9ad-0fb090488f06-lorem.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFileFromUrlWithJavaIO(String fileName, String fileUrl)
            throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(fileUrl).openStream());
            fout = new FileOutputStream(fileName);

            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null)
                in.close();
            if (fout != null)
                fout.close();
        }
    }

    public static void generatePDF(String inputHtmlPath, String outputPdfPath)
    {
        try {
            String url = new File(inputHtmlPath).toURI().toURL().toString();
            System.out.println("URL: " + url);

            OutputStream out = new FileOutputStream(outputPdfPath);

            //Flying Saucer part
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocument(url);
            renderer.layout();
            renderer.createPDF(out);

            out.close();
        } catch (DocumentException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
