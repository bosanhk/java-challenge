package jp.co.axa.apidemo.dto.common;

import lombok.Getter;
import lombok.Setter;

public class ResponseDTO<T> {

    @Getter
    @Setter
    boolean success;

    @Getter
    @Setter
    String message;

    @Getter
    @Setter
    T data;

    public ResponseDTO() {
        this.success = true;
    }

    public ResponseDTO(T data) {
        this.success = true;
        this.data = data;
    }

    public ResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
