package stud.pw.enviromentparametersapp.models;

import java.util.ArrayList;
import java.util.List;

public class ArrayResponse <T>{
    ArrayList<T> elements;

    public ArrayList<T> getElements() {
        return elements;
    }

    public void setElements(ArrayList<T> elements) {
        this.elements = elements;
    }
}
