/*     */ package com.pickandblade.towerevolution;
/*     */ 
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.image.BufferStrategy;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.util.Random;
/*     */ import javax.imageio.ImageIO;
/*     */ 
/*     */ 
/*     */ public class Game
/*     */   extends Canvas
/*     */   implements Runnable
/*     */ {
/*     */   private static final long serialVersionUID = 69L;
/*     */   public static final int PWIDTH = 320;
/*     */   public static final int PHEIGHT = 240;
/*     */   public static final int PSCALE = 2;
/*     */   public static final double TARGETFPS = 60.0D;
/*     */   public static final double TARGETTPS = 60.0D;
/*  24 */   private static final Random random = new Random();
/*     */   
/*     */ 
/*     */   private static final int MAXDIFFICULTY = 4;
/*     */   
/*     */ 
/*     */   public InputListener input;
/*     */   
/*  32 */   private boolean running = false;
/*  33 */   private long ticks = 0L;
/*     */   
/*     */ 
/*  36 */   private BufferedImage buffer = new BufferedImage(320, 240, 1);
/*     */   
/*     */   private BufferedImage sprites;
/*     */   
/*  40 */   private int cardHandX = 224; private int cardHandY = 208;
/*     */   
/*     */   private static enum Card
/*     */   {
/*  44 */     TOWER_ROUND,  TOWER_SQUARE, 
/*  45 */     BASE_NORMAL,  BASE_FIRE,  BASE_MORTAR,  BASE_PULSE,  BASE_ENERGY, 
/*  46 */     GUN_ADD,  GUN_RANGE,  GUN_POWER, 
/*  47 */     SHAPE_ROUNDER,  SHAPE_SQUARER; }
/*     */   
/*  49 */   private Card[] cardVals = Card.values();
/*  50 */   private int numCardTypes = 12;
/*     */   private Level level;
/*  52 */   private int offsetX = 0; private int offsetY = 0;
/*  53 */   private int resources = 0;
/*  54 */   Card[] cards = new Card[3];
/*  55 */   int draggingCard = -1;
/*  56 */   private boolean ready = false;
/*  57 */   private int difficulty = 0;
/*     */   private int shuffles;
/*     */   
/*     */   private void init()
/*     */   {
/*  62 */     this.input = new InputListener();
/*  63 */     addKeyListener(this.input);
/*  64 */     addMouseListener(this.input);
/*  65 */     addMouseMotionListener(this.input);
/*     */   }
/*     */   
/*     */   public void start() {
/*  69 */     this.running = true;
/*  70 */     new Thread(this).start();
/*     */   }
/*     */   
/*     */   public void stop() {
/*  74 */     this.running = false;
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/*  79 */       this.sprites = ImageIO.read(Game.class.getResource("/sprites.png"));
/*     */     } catch (IOException e) {
/*  81 */       e.printStackTrace();
/*     */     }
/*  83 */     loadLevel("/level" + (this.difficulty + 1) + ".png");
/*     */     
/*  85 */     long lastTime = System.nanoTime();
/*  86 */     long currentTime = 0L;
/*  87 */     double frameCounter = 0.0D;
/*  88 */     double tickCounter = 0.0D;
/*     */     
/*  90 */     init();
/*     */     
/*  92 */     while (this.running) {
/*  93 */       currentTime = System.nanoTime();
/*     */       
/*  95 */       frameCounter += (currentTime - lastTime) * 60.0D / 1.0E9D;
/*  96 */       while (frameCounter >= 1.0D) {
/*  97 */         render();
/*  98 */         frameCounter -= 1.0D;
/*     */       }
/*     */       
/* 101 */       tickCounter += (currentTime - lastTime) * 60.0D / 1.0E9D;
/* 102 */       while (tickCounter >= 1.0D) {
/* 103 */         tick();
/* 104 */         tickCounter -= 1.0D;
/*     */       }
/*     */       
/* 107 */       lastTime = currentTime;
/*     */     }
/*     */   }
/*     */   
/*     */   private void render() {
/* 112 */     Graphics2D g = this.buffer.createGraphics();
/* 113 */     g.setColor(Color.black);
/* 114 */     g.fillRect(0, 0, getWidth(), getHeight());
/*     */     
/*     */ 
/*     */ 
/* 118 */     g.drawImage(this.level.render(), 0, 0, 320, 240, this.offsetX, this.offsetY, this.offsetX + 320, this.offsetY + 240, null);
/* 119 */     drawCards(g);
/* 120 */     if (this.draggingCard != -1) {
/* 121 */       g.drawImage(getCardIcon(this.cards[this.draggingCard]), this.input.mouseX / 2 - 8, this.input.mouseY / 2 - 8, null);
/*     */     }
/* 123 */     drawProgress(g);
/*     */     
/*     */ 
/*     */ 
/* 127 */     g.dispose();
/*     */     
/* 129 */     BufferStrategy bs = getBufferStrategy();
/* 130 */     if (bs == null) {
/* 131 */       createBufferStrategy(3);
/*     */       
/* 133 */       return;
/*     */     }
/* 135 */     Graphics gg = bs.getDrawGraphics();
/* 136 */     gg.drawImage(this.buffer, 0, 0, 640, 480, null);
/* 137 */     gg.dispose();
/* 138 */     bs.show();
/*     */   }
/*     */   
/*     */   private void drawProgress(Graphics2D g) {
/* 142 */     g.setColor(new Color(0, 0, 0, 255));
/* 143 */     g.fillRect(4, 4, 312, 8);
/* 144 */     g.setColor(new Color(128, 0, 0, 255));
/* 145 */     g.drawRect(4, 4, 311, 7);
/* 146 */     g.setColor(new Color(255, 0, 0, 255));
/* 147 */     g.fillRect(6, 6, 308 * this.level.remainingEnemies / this.level.numEnemies, 4);
/*     */   }
/*     */   
/*     */   private void drawCards(Graphics g) {
/* 151 */     for (int i = 0; i < 3; i++) {
/* 152 */       g.drawImage(this.sprites, this.cardHandX + 32 * i, this.cardHandY, this.cardHandX + 32 * i + 32, this.cardHandY + 32, 0, 64, 32, 96, null);
/* 153 */       g.drawImage(getCardIcon(this.cards[i]), this.cardHandX + 32 * i + 8, this.cardHandY + 8, null);
/*     */     }
/* 155 */     for (int i = 0; i < this.shuffles; i++) {
/* 156 */       g.drawImage(this.sprites, this.cardHandX + 16 * i, this.cardHandY - 16, this.cardHandX + 16 * i + 16, this.cardHandY, 0, 112, 16, 128, null);
/*     */     }
/*     */   }
/*     */   
/*     */   private BufferedImage getCardIcon(Card c) {
/* 161 */     switch (c) {
/*     */     case GUN_POWER: 
/* 163 */       return this.sprites.getSubimage(64, 16, 16, 16);
/*     */     case BASE_NORMAL: 
/* 165 */       return this.sprites.getSubimage(96, 16, 16, 16);
/*     */     case BASE_PULSE: 
/* 167 */       return this.sprites.getSubimage(80, 16, 16, 16);
/*     */     case BASE_MORTAR: 
/* 169 */       return this.sprites.getSubimage(80, 32, 16, 16);
/*     */     case GUN_ADD: 
/* 171 */       return this.sprites.getSubimage(112, 16, 16, 16);
/*     */     case GUN_RANGE: 
/* 173 */       return this.sprites.getSubimage(0, 32, 16, 16);
/*     */     case SHAPE_SQUARER: 
/* 175 */       return this.sprites.getSubimage(32, 32, 16, 16);
/*     */     case SHAPE_ROUNDER: 
/* 177 */       return this.sprites.getSubimage(16, 32, 16, 16);
/*     */     case TOWER_ROUND: 
/* 179 */       return this.sprites.getSubimage(0, 96, 16, 16);
/*     */     case TOWER_SQUARE: 
/* 181 */       return this.sprites.getSubimage(16, 96, 16, 16);
/*     */     case BASE_ENERGY: 
/* 183 */       return this.sprites.getSubimage(0, 16, 16, 16);
/*     */     case BASE_FIRE: 
/* 185 */       return this.sprites.getSubimage(48, 16, 16, 16);
/*     */     }
/* 187 */     return null;
/*     */   }
/*     */   
/*     */   private void tick()
/*     */   {
/* 192 */     this.ticks += 1L;
/*     */     
/*     */ 
/* 195 */     if (this.level.complete) {
/* 196 */       this.difficulty += 1;
/* 197 */       if (this.difficulty > 4) this.difficulty = 0;
/* 198 */       loadLevel("/level" + (this.difficulty + 1) + ".png");
/*     */     }
/*     */     
/* 201 */     if (this.input.keyDown(87)) this.offsetY -= 1;
/* 202 */     if (this.input.keyDown(65)) this.offsetX -= 1;
/* 203 */     if (this.input.keyDown(83)) this.offsetY += 1;
/* 204 */     if (this.input.keyDown(68)) { this.offsetX += 1;
/*     */     }
/* 206 */     int mouseX = this.input.mouseX / 2;
/* 207 */     int mouseY = this.input.mouseY / 2;
/*     */     
/* 209 */     if (this.input.mouseClicked()) {
/* 210 */       if ((mouseX >= this.cardHandX) && (mouseX < this.cardHandX + 96) && (mouseY >= this.cardHandY) && (mouseY < this.cardHandY + 32)) {
/* 211 */         this.draggingCard = ((mouseX - this.cardHandX) / 32);
/* 212 */       } else if ((mouseX >= this.cardHandX) && (mouseX < this.cardHandX + this.shuffles * 16) && (mouseY >= this.cardHandY - 16) && (mouseY < this.cardHandY)) {
/* 213 */         shuffle();
/* 214 */       } else if ((mouseX + this.offsetX >= 0) && (mouseX + this.offsetX < this.level.width * 16) && (mouseY + this.offsetY >= 0) && (mouseY + this.offsetY < this.level.height * 16)) {
/* 215 */         int mTileX = (mouseX + this.offsetX) / 16;
/* 216 */         int mTileY = (mouseY + this.offsetY) / 16;
/* 217 */         if (this.draggingCard != -1) {
/* 218 */           Tile targetTile = this.level.getTile(mTileX, mTileY);
/* 219 */           if (targetTile.type == Tile.Type.TOWER) {
/* 220 */             boolean cardUsed = false;
/* 221 */             switch (this.cards[this.draggingCard]) {
/*     */             case GUN_POWER: 
/* 223 */               if ((targetTile.hasTower) && 
/* 224 */                 (targetTile.tower.disabled <= 0)) {
/* 225 */                 targetTile.tower.setType(Tower.PlatformType.ENERGY);
/* 226 */                 cardUsed = true; }
/* 227 */               break;
/*     */             case BASE_NORMAL: 
/* 229 */               if ((targetTile.hasTower) && 
/* 230 */                 (targetTile.tower.disabled <= 0)) {
/* 231 */                 targetTile.tower.setType(Tower.PlatformType.FIRE);
/* 232 */                 cardUsed = true; }
/* 233 */               break;
/*     */             case BASE_PULSE: 
/* 235 */               if ((targetTile.hasTower) && 
/* 236 */                 (targetTile.tower.disabled <= 0)) {
/* 237 */                 targetTile.tower.setType(Tower.PlatformType.EXPLOSION);
/* 238 */                 cardUsed = true; }
/* 239 */               break;
/*     */             case BASE_MORTAR: 
/* 241 */               if ((targetTile.hasTower) && 
/* 242 */                 (targetTile.tower.disabled <= 0)) {
/* 243 */                 targetTile.tower.setType(Tower.PlatformType.NORMAL);
/* 244 */                 cardUsed = true; }
/* 245 */               break;
/*     */             case GUN_ADD: 
/* 247 */               if ((targetTile.hasTower) && 
/* 248 */                 (targetTile.tower.disabled <= 0)) {
/* 249 */                 targetTile.tower.setType(Tower.PlatformType.PULSE);
/* 250 */                 cardUsed = true; }
/* 251 */               break;
/*     */             case GUN_RANGE: 
/* 253 */               if ((targetTile.hasTower) && 
/* 254 */                 (targetTile.tower.disabled <= 0) && 
/* 255 */                 (targetTile.tower.numGuns != -1)) {
/* 256 */                 targetTile.tower.numGuns += 1;
/* 257 */                 cardUsed = true; }
/* 258 */               break;
/*     */             case SHAPE_SQUARER: 
/* 260 */               if ((targetTile.hasTower) && 
/* 261 */                 (targetTile.tower.disabled <= 0) && 
/* 262 */                 (targetTile.tower.numGuns != -1)) {
/* 263 */                 targetTile.tower.gun.power += 5;
/* 264 */                 cardUsed = true; }
/* 265 */               break;
/*     */             case SHAPE_ROUNDER: 
/* 267 */               if ((targetTile.hasTower) && 
/* 268 */                 (targetTile.tower.disabled <= 0) && 
/* 269 */                 (targetTile.tower.numGuns != -1)) {
/* 270 */                 targetTile.tower.gun.range += 16;
/* 271 */                 targetTile.tower.gun.minRange += 8;
/* 272 */                 cardUsed = true; }
/* 273 */               break;
/*     */             case TOWER_ROUND: 
/* 275 */               if ((targetTile.hasTower) && 
/* 276 */                 (targetTile.tower.disabled <= 0)) {
/* 277 */                 if (targetTile.tower.squareness > 0) targetTile.tower.squareness -= 1;
/* 278 */                 cardUsed = true; }
/* 279 */               break;
/*     */             case TOWER_SQUARE: 
/* 281 */               if ((targetTile.hasTower) && 
/* 282 */                 (targetTile.tower.disabled <= 0)) {
/* 283 */                 if (targetTile.tower.squareness < 3) targetTile.tower.squareness += 1;
/* 284 */                 cardUsed = true; }
/* 285 */               break;
/*     */             case BASE_ENERGY: 
/* 287 */               if (!targetTile.hasTower) {
/* 288 */                 targetTile.hasTower = true;
/* 289 */                 targetTile.tower = new Tower();
/* 290 */                 cardUsed = true; }
/* 291 */               break;
/*     */             case BASE_FIRE: 
/* 293 */               if (!targetTile.hasTower) {
/* 294 */                 targetTile.hasTower = true;
/* 295 */                 targetTile.tower = new Tower();
/* 296 */                 targetTile.tower.squareness = 3;
/* 297 */                 cardUsed = true;
/*     */               }
/*     */               break; }
/* 300 */             if (cardUsed) {
/* 301 */               this.cards[this.draggingCard] = this.cardVals[random.nextInt(this.numCardTypes)];
/* 302 */               this.draggingCard = -1;
/* 303 */               this.ready = true;
/* 304 */               targetTile.tower.disabled = targetTile.tower.getDisableTime();
/*     */             }
/*     */           }
/*     */         }
/*     */       } else {
/* 309 */         this.draggingCard = -1;
/*     */       }
/*     */     }
/*     */     
/* 313 */     if (this.ready) this.level.tick();
/* 314 */     if (this.level.coreHealth <= 0) {
/* 315 */       loadLevel("/level" + (this.difficulty + 1) + ".png");
/*     */     }
/*     */   }
/*     */   
/*     */   private void shuffle() {
/* 320 */     this.draggingCard = -1;
/* 321 */     this.cards[0] = this.cardVals[random.nextInt(this.numCardTypes)];
/* 322 */     this.cards[1] = this.cardVals[random.nextInt(this.numCardTypes)];
/* 323 */     this.cards[2] = this.cardVals[random.nextInt(this.numCardTypes)];
/* 324 */     this.shuffles -= 1;
/*     */   }
/*     */   
/*     */   private void loadLevel(String n) {
/*     */     try {
/* 329 */       BufferedImage levelSource = ImageIO.read(Game.class.getResource(n));
/* 330 */       this.level = new Level(levelSource, this.sprites, this.difficulty);
/*     */     } catch (IOException e) {
/* 332 */       e.printStackTrace();
/*     */     }
/*     */     
/* 335 */     this.offsetX = (this.level.width * 16 / 2 - 160);
/* 336 */     this.offsetY = (this.level.height * 16 / 2 - 120);
/*     */     
/* 338 */     this.ready = false;
/* 339 */     this.cards[0] = Card.TOWER_ROUND;
/* 340 */     this.cards[1] = Card.TOWER_SQUARE;
/* 341 */     this.cards[2] = Card.GUN_ADD;
/* 342 */     this.draggingCard = -1;
/*     */     
/* 344 */     this.shuffles = 5;
/*     */   }
/*     */ }


/* Location:              D:\Dropbox\Public\Tower Evolution\towerevolution.jar!\com\pickandblade\towerevolution\Game.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */