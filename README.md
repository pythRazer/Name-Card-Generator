# Name-Card-Generator
OpenCV Facial Capture Java program, be able to automatic identify the face and take the picture. After inputing some information, a simple name card can be generated and saved

# Installation
You need to install Opencv for Java to be able to run this program

# Detail
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
 
 # Example Name Card
 
 ![Image description](link-to-image)
