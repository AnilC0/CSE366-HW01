package imghw01;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author ANIL
 */
public class imgHW01 {
    private static BufferedImage original,pixset,grays,sCh;
    public static String BomPath = System.getProperty("user.dir") + "\\src\\imgfolder\\";
    
    public static void main(String[] args) throws IOException{
      
/*  SETUP PART    */

        Scanner Scan = new Scanner(System.in);
        File original_f = new File(BomPath + "image00.png");       //Input image 
        original = ImageIO.read(original_f);
        
/*  CLEAR PREVIOUS SAMPLEs    */
        File[] cr;
        File CRoute = new File(BomPath);
        cr = CRoute.listFiles();
        System.out.println(BomPath+" Location of created Images");
        for(File path:cr) {
            if(!(path.equals(original_f))) path.delete();
        }
        
        
/*  Menu Part     */
        while(true){
            System.out.println("HW01 by AnilC \n press 0 to get_Pixel \n press 1 to set_Pixel \n press 2 to Copy_Image \n press 3 to do_Grayscale \n press 4 to shift_Image \n press 5 to move_Pixels \n write -1 to terminate \n sol function used by shift_Image");
            int m = Scan.nextInt();
            if(m==-1)break;
            switch(m){
                case 0:
                    System.out.println("Enter x coordinate with range 0-" +original.getWidth()+".");
                    int a1= Scan.nextInt();
                    System.out.println("Enter y coordinate with range 0-" +original.getHeight()+".");
                    int s1= Scan.nextInt();
                    for(int i=0;i<3;i++) get_Pixel(original, a1, s1, i);
                    break;
                case 1:
                    System.out.println("Enter x coordinate with range 0-" +original.getWidth()+".");
                    int z2= Scan.nextInt();
                    System.out.println("Enter y coordinate with range 0-" +original.getHeight()+".");
                    int x2= Scan.nextInt();
                    System.out.println("Enter value in Red Channel with range 0-255.");
                    int c2 =Scan.nextInt();
                    System.out.println("Enter value in Green Channel with range 0-255.");
                    int v2 =Scan.nextInt();
                    System.out.println("Enter value in Blue Channel with range 0-255.");
                    int b2 =Scan.nextInt();
                    set_Pixel(original,z2,x2,c2,v2,b2);
                    System.out.println("Pixel Set");
                    break;
                case 2:
                    System.out.println("Enter Copy Image Name");
                    copy_Image(original,Scan.next());
                    System.out.println("Image Copy Completed");
                    break;
                case 3:
                    grayscale_Maker(original);
                    System.out.println("Grayscale Image Prepared Succesfully");
                    break;
                case 4:    
                    System.out.println("Enter Shift Amount in Red Channel.");
                    int q4 = Scan.nextInt();
                    System.out.println("Enter Shift Amount in Green Channel.");
                    int w4 = Scan.nextInt();
                    System.out.println("Enter Shift Amount in Blue Channel.");
                    int e4 = Scan.nextInt();
                    ShiftCh(original,q4,w4,e4);
                    System.out.println("Image Shifted Succesfully");
                    break;
                case 5:
                    System.out.println("This Function moves pixel in x direction with given int, enter an integer.");
                    int px=Scan.nextInt();
                    Shiftter(original,px);
                    System.out.println("Pixels move succesfully");
                    break;
                
            }
        }
    }
    
    public static void copy_Image(BufferedImage im,String NameIm) throws IOException{
        BufferedImage copyy = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        File file = new File(BomPath+ "" + NameIm + ".png");
        for(int x=0; x<im.getWidth();x++){
            for(int y=0; y<im.getHeight();y++){
                copyy.setRGB(x, y, im.getRGB(x, y));
            }
        }
        ImageIO.write(copyy, "png", file);   
    } 
    public static Color get_Pixel(BufferedImage im, int x, int y,int c){
        if(im.getHeight()<y || im.getWidth()<x){
            System.err.println("ERROR, image out of Bound");
            return null;
        }
        else{
            Color a = new Color(im.getRGB(x, y));
            if(c==0) System.out.println("Red Value is " + a.getRed());
            else if(c==1)System.out.println("Green Value is " + a.getGreen());
            else if(c==2) System.out.println("Blue Value is " + a.getBlue());
//System.out.println("RGB= " + a.getRed()+ ","+ a.getGreen()+","+a.getBlue()+" // pixel at " + x + "," + y );    
            return a;
        }

    }
    public static void set_Pixel(BufferedImage im, int x, int y, int r, int g,int b) throws IOException{
        File f = new File(BomPath+"pixset.png");
        copy_Image(im,"pixset");
        pixset= ImageIO.read(f);
        Color pixcol = new Color(r, g, b);
        pixset.setRGB(x, y, pixcol.getRGB());
        ImageIO.write(pixset, "png", f);     
    }    
    public static void grayscale_Maker(BufferedImage im) throws IOException{
        File f = new File (BomPath+"grays.png");        
        copy_Image(im,"Grays");
        grays = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grays.getGraphics();  
        g.drawImage(original, 0, 0, null);
        ImageIO.write(grays, "png", f);     //from my statistic homework last year (Grayscaled image and Binarized image with Otsu's Threshold)
    }
    public static void ShiftCh(BufferedImage im,int sr,int sg,int sb) throws IOException{
        File f = new File(BomPath+"sl.png");
        copy_Image(im,"sl");
        sCh= ImageIO.read(f);
        int r=0;
        int g=0;
        int b=0;
        for(int x=0; x<sCh.getWidth();x++){
            for(int y=0; y<sCh.getHeight();y++){
                if(y<sCh.getHeight() && x<sCh.getWidth()){
                    Color a = new Color(sCh.getRGB(x, y));
                    r=a.getRed()+sr;
                    g=a.getGreen()+sg;
                    b=a.getBlue()+sb;
                    r=solve_Boundry(r);
                    g=solve_Boundry(g);
                    b=solve_Boundry(b);
                    
                    Color newcol = new Color(r,g,b);
                    sCh.setRGB(x, y, newcol.getRGB());
                }
            }
        }
        ImageIO.write(sCh, "png", f);
    }
    public static int solve_Boundry(int x){
        if(x<0) return 0;
        else if(x>255) return 255;
        else return x;
    }
    public static void Shiftter (BufferedImage im, int c) throws IOException{
        BufferedImage shiftedd = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB);
        File file = new File(BomPath+ "Shifted.png");
        for(int x=im.getWidth(); x>0; x--){
            for(int y=im.getHeight(); y>0; y--){
                if(y<im.getHeight()&& x<im.getWidth()-c){
                    shiftedd.setRGB(x, y, im.getRGB(x+c, y));       //SAĞA KAYDIRAN MEKANIZMA
                } 
            }
        }
        ImageIO.write(shiftedd, "png", file);   
    }
    
}