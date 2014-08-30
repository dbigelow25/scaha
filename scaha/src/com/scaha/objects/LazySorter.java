package com.scaha.objects;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

 
public class LazySorter implements Comparator<Stat> {
 
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    public int compare(Stat car1, Stat car2) {
        try {
            Object value1 = Stat.class.getField(this.sortField).get(car1);
            Object value2 = Stat.class.getField(this.sortField).get(car2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}