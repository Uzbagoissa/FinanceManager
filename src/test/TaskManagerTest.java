package test;

import manager.TaskManager;
import org.junit.jupiter.api.Test;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    public void allTest(){}         //пытался поместить сюда тесты интерфейса из InMemoryTaskManagerTest, но тогда все перестает работать и ломается

}
