package com.moyanshushe.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Result {
    private String Message; // Message
    private Object Data; // Result
    private Short Status; // 1:success others: failed

    private Result(String Message, Object Data, Short Status) {
        this.Message = Message;
        this.Data = Data;
        this.Status = Status;

    }

    public static Result success(String Message, Object Data) {
        return new Result(Message, Data, (short) 1);
    }

    public static Result success(String Message) {
        return new Result(Message, null, (short) 1);
    }

    public static Result success(Object Data) {
        return new Result("", Data, (short) 1);
    }

    public static Result success() {
        return new Result("", null, (short) 1);
    }

    public static Result error(String Message) {


        return new Result(Message, null, (short) 0);
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setData(Object data) {
        Data = data;
    }

    public void setStatus(Short status) {
        Status = status;
    }
}
