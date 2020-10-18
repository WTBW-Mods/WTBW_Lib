package com.wtbw.mods.lib.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/*
  @author: Naxanria
*/
public class FluidIngredient implements Predicate<FluidStack>
{
  public final ResourceLocation location;
  public final boolean isTag;
  
  public FluidIngredient(ResourceLocation location, boolean isTag)
  {
    this.location = location;
    this.isTag = isTag;
  }
  
  @Override
  public boolean test(@Nullable FluidStack fluidStack)
  {
    if (fluidStack == null)
    {
      return false;
    }
    
    Fluid fluid = fluidStack.getFluid();
    
    if (!isTag)
    {
      return fluid.getRegistryName().equals(location);
    }
    
    return fluid.getTags().contains(location);
  }
  
  public boolean test(FluidStack fluidStack, int amount)
  {
    return fluidStack.getAmount() >= amount && test(fluidStack);
  }
  
  public final void write(PacketBuffer buffer)
  {
    buffer.writeString(location.toString());
    buffer.writeBoolean(isTag);
  }
  
  public JsonElement serialize()
  {
    JsonObject object = new JsonObject();
    object.addProperty(isTag ? "fluidTag" : "fluid", location.toString());
    return object;
  }
  
  public static FluidIngredient deserialize(@Nullable JsonElement json)
  {
    if (json != null && !json.isJsonNull())
    {
      if (json.isJsonObject())
      {
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.has("fluid"))
        {
          String fluid = jsonObject.get("fluid").getAsString();
  
          return new FluidIngredient(new ResourceLocation(fluid), false);
        }
        else if (jsonObject.has("fluidTag"))
        {
          String fluid = jsonObject.get("fluidTag").getAsString();
  
          return new FluidIngredient(new ResourceLocation(fluid), true);
        }
        else
        {
          throw new JsonSyntaxException("Expected \"fluid\" or \"fluidTag\"");
        }
      }
      else
      {
        throw new JsonSyntaxException("Invalid format");
      }
    }
    else
    {
      throw new JsonSyntaxException("Fluid can not be null!");
    }
  }
  
  public static FluidIngredient read(PacketBuffer buffer)
  {
    String location = buffer.readString();
    boolean isTag = buffer.readBoolean();
    
    return new FluidIngredient(new ResourceLocation(location), isTag);
  }
}
