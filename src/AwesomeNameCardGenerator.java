/* This program is a simple name card generator, the camera can detect the face part and users can take photo of their face by
 * pressing the button, then we can see the name card template's default black part will be replaced into user's face.
 * Then, users can input their information in the text field, and click update button to update the information to the name card.
 * Also, this program allow users to save their information, picture and their own name card by clicking the buttons on the top part.
 *
 * * For some reasons, it is better to not to wear glasses for the better capture image. I found that glasses may affect the quality of the detection.
 *
 *
 * The technologies I used for this program in the list:
 * a) AWT: for drawing the text strings and rectangles
 * b) Swing: for using JFrame to create the whole GUI
 * c) ActionListener: for clicking the button and getActionCommand
 * d) File input or output without image: for saving the user's input for the information into "Information.txt"
 * e) Image load or save: using ImageIO to save the user's picture into "Picture.png" and the NameCard into "NameCard.png"
 * f) BufferedImage: for using bimage1 (for the camera) and bimage2 (the captured image)
 * i) OpenCV, using built-in camera: for using built-in camera
 * j) OpenCV, Haar Cascade: for capturing the face through the camera
 *
 *
 *
 * by Lee Jui Chi on January, 13, 2019
 * Email: is0484xe@ed.ritsumei.ac.jp
 * */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class AwesomeNameCardGenerator extends JFrame implements ActionListener {
    BufferedImage bimage1, bimage2;

    Mat imageArray;
    JLabel l1, l2, l3;
    JTextField tf1, tf2, tf3;
    int width, height;
    JButton button1, button2, button3, button4, button5;
    boolean flag = true;
    String path
            = "/usr/local/Cellar/opencv/3.4.3_2/share/OpenCV/haarcascades/";
    String cascade = "haarcascade_frontalface_alt2.xml";
    Rect[] faceArea = null;
    String infoName, infoAffiliation, infoPosition;
    BufferedWriter bw;
    int frameWidth = 700;
    int frameHeight = 800;

    public AwesomeNameCardGenerator() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        setSize(frameWidth, frameHeight);
        setTitle("Awesome Name Card Generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        button1 = new JButton("Stop & Take Picture");
        button1.addActionListener(this);
        button1.setActionCommand("stop");

        button2 = new JButton("Update Information");
        button2.addActionListener(this);
        button2.setActionCommand("Generate");

        button3 = new JButton("Save Name Card");
        button3.addActionListener((this));
        button3.setActionCommand("SaveNameCard");

        button4 = new JButton("Save Information");
        button4.addActionListener((this));
        button4.setActionCommand("SaveInfo");

        button5 = new JButton("Save Image");
        button5.addActionListener((this));
        button5.setActionCommand("SaveImage");

        JPanel p = new JPanel();
        p.add(button1);
        p.add(button3);
        p.add(button4);
        p.add(button5);
        getContentPane().add(p, BorderLayout.NORTH);


        JPanel p2 = new JPanel();

        l1 = new JLabel("What's your name?");
        l2 = new JLabel("Affiliation?");
        l3 = new JLabel("Position?");


        tf1 = new JTextField(10);
        tf2 = new JTextField(10);
        tf3 = new JTextField(10);


        p2.setLayout(new BoxLayout(p2, BoxLayout.PAGE_AXIS));
        p2.add(l1);
        p2.add(tf1);
        p2.add(l2);
        p2.add(tf2);
        p2.add(l3);
        p2.add(tf3);
        p2.add(button2);
        this.getContentPane().add(p2, BorderLayout.SOUTH);
        setVisible(true);
        cameraImage();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.red);

        g.drawRect(200, 430, 400, 200);
        Font font2 = new Font("Lucida Sans Typewriter", Font.BOLD, 20);
        g.setFont(font2);
        g.setColor(Color.black);
        g.fillRect(270, 480, 100, 100);
        g.drawString("Name:", 450, 475);
        g.drawString("Affiliation:", 450, 525);
        g.drawString("Position:", 450, 575);

        if (bimage1 != null) {
            g.drawImage(bimage1, 30, 60, width, height, this);
        }
        if (faceArea != null) {
            g.setColor(Color.yellow);
            for (Rect rect : faceArea) {
                g.drawRect(rect.x / 2 + 30, rect.y / 2 + 60, rect.width / 2, rect.height / 2);
            }

        }


        if (bimage2 != null) {

            g.drawImage(bimage2, 270, 480, 100, 100, this);

        }


        Font font1 = new Font("Lucida Sans Typewriter", Font.BOLD, 20);
        g.setFont(font1);
        g.setColor(Color.BLUE);

        if (infoName != null) {


            g.drawString(infoName, 450, 500);

        } else {

            g.drawString("Default", 450, 500);
        }

        if (infoAffiliation != null) {


            g.drawString(infoAffiliation, 450, 550);

        } else {
            g.drawString("Default", 450, 550);
        }
        if (infoPosition != null) {


            g.drawString(infoPosition, 450, 600);

        } else {
            g.drawString("Default", 450, 600);
        }

    }

    public void cameraImage() {

        imageArray = new Mat();
        VideoCapture videoDevice = new VideoCapture(0);
        if (videoDevice.isOpened()) {
            videoDevice.read(imageArray);
            while (imageArray.cols() == 0) {
                videoDevice.read(imageArray);
            }
            bimage1 = matToBufferedImage(imageArray);

            width = bimage1.getWidth() / 2;
            height = bimage1.getHeight() / 2;
            while (flag) {
                videoDevice.read(imageArray);
                bimage1 = matToBufferedImage(imageArray);
                faceArea = getArea(cascade);
                repaint();
            }
            videoDevice.release();
        } else {
            System.out.println("Error.");
        }
    }

    Rect[] getArea(String str) {
        Rect[] area = null;
        CascadeClassifier faceDetector = new CascadeClassifier(path + str);
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(imageArray, faceDetections);
        area = faceDetections.toArray();
        return area;
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("stop")) {
            flag = false;
            drawFace();

        }
        if (e.getActionCommand().equals("Generate")) {

            infoName = tf1.getText();
            infoAffiliation = tf2.getText();
            infoPosition = tf3.getText();

            repaint();

        }

        if (e.getActionCommand().equals("SaveImage")) {
            saveImage();
        }

        if (e.getActionCommand().equals("SaveInfo")) {
            saveInfo();
        }

        if (e.getActionCommand().equals("SaveNameCard")) {
            saveNameCard();
        }

    }


    void drawFace() {
        Graphics g = getGraphics();
        Mat part = new Mat(imageArray, faceArea[0]);
        bimage2 = matToBufferedImage(part);
        g.drawImage(bimage2, 270, 480, 100, 100, this);
    }

    public void saveImage() {
        try {

            BufferedImage bi = bimage2;
            File outputfile = new File("Picture.png");
            ImageIO.write(bi, "png", outputfile);
        } catch (IOException e) {
            System.out.println("Error");

        }
    }


    public void saveNameCard() {


        try {


            Rectangle screenRectangle = new Rectangle(getX() + 210, getY() + 440, 380, 180);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRectangle);
            File outputfile = new File("NameCard.png");
            ImageIO.write(image, "png", outputfile);

        } catch (Exception e) {
            System.out.println("Error");
        }

    }


    public void saveInfo() {

        File file = new File("Information.txt");

        try {
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write("Name: " + infoName);
            bw.newLine();
            bw.write("Affiliation: " + infoAffiliation);
            bw.newLine();
            bw.write("Position: " + infoPosition);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        new AwesomeNameCardGenerator();

    }

    public static BufferedImage matToBufferedImage(Mat matrix) {
        int cols = matrix.cols();
        int rows = matrix.rows();
        int elemSize = (int) matrix.elemSize();
        byte[] data = new byte[cols * rows * elemSize];
        int type;
        matrix.get(0, 0, data);
        switch (matrix.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                byte b;
                for (int i = 0; i < data.length; i = i + 3) {
                    b = data[i];
                    data[i] = data[i + 2];
                    data[i + 2] = b;
                }
                break;
            default:
                return null;
        }
        BufferedImage out = new BufferedImage(cols, rows, type);
        out.getRaster().setDataElements(0, 0, cols, rows, data);
        return out;
    }

}