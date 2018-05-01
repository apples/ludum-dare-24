/*     */ package com.pickandblade.towerevolution;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ 
/*     */ public class Enemy extends Entity { public Type type;
/*     */   private Level level;
/*     */   private int ox;
/*     */   
/*   9 */   static enum Type { NORMAL,  SHIELD,  FAST,  SPAWNER;
/*     */   }
/*     */   
/*     */ 
/*     */   private int oy;
/*     */   
/*     */   Tile.Direction dir;
/*     */   
/*     */   public int health;
/*     */   
/*  19 */   public int maxHealth = 1;
/*     */   
/*  21 */   public double vel = 1.0D;
/*  22 */   private double velAccum = 0.0D;
/*     */   
/*  24 */   public int spawnInterval = 180; public int spawnTimer = 0;
/*  25 */   public Type spawnType = Type.NORMAL;
/*     */   
/*     */   public Enemy(Type t, Level l, int variation) {
/*  28 */     this.type = t;
/*  29 */     this.level = l;
/*  30 */     this.h = 16;
/*  31 */     switch (this.type) {
/*     */     case SHIELD: 
/*  33 */       if (variation > 2) variation = 2;
/*  34 */       this.ox = (4 + variation * 2);
/*  35 */       this.oy = 8;
/*  36 */       this.vel = 1.0D;
/*  37 */       setHealth(20);
/*  38 */       this.w = 8;
/*  39 */       break;
/*     */     case FAST: 
/*  41 */       this.ox = 8;
/*  42 */       this.oy = 8;
/*  43 */       this.vel = 0.5D;
/*  44 */       setHealth(50);
/*  45 */       this.w = 16;
/*  46 */       break;
/*     */     case NORMAL: 
/*  48 */       this.ox = 8;
/*  49 */       this.oy = 8;
/*  50 */       this.vel = 0.5D;
/*  51 */       setHealth(50);
/*  52 */       this.w = 16;
/*  53 */       break;
/*     */     case SPAWNER: 
/*  55 */       this.ox = 8;
/*  56 */       this.oy = 8;
/*  57 */       this.vel = 0.3D;
/*  58 */       setHealth(50);
/*  59 */       this.w = 16;
/*     */     }
/*     */     
/*  62 */     this.x = (this.level.spawnX * 16 + this.ox);
/*  63 */     this.y = (this.level.spawnY * 16 + this.oy);
/*  64 */     this.dir = this.level.tiles[(this.level.spawnX + this.level.spawnY * this.level.width)].dirNext;
/*     */   }
/*     */   
/*     */   public void render(java.awt.image.BufferedImage target) {
/*  68 */     Graphics g = target.createGraphics();
/*  69 */     switch (this.type) {
/*     */     case SHIELD: 
/*  71 */       g.drawImage(this.level.spriteSheet, this.x - this.w / 2, this.y - this.h / 2, this.x + this.w / 2, this.y + this.h / 2, 36, 0, 44, 16, null);
/*  72 */       break;
/*     */     case FAST: 
/*  74 */       g.drawImage(this.level.spriteSheet, this.x - this.w / 2, this.y - this.h / 2, this.x + this.w / 2, this.y + this.h / 2, 0, 0, 16, 16, null);
/*  75 */       break;
/*     */     case NORMAL: 
/*  77 */       g.drawImage(this.level.spriteSheet, this.x - this.w / 2, this.y - this.h / 2, this.x + this.w / 2, this.y + this.h / 2, 16, 0, 32, 16, null);
/*  78 */       break;
/*     */     case SPAWNER: 
/*  80 */       g.drawImage(this.level.spriteSheet, this.x - this.w / 2, this.y - this.h / 2, this.x + this.w / 2, this.y + this.h / 2, 48, 0, 64, 16, null);
/*     */     }
/*     */     
/*  83 */     g.setColor(java.awt.Color.red);
/*  84 */     g.drawLine(this.x - this.w / 2, this.y + this.w / 2 + 1, this.x + this.w / 2 - this.w * (this.maxHealth - this.health) / this.maxHealth - 1, this.y + this.w / 2 + 1);
/*  85 */     g.dispose();
/*     */   }
/*     */   
/*     */   public void tick() {
/*  89 */     this.velAccum += this.vel;
/*  90 */     while (this.velAccum >= 1.0D) {
/*  91 */       switch (this.dir) {
/*     */       case WEST: 
/*  93 */         this.deleteMe = true;
/*  94 */         this.hitCore = true;
/*  95 */         break;
/*     */       case NORTH: 
/*  97 */         this.x += 1;
/*  98 */         break;
/*     */       case EAST: 
/* 100 */         this.y -= 1;
/* 101 */         break;
/*     */       case NONE: 
/* 103 */         this.y += 1;
/* 104 */         break;
/*     */       case SOUTH: 
/* 106 */         this.x -= 1;
/*     */       }
/*     */       
/*     */       
/* 110 */       if ((this.x % 16 == this.ox) && (this.y % 16 == this.oy)) {
/* 111 */         this.dir = this.level.tiles[((this.x - this.ox) / 16 + (this.y - this.oy) * this.level.width / 16)].dirNext;
/*     */       }
/* 113 */       this.velAccum -= 1.0D;
/*     */     }
/*     */   }
/*     */   
/*     */   public void hurt(int power) {
/* 118 */     this.health -= power;
/* 119 */     if (this.health <= 0) this.deleteMe = true;
/*     */   }
/*     */   
/*     */   public void setHealth(int h) {
/* 123 */     this.health = h;
/* 124 */     this.maxHealth = h;
/*     */   }
/*     */ }


/* Location:              D:\Dropbox\Public\Tower Evolution\towerevolution.jar!\com\pickandblade\towerevolution\Enemy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */