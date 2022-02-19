package GUI;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.Line2D;
//import java.awt.geom.AffineTransform;
//import java.awt.geom.Ellipse2D;


public class MyPanel extends JPanel{

 Image image;
 Image image2;
 
 MyPanel(){
  
  image = new ImageIcon("staff5.jpeg").getImage();
  image2 = new ImageIcon("tab.jpeg").getImage();
  this.setPreferredSize(new Dimension(1304,800));
 }
 
 public void paint(Graphics g) {
	 
  Graphics2D note = (Graphics2D) g;
  super.paintComponents(g);
  note.drawImage(image, 0, 0, null);
  
  Graphics2D note2 = (Graphics2D) g;
  super.paintComponents(g);
  note2.drawImage(image2, 0, 300, null);
  
  //g2D.setPaint(Color.blue);
  //g2D.setStroke(new BasicStroke(5));
  //g2D.drawLine(0, 0, 500, 500);
  
  //g2D.setPaint(Color.pink);
  //g2D.drawRect(0, 0, 100, 200);
  //g2D.fillRect(0, 0, 100, 200);
  
  //g2D.setPaint(Color.orange);
  //drawOval( int X, int Y, int width, int height )//FOR REFERENCE
  //g.drawOval(100, 100, 55, 40);
  note.fillOval(300, 163, 45, 30);
  
  //note.rotate(3);
  //AffineTransform rotated;
  //rotated = new AffineTransform();
  
  //rotated.translate(60.0, 60.0);
  //rotated.setToRotation(Math.PI/4);
  //note.setTransform(rotated);
  note.setPaint(Color.white);
  note.fillOval(315, 165, 15, 25);
  note.setPaint(Color.black);
  
  
  //drawLine(int x1, int y1, int x2, int y2)
  // citation: https://stackoverflow.com/questions/2839508/java2d-increase-the-line-width
  note.setStroke(new BasicStroke(5));
  note.draw(new Line2D.Float(342, 175, 342, 100));
  
  note.fillOval(300+60, 163, 45, 30);
  
  
  note.setPaint(Color.white);
  note.fillOval(315+60, 165, 15, 25);
  note.setPaint(Color.black);
  
  note.setStroke(new BasicStroke(5));
  note.draw(new Line2D.Float(342+60, 175, 342+60, 100));
  //note.draw(new Line2D.Float(175, 342+60, 100, 342+60));
//  
  
  

  
  // guitar tab
  note2.setStroke(new BasicStroke(5));
  note2.draw(new Line2D.Float(1000, 450, 1000, 300));
  //1st
  note2.setPaint(Color.white);
  note2.fillOval(198, 438, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("0", 200, 448);
  //2nd
  note2.setPaint(Color.white);
  note2.fillOval(198+40, 438-40, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("2", 200+40, 448-30);
  
//3rd
  note2.setPaint(Color.white);
  note2.fillOval(198+80, 438-60, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("2", 200+80, 448-47);
  //4rth
  note2.setPaint(Color.white);
  note2.fillOval(198+120, 438-80, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("0", 200+120, 448-72);
  //5th
  note2.setPaint(Color.white);
  note2.fillOval(198+160, 438-100, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("0", 200+160, 448-95);
  //6th
  note2.setPaint(Color.white);
  note2.fillOval(198+200, 438-120, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("0", 200+200, 448-120);
  //5th
  note2.setPaint(Color.white);
  note2.fillOval(440, 438-100, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("0", 440, 448-95);
  //4th
  note2.setPaint(Color.white);
  note2.fillOval(440+40, 438-80, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("0", 440+40, 448-72);
  
  
// numbers after the big vertical line  
  //1st
  note2.setPaint(Color.white);
  note2.fillOval(1050, 438, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("0", 1055, 448);
  //2nd
  note2.setPaint(Color.white);
  note2.fillOval(1050, 438-38, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("2", 1055, 448-28);
//  3rd
  note2.setPaint(Color.white);
  note2.fillOval(1050, 438-65, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("2", 1055, 448-51);
  //4rth
  note2.setPaint(Color.white);
  note2.fillOval(1050, 438-80, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("0", 1055, 448-75);
  //5th
  note2.setPaint(Color.white);
  note2.fillOval(1050, 438-100, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("0", 1055, 448-100);
  //6th
  note2.setPaint(Color.white);
  note2.fillOval(1050, 438-130, 15, 25);
  note2.setPaint(Color.black);
  
  g.drawString("0", 1055, 448-125);
  
  
  
  

//  //note.setPaint(Color.black);
  //note.draw(new Ellipse2D.Double(250, 50, 25, 250));
  //g2D.fillOval(0, 0, 100, 100);
  
  //g2D.setPaint(Color.red);
  //g2D.drawArc(0, 0, 100, 100, 0, 180);
  //g2D.fillArc(0, 0, 100, 100, 0, 180);
  //g2D.setPaint(Color.white);
  //g2D.fillArc(0, 0, 100, 100, 180, 180);
  
  //int[] xPoints = {150,250,350};
  //int[] yPoints = {300,150,300};
  //g2D.setPaint(Color.yellow);
  //g2D.drawPolygon(xPoints, yPoints, 3);
  //g2D.fillPolygon(xPoints, yPoints, 3);
  
  //g.setPaint(Color.magenta);
  //g2D.setFont(new Font("Ink Free",Font.BOLD,50));
  //g2D.drawString("U R A WINNER! :D", 50, 50);  
 }
 
}