package binary404.autotech.common.core.util;

public class ValidationResult<T> {

    private final ValidationResultType type;
    private final T result;

    public ValidationResult(ValidationResultType type, T result) {
        this.type = type;
        this.result = result;
    }

    public ValidationResultType getType() {
        return type;
    }

    public T getResult() {
        return result;
    }

    public static <T> ValidationResult<T> newResult(ValidationResultType result, T value) {
        return new ValidationResult<>(result, value);
    }
}
