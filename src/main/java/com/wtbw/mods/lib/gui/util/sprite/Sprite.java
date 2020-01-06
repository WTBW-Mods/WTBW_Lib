package com.wtbw.mods.lib.gui.util.sprite;

/*
  @author: Naxanria
*/
public class Sprite
{
  public final SpriteMap map;
  public final int width;
  public final int height;
  public final int u;
  public final int v;
  
  Sprite(SpriteMap map, int width, int height, int u, int v)
  {
    this.map = map;
    this.width = width;
    this.height = height;
    this.u = u;
    this.v = v;
    
    assertInside();
  }
  
  public void render(int x, int y)
  {
    render(x, y, 0xffffffff);
  }
  
  public void render(int x, int y, int color)
  {
    map.render(x, y, color, this);
  }
  
  public void render(int x, int y, int width, int height)
  {
    render(x, y, width, height, 0xffffffff);
  }
  
  public void render(int x, int y, int width, int height, int color)
  {
    map.render(x, y, width, height, color, this);
  }
  
  public Sprite getAdjacent(int width, int height, Direction direction)
  {
    switch (direction)
    {
      default:
      case UP:
        return new Sprite(map, u, v - height, width, height);
      case DOWN:
        return new Sprite(map, u, v + this.height, width, height);
      case LEFT:
        return new Sprite(map, u - width, v, width, height);
      case RIGHT:
        return new Sprite(map, u + this.width, v, width, height);
    }
  }
  
  public Sprite getLeft(int width, int height)
  {
    return getAdjacent(width, height, Direction.LEFT);
  }
  
  public Sprite getLeft(int size)
  {
    return getAdjacent(size, size, Direction.LEFT);
  }
  
  public Sprite getRight(int width, int height)
  {
    return getAdjacent(width, height, Direction.RIGHT);
  }
  
  public Sprite getRight(int size)
  {
    return getAdjacent(size, size, Direction.RIGHT);
  }
  
  public Sprite getAbove(int width, int height)
  {
    return getAdjacent(width, height, Direction.UP);
  }
  
  public Sprite getAbove(int size)
  {
    return getAdjacent(size, size, Direction.UP);
  }
  
  public Sprite getBelow(int width, int height)
  {
    return getAdjacent(width, height, Direction.DOWN);
  }
  
  public Sprite getBelow(int size)
  {
    return getAdjacent(size, size, Direction.DOWN);
  }
  
  private void assertInside()
  {
    if (u < 0 || v < 0 || u + width > map.width || v + height > map.height)
    {
      throw new IllegalArgumentException("Sprite is not in bounds! " + toString());
    }
  }
  
  @Override
  public String toString()
  {
    return "Sprite{" +
      ", u=" + u +
      ", v=" + v +
      ", width=" + width +
      ", height=" + height +
      '}';
  }
}
