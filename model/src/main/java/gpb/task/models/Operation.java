package gpb.task.models;

import java.time.LocalDateTime;

import static gpb.task.models.Const.DELIMITER;

public class Operation {

    private final LocalDateTime operationDate;
    private final int salePoint;
    private final int currentOperationNum;
    private final double operationSum;

    public Operation(LocalDateTime operationDate, String salePoint, int currentOperationNum, double operationSum) {
        this.operationDate = operationDate;
        this.salePoint = Integer.parseInt(salePoint);
        this.currentOperationNum = currentOperationNum;
        this.operationSum = operationSum;
    }

    public Operation(String line) {
        String[] elem = line.split(DELIMITER);

        if (elem.length != 4) {
            throw new RuntimeException("incorrect line: " + line);
        }

        operationDate = LocalDateTime.parse(elem[0]);
        salePoint = Integer.parseInt(elem[1]);
        currentOperationNum = Integer.parseInt(elem[2]);
        operationSum = Double.parseDouble(elem[3]);
    }

    public String getLine() {
        return operationDate.toString() + DELIMITER +
                salePoint + DELIMITER +
                currentOperationNum + DELIMITER +
                Math.round(operationSum * 100.0) / 100.0D + System.lineSeparator();
    }

    @Override
    public String toString() {
        return "Operation{" +
                "operationDate=" + operationDate +
                ", salePoint='" + salePoint + '\'' +
                ", currentOperationNum=" + currentOperationNum +
                ", operationSum=" + operationSum +
                '}';
    }

    public LocalDateTime getOperationDate() {
        return operationDate;
    }

    public int getSalePoint() {
        return salePoint;
    }

    public int getCurrentOperationNum() {
        return currentOperationNum;
    }

    public double getOperationSum() {
        return operationSum;
    }
}
