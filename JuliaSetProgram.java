import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JuliaSetProgram extends JPanel implements AdjustmentListener
{

    JFrame frame;
    JScrollBar aBar,bBar,zoomBar,hueBar,satBar,brightBar, iterationsBar;
    JPanel scrollPanel,labelPanel,bigPanel, buttonPanel;
    JLabel aLabel,bLabel,zoomLabel,hueLabel,satLabel,brightLabel,iterationsLabel;
    double aVal,bVal;
    double zoom;
    float hue;
    float sat;
    float bright;
    float maxIter; //25-1000
    JButton clear,save;
    JFileChooser fileChooser;
    BufferedImage image;

    public JuliaSetProgram()
    {
        frame=new JFrame("Julia Set Program");
        frame.add(this);
        frame.setSize(1000,600);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //orientation,starting value,doodad size,min value,max value
        aBar=new JScrollBar(JScrollBar.HORIZONTAL,0,0,-2000,2000);
        aVal=aBar.getValue()/1000.0;
        aBar.addAdjustmentListener(this);

        bBar=new JScrollBar(JScrollBar.HORIZONTAL,0,0,-2000,2000);
        bVal=bBar.getValue()/1000.0;
        bBar.addAdjustmentListener(this);

        zoomBar=new JScrollBar(JScrollBar.HORIZONTAL,10,0,0,100);
        zoom=zoomBar.getValue()/10.0;
        zoomBar.addAdjustmentListener(this);

        hueBar=new JScrollBar(JScrollBar.HORIZONTAL,888,0,0,1000);
        hue=hueBar.getValue()/1000f;
        hueBar.addAdjustmentListener(this);

        satBar=new JScrollBar(JScrollBar.HORIZONTAL,1000,0,0,1000);
        sat=satBar.getValue()/1000f;
        satBar.addAdjustmentListener(this);

        brightBar=new JScrollBar(JScrollBar.HORIZONTAL,1000,0,0,1000);
        bright=brightBar.getValue()/1000f;
        brightBar.addAdjustmentListener(this);

        iterationsBar=new JScrollBar(JScrollBar.HORIZONTAL,300,0,0,1000);
        maxIter=iterationsBar.getValue();
        iterationsBar.addAdjustmentListener(this);

        aLabel=new JLabel("A: "+ aVal);
        bLabel=new JLabel("B: "+ bVal);
        zoomLabel=new JLabel("Zoom: "+ zoom);
        hueLabel=new JLabel("Hue: "+ hue);
        satLabel=new JLabel("Saturation: "+ sat);
        brightLabel=new JLabel("Brightness: "+ bright);
        iterationsLabel=new JLabel("Iterations: "+ maxIter);

        GridLayout grid=new GridLayout(7,1);

        labelPanel=new JPanel();
        labelPanel.setLayout(grid);
        labelPanel.add(aLabel);
        labelPanel.add(bLabel);
        labelPanel.add(zoomLabel);
        labelPanel.add(hueLabel);
        labelPanel.add(satLabel);
        labelPanel.add(brightLabel);
        labelPanel.add(iterationsLabel);

        scrollPanel=new JPanel();
        scrollPanel.setLayout(grid);
        scrollPanel.add(aBar);
        scrollPanel.add(bBar);
        scrollPanel.add(zoomBar);
        scrollPanel.add(hueBar);
        scrollPanel.add(satBar);
        scrollPanel.add(brightBar);
        scrollPanel.add(iterationsBar);

        buttonPanel = new JPanel();
        clear = new JButton("Clear");
        save = new JButton("Save");
        buttonPanel.add(clear);
        buttonPanel.add(save);

        String currDir=System.getProperty("user.dir");
        fileChooser=new JFileChooser(currDir);

        bigPanel=new JPanel();
        bigPanel.setLayout(new BorderLayout());
        bigPanel.add(labelPanel,BorderLayout.WEST);
        bigPanel.add(scrollPanel,BorderLayout.CENTER);
        bigPanel.add(buttonPanel,BorderLayout.EAST);

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==clear){
                    zoom = 1;
                    hue = 0.888f;
                    sat = 1f;
                    bright = 1f;
                    maxIter = 300f;
                    aBar.setValue(0);
                    bBar.setValue(0);
                    zoomBar.setValue(10);
                    hueBar.setValue(888);
                    satBar.setValue(1000);
                    brightBar.setValue(1000);
                    iterationsBar.setValue(300);
                }
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==save)
                    saveImage();
            }
        });
        frame.add(bigPanel,BorderLayout.SOUTH);

        frame.setVisible(true);
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(drawJulia(),0,0,null);

    }
    public BufferedImage drawJulia()
    {
        int w = frame.getWidth();
        int h = frame.getHeight();
        image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < w;x++){
            for(int y =0; y < h; y++){
                float i = maxIter;
                double zx = 1.5*((x-w/2.0)/(zoom*w/2.0));
                double zy = ((y-h/2.0)/(zoom*h/2.0));

                while(zx*zx + zy*zy < 6 && i>0){
                    double temp = zx*zx - zy*zy + aVal;
                    zy = 2.0*zy*zx + bVal;
                    zx=temp;
                    i--;
                }


                int c;
                float hue2 = 1f;
                if(i>0)
                    c = Color.HSBtoRGB(hue*(i/maxIter)%1,sat,bright);
                else c = Color.HSBtoRGB(hue2,1,1);
                image.setRGB(x,y,c);
            }
        }
      return image;
    }



    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        if(e.getSource()==aBar) {
            aVal = aBar.getValue()/1000.0;
            aLabel.setText("A: "+aVal+"\t\t");
        }
        if(e.getSource()==bBar) {
            bVal = bBar.getValue()/1000.0;
            bLabel.setText("B: " + bVal + "\t\t");
        }
        if(e.getSource()==zoomBar) {
            zoom = zoomBar.getValue()/10.0;
            zoomLabel.setText("Zoom: " + zoom + "\t\t");
        }
        if(e.getSource()==hueBar) {
            hue = hueBar.getValue()/1000f;
            hueLabel.setText("Hue: " + hue + "\t\t");
        }
        if(e.getSource()==satBar) {
            sat = satBar.getValue()/1000f;
            satLabel.setText("Saturation: " + sat + "\t\t");
        }
        if(e.getSource()==brightBar) {
            bright = brightBar.getValue()/1000f;
            brightLabel.setText("Brightness: " + bright + "\t\t");
        }
        if(e.getSource()==iterationsBar) {
            maxIter = iterationsBar.getValue();
            iterationsLabel.setText("Iterations: " + maxIter + "\t\t");
        }

        repaint();
    }

    public void saveImage()
    {
        if(image!=null)
        {
            FileFilter filter=new FileNameExtensionFilter("*.png","png");
            fileChooser.setFileFilter(filter);
            if(fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
            {
                File file=fileChooser.getSelectedFile();
                try
                {
                    String st=file.getAbsolutePath();
                    if(st.indexOf(".png")>=0)
                        st=st.substring(0,st.length()-4);
                    ImageIO.write(image,"png",new File(st+".png"));
                }catch(IOException e)
                {
                }

            }
        }
    }

    public static void main(String[] args)
    {
        JuliaSetProgram app=new JuliaSetProgram();
    }

}