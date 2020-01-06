package com.wtbw.mods.lib.gui.util;

import com.wtbw.mods.lib.gui.util.sprite.Sprite;

import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class SpriteProgressBar extends ProgressBar
{
  protected Sprite backgroundSprite;
  protected Sprite progressSprite;
  protected int xOffset = 0;
  protected int yOffset = 0;
  
  public SpriteProgressBar(int x, int y, Sprite progressSprite)
  {
    super(x, y, progressSprite.width, progressSprite.height);
    this.progressSprite = progressSprite;
  }
  
  public SpriteProgressBar(int x, int y, Sprite progressSprite, Sprite backgroundSprite)
  {
    super(x, y, progressSprite.width, progressSprite.height);
    this.progressSprite = progressSprite;
    this.backgroundSprite = backgroundSprite;
  }
  
  public SpriteProgressBar(int x, int y, Sprite progressSprite, Supplier<Integer> capacitySupplier, Supplier<Integer> storageSupplier)
  {
    super(x, y, progressSprite.width, progressSprite.height, capacitySupplier, storageSupplier);
    this.progressSprite = progressSprite;
  }
  
  public SpriteProgressBar(int x, int y, Sprite progressSprite, Sprite backgroundSprite, Supplier<Integer> capacitySupplier, Supplier<Integer> storageSupplier)
  {
    super(x, y, progressSprite.width, progressSprite.height, capacitySupplier, storageSupplier);
    this.progressSprite = progressSprite;
    this.backgroundSprite = backgroundSprite;
  }
  
  public Sprite getBackgroundSprite()
  {
    return backgroundSprite;
  }
  
  public SpriteProgressBar setBackgroundSprite(Sprite backgroundSprite)
  {
    this.backgroundSprite = backgroundSprite;
    return this;
  }
  
  public Sprite getProgressSprite()
  {
    return progressSprite;
  }
  
  public SpriteProgressBar setProgressSprite(Sprite progressSprite)
  {
    this.progressSprite = progressSprite;
    return this;
  }
  
  public SpriteProgressBar setOffset(int xOffset, int yOffset)
  {
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    
    return this;
  }
  
  @Override
  public void draw(int xOffset, int yOffset)
  {
    int x = this.x + xOffset + this.xOffset;
    int y = this.y + yOffset + this.yOffset;
  
    int fillHeight = height;
    int fillWidth = width;
    int fillX = x;
    int fillY = y;
  
    switch (fillDirection)
    {
      case LEFT_RIGHT:
        fillWidth = (int) (fillWidth * currentProgress);
        break;
      case RIGHT_LEFT:
        fillWidth = (int) (fillWidth * currentProgress);
        fillX = x + width - fillWidth;
        break;
      case BOTTOM_TOP:
        fillHeight = (int) (fillHeight * currentProgress);
        fillY = y + height - fillHeight;
        break;
      case TOP_BOTTOM:
        fillHeight = (int) (fillHeight * currentProgress);
        break;
    }
    
    if (backgroundSprite != null)
    {
      backgroundSprite.render(x, y);
    }
    
    progressSprite.render(fillX, fillY, fillWidth, fillHeight);
  }
}
