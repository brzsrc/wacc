package backend.instructions.memory;

public interface Stack {

  /* used for task3 optimisation */
  int push(String ident);

  int pop(String ident);

  int lookUp(String ident);

  boolean isFull();
}
