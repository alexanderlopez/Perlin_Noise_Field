import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Perlin_Noise_Field extends PApplet {

int cols;
int rows;
int scale = 10;
float noiseScale = 0.05f;

boolean running = true;

float timeOff = 0;

Vector[][] field;
Particle[] particles;

public void setup()
{
  //,P2D);
  cols = width/scale;
  rows = height/scale;
  field = new Vector[cols][rows];
  particles = new Particle[10000];
  
  for (int i = 0; i < particles.length; i++)
    particles[i] = new Particle();
    
  background(255);
}

public void calculateField()
{
  for (int i = 0; i < cols; i++)
    for (int j = 0; j < rows; j++)
    {
      float angle = noise(i*noiseScale, j*noiseScale, timeOff)*TWO_PI*4;
      float r = 1;
      field[i][j] = new Vector(r,angle,true);
    }
}

public void keyPressed()
{
  if (keyCode == DOWN)
    running = !running;
}

public void draw()
{
  if (running){
    //background(255);
    //stroke(0,50);
    strokeWeight(1);
    println(frameRate);
    calculateField();
    
    /*for (int i = 0; i < cols; i++)
      for (int j = 0; j < rows; j++)
      {
        Vector line = field[i][j];
        
        float x = i*scale;
        float y = (j+1)*scale;
        line(x, y, (line.x()*scale)+x, y-(line.y()*scale));
      }*/
      
    for (int i = 0; i < particles.length; i++)
    {
      Particle temp = particles[i];
      int col = (int)Math.round(Math.floor(temp.posX()/(double)scale));
      int row = (int)Math.round(Math.floor(temp.posY()/(double)scale));
      
      if (col == cols) col--;
      if (row == rows) row--;
      
      temp.applyForce(field[col][row].x(), -field[col][row].y());
      temp.update();
      temp.render();
    }
      
    timeOff += 0.005f;
  }
}

class Vector
{
  private float x;
  private float y;
  private float r;
  private float a;
  
  public Vector(float _x, float _y, boolean isAngle)
  {
    if (!isAngle)
    {
      x = _x;
      y = _y;
      r = (float)Math.sqrt(Math.pow((double)x,2)+Math.pow((double)y,2));
      a = (float)Math.atan((double)(y/x));
    } else {
      r = _x;
      a = _y;
      x = (float)(r*Math.cos((double)a));
      y = (float)(r*Math.sin((double)a));
    }
  }
  
  public float x() { return x; }
  public float y() { return y; }
  public float a() { return a; }
  public float r() { return r; }
  
}
class Particle
{
  private float posX;
  private float posY;
  private float velX;
  private float velY;
  private float accX;
  private float accY;
  
  private float prevX;
  private float prevY;
  
  private float maxForce = 5;
  
  public Particle(float _posX, float _posY)
  {
    posX = _posX;
    posY = _posY;
    velX = 1;
    velY = 1;
    accX = 0;
    accY = 0;
  }
  
  public float posX() { return posX; }
  public float posY() { return posY; }
  
  public Particle()
  {
    posX = random(width);
    posY = random(height);
    velX = 1;
    velY = 1;
    accX = 0;
    accY = 0;
  }
  
  public void update()
  {
    prevX = posX;
    prevY = posY;
    
    velX += accX;
    velY += accY;
    posX += velX;
    posY += velY;
    accX = 0;
    accY = 0;
    
    double radius = Math.sqrt(Math.pow((double)velX, 2)+Math.pow((double)velY,2));
    
    if (radius >= maxForce)
    {
      float ratio = maxForce/(float)radius;
      velX *= ratio;
      velY *= ratio;
    }
    
    if (posX > width){
      posX = 0;
      prevX = posX;
    }
    if (posX < 0){
      posX = width - 1;
      prevX = posX;
    }
    if (posY > height){
      posY = 0;
      prevY = posY;
    }
    if (posY < 0){
      posY = height - 1;
      prevY = posY;
    }
  }
  
  public void applyForce(float fX, float fY)
  {
    accX += fX;
    accY += fY;
  }
  
  public void render()
  {
    stroke(0,5);
    strokeWeight(1);
    line(prevX, prevY, posX, posY);
  }
}
  public void settings() {  size(1100,700); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Perlin_Noise_Field" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
