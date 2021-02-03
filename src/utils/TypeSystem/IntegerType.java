package utils.TypeSystem;

public class IntegerType implements TypeSystem {
    public int val;

    public IntegerType(Integer val) {
      this.val = val;
    }

    @Override
    public boolean check(utils.TypeSystem.TypeSystem value) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
