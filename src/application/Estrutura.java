/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Kanan
 */
public class Estrutura extends Rectangle {

    public Estrutura(AnchorPane pane, ArrayList<Rectangle> estruturas, Color color, int X, int Y, int Width, int Height, String Id) {
        Rectangle estrutura = new Rectangle(X, Y, Width, Height);
        estrutura.setId(Id);
        estrutura.setX(X);
        estrutura.setY(Y);
        estrutura.setFill(color);
        estruturas.add(estrutura);
        pane.getChildren().add(estrutura);
    }

}
