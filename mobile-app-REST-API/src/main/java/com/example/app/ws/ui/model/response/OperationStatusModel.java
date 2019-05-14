package com.example.app.ws.ui.model.response;

public class OperationStatusModel {
    private String operatedObject;
    private String operationName;
    private String operationResult;

    public OperationStatusModel(String operatedObject, String operationName) {
        this.operatedObject = operatedObject;
        this.operationName = operationName;
    }

    public OperationStatusModel() {
    }

    public String getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperatedObject() {
        return operatedObject;
    }

    public void setOperatedObject(String operatedObject) {
        this.operatedObject = operatedObject;
    }
}
