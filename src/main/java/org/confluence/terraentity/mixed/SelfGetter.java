package org.confluence.terraentity.mixed;

@SuppressWarnings("unchecked")
public interface SelfGetter<T> {
    default T te$getSelf(){
        return (T) this;
    }
}
