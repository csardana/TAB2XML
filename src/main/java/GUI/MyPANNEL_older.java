//package GUI;
//
//import java.awt.*;
//import javax.swing.*;
//import java.awt.geom.Line2D;
////import java.awt.geom.AffineTransform;
////import java.awt.geom.Ellipse2D;
//
//
//public class MyPanel extends JPanel{
//
// Image image;
// 
// MyPanel(){
//  
//  image = new ImageIcon("staff5.jpeg").getImage();
//  this.setPreferredSize(new Dimension(1304,260));
// }
// 
// public void paint(Graphics g) {
//	 
//  Graphics2D note = (Graphics2D) g;
//  super.paintComponents(g);
//  note.drawImage(image, 0, 0, null);
//  
//  //g2D.setPaint(Color.blue);
//  //g2D.setStroke(new BasicStroke(5));
//  //g2D.drawLine(0, 0, 500, 500);
//  
//  //g2D.setPaint(Color.pink);
//  //g2D.drawRect(0, 0, 100, 200);
//  //g2D.fillRect(0, 0, 100, 200);
//  
//  //g2D.setPaint(Color.orange);
//  //drawOval( int X, int Y, int width, int height )//FOR REFERENCE
//  //g.drawOval(100, 100, 55, 40);
//  note.fillOval(300, 163, 45, 30);
//  
//  //note.rotate(3);
//  //AffineTransform rotated;
//  //rotated = new AffineTransform();
//  
//  //rotated.translate(60.0, 60.0);
//  //rotated.setToRotation(Math.PI/4);
//  //note.setTransform(rotated);
//  note.setPaint(Color.white);
//  note.fillOval(315, 165, 15, 25);
//  note.setPaint(Color.black);
//  //drawLine(int x1, int y1, int x2, int y2)
//  // citation: https://stackoverflow.com/questions/2839508/java2d-increase-the-line-width
//  note.setStroke(new BasicStroke(5));
//  note.draw(new Line2D.Float(342, 175, 342, 100));
//  
//  
//  
//  note.fillOval(300+60, 163, 45, 30);
//  
//  
//  note.setPaint(Color.white);
//  note.fillOval(315+60, 165, 15, 25);
//  note.setPaint(Color.black);
//  
//  note.setStroke(new BasicStroke(5));
//  note.draw(new Line2D.Float(342+60, 175, 342+60, 100));
//  note.draw(new Line2D.Float(175, 342+60, 100, 342+60));
//  //note.setPaint(Color.black);
//  //note.draw(new Ellipse2D.Double(250, 50, 25, 250));
//  //g2D.fillOval(0, 0, 100, 100);
//  
//  //g2D.setPaint(Color.red);
//  //g2D.drawArc(0, 0, 100, 100, 0, 180);
//  //g2D.fillArc(0, 0, 100, 100, 0, 180);
//  //g2D.setPaint(Color.white);
//  //g2D.fillArc(0, 0, 100, 100, 180, 180);
//  
//  //int[] xPoints = {150,250,350};
//  //int[] yPoints = {300,150,300};
//  //g2D.setPaint(Color.yellow);
//  //g2D.drawPolygon(xPoints, yPoints, 3);
//  //g2D.fillPolygon(xPoints, yPoints, 3);
//  
//  //g.setPaint(Color.magenta);
//  //g2D.setFont(new Font("Ink Free",Font.BOLD,50));
//  //g2D.drawString("U R A WINNER! :D", 50, 50);  
// }
// 
//}