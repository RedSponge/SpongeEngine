package com.redsponge.sponge.animation;

public interface Values {

    interface ValueHolder<T> {
        T get();
    }

    public class ConstantValueHolder<T> implements ValueHolder<T> {
        private final T value;

        public ConstantValueHolder(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }
    }

    @FunctionalInterface
    interface VariableSupplier {
        <T> T supply(String variableName);
    }

    public class VariableValueHolder<T> implements ValueHolder<T> {
        private VariableSupplier supplier;
        private String variableName;

        public VariableValueHolder(VariableSupplier supplier, String variableName) {
            this.supplier = supplier;
            this.variableName = variableName;
        }

        @Override
        public T get() {
            return supplier.supply(variableName);
        }
    }

    public class CompInt implements Comparable<Object> {
        private int value;

        public CompInt(int value) {
            this.value = value;
        }


        @Override
        public int compareTo(Object o) {
            return value - ((CompInt) o).value;
        }
    }

    public class CompBool implements Comparable<Object> {
        private boolean value;

        public CompBool(boolean value) {
            this.value = value;
        }


        @Override
        public int compareTo(Object o) {
            boolean other = ((CompBool)o).value;
            return value == other ? 0 : 1;
        }
    }

}
