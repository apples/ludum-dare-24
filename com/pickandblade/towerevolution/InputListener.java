/*    */ package com.pickandblade.towerevolution;
/*    */ 
/*    */ import java.awt.event.MouseEvent;
/*    */ 
/*    */ public class InputListener implements java.awt.event.KeyListener, java.awt.event.MouseListener, java.awt.event.MouseMotionListener
/*    */ {
/*    */   public int mouseX;
/*    */   public int mouseY;
/*    */   
/*    */   public class Key
/*    */   {
/* 12 */     public boolean clicked = false; public boolean down = false;
/*    */     
/*    */     public Key() {} }
/*    */   
/* 16 */   private boolean mClick = false;
/* 17 */   public boolean mouseDown = false;
/*    */   
/* 19 */   private java.util.concurrent.ConcurrentSkipListMap<Integer, Key> keys = new java.util.concurrent.ConcurrentSkipListMap();
/*    */   
/*    */   public boolean keyDown(int kc) {
/* 22 */     if (this.keys.containsKey(Integer.valueOf(kc))) {
/* 23 */       return ((Key)this.keys.get(Integer.valueOf(kc))).down;
/*    */     }
/* 25 */     return false;
/*    */   }
/*    */   
/*    */   public boolean mouseClicked() {
/* 29 */     if (this.mClick) {
/* 30 */       this.mClick = false;
/* 31 */       return true;
/*    */     }
/* 33 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public void mouseDragged(MouseEvent e) {}
/*    */   
/*    */   public void mouseMoved(MouseEvent e)
/*    */   {
/* 41 */     this.mouseX = e.getX();
/* 42 */     this.mouseY = e.getY();
/*    */   }
/*    */   
/*    */ 
/*    */   public void mouseClicked(MouseEvent e) {}
/*    */   
/*    */ 
/*    */   public void mouseEntered(MouseEvent e) {}
/*    */   
/*    */ 
/*    */   public void mouseExited(MouseEvent e) {}
/*    */   
/*    */ 
/*    */   public void mousePressed(MouseEvent e)
/*    */   {
/* 57 */     this.mouseDown = true;
/* 58 */     this.mClick = true;
/*    */   }
/*    */   
/*    */   public void mouseReleased(MouseEvent e) {
/* 62 */     this.mouseDown = false;
/*    */   }
/*    */   
/*    */   public void keyPressed(java.awt.event.KeyEvent e) {
/* 66 */     if (this.keys.containsKey(Integer.valueOf(e.getKeyCode()))) {
/* 67 */       ((Key)this.keys.get(Integer.valueOf(e.getKeyCode()))).down = true;
/* 68 */       ((Key)this.keys.get(Integer.valueOf(e.getKeyCode()))).clicked = true;
/*    */     } else {
/* 70 */       this.keys.put(Integer.valueOf(e.getKeyCode()), new Key());
/* 71 */       ((Key)this.keys.get(Integer.valueOf(e.getKeyCode()))).down = true;
/* 72 */       ((Key)this.keys.get(Integer.valueOf(e.getKeyCode()))).clicked = true;
/*    */     }
/*    */   }
/*    */   
/*    */   public void keyReleased(java.awt.event.KeyEvent e) {
/* 77 */     if (this.keys.containsKey(Integer.valueOf(e.getKeyCode()))) {
/* 78 */       ((Key)this.keys.get(Integer.valueOf(e.getKeyCode()))).down = false;
/*    */     }
/*    */   }
/*    */   
/*    */   public void keyTyped(java.awt.event.KeyEvent e) {}
/*    */ }


/* Location:              D:\Dropbox\Public\Tower Evolution\towerevolution.jar!\com\pickandblade\towerevolution\InputListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */