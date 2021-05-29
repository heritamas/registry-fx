package regfx.dialogs;

@FunctionalInterface
public interface DialogController<T> {
    T getResult();
}
