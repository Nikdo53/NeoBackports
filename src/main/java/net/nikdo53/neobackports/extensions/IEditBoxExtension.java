package net.nikdo53.neobackports.extensions;

public interface IEditBoxExtension {
    default void setTextShadow(boolean textShadow) {
    }

    default boolean getTextShadow() {
        throw new IllegalStateException("Not implemented");
    }
}
