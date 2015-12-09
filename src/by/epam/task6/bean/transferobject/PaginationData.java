package by.epam.task6.bean.transferobject;

import java.io.Serializable;

public class PaginationData implements Serializable {

    private static final long serialVersionUID = 1L;

    private int countRecordsPerPage;
    private int offset;

    public int getCountRecordsPerPage() {
        return countRecordsPerPage;
    }

    public void setCountRecordsPerPage(int countRecordsPerPage) {
        this.countRecordsPerPage = countRecordsPerPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
