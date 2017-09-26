int cols;
int rows;
int scale = 10;
float noiseScale = 0.05f;

boolean running = true;

float timeOff = 0;

Vector[][] field;
Particle[] particles;

void setup()
{
  size(1100,700);//,P2D);
  cols = width/scale;
  rows = height/scale;
  field = new Vector[cols][rows];
  particles = new Particle[10000];
  
  for (int i = 0; i < particles.length; i++)
    particles[i] = new Particle();
    
  background(255);
}

void calculateField()
{
  for (int i = 0; i < cols; i++)
    for (int j = 0; j < rows; j++)
    {
      float angle = noise(i*noiseScale, j*noiseScale, timeOff)*TWO_PI*4;
      float r = 1;
      field[i][j] = new Vector(r,angle,true);
    }
}

void keyPressed()
{
  if (keyCode == DOWN)
    running = !running;
}

void draw()
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