package utils.backend;

import java.util.Objects;

/* the abstract class representing any types of register. The generic <T> here represents the labeling system of the registers */
public abstract class Register<T> {
    protected T label;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Register<?> register = (Register<?>) o;
        return Objects.equals(label, register.label);
    }

//    public boolean equals(Object obj) {
//        /* TODO: need better code quality here */
//        return obj != null && this.label.equals(((Register) obj).label);
//    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public String toString() {
        return label.toString();
    }
}
