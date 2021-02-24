package backend.instructions.memory;

public interface Stack {
  public int push(String ident);
  public int pop(String ident);
  public int lookUp(String ident);
  public boolean isFull();
}
