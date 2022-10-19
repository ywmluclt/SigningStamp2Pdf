import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

public class AddImageToPDF {
    public static void main (String[] args) throws IOException{
    	
    	args = new String[] {"src\\assets\\Unsign.pdf", "src\\assets\\stamp.PNG", "src\\assets\\Signed.pdf"};
    	
    	AddImageToPDF app = new AddImageToPDF();
        if( args.length != 3 )
        {
            app.usage();
        }
        else
        {
            app.createPDFFromImage( args[0], args[1], args[2] );
        }
    }
    
    public void createPDFFromImage( String inputFile, String imagePath, String outputFile )
            throws IOException
    {
    	try (PDDocument doc = Loader.loadPDF(new File(inputFile)))
        {
            //we will add the image to the first page.
            PDPage page = doc.getPage(0);    

            // createFromFile is the easiest way with an image file
            // if you already have the image in a BufferedImage, 
            // call LosslessFactory.createFromImage() instead
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
           // pdImage.
            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page, AppendMode.APPEND, true, true))
            {
                contentStream.saveGraphicsState();
                contentStream.transform(Matrix.getRotateInstance(Math.toRadians(90), page.getCropBox().getWidth() + page.getCropBox().getLowerLeftX(), 0));
                contentStream.drawImage(pdImage, 100, 100, pdImage.getWidth(), pdImage.getHeight());
                contentStream.restoreGraphicsState();
            }
            doc.save(outputFile);
        }
    }
    

    
    private void usage()
    {
        System.err.println( "usage: " + this.getClass().getName() + " <input-pdf> <image> <output-pdf>" );
    }
}