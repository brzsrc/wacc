package utils;

public interface SymbolTableInterface {
    public void add(String name, TypeSystem type);
    public TypeSystem lookUp(String name);
    public TypeSystem lookUpAll(String name);
}
