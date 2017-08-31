/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

public abstract class Comando{

    public abstract void draw(int x, int y);
    public abstract void run();
    public abstract String writeCode();
    public abstract String getId();
    public abstract String getType();
    
}