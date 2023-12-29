package stud.pw.enviromentparametersapp.models;

import java.util.Objects;

public class DateStringResponse {
    String lastUpdateDate;

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateStringResponse that = (DateStringResponse) o;
        return Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastUpdateDate);
    }
}
