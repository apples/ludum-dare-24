/*    */ package com.pickandblade.towerevolution;
/*    */ 
/*    */ public class Tower {
/*    */   public static final int SHOOTDUR = 4;
/*    */   public static final double GUNANGLE = 0.523599D;
/*    */   
/*    */   public static enum PlatformType {
/*  8 */     NORMAL,  ENERGY,  FIRE,  EXPLOSION,  PULSE;
/*    */   }
/*    */   
/* 11 */   PlatformType type = PlatformType.NORMAL;
/* 12 */   Gun gun = new Gun();
/* 13 */   int numGuns = 1;
/* 14 */   int squareness = 0;
/* 15 */   double angle = 3.141592653589793D;
/*    */   
/* 17 */   Enemy target = null;
/* 18 */   int shoot = 0;
/*    */   int shootX;
/*    */   int shootY;
/* 21 */   boolean energyBurst = false;
/*    */   
/* 23 */   private int disableTime = 300;
/* 24 */   public int disabled = 0;
/*    */   
/*    */   public void setType(PlatformType pt) {
/* 27 */     if (this.type == pt) return;
/* 28 */     if (!typesCompatible(this.type, pt)) {
/* 29 */       this.gun = new Gun();
/* 30 */       this.numGuns = 1;
/*    */     }
/* 32 */     this.gun = new Gun();
/* 33 */     switch (pt) {
/*    */     case EXPLOSION: 
/* 35 */       this.numGuns = -1;
/* 36 */       break;
/*    */     case NORMAL: 
/* 38 */       this.gun.power *= 2;
/* 39 */       this.gun.range = 64;
/* 40 */       this.gun.chargeTime *= 2;
/* 41 */       break;
/*    */     case FIRE: 
/* 43 */       this.gun.power /= 10;
/* 44 */       this.gun.chargeTime /= 10;
/* 45 */       break;
/*    */     case ENERGY: 
/*    */       break;
/*    */     case PULSE: 
/* 49 */       this.gun = new Gun();
/* 50 */       this.gun.range = 24;
/* 51 */       this.numGuns = -1;
/* 52 */       break;
/*    */     }
/*    */     
/*    */     
/*    */ 
/* 57 */     this.type = pt;
/*    */   }
/*    */   
/*    */   private boolean typesCompatible(PlatformType t1, PlatformType t2) {
/* 61 */     if (((t1 == PlatformType.ENERGY) || (t1 == PlatformType.PULSE)) && (
/* 62 */       (t2 == PlatformType.ENERGY) || (t2 == PlatformType.PULSE))) {
/* 63 */       return true;
/*    */     }
/*    */     
/* 66 */     if (((t1 == PlatformType.NORMAL) || (t1 == PlatformType.FIRE) || (t1 == PlatformType.EXPLOSION)) && (
/* 67 */       (t2 == PlatformType.NORMAL) || (t2 == PlatformType.FIRE) || (t2 == PlatformType.EXPLOSION))) {
/* 68 */       return true;
/*    */     }
/*    */     
/* 71 */     return false;
/*    */   }
/*    */   
/*    */   public int getDisableTime() {
/* 75 */     return this.disableTime * (this.squareness * this.squareness + 3) / 12;
/*    */   }
/*    */   
/*    */   public int getChargeTime() {
/* 79 */     if (this.numGuns == -1) return this.gun.chargeTime * 3 / (this.squareness + 3);
/* 80 */     return this.gun.chargeTime * 3 / (this.numGuns * (this.squareness + 3));
/*    */   }
/*    */ }


/* Location:              D:\Dropbox\Public\Tower Evolution\towerevolution.jar!\com\pickandblade\towerevolution\Tower.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */