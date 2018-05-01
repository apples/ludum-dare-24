/*     */ package com.pickandblade.towerevolution;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class Level
/*     */ {
/*     */   public static final int TILESIZE = 16;
/*     */   public int width;
/*     */   public int height;
/*     */   public Tile[] tiles;
/*     */   public int spawnX;
/*     */   public int spawnY;
/*     */   public int coreX;
/*     */   public int coreY;
/*  20 */   public ArrayList<Entity> entities = new ArrayList();
/*     */   
/*     */   private BufferedImage levelImage;
/*     */   
/*     */   public BufferedImage spriteSheet;
/*  25 */   private long spawnTimer = 0L; private long spawnInterval = 60L;
/*     */   
/*  27 */   public int coreHealth = 16;
/*     */   
/*  29 */   public Enemy.Type[] enemyTypes = Enemy.Type.values();
/*  30 */   public Random random = new Random();
/*     */   
/*     */   public int remainingEnemies;
/*     */   public int numEnemies;
/*  34 */   public boolean complete = false;
/*     */   
/*     */   public Level(BufferedImage source, BufferedImage sprites, int d) {
/*  37 */     this.numEnemies = (d * 10 + 20);
/*  38 */     this.remainingEnemies = this.numEnemies;
/*  39 */     this.width = source.getWidth();
/*  40 */     this.height = source.getHeight();
/*  41 */     this.tiles = new Tile[this.width * this.height];
/*  42 */     for (int x = 0; x < this.width; x++) {
/*  43 */       for (int y = 0; y < this.height; y++) {
/*  44 */         this.tiles[(x + y * this.width)] = new Tile(this);
/*  45 */         switch (source.getRGB(x, y) & 0xFFFFFF) {
/*     */         case 0: 
/*  47 */           this.tiles[(x + y * this.width)].type = Tile.Type.WALL;
/*  48 */           break;
/*     */         case 16711680: 
/*  50 */           this.tiles[(x + y * this.width)].type = Tile.Type.TOWER;
/*  51 */           break;
/*     */         case 65280: 
/*  53 */           this.tiles[(x + y * this.width)].type = Tile.Type.PATH;
/*  54 */           break;
/*     */         case 255: 
/*  56 */           this.tiles[(x + y * this.width)].type = Tile.Type.SPAWN;
/*  57 */           break;
/*     */         case 16777215: 
/*  59 */           this.tiles[(x + y * this.width)].type = Tile.Type.BLANK;
/*  60 */           break;
/*     */         case 16776960: 
/*  62 */           this.tiles[(x + y * this.width)].type = Tile.Type.CORE;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*  67 */     this.spriteSheet = sprites;
/*  68 */     generateImage();
/*  69 */     findAIPath();
/*     */   }
/*     */   
/*     */   private void generateImage() {
/*  73 */     this.levelImage = new BufferedImage(this.width * 16, this.height * 16, 1);
/*  74 */     java.awt.Graphics g = this.levelImage.createGraphics();
/*  75 */     for (int x = 0; x < this.width; x++) {
/*  76 */       for (int y = 0; y < this.height; y++) {
/*  77 */         int sourceX = 0;int sourceY = 0;
/*  78 */         switch (this.tiles[(x + y * this.width)].type) {
/*     */         case CORE: 
/*  80 */           sourceX = 32;
/*  81 */           sourceY = 48;
/*  82 */           break;
/*     */         case BLANK: 
/*  84 */           sourceX = 48;
/*  85 */           sourceY = 48;
/*  86 */           break;
/*     */         case WALL: 
/*  88 */           sourceX = 64;
/*  89 */           sourceY = 48;
/*  90 */           break;
/*     */         case SPAWN: 
/*  92 */           sourceX = 0;
/*  93 */           sourceY = 48;
/*  94 */           break;
/*     */         case TOWER: 
/*  96 */           sourceX = 80;
/*  97 */           sourceY = 48;
/*  98 */           break;
/*     */         case PATH: 
/* 100 */           sourceX = 16;
/* 101 */           sourceY = 48;
/*     */         }
/*     */         
/* 104 */         g.drawImage(this.spriteSheet, x * 16, y * 16, (x + 1) * 16, (y + 1) * 16, sourceX, sourceY, sourceX + 16, sourceY + 16, null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void findAIPath()
/*     */   {
/* 111 */     boolean foundStartNode = false;
/* 112 */     for (int x = 0; x < this.width; x++) {
/* 113 */       for (int y = 0; y < this.height; y++) {
/* 114 */         if (this.tiles[(x + y * this.width)].type == Tile.Type.SPAWN) {
/* 115 */           foundStartNode = true;
/* 116 */           Tile startTile = this.tiles[(x + y * this.width)];
/* 117 */           startTile.dirLast = Tile.Direction.SPAWN;
/* 118 */           this.spawnX = x;
/* 119 */           this.spawnY = y;
/* 120 */           System.out.println("Spawn node found at " + x + " " + y);
/* 121 */           if (!findNextNode(x, y)) {
/* 122 */             System.out.println("Invalid level!");
/*     */           }
/*     */         }
/* 125 */         if (foundStartNode) break;
/*     */       }
/* 127 */       if (foundStartNode)
/*     */         break;
/*     */     }
/*     */   }
/*     */   
/* 132 */   private boolean findNextNode(int x, int y) { Tile curTile = this.tiles[(x + y * this.width)];
/*     */     
/* 134 */     if (curTile.type == Tile.Type.CORE) {
/* 135 */       curTile.dirNext = Tile.Direction.CORE;
/* 136 */       this.coreX = x;
/* 137 */       this.coreY = y;
/* 138 */       System.out.println("Core node found at " + x + " " + y);
/* 139 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 144 */     if (x != 0) {
/* 145 */       Tile nextTile = this.tiles[(x - 1 + y * this.width)];
/* 146 */       if ((nextTile.dirNext == Tile.Direction.NONE) && ((nextTile.type == Tile.Type.CORE) || (nextTile.type == Tile.Type.PATH))) {
/* 147 */         curTile.dirNext = Tile.Direction.WEST;
/* 148 */         nextTile.dirLast = Tile.Direction.EAST;
/* 149 */         return findNextNode(x - 1, y);
/*     */       }
/*     */     }
/* 152 */     if (y != 0) {
/* 153 */       Tile nextTile = this.tiles[(x + (y - 1) * this.width)];
/* 154 */       if ((nextTile.dirNext == Tile.Direction.NONE) && ((nextTile.type == Tile.Type.CORE) || (nextTile.type == Tile.Type.PATH))) {
/* 155 */         curTile.dirNext = Tile.Direction.NORTH;
/* 156 */         nextTile.dirLast = Tile.Direction.SOUTH;
/* 157 */         return findNextNode(x, y - 1);
/*     */       }
/*     */     }
/* 160 */     if (x != this.width - 1) {
/* 161 */       Tile nextTile = this.tiles[(x + 1 + y * this.width)];
/* 162 */       if ((nextTile.dirNext == Tile.Direction.NONE) && ((nextTile.type == Tile.Type.CORE) || (nextTile.type == Tile.Type.PATH))) {
/* 163 */         curTile.dirNext = Tile.Direction.EAST;
/* 164 */         nextTile.dirLast = Tile.Direction.WEST;
/* 165 */         return findNextNode(x + 1, y);
/*     */       }
/*     */     }
/* 168 */     if (y != this.height - 1) {
/* 169 */       Tile nextTile = this.tiles[(x + (y + 1) * this.width)];
/* 170 */       if ((nextTile.dirNext == Tile.Direction.NONE) && ((nextTile.type == Tile.Type.CORE) || (nextTile.type == Tile.Type.PATH))) {
/* 171 */         curTile.dirNext = Tile.Direction.SOUTH;
/* 172 */         nextTile.dirLast = Tile.Direction.NORTH;
/* 173 */         return findNextNode(x, y + 1);
/*     */       }
/*     */     }
/* 176 */     return false;
/*     */   }
/*     */   
/*     */   public Tile getTile(int x, int y) {
/* 180 */     if ((x < 0) || (y < 0) || (x > this.width - 1) || (y > this.height - 1)) return null;
/* 181 */     return this.tiles[(x + y * this.width)];
/*     */   }
/*     */   
/*     */   public BufferedImage render() {
/* 185 */     BufferedImage rval = new BufferedImage(this.levelImage.getColorModel(), this.levelImage.copyData(null), this.levelImage.isAlphaPremultiplied(), null);
/*     */     
/* 187 */     Graphics2D g = rval.createGraphics();
/*     */     
/* 189 */     for (int x = 0; x < this.width; x++) {
/* 190 */       for (int y = 0; y < this.height; y++) {
/* 191 */         getTile(x, y).draw(x, y, g);
/*     */       }
/*     */     }
/*     */     
/* 195 */     for (int x = 0; x < this.width; x++) {
/* 196 */       for (int y = 0; y < this.height; y++) {
/* 197 */         Tile t = getTile(x, y);
/* 198 */         if ((t.hasTower) && 
/* 199 */           (t.tower.shoot > 0)) {
/* 200 */           t.tower.shoot -= 1;
/* 201 */           g.setColor(Color.white);
/* 202 */           if (t.tower.type == Tower.PlatformType.PULSE) {
/* 203 */             g.drawArc(x * 16 - 32 + 8, y * 16 - 32 + 8, 64, 64, 0, 360);
/*     */           } else {
/* 205 */             if (t.tower.type == Tower.PlatformType.FIRE) g.setColor(Color.red);
/* 206 */             g.drawLine(x * 16 + 8, y * 16 + 8, t.tower.shootX, t.tower.shootY);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 213 */     for (int i = 0; i < this.entities.size(); i++) {
/* 214 */       ((Entity)this.entities.get(i)).render(rval);
/*     */     }
/*     */     
/* 217 */     g.dispose();
/*     */     
/* 219 */     return rval;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 223 */     if (this.complete) { return;
/*     */     }
/* 225 */     this.spawnTimer -= 1L;
/* 226 */     if ((this.spawnTimer <= 0L) && (this.remainingEnemies > 0)) {
/* 227 */       Enemy.Type t = this.enemyTypes[this.random.nextInt(this.enemyTypes.length)];
/* 228 */       this.spawnTimer = this.spawnInterval;
/* 229 */       if ((t == Enemy.Type.SHIELD) || (t == Enemy.Type.SPAWNER)) this.spawnTimer *= 5L;
/* 230 */       Enemy enemy = new Enemy(t, this, 0);
/* 231 */       enemy.setHealth(enemy.health * (this.numEnemies - this.remainingEnemies + 20) / 20);
/* 232 */       this.entities.add(enemy);
/* 233 */       if (t == Enemy.Type.FAST) {
/* 234 */         Enemy enemy2 = new Enemy(t, this, 2);
/* 235 */         enemy.setHealth(enemy2.health * (this.numEnemies * 5 - this.remainingEnemies) / this.numEnemies);
/* 236 */         this.entities.add(enemy2);
/*     */       }
/* 238 */       this.remainingEnemies -= 1;
/*     */     }
/*     */     
/* 241 */     for (int i = 0; i < this.entities.size(); i++) {
/* 242 */       Entity e = (Entity)this.entities.get(i);
/* 243 */       e.tick();
/* 244 */       if (e.explode) {
/* 245 */         Mortar m = (Mortar)e;
/* 246 */         for (int j = 0; j < this.entities.size(); j++) {
/* 247 */           Entity ee = (Entity)this.entities.get(j);
/* 248 */           if ((ee instanceof Enemy)) {
/* 249 */             double dx = ee.x - m.x;double dy = ee.y - m.y;
/* 250 */             double dist2 = dx * dx + dy * dy;
/* 251 */             if (dist2 <= m.radius * m.radius) {
/* 252 */               ((Enemy)ee).hurt(m.power);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 257 */         this.entities.remove(i);
/* 258 */         i--;
/*     */ 
/*     */       }
/* 261 */       else if (e.deleteMe) {
/* 262 */         if (e.hitCore) this.coreHealth -= 1;
/* 263 */         this.entities.remove(i);
/* 264 */         i--;
/*     */ 
/*     */       }
/* 267 */       else if ((e instanceof Enemy)) {
/* 268 */         Enemy ee = (Enemy)e;
/* 269 */         if (ee.type == Enemy.Type.SPAWNER) {
/* 270 */           ee.spawnTimer -= 1;
/* 271 */           if (ee.spawnTimer <= 0) {
/* 272 */             ee.spawnTimer = ee.spawnInterval;
/* 273 */             Enemy ees = new Enemy(ee.spawnType, this, 0);
/* 274 */             ees.x = ee.x;ees.y = ee.y;
/* 275 */             ees.dir = ee.dir;
/* 276 */             this.entities.add(ees);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 282 */     for (int x = 0; x < this.width; x++) {
/* 283 */       for (int y = 0; y < this.height; y++) {
/* 284 */         Tile t = getTile(x, y);
/* 285 */         if ((t.hasTower) && 
/* 286 */           (t.tower != null))
/* 287 */           if (t.tower.disabled > 0) {
/* 288 */             t.tower.disabled -= 1;
/*     */ 
/*     */           }
/* 291 */           else if (t.tower.type == Tower.PlatformType.ENERGY) {
/* 292 */             t.tower.energyBurst = (!t.tower.energyBurst);
/* 293 */             if ((t.tower.energyBurst) || (t.tower.squareness == 3)) {
/* 294 */               for (int x1 = -1; x1 <= 1; x1++) {
/* 295 */                 for (int y1 = -1; y1 <= 1; y1++) {
/* 296 */                   if ((x1 != 0) || (y1 != 0)) {
/* 297 */                     Tile t1 = getTile(x + x1, y + y1);
/* 298 */                     if ((t1 != null) && (t1.hasTower) && 
/* 299 */                       (t1.tower.gun.recharging > 0)) t1.tower.gun.recharging -= 1;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           else {
/* 306 */             if (t.tower.gun.recharging > 0) t.tower.gun.recharging -= 1;
/* 307 */             if ((t.tower.type == Tower.PlatformType.PULSE) && (t.tower.gun.recharging <= 0)) {
/* 308 */               for (int i = 0; i < this.entities.size(); i++) {
/* 309 */                 Entity e = (Entity)this.entities.get(i);
/* 310 */                 if ((e instanceof Enemy)) {
/* 311 */                   double dx = e.x - x * 16 - 8;double dy = e.y - y * 16 - 8;
/* 312 */                   double dist2 = dx * dx + dy * dy;
/* 313 */                   if (dist2 <= t.tower.gun.range * t.tower.gun.range) {
/* 314 */                     ((Enemy)e).hurt(t.tower.gun.power);
/* 315 */                     t.tower.shoot = 4;
/*     */                   }
/*     */                 }
/*     */               }
/*     */               
/* 320 */               t.tower.gun.recharging = t.tower.getChargeTime();
/*     */             }
/*     */             else {
/* 323 */               if (t.tower.target != null) {
/* 324 */                 double dx = t.tower.target.x - x * 16 - 8;double dy = t.tower.target.y - y * 16 - 8;
/* 325 */                 double dist2 = dx * dx + dy * dy;
/* 326 */                 if ((t.tower.target.deleteMe) || 
/* 327 */                   (dist2 > t.tower.gun.range * t.tower.gun.range) || 
/* 328 */                   (!lineOfSight(x, y, t.tower.target.x / 16, t.tower.target.y / 16))) {
/* 329 */                   t.tower.target = null;
/*     */                 }
/*     */               }
/* 332 */               if (t.tower.target == null) {
/* 333 */                 for (int i = 0; i < this.entities.size(); i++) {
/* 334 */                   Entity e = (Entity)this.entities.get(i);
/* 335 */                   if ((e instanceof Enemy)) {
/* 336 */                     double dx = e.x - x * 16 - 8;double dy = e.y - y * 16 - 8;
/* 337 */                     double dist2 = dx * dx + dy * dy;
/* 338 */                     if ((dist2 <= t.tower.gun.range * t.tower.gun.range) && 
/* 339 */                       (lineOfSight(x, y, e.x / 16, e.y / 16))) {
/* 340 */                       t.tower.target = ((Enemy)e);
/* 341 */                       break;
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/* 346 */               if ((t.tower.target != null) && 
/* 347 */                 (t.tower.gun.recharging <= 0)) {
/* 348 */                 if (t.tower.type == Tower.PlatformType.EXPLOSION) {
/* 349 */                   Mortar m = new Mortar(this, x * 16 + 8, y * 16 + 8, 
/* 350 */                     t.tower.target.x, t.tower.target.y, t.tower.gun.power, 16);
/* 351 */                   this.entities.add(m);
/*     */                 } else {
/* 353 */                   t.tower.shoot = 4;
/* 354 */                   t.tower.shootX = t.tower.target.x;
/* 355 */                   t.tower.shootY = t.tower.target.y;
/* 356 */                   t.tower.target.hurt(t.tower.gun.power);
/*     */                 }
/* 358 */                 t.tower.gun.recharging = t.tower.getChargeTime();
/*     */               }
/*     */             }
/*     */           }
/*     */       }
/*     */     }
/* 364 */     checkComplete();
/*     */   }
/*     */   
/*     */   private void checkComplete() {
/* 368 */     if (this.remainingEnemies > 0) return;
/* 369 */     for (int i = 0; i < this.entities.size(); i++) {
/* 370 */       if ((this.entities.get(i) instanceof Enemy)) return;
/*     */     }
/* 372 */     this.complete = true;
/*     */   }
/*     */   
/*     */   public boolean lineOfSight(int x0, int y0, int x1, int y1)
/*     */   {
/* 377 */     int dx = Math.abs(x1 - x0);
/* 378 */     int dy = Math.abs(y1 - y0);
/* 379 */     int x = x0;
/* 380 */     int y = y0;
/* 381 */     int n = 1 + dx + dy;
/* 382 */     int x_inc = x1 > x0 ? 1 : -1;
/* 383 */     int y_inc = y1 > y0 ? 1 : -1;
/* 384 */     int error = dx - dy;
/* 385 */     dx *= 2;
/* 386 */     dy *= 2;
/* 388 */     for (; 
/* 388 */         n > 0; n--)
/*     */     {
/* 390 */       if (getTile(x, y).type == Tile.Type.WALL) { return false;
/*     */       }
/* 392 */       if (error > 0)
/*     */       {
/* 394 */         x += x_inc;
/* 395 */         error -= dy;
/*     */       }
/*     */       else
/*     */       {
/* 399 */         y += y_inc;
/* 400 */         error += dx;
/*     */       }
/*     */     }
/*     */     
/* 404 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Dropbox\Public\Tower Evolution\towerevolution.jar!\com\pickandblade\towerevolution\Level.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */