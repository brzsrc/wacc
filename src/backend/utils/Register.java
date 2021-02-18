package backend.utils;

public class Register {

    private int num;

    public Register(int num) {
        this.num = num;
    }

    public String getName() {
        if(num < 13) {
            return "r" + num;
        }
        switch(num) {
            case 11: return "fp";
            case 12: return "scr";
            case 13: return "sp";
            case 14: return "lr";
            case 15: return "pc";
            default: 
                throw new IllegalArgumentException("Invalid register id " 
                + num + " passed to Registers::intToName");
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Register) {
            return ((Register) obj).getName().equals(this.getName());
        }
        return false;
    }
}

