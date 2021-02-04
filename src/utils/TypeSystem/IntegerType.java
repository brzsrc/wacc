package utils.TypeSystem;

public class IntegerType implements TypeSystem {
    public int val;

    public IntegerType(Integer val) {
      this.val = val;
    }

    public static IntegerType defaultInstance() {
      return new IntegerType(0);
    }
    
}
