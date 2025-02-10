package org.confluence.terraentity.mixinauxiliary;

@SuppressWarnings("unchecked")
public interface SelfGetter<T> {
    default T te$getSelf(){
        return (T) this;
    }
}
