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