package com.wtbw.lib.gui.util;

/*
  @author: Naxanria
*/
public class NineSliceSprite
{
  private Sprite[] sprites;
  
  public NineSliceSprite(Sprite[] sprites)
  {
    if (sprites.length != 9)
    {
      throw new IllegalArgumentException("NineSliceSprite requires exactly 9 sprites!");
    }
    this.sprites = sprites;
  }
  
  public NineSliceSprite(SpriteMap map, int u, int v, int size)
  {
    this(map, u, v, size, size);
  }
  
  public NineSliceSprite(SpriteMap map, int u, int v, int width, int height)
  {
    sprites = new Sprite[9];
    
    for (int x = 0; x < 3; x++)
    {
      for (int y = 0; y < 3; y++)
      {
        int index = x + y * 3;
        sprites[index] = new Sprite(map, width, height, u + width * x, v + height * y);
      }
    }
  }
  
  public static NineSliceSprite create(SpriteMap map, int width, int height, int... spriteUV)
  {
    Sprite[] sprites = new Sprite[9];
    int index = 0;
    for (int i = 0; i < spriteUV.length; i += 2, index++)
    {
      sprites[index] = new Sprite(map, width, height, spriteUV[i], spriteUV[i + 1]);
    }
    
    return new NineSliceSprite(sprites);
  }
  
  public void render(int x, int y, int width, int height)
  {
    render(x, y, width, height, 0xffffffff);
  }
  
  public void render(int x, int y, int width, int height, int color)
  {
    int middleWidth = width - getTopLeft().width - getTopRight().width;
    int centerHeight = height - getTopLeft().height - getBottomLeft().height;
    
    int xp = x;
    int yp = y;
    
    getTopLeft().render(xp, yp, color);
    xp += getTopLeft().width;
    getTopMiddle().render(xp, yp, middleWidth, getTopMiddle().height, color);
    xp += middleWidth;
    getTopRight().render(xp, yp, color);
    
    xp = x;
    yp += getTopLeft().height;
    getCenterLeft().render(xp, yp, getCenterLeft().width, centerHeight, color);
    xp += getTopLeft().width;
    getCenterMiddle().render(xp, yp, middleWidth, centerHeight, color);
    xp += middleWidth;
    getCenterRight().render(xp, yp, getCenterRight().width, centerHeight, color);
    
    xp = x;
    yp += centerHeight;
    getBottomLeft().render(xp, yp, color);
    xp += getBottomLeft().width;
    getBottomMiddle().render(xp, yp, middleWidth, getBottomMiddle().height, color);
    xp += middleWidth;
    getBottomRight().render(xp, yp, color);
  }
  
  public Sprite getSprite(int index)
  {
    return sprites[index % 9];
  }
  
  public Sprite getTopLeft()
  {
    return getSprite(0);
  }
  
  public Sprite getTopMiddle()
  {
    return getSprite(1);
  }
  
  public Sprite getTopRight()
  {
    return getSprite(2);
  }
  
  public Sprite getCenterLeft()
  {
    return getSprite(3);
  }
  
  public Sprite getCenterMiddle()
  {
    return getSprite(4);
  }
  
  public Sprite getCenterRight()
  {
    return getSprite(5);
  }
  
  public Sprite getBottomLeft()
  {
    return getSprite(6);
  }
  
  public Sprite getBottomMiddle()
  {
    return getSprite(7);
  }
  
  public Sprite getBottomRight()
  {
    return getSprite(8);
  }
}
