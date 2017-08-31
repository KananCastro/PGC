/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

/**
 *
 * @author Kanan
 */
public class StackCommand{
    private Comando itens[];
    private int     topo;
    
    public StackCommand(){
        itens = new Comando[1000];
        topo = -1;
    }
    
    public void push(Comando cmd){
        itens[++topo] = cmd;
    }
    public boolean isEmpty(){
        return topo == -1;
    }
    public Comando pop(){
        return itens[topo--];
    }
    
    public Comando getTopo(){
        return itens[topo];
    }
}
