//package com.wtbw.block.ctm;
//
//import com.wtbw.WTBWLib;
//import net.minecraft.block.BlockState;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.Vector3f;
//import net.minecraft.client.renderer.model.BakedQuad;
//import net.minecraft.client.renderer.model.ItemCameraTransforms;
//import net.minecraft.client.renderer.model.ItemOverrideList;
//import net.minecraft.client.renderer.model.ItemTransformVec3f;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.client.renderer.vertex.VertexFormat;
//import net.minecraft.util.Direction;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.Vec3d;
//import net.minecraftforge.client.model.data.IDynamicBakedModel;
//import net.minecraftforge.client.model.data.IModelData;
//import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//
///*
//  @author: Naxanria
//*/
//public class CTMBakedModel implements IDynamicBakedModel
//{
//  private CTMTextureData textureData;
//  private VertexFormat format;
//  private ItemCameraTransforms transforms = getAllTransforms();
//
//  private boolean ambientOcclusion = true;
//
//  public CTMBakedModel(VertexFormat format, CTMTextureData textureData)
//  {
//    this.format = format;
//    this.textureData = textureData;
//  }
//
//  private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b)
//  {
//    for (int e = 0; e < format.getElementCount(); e++)
//    {
//      switch (format.getElement(e).getUsage())
//      {
//        case POSITION:
//          builder.put(e, (float) x, (float) y, (float) z, 1.0f);
//          break;
//        case COLOR:
//          builder.put(e, r, g, b, 1.0f);
//          break;
//        case UV:
//          if (format.getElement(e).getIndex() == 0)
//          {
//            u = sprite.getInterpolatedU(u);
//            v = sprite.getInterpolatedV(v);
//            builder.put(e, u, v, 0f, 1f);
//            break;
//          }
//        case NORMAL:
//          builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0f);
//          break;
//        default:
//          builder.put(e);
//          break;
//      }
//    }
//  }
//
//  private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite)
//  {
//    Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
//
//    UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
//    builder.setTexture(sprite);
//    putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1f, 1f, 1f);
//    putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 16, sprite, 1f, 1f, 1f);
//    putVertex(builder, normal, v3.x, v3.y, v3.z, 16, 16, sprite, 1f, 1f, 1f);
//    putVertex(builder, normal, v4.x, v4.y, v4.z, 16, 0, sprite, 1f, 1f, 1f);
//
//    return builder.build();
//  }
//
//  @Nonnull
//  @Override
//  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData)
//  {
//    if (side != null)
//    {
//      return Collections.emptyList();
//    }
//
//    if (state == null)
//    {
//      TextureAtlasSprite texture = getTexture(false, false, false, false);
//      List<BakedQuad> quads = new ArrayList<>();
//
//      double l = 0;
//      double r = 1;
//
//      quads.add(createQuad(v(l, r, l), v(l, r, r), v(r, r, r), v(r, r, l), texture));
//      quads.add(createQuad(v(l, l, l), v(r, l, l), v(r, l, r), v(l, l, r), texture));
//      quads.add(createQuad(v(r, r, r), v(r, l, r), v(r, l, l), v(r, r, l), texture));
//      quads.add(createQuad(v(l, r, l), v(l, l, l), v(l, l, r), v(l, r, r), texture));
//      quads.add(createQuad(v(r, r, l), v(r, l, l), v(l, l, l), v(l, r, l), texture));
//      quads.add(createQuad(v(l, r, r), v(l, l, r), v(r, l, r), v(r, r, r), texture));
//
//      return quads;
//    }
//
//    boolean north = state.get(CTMBlock.NORTH);
//    boolean south = state.get(CTMBlock.SOUTH);
//    boolean west = state.get(CTMBlock.WEST);
//    boolean east = state.get(CTMBlock.EAST);
//    boolean up = state.get(CTMBlock.UP);
//    boolean down = state.get(CTMBlock.DOWN);
//
//    TextureAtlasSprite up_face = getTexture(north, south, west, east);
//    TextureAtlasSprite down_face = getTexture(north, south, west, east);
//    TextureAtlasSprite east_face = getTexture(up, down, south, north);
//    TextureAtlasSprite west_face = getTexture(up, down, north, south);
//    TextureAtlasSprite north_face = getTexture(up, down, east, west);
//    TextureAtlasSprite south_face = getTexture(up, down, west, east);
//    List<BakedQuad> quads = new ArrayList<>();
//
//    double l = 0;
//    double r = 1;
//
//    quads.add(createQuad(v(l, r, l), v(l, r, r), v(r, r, r), v(r, r, l), up_face)); // up
//    quads.add(createQuad(v(l, l, l), v(r, l, l), v(r, l, r), v(l, l, r), down_face)); // down
//    quads.add(createQuad(v(r, r, r), v(r, l, r), v(r, l, l), v(r, r, l), east_face)); // east
//    quads.add(createQuad(v(l, r, l), v(l, l, l), v(l, l, r), v(l, r, r), west_face)); // west
//    quads.add(createQuad(v(r, r, l), v(r, l, l), v(l, l, l), v(l, r, l), north_face)); // north
//    quads.add(createQuad(v(l, r, r), v(l, l, r), v(r, l, r), v(r, r, r), south_face)); // south
//
//    return quads;
//  }
//
//  @Override
//  public boolean isAmbientOcclusion()
//  {
//    return ambientOcclusion;
//  }
//
//  public CTMBakedModel setAmbientOcclusion(boolean ambientOcclusion)
//  {
//    this.ambientOcclusion = ambientOcclusion;
//    return this;
//  }
//
//  @Override
//  public boolean isGui3d()
//  {
//    return false;
//  }
//
//  @Override
//  public boolean isBuiltInRenderer()
//  {
//    return false;
//  }
//
//  @Override
//  public TextureAtlasSprite getParticleTexture()
//  {
//    return getTexture(false, false, false, false);
//  }
//
//  @Override
//  public ItemOverrideList getOverrides()
//  {
//    return ItemOverrideList.EMPTY;
//  }
//
//  private static Vec3d v(double x, double y, double z)
//  {
//    return new Vec3d(x, y, z);
//  }
//
//  private TextureAtlasSprite getTexture(boolean up, boolean down, boolean left, boolean right)
//  {
//     return getTexture(textureData.getTextures()[(!up ?  1 : 0) | (!right ?  2 : 0)| (!down ?  4 : 0)| (!left ?  8 : 0)]);
//  }
//
//  public static TextureAtlasSprite getTexture(ResourceLocation location)
//  {
//    return Minecraft.getInstance().getTextureMap().getSprite(location);
//  }
//
//  @Override
//  public ItemCameraTransforms getItemCameraTransforms()
//  {
//    return transforms;
//  }
//
//  private ItemCameraTransforms getAllTransforms()
//  {
//    ItemTransformVec3f tpLeft = getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
//    ItemTransformVec3f tpRight = getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
//    ItemTransformVec3f fpLeft = getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND);
//    ItemTransformVec3f fpRight = getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
//    ItemTransformVec3f head = getTransform(ItemCameraTransforms.TransformType.HEAD);
//    ItemTransformVec3f gui = getTransform(ItemCameraTransforms.TransformType.GUI);
//    ItemTransformVec3f ground = getTransform(ItemCameraTransforms.TransformType.GROUND);
//    ItemTransformVec3f fixed = getTransform(ItemCameraTransforms.TransformType.FIXED);
//
//    return new ItemCameraTransforms(tpLeft, tpRight, fpLeft, fpRight, head, gui, ground, fixed);
//  }
//
//  private ItemTransformVec3f getTransform(ItemCameraTransforms.TransformType type)
//  {
//    if (type.equals(ItemCameraTransforms.TransformType.GUI))
//    {
//      return new ItemTransformVec3f(new Vector3f(200, 50, 100), new Vector3f(), new Vector3f(.85f, .85f, .85f));
//    }
//
//    return ItemTransformVec3f.DEFAULT;
//  }
//
//
//}
