/*    */ package com.pickandblade.towerevolution;
/*    */ 
/*    */ import java.applet.Applet;
/*    */ import java.awt.BorderLayout;
/*    */ 
/*    */ public class GameContainer extends Applet
/*    */ {
/*    */   private static final long serialVersionUID = 69L;
/*  9 */   private Game game = new Game();
/*    */   
/*    */   public void init() {
/* 12 */     setLayout(new BorderLayout());
/* 13 */     add(this.game, "Center");
/*    */   }
/*    */   
/*    */   public void start() {
/* 17 */     this.game.start();
/*    */   }
/*    */   
/*    */   public void stop() {
/* 21 */     this.game.stop();
/*    */   }
/*    */ }


/* Location:              D:\Dropbox\Public\Tower Evolution\towerevolution.jar!\com\pickandblade\towerevolution\GameContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */