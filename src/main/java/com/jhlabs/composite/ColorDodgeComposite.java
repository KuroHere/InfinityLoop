/*    */ package com.jhlabs.composite;
/*    */ 
/*    */ import java.awt.CompositeContext;
/*    */ import java.awt.RenderingHints;
/*    */ import java.awt.image.ColorModel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ColorDodgeComposite
/*    */   extends RGBComposite
/*    */ {
/*    */   public ColorDodgeComposite(float alpha) {
/* 26 */     super(alpha);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
/* 31 */     return new Context(this.extraAlpha, srcColorModel, dstColorModel);
/*    */   }
/*    */   
/*    */   static class Context
/*    */     extends RGBCompositeContext
/*    */   {
/*    */     public Context(float alpha, ColorModel srcColorModel, ColorModel dstColorModel) {
/* 38 */       super(alpha, srcColorModel, dstColorModel);
/*    */     }
/*    */ 
/*    */     
/*    */     public void composeRGB(int[] src, int[] dst, float alpha) {
/* 43 */       int w = src.length;
/*    */       
/* 45 */       for (int i = 0; i < w; i += 4) {
/*    */         
/* 47 */         int dor, dog, dob, sr = src[i];
/* 48 */         int dir = dst[i];
/* 49 */         int sg = src[i + 1];
/* 50 */         int dig = dst[i + 1];
/* 51 */         int sb = src[i + 2];
/* 52 */         int dib = dst[i + 2];
/* 53 */         int sa = src[i + 3];
/* 54 */         int dia = dst[i + 3];
/*    */ 
/*    */         
/* 57 */         if (sr != 255) {
/*    */           
/* 59 */           dor = Math.min((dir << 8) / (255 - sr), 255);
/*    */         }
/*    */         else {
/*    */           
/* 63 */           dor = sr;
/*    */         } 
/*    */         
/* 66 */         if (sg != 255) {
/*    */           
/* 68 */           dog = Math.min((dig << 8) / (255 - sg), 255);
/*    */         }
/*    */         else {
/*    */           
/* 72 */           dog = sg;
/*    */         } 
/*    */         
/* 75 */         if (sb != 255) {
/*    */           
/* 77 */           dob = Math.min((dib << 8) / (255 - sb), 255);
/*    */         }
/*    */         else {
/*    */           
/* 81 */           dob = sb;
/*    */         } 
/*    */         
/* 84 */         float a = alpha * sa / 255.0F;
/* 85 */         float ac = 1.0F - a;
/* 86 */         dst[i] = (int)(a * dor + ac * dir);
/* 87 */         dst[i + 1] = (int)(a * dog + ac * dig);
/* 88 */         dst[i + 2] = (int)(a * dob + ac * dib);
/* 89 */         dst[i + 3] = (int)(sa * alpha + dia * ac);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Home\Downloads\Nightmare.jar!\com\jhlabs\composite\ColorDodgeComposite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */