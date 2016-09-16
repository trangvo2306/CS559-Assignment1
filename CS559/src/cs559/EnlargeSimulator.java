package cs559;


import java.awt.image.*;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;
import com.pearsoneduc.ip.io.*;
import com.pearsoneduc.ip.gui.*;
import com.pearsoneduc.ip.op.OperationException;



public class EnlargeSimulator extends ImageSelector {


  public EnlargeSimulator(String imageFile)
   throws IOException, ImageDecoderException, OperationException {
    super(imageFile);
  }


  // Checks that the image is suitable for simulation

  public boolean imageOK() {
    //Make sure that it's RGB type
      return getSourceImage().getType() == BufferedImage.TYPE_INT_RGB;
  
  }


  // Averages over blocks of pixels to simulate reduced resolution

  public BufferedImage resizeImage(int ratio) {
      
    int width = getSourceImage().getWidth();
    int height= getSourceImage().getHeight(); 
    
    int newWidth = ratio * width;
    int newHeight = ratio * height;
    
    
    BufferedImage destImage =
     new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
    

    Raster src = getSourceImage().getRaster();
    WritableRaster dest = destImage.getRaster();
    
    //Copy each pixel and duplicate it to increase size
    for(int x = 0; x < newWidth; x++)
        for(int y = 0; y < newHeight; y++)
            for (int band = 0; band < 3; band++){
            int color = src.getSample(x/ratio, y/ratio, band);
            dest.setSample(x, y, band, color);
    }
        
    
    return destImage;

}


  // Creates simulated views of an image at different resolutions

  public Vector generateImages() {

    Vector resolutions = new Vector();
    int width = getSourceImage().getWidth();
    int height = getSourceImage().getHeight();

    //Enlarge source Image

    for (int n = 1; n < 4  ; ++n) {
      String key = "k = " + Integer.toString(n);
      System.out.println(key + "...");
      resolutions.addElement(key);
      addImage(key, new ImageIcon(resizeImage(n)));
    }

    return resolutions;

  }


  public static void main(String[] argv) {
    if (argv.length > 0) {
      try {
        JFrame frame = new EnlargeSimulator(argv[0]);
        frame.pack();
        frame.setVisible(true);
      }
      catch (Exception e) {
	System.err.println(e);
	System.exit(1);
      }
    }
    else {
      System.err.println("usage: java ResolutionSimulator <imagefile>");
      System.exit(1);
    }
  }


}
