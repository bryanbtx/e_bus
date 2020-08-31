package com.e_bus.e_bus;

public class Pendente {
    public int ID;
    public int CARD_NUMBER;
    public double VALUE;
    public String DATE;
    public Pendente(int id,int numero_cartao,double valor,String data){
        ID=id;
        CARD_NUMBER=numero_cartao;
        VALUE=valor;
        DATE=data;
    }

    @Override
    public String toString() {
        return ID+" "+CARD_NUMBER+" "+VALUE+" "+DATE;
    }
}
