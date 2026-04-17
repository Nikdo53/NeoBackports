package net.nikdo53.neobackports.extensions;

public interface IAbstractWidgetExtension {
    default int getBottom(){
        return 0;
    }

    default int getRight(){
        return 0;
    }

}
