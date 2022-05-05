package manager;

import model.Task;

import java.io.Serializable;
import java.util.Comparator;

public class TaskComparator implements Comparator<Task>, Serializable {

    @Override
    public int compare (Task x, Task y) {
        int o = 0;
        if (x.getStartTime() == null && y.getStartTime() == null) {
            o = 0;
        } else if (x.getStartTime() != null && y.getStartTime() == null) {
            o = -1;
        } else if (x.getStartTime() == null && y.getStartTime() != null) {
            o = 1;
        } else if (x.getStartTime().isBefore(y.getStartTime())) {
            o = -1;
        } else if (x.getStartTime().isAfter(y.getStartTime())) {
            o = 1;
        }
        return o;
    }

}