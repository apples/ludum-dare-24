/*    */ package com.pickandblade.towerevolution;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ 
/*    */ public class Mortar extends Entity
/*    */ {
/*    */   private int targetX;
/*    */   private int targetY;
/*  9 */   private double vel = 1.0D;
/*    */   private double vx;
/* 11 */   private double vy; private double xAccum = 0.0D; private double yAccum = 0.0D;
/* 12 */   private double epsilon = 2.0D;
/*    */   public int power;
/*    */   public int radius;
/*    */   private Level level;
/*    */   
/*    */   public Mortar(Level l, int x, int y, int tx, int ty, int p, int r)
/*    */   {
/* 19 */     this.level = l;
/* 20 */     this.x = x;
/* 21 */     this.y = y;
/* 22 */     this.w = 8;this.h = 8;
/* 23 */     this.targetX = tx;
/* 24 */     this.targetY = ty;
/* 25 */     this.power = p;
/* 26 */     this.radius = r;
/*    */     
/* 28 */     double dx = tx - x;double dy = ty - y;
/* 29 */     double dist = Math.sqrt(dx * dx + dy * dy);
/* 30 */     this.vx = (this.vel * dx / dist);
/* 31 */     this.vy = (this.vel * dy / dist);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 35 */     this.xAccum += this.vx;
/* 36 */     this.yAccum += this.vy;
/* 37 */     while (this.xAccum >= 1.0D) {
/* 38 */       this.x += 1;
/* 39 */       this.xAccum -= 1.0D;
/*    */     }
/* 41 */     while (this.yAccum >= 1.0D) {
/* 42 */       this.y += 1;
/* 43 */       this.yAccum -= 1.0D;
/*    */     }
/* 45 */     while (this.xAccum <= 1.0D) {
/* 46 */       this.x -= 1;
/* 47 */       this.xAccum += 1.0D;
/*    */     }
/* 49 */     while (this.yAccum <= 1.0D) {
/* 50 */       this.y -= 1;
/* 51 */       this.yAccum += 1.0D;
/*    */     }
/*    */     
/* 54 */     if ((Math.abs(this.x - this.targetX) <= this.epsilon) && (Math.abs(this.y - this.targetY) <= this.epsilon)) {
/* 55 */       this.explode = true;
/*    */     }
/*    */   }
/*    */   
/*    */   public void render(BufferedImage target) {
/* 60 */     java.awt.Graphics g = target.createGraphics();
/* 61 */     g.drawImage(this.level.spriteSheet, this.x - this.w / 2, this.y - this.h / 2, this.x + this.w / 2, this.y + this.h / 2, 32, 64, 40, 72, null);
/* 62 */     g.dispose();
/*    */   }
/*    */ }


/* Location:              D:\Dropbox\Public\Tower Evolution\towerevolution.jar!\com\pickandblade\towerevolution\Mortar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */