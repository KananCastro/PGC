/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.ArrayList;

/**
 *
 * @author Kanan
 */
public class Observer {

    /**
     * @return the comandos
     */
    public ArrayList<Comando> getComandos() {
        return layout.getComandos();
    }
    
    public int getIndexComando (DraggableNode n) {
        return layout.getIndexComando(n);
    }
    
    public Comando getComando(DraggableNode n) {
        return layout.getComando(n);
    }

    /**
     * @param comandos the comandos to set
     */
    public void setComandos(ArrayList<Comando> comandos) {
        layout.setComandos(comandos);
    }

    private DraggableNode node;
    private RootLayout    layout;
    private ArrayList<Comando> comandos;
    
  
    public Observer(DraggableNode node, RootLayout layout){
        this.node = node;
        this.layout = layout;
    }
    
    public void carregar(){
        layout.id.setText(node.getId());
        layout.num.setText(node.getNum().toString());
        layout.tipo.setText(node.getType().toString());
        layout.nome.setText(node.getNome());
        layout.valor.setText(node.getValor());
    }
    
    public int getX_Cursor(){
        return layout.getX_Cursor();
    }
    
    public int getY_Cursor(){
        return layout.getY_Cursor();
    }
    
    public void setX_Cursor(int X_Cursor) {
        layout.X_Cursor = X_Cursor;
    }

    /**
     * @param Y_Cursor the Y_Cursor to set
     */
    public void setY_Cursor(int Y_Cursor) {
        layout.Y_Cursor = Y_Cursor;
    }
    
    public int getFlag(){
        return layout.getFlag();
    }
    
    public void setFlag(int flag) {
        layout.setFlag(flag);
    }
    
    public void update(){
        layout.update();
    }

}
