/*    */ package com.pickandblade.towerevolution;
/*    */ 
/*    */ import java.awt.Graphics2D;
/*    */ 
/*    */ public class Tile {
/*    */   public Type type;
/*    */   
/*    */   public static enum Type {
/*  9 */     BLANK,  WALL,  TOWER,  PATH,  SPAWN,  CORE;
/*    */   }
/*    */   
/*    */   public static enum Direction {
/* 13 */     NONE,  NORTH,  SOUTH,  EAST,  WEST,  SPAWN,  CORE;
/*    */   }
/*    */   
/*    */ 
/* 17 */   boolean hasTower = false;
/* 18 */   Tower tower = null;
/*    */   
/* 20 */   public Direction dirLast = Direction.NONE; public Direction dirNext = Direction.NONE;
/*    */   private Level level;
/*    */   
/*    */   public Tile(Level l)
/*    */   {
/* 25 */     this.level = l;
/*    */   }
/*    */   
/*    */   public void draw(int x, int y, Graphics2D g) {
/* 29 */     if (this.type == Type.CORE) {
/* 30 */       g.drawImage(this.level.spriteSheet, x * 16, y * 16 + this.level.coreHealth, x * 16 + 16, y * 16 + 16, 64, 64 + this.level.coreHealth, 80, 80, null);
/*    */     }
/* 32 */     if (this.hasTower) {
/* 33 */       g.drawImage(this.level.spriteSheet, x * 16, y * 16, (x + 1) * 16, (y + 1) * 16, this.tower.squareness * 16, 16, (this.tower.squareness + 1) * 16, 32, null);
/* 34 */       java.awt.image.BufferedImage guns = new java.awt.image.BufferedImage(32, 32, 2);
/* 35 */       Graphics2D g2dGuns = guns.createGraphics();
/* 36 */       if (this.tower.disabled > 0) {
/* 37 */         g2dGuns.setColor(new java.awt.Color(128, 128, 255, 255));
/* 38 */         g2dGuns.fillArc(8, 8, 16, 16, 0, 360 * this.tower.disabled / this.tower.getDisableTime());
/*    */       } else {
/* 40 */         java.awt.image.BufferedImage gunSprite = new java.awt.image.BufferedImage(16, 16, 2);
/* 41 */         Graphics2D g2dGunSprite = gunSprite.createGraphics();
/* 42 */         if (this.tower.type == Tower.PlatformType.FIRE) {
/* 43 */           g2dGunSprite.drawImage(this.level.spriteSheet, 0, 0, 16, 16, 64, 32, 80, 48, null);
/*    */         } else {
/* 45 */           int offsetA = 0;
/* 46 */           if (this.tower.gun.range > 48) offsetA += 16;
/* 47 */           if (this.tower.gun.power > 10) offsetA += 32;
/* 48 */           g2dGunSprite.drawImage(this.level.spriteSheet, 0, 0, 16, 16, offsetA, 32, offsetA + 16, 48, null);
/*    */         }
/* 50 */         g2dGunSprite.dispose();
/* 51 */         for (int i = 0; i < this.tower.numGuns; i++) {
/* 52 */           g2dGuns.drawImage(gunSprite, 8, 4, null);
/* 53 */           g2dGuns.rotate(0.5D, 16.0D, 16.0D);
/*    */         }
/*    */       }
/* 56 */       g2dGuns.dispose();
/* 57 */       if (this.tower.type == Tower.PlatformType.ENERGY) {
/* 58 */         g.drawImage(this.level.spriteSheet, x * 16, y * 16, x * 16 + 16, y * 16 + 16, 64, 16, 80, 32, null);
/*    */       }
/* 60 */       if (this.tower.type == Tower.PlatformType.FIRE) {
/* 61 */         g.drawImage(this.level.spriteSheet, x * 16, y * 16, x * 16 + 16, y * 16 + 16, 96, 16, 112, 32, null);
/*    */       }
/* 63 */       if (this.tower.type == Tower.PlatformType.EXPLOSION) {
/* 64 */         g.drawImage(this.level.spriteSheet, x * 16, y * 16, x * 16 + 16, y * 16 + 16, 80, 16, 96, 32, null);
/*    */       }
/* 66 */       if (this.tower.type == Tower.PlatformType.PULSE) {
/* 67 */         g.drawImage(this.level.spriteSheet, x * 16, y * 16, x * 16 + 16, y * 16 + 16, 112, 16, 128, 32, null);
/*    */       }
/* 69 */       Graphics2D gtmp = (Graphics2D)g.create();
/* 70 */       if (this.tower.target != null) this.tower.angle = Math.atan2(y * 16 + 8 - this.tower.target.y, x * 16 + 8 - this.tower.target.x);
/* 71 */       gtmp.rotate(this.tower.angle - 1.570796D - 0.523599D * (this.tower.numGuns - 1) / 2.0D, x * 16 + 8, y * 16 + 8);
/* 72 */       gtmp.drawImage(guns, x * 16 - 8, y * 16 - 8, null);
/* 73 */       gtmp.dispose();
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Dropbox\Public\Tower Evolution\towerevolution.jar!\com\pickandblade\towerevolution\Tile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */