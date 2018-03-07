package sebet.space.list.tasks;


import sebet.space.utils.EnumTasks;

import static sebet.space.utils.EnumTasks.invalid;

public abstract class Task {

    private boolean active;

    public abstract EnumTasks getType();

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
