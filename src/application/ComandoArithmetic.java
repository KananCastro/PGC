/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javafx.scene.input.DragEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author isidro
 */
public class ComandoArithmetic extends Comando{

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    public String getId(DraggableNode n) {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    private int x;
    private int y;
    private int altura;
    private int largura;
    private String texto;
    private DragIconType tipo;
    private String id;
    private String type;
    private AnchorPane pane;
    private DraggableNode node;    
    
    
    public ComandoArithmetic(AnchorPane pane, DraggableNode node){
        try{
            this.pane = pane;
            this.node = node;
            this.id = node.getId();
            this.tipo = node.getType();
            this.altura = (int) node.getAltura();
            this.largura = (int) node.getLargura();
            //this.texto = texto;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void run(){
    }
    
    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the altura
     */
    public int getAltura() {
        return altura;
    }

    /**
     * @param altura the altura to set
     */
    public void setAltura(int altura) {
        this.altura = altura;
    }

    /**
     * @return the largura
     */
    public int getLargura() {
        return largura;
    }

    /**
     * @param largura the largura to set
     */
    public void setLargura(int largura) {
        this.largura = largura;
    }

   
    public boolean isClicked(int x, int y){
        return (x >= x && x <= x+getLargura() && y >= y && y <= y+getAltura());
    }

    /**
     * @return the texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * @param texto the texto to set
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String writeCode() {
        return null;
    }

    @Override
    public void draw(int x, int y) {
        pane.getChildren().add(node);
        node.relocateToPoint(new Point2dSerial(x, y));
        node.setX(node.getLayoutX());
        node.setY(node.getLayoutY());
        node.observer.carregar();
    }
    
}
