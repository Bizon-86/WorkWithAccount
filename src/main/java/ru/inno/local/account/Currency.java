package ru.inno.local.account;
public enum Currency {
    RUB("Российский рубль"),USD("Доллар США"),EUR("Евро");
    private String curName;
    Currency(String curName){
        this.curName = curName;
    }
    public String getCurName(){
        return this.curName;
    }
}
